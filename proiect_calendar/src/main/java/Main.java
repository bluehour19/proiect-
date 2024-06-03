import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Main {

    private static EntityManagerFactory emf;
    private static EntityManager entityManager;

    public static void main(String[] args) {
        try {

            emf = EntityManagerFactorySingleton.getEntityManagerFactory();
            entityManager = emf.createEntityManager();

            EmployeeRepository employeeRepository = new EmployeeRepository(entityManager);
            WorkHoursRepository workHoursRepository = new WorkHoursRepository(entityManager);
            VacantDaysRepository vacantDaysRepository = new VacantDaysRepository(entityManager);

            Employee employee = new Employee();
            employee.setFirstName("Alexandru");
            employee.setLastName("Margineanu");
            employee.setScheduleType("full time");
            employeeRepository.create(employee);

            WorkHours workHours = new WorkHours();
            workHours.setEmployee(employee);
            workHours.setScheduleType("full time");
            workHours.setStartTime(LocalTime.of(9, 0));
            workHours.setEndTime(LocalTime.of(17, 0));
            workHoursRepository.create(workHours);


            VacantDays vacantDays = new VacantDays();
            vacantDays.setEmployee(employee);
            vacantDays.setStartDate(LocalDate.of(2024, 6, 1));
            vacantDays.setEndDate(LocalDate.of(2024, 6, 15));
            vacantDaysRepository.create(vacantDays);


            startSwingApp();
        } finally {

        }
    }

    private static void startSwingApp() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new StaffHolidaysApp();
            }
        });
    }
}
