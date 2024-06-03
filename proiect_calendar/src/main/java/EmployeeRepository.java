import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

public class EmployeeRepository {
    private EntityManager entityManager;

    public EmployeeRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(Employee employee) {
        // Check if an employee with the same first name and last name already exists
        List<Employee> existingEmployees = findByFirstNameAndLastName(employee.getFirstName(), employee.getLastName());
        if (!existingEmployees.isEmpty()) {
            System.out.println("Employee with the same first name and last name already exists. Skipping insertion.");
            return; // Skip insertion
        }

        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(employee);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to create employee: " + e.getMessage());
        }
    }

    public List<Employee> findByFirstNameAndLastName(String firstName, String lastName) {
        TypedQuery<Employee> query = entityManager.createQuery("SELECT e FROM Employee e WHERE e.firstName = :firstName AND e.lastName = :lastName", Employee.class);
        query.setParameter("firstName", firstName);
        query.setParameter("lastName", lastName);
        return query.getResultList();
    }

    public List<Employee> findByLastName(String lastName) {
        TypedQuery<Employee> query = entityManager.createQuery("SELECT e FROM Employee e WHERE e.lastName = :lastName", Employee.class);
        query.setParameter("lastName", lastName);
        return query.getResultList();
    }

    public Employee findById(int id) {
        return entityManager.find(Employee.class, id);
    }

    public List<Employee> findByFirstOrLastName(String name) {
        String queryStr = "SELECT e FROM Employee e WHERE e.firstName LIKE :name OR e.lastName LIKE :name";
        TypedQuery<Employee> query = entityManager.createQuery(queryStr, Employee.class);
        query.setParameter("name", "%" + name + "%"); // Search for names containing the input

        return query.getResultList();
    }
}
