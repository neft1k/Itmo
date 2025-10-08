package vehicle.repo;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import vehicle.model.FuelType;
import vehicle.model.Vehicle;
import vehicle.model.VehicleType;

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

        Vehicle managed;
        if (v.getId() == null) {
            em.persist(v);
            managed = v;
        } else {
            managed = em.merge(v);
        }

        tx.commit();
        return managed;
    }


    public void delete(Long id) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Vehicle v = em.find(Vehicle.class, id);
        if (v != null)
            em.remove(v);
        tx.commit();
    }
}
