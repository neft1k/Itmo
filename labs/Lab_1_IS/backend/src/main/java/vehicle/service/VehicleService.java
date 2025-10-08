package vehicle.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import vehicle.dto.VehicleDto;
import vehicle.mapper.VehicleMapper;
import vehicle.model.Coordinates;
import vehicle.model.Vehicle;
import vehicle.repo.VehicleRepository;
import vehicle.events.VehicleEvents;
import java.util.logging.Logger;
import java.util.logging.Level;


import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
@Transactional
public class VehicleService {
    @Inject
    VehicleRepository repo;
    @Inject
    VehicleEvents events;
    @PersistenceContext
    EntityManager em;
    @Inject
    Validator validator;



    public VehicleRepository.Page<Vehicle> list(int page, int size, String sort, boolean asc, Map<String,String> filters) {
        return repo.findPage(page, size, sort, asc, filters);
    }

    public Vehicle get(Long id) {
        return repo.findById(id);
    }

    public Vehicle create(@Valid VehicleDto dto) {
        Vehicle v = new Vehicle();
        VehicleMapper.apply(v, dto);
        resolveCoordinatesLink(v, dto);
        validate(v);
        Vehicle saved = repo.save(v);
        events.fireChanged("CREATED", saved.getId());
        return saved;
    }

    public Vehicle update(Long id, @Valid VehicleDto dto) {
        Vehicle db = repo.findById(id);
        VehicleMapper.apply(db, dto);
        resolveCoordinatesLink(db, dto);
        validate(db);
        Vehicle saved = repo.save(db);
        events.fireChanged("UPDATED", saved.getId());
        return saved;
    }

    public void delete(Long id) {
        repo.delete(id);
        events.fireChanged("DELETED", id);
    }

    public Double avgFuelConsumption() {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Number n = (Number) em.createNativeQuery("SELECT vehicle_avg_fuel_consumption()").getSingleResult();
        tx.commit();
        return n != null ? n.doubleValue() : null;

    }




    private static final Logger LOG = Logger.getLogger(VehicleService.class.getName());

    public Vehicle anyWithMaxType() {
        var tx = em.getTransaction();
        try {
            tx.begin();
            var ids = em.createNativeQuery(
                            "SELECT v.id FROM vehicle_any_with_max_type() v")
                    .getResultList();
            if (ids.isEmpty()) {
                tx.commit();
                return null;
            }
            Number n = (Number) ids.get(0);
            tx.commit();
            Vehicle v = repo.findById(n.longValue());
            return v;

        } catch (RuntimeException e) {
            if (tx.isActive()) {
                try {
                    tx.rollback();
                } catch (Exception re) {
                    LOG.log(Level.SEVERE, "Rollback failed", re);
                }
            }
            throw e;
        }
    }



    public List<Vehicle> findByNameContains(String substr) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        @SuppressWarnings("unchecked")
        List<Number> ids = em.createNativeQuery(
                        "SELECT v.id FROM vehicle_name_contains(?1) v")
                .setParameter(1, substr)
                .getResultList();
        tx.commit();
        return ids.stream().map(Number::longValue).map(repo::findById).collect(Collectors.toList());
    }

    public List<Vehicle> findByWheelsRange(long from, long to) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        @SuppressWarnings("unchecked")
        List<Number> ids = em.createNativeQuery(
                        "SELECT v.id FROM vehicle_wheels_in_range(?1, ?2) v")
                .setParameter(1, from)
                .setParameter(2, to)
                .getResultList();
        tx.commit();
        return ids.stream().map(Number::longValue).map(repo::findById).collect(Collectors.toList());
    }

    public void resetDistanceToZero(Long id) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.createNativeQuery("SELECT vehicle_reset_distance(?1)")
                .setParameter(1, id)
                .getSingleResult();
        tx.commit();
        events.fireChanged("UPDATED", id);
        em.clear();
    }

    private void validate(Vehicle v) {
        var violations = validator.validate(v);
        if (!violations.isEmpty()) {
            throw new jakarta.validation.ConstraintViolationException(violations);
        }
    }

    private void resolveCoordinatesLink(Vehicle v, VehicleDto dto) {
        if (dto.coordinates != null && dto.coordinates.id != null) {
            Coordinates existing = em.find(Coordinates.class, dto.coordinates.id);
            if (existing == null) throw new IllegalArgumentException("Coordinates not found: id=" + dto.coordinates.id);
            v.setCoordinates(existing);
        }
    }
}
