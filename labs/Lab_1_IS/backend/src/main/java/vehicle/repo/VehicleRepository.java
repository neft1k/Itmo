package vehicle.repo;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import vehicle.exception.UniqueConstraintViolationException;
import vehicle.model.FuelType;
import vehicle.model.Vehicle;
import vehicle.model.VehicleType;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

@ApplicationScoped
public class VehicleRepository {

    public static final class Page<T> {
        public final List<T> content;
        public final int page;
        public final int size;
        public final long total;
        public Page(List<T> content, int page, int size, long total) {
            this.content = content;
            this.page = page;
            this.size = size;
            this.total = total;
        }
    }

    @PersistenceContext
    EntityManager em;

    public Vehicle findById(Long id) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        TypedQuery<Vehicle> q = em.createQuery("SELECT v FROM Vehicle v JOIN FETCH v.coordinates WHERE v.id=:id", Vehicle.class);
        q.setParameter("id", id);
        Vehicle v = q.getSingleResult();
        tx.commit();
        return v;
    }


    public Page<Vehicle> findPage(int page, int size, String sortField, boolean asc, Map<String, String> eqFilters) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        int p = Math.max(0, page);
        int s = size <= 0 ? 20 : size;
        String sort = (sortField == null || sortField.isBlank()) ? "id" : sortField;

        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Vehicle> cq = cb.createQuery(Vehicle.class);
        Root<Vehicle> root = cq.from(Vehicle.class);

        List<Predicate> preds = new ArrayList<>();
        eqFilters.forEach((k, v) -> {
            if (v != null && !v.isBlank()) {
                switch (k) {
                    case "type" -> preds.add(cb.equal(root.get(k), VehicleType.valueOf(v)));
                    case "fuelType" -> preds.add(cb.equal(root.get(k), FuelType.valueOf(v)));
                    default -> preds.add(cb.equal(root.get(k), v));
                }
            }
        });
        cq.where(preds.toArray(Predicate[]::new));
        cq.orderBy(asc ? cb.asc(root.get(sort)) : cb.desc(root.get(sort)));

        List<Vehicle> content = em.createQuery(cq)
                .setHint("jakarta.persistence.cache.storeMode", "REFRESH")
                .setFirstResult(p * s)
                .setMaxResults(s)
                .getResultList();

        CriteriaQuery<Long> countQ = cb.createQuery(Long.class);
        Root<Vehicle> r2 = countQ.from(Vehicle.class);
        countQ.select(cb.count(r2)).where(preds.toArray(Predicate[]::new));
        long total = em.createQuery(countQ).getSingleResult();
        tx.commit();
        return new Page<>(content, p, s, total);
    }

    public Vehicle save(Vehicle v) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Connection connection = unwrapConnection();
        Integer prevIsolation = null;
        try {
            if (connection != null) {
                prevIsolation = connection.getTransactionIsolation();
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            }
            assertUnique(v, v.getId());
            Vehicle managed;
            if (v.getId() == null) {
                em.persist(v);
                managed = v;
            } else {
                managed = em.merge(v);
            }
            tx.commit();
            return managed;
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } catch (SQLException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Не удалось установить уровень изоляции транзакции", e);
        } finally {
            if (connection != null && prevIsolation != null) {
                try {
                    connection.setTransactionIsolation(prevIsolation);
                } catch (SQLException ignored) {}
            }
        }
    }


    public void delete(Long id) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Vehicle v = em.find(Vehicle.class, id);
        if (v != null)
            em.remove(v);
        tx.commit();
    }

    public boolean existsByNameIgnoreCase(String name, Long excludeId) {
        if (name == null) {
            return false;
        }
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Long cnt = em.createQuery(
                        "SELECT COUNT(v) FROM Vehicle v WHERE lower(v.name) = :name " +
                                "AND (:excludeId IS NULL OR v.id <> :excludeId)", Long.class)
                .setParameter("name", name == null ? null : name.toLowerCase())
                .setParameter("excludeId", excludeId)
                .getSingleResult();
        tx.commit();
        return cnt != null && cnt > 0;
    }

    public boolean existsByCoordinates(Double x, float y, Long excludeId) {
        if (x == null) {
            return false;
        }
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Long cnt = em.createQuery(
                        "SELECT COUNT(v) FROM Vehicle v WHERE v.coordinates.x = :x AND v.coordinates.y = :y " +
                                "AND (:excludeId IS NULL OR v.id <> :excludeId)",
                        Long.class)
                .setParameter("x", x)
                .setParameter("y", y)
                .setParameter("excludeId", excludeId)
                .getSingleResult();
        tx.commit();
        return cnt != null && cnt > 0;
    }

    private Connection unwrapConnection() {
        try {
            return em.unwrap(Connection.class);
        } catch (RuntimeException ex) {
            return null;
        }
    }

    private void assertUnique(Vehicle vehicle, Long excludeId) {
        if (vehicle.getName() != null) {
            Long nameConflict = em.createQuery(
                            "SELECT COUNT(v) FROM Vehicle v WHERE lower(v.name) = :name " +
                                    "AND (:excludeId IS NULL OR v.id <> :excludeId)", Long.class)
                    .setParameter("name", vehicle.getName().toLowerCase(Locale.ROOT))
                    .setParameter("excludeId", excludeId)
                    .getSingleResult();
            if (nameConflict != null && nameConflict > 0) {
                throw new UniqueConstraintViolationException("Имя \"" + vehicle.getName() + "\" уже занято");
            }
        }
        if (vehicle.getCoordinates() != null && vehicle.getCoordinates().getX() != null) {
            Long coordsConflict = em.createQuery(
                            "SELECT COUNT(v) FROM Vehicle v WHERE v.coordinates.x = :x AND v.coordinates.y = :y " +
                                    "AND (:excludeId IS NULL OR v.id <> :excludeId)", Long.class)
                    .setParameter("x", vehicle.getCoordinates().getX())
                    .setParameter("y", vehicle.getCoordinates().getY())
                    .setParameter("excludeId", excludeId)
                    .getSingleResult();
            if (coordsConflict != null && coordsConflict > 0) {
                throw new UniqueConstraintViolationException("Координаты (" +
                        vehicle.getCoordinates().getX() + ", " + vehicle.getCoordinates().getY() + ") уже используются");
            }
        }
    }
}
