package vehicle.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import jakarta.ws.rs.NotFoundException;
import vehicle.dto.VehicleDto;
import vehicle.events.VehicleEvents;
import vehicle.mapper.VehicleMapper;
import vehicle.model.ImportJob;
import vehicle.model.ImportStatus;
import vehicle.model.Vehicle;
import vehicle.repo.ImportJobRepository;
import vehicle.security.CurrentUser;

import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class VehicleImportService {

    @PersistenceContext
    EntityManager em;

    @Inject
    Validator validator;

    @Inject
    VehicleImportParser parser;

    @Inject
    ImportJobRepository jobs;

    @Inject
    VehicleEvents events;

    @Inject
    CurrentUser currentUser;

    public ImportJob importVehicles(InputStream stream, String fileName) {
        ImportJob job = createJob(fileName);
        try {
            List<VehicleImportParser.ParsedRow> rows = parser.parse(stream);
            List<Long> createdIds = persistAll(rows);
            markSuccess(job, createdIds.size());
            createdIds.forEach(id -> events.fireChanged("CREATED", id));
            return job;
        } catch (ImportProcessingException e) {
            markFailed(job, e.getMessage(), e.getErrors());
            throw e;
        } catch (ConstraintViolationException e) {
            List<String> messages = e.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .toList();
            markFailed(job, "Ошибка валидации импортируемых данных", messages);
            throw e;
        } catch (RuntimeException e) {
            markFailed(job, e.getMessage(), List.of(e.getClass().getSimpleName()));
            throw e;
        }
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    private ImportJob createJob(String fileName) {
        ImportJob job = new ImportJob();
        job.setFileName((fileName == null || fileName.isBlank()) ? "unknown.json" : fileName);
        job.setRequestedBy(currentUser.username());
        job.setRequestedRole(currentUser.role());
        job.setStatus(ImportStatus.IN_PROGRESS);
        job.setStartedAt(OffsetDateTime.now());
        return jobs.save(job);
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    private void markFailed(ImportJob job, String reason, List<String> errors) {
        job.setStatus(ImportStatus.FAILED);
        job.setFinishedAt(OffsetDateTime.now());
        String message = reason;
        if (errors != null && !errors.isEmpty()) {
            message = reason + ": " + String.join("; ", errors);
        }
        job.setErrorMessage(message);
        jobs.save(job);
}

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    private void markSuccess(ImportJob job, int createdCount) {
        job.setCreatedCount(createdCount);
        job.setStatus(ImportStatus.SUCCESS);
        job.setFinishedAt(OffsetDateTime.now());
        jobs.save(job);
    }

    private List<Long> persistAll(List<VehicleImportParser.ParsedRow> rows) {
        if (rows.isEmpty()) {
            return List.of();
        }
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        List<Vehicle> created = new ArrayList<>();
        try {
            Set<String> names = new HashSet<>();
            Set<String> coordinatesSet = new HashSet<>();
            for (VehicleImportParser.ParsedRow row : rows) {
                VehicleDto dto = row.dto();
                ensureCoordinatesPresent(dto, row.index());
                ensureUniqueInBatch(dto, row.index(), names, coordinatesSet);
                Vehicle vehicle = new Vehicle();
                try {
                    VehicleMapper.apply(vehicle, dto);
                } catch (IllegalArgumentException ex) {
                    throw new ImportProcessingException("Некорректное значение перечисления", List.of("Запись " + row.index() + ": " + ex.getMessage()));
                }
                validate(vehicle, row.index());
                ensureUniqueInDatabase(vehicle, null, row.index());
                em.persist(vehicle);
                created.add(vehicle);
            }
            em.flush();
            tx.commit();
            return created.stream()
                    .map(Vehicle::getId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    private void ensureCoordinatesPresent(VehicleDto dto, int lineNumber) {
        if (dto.coordinates == null || dto.coordinates.x == null || dto.coordinates.y == null) {
            throw new ImportProcessingException("Отсутствует координата", List.of("Запись " + lineNumber + ": координаты заданы некорректно"));
        }
    }

    private void ensureUniqueInBatch(VehicleDto dto, int lineNumber, Set<String> names, Set<String> coordinatesSet) {
        String name = dto.name.toLowerCase(Locale.ROOT);
        if (!names.add(name)) {
            throw new ImportProcessingException("Дублирование данных внутри файла", List.of("Запись " + lineNumber + ": имя \"" + dto.name + "\" уже встречалось ранее"));
        }
        String coordKey = dto.coordinates.x + "|" + dto.coordinates.y;
        if (!coordinatesSet.add(coordKey)) {
            throw new ImportProcessingException("Дублирование координат внутри файла", List.of("Запись " + lineNumber + ": координаты уже встречались ранее"));
        }
    }

    private void ensureUniqueInDatabase(Vehicle vehicle, Long excludeId, int lineNumber) {
        if (vehicle.getName() == null) {
            throw new ImportProcessingException("Нарушение ограничения уникальности", List.of("Запись " + lineNumber + ": имя не задано"));
        }
        if (vehicle.getCoordinates() == null || vehicle.getCoordinates().getX() == null) {
            throw new ImportProcessingException("Нарушение ограничения уникальности", List.of("Запись " + lineNumber + ": координаты заданы некорректно"));
        }
        Long nameConflict;
        if (excludeId == null) {
            nameConflict = em.createQuery("SELECT COUNT(v) FROM Vehicle v WHERE lower(v.name) = :name", Long.class)
                    .setParameter("name", vehicle.getName().toLowerCase(Locale.ROOT))
                    .getSingleResult();
        } else {
            nameConflict = em.createQuery(
                            "SELECT COUNT(v) FROM Vehicle v WHERE lower(v.name) = :name AND v.id <> :excludeId", Long.class)
                    .setParameter("name", vehicle.getName().toLowerCase(Locale.ROOT))
                    .setParameter("excludeId", excludeId)
                    .getSingleResult();
        }
        if (nameConflict != null && nameConflict > 0) {
            throw new ImportProcessingException("Нарушение ограничения уникальности", List.of("Запись " + lineNumber + ": имя \"" + vehicle.getName() + "\" уже используется"));
        }

        Long coordsConflict;
        if (excludeId == null) {
            coordsConflict = em.createQuery("SELECT COUNT(v) FROM Vehicle v WHERE v.coordinates.x = :x AND v.coordinates.y = :y", Long.class).setParameter("x", vehicle.getCoordinates().getX())
                    .setParameter("y", vehicle.getCoordinates().getY())
                    .getSingleResult();
        } else {
            coordsConflict = em.createQuery("SELECT COUNT(v) FROM Vehicle v WHERE v.coordinates.x = :x AND v.coordinates.y = :y" + " AND v.id <> :excludeId", Long.class)
                    .setParameter("x", vehicle.getCoordinates().getX())
                    .setParameter("y", vehicle.getCoordinates().getY())
                    .setParameter("excludeId", excludeId)
                    .getSingleResult();
        }
        if (coordsConflict != null && coordsConflict > 0) {
            throw new ImportProcessingException("Нарушение ограничения уникальности", List.of("Запись " + lineNumber + ": координаты (" + vehicle.getCoordinates().getX() + ", " + vehicle.getCoordinates().getY() + ") уже заняты"));
        }
    }

    private void validate(Vehicle vehicle, int lineNumber) {
        var violations = validator.validate(vehicle);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException("Ошибка валидации записи " + lineNumber, violations);
        }
    }

    public List<ImportJob> history() {
        if (currentUser.isAdmin()) {
            return jobs.findAllOrdered();
        }
        return jobs.findByRequestedBy(currentUser.username());
    }

    public ImportJob getJob(Long id) {
        return jobs.findById(id)
                .filter(job -> currentUser.isAdmin() || currentUser.username().equals(job.getRequestedBy()))
                .orElseThrow(() -> new jakarta.ws.rs.NotFoundException("Import job not found"));
    }
}
