import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

public class WorkHoursRepository
{
	private EntityManager entityManager;

	public WorkHoursRepository(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}

	public void create(WorkHours workHours)
	{
		EntityTransaction transaction = entityManager.getTransaction();
		try
		{
			transaction.begin();
			entityManager.persist(workHours);
			transaction.commit();
		} catch (Exception e)
		{
			if (transaction.isActive())
			{
				transaction.rollback();
			}
			e.printStackTrace();
		}
	}

	public List<WorkHours> findByEmployeeId(int employeeId)
	{
		TypedQuery<WorkHours> query = entityManager
				.createQuery("SELECT w FROM WorkHours w WHERE w.employee.id = :employeeId", WorkHours.class);
		query.setParameter("employeeId", employeeId);
		return query.getResultList();
	}

	public WorkHours findById(int id)
	{
		return entityManager.find(WorkHours.class, id);
	}
}
