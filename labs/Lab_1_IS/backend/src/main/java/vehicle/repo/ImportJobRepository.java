package vehicle.repo;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;
import vehicle.model.ImportJob;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ImportJobRepository {

    @PersistenceContext
    EntityManager em;

    public ImportJob save(ImportJob job) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            if (job.getId() == null) {
                em.persist(job);
                tx.commit();
                return job;
            }
            ImportJob merged = em.merge(job);
            tx.commit();
            return merged;
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public Optional<ImportJob> findById(Long id) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            ImportJob job = em.find(ImportJob.class, id);
            tx.commit();
            return Optional.ofNullable(job);
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public List<ImportJob> findAllOrdered() {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            List<ImportJob> list = em.createQuery("SELECT j FROM ImportJob j ORDER BY j.startedAt DESC", ImportJob.class).getResultList();
            tx.commit();
            return list;
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public List<ImportJob> findByRequestedBy(String user) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            List<ImportJob> list = em.createQuery("SELECT j FROM ImportJob j WHERE j.requestedBy = :user ORDER BY j.startedAt DESC", ImportJob.class)
                    .setParameter("user", user)
                    .getResultList();
            tx.commit();
            return list;
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }
}
