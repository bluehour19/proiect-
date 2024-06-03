import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

public class VacantDaysRepository {
    private EntityManager entityManager;

    public VacantDaysRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(VacantDays vacantDays) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(vacantDays);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public List<VacantDays> findByEmployeeId(int employeeId) {
        TypedQuery<VacantDays> query = entityManager.createQuery("SELECT v FROM VacantDays v WHERE v.employee.id = :employeeId", VacantDays.class);
        query.setParameter("employeeId", employeeId);
        return query.getResultList();
    }

    public VacantDays findById(int id) {
        return entityManager.find(VacantDays.class, id);
    }
}
