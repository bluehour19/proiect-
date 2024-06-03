import javax.persistence.EntityManager;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class StaffHolidaysApp extends JFrame {

    private EntityManager entityManager;
    private EmployeeRepository employeeRepository;
    private VacantDaysRepository vacantDaysRepository;
    private WorkHoursRepository workHoursRepository;
    private JLabel nameLabel;
    private JTextField nameTextField;
    private JButton searchButton;
    private JTextArea resultArea;

    public StaffHolidaysApp() {
        super("Staff Holidays Application");
        entityManager = EntityManagerFactorySingleton.createEntityManager();
        employeeRepository = new EmployeeRepository(entityManager);
        vacantDaysRepository = new VacantDaysRepository(entityManager);
        workHoursRepository = new WorkHoursRepository(entityManager);


        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        nameLabel = new JLabel("Enter Employee Name:");
        nameTextField = new JTextField(20);
        searchButton = new JButton("Search");
        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));


        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);


        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(nameLabel, constraints);

        constraints.gridx = 1;
        panel.add(nameTextField, constraints);

        constraints.gridx = 2;
        panel.add(searchButton, constraints);


        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchVacantDays();
            }
        });


        setLayout(new BorderLayout());
        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);


        pack();
        setVisible(true);
    }

    private void searchVacantDays() {
        String name = nameTextField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an employee name.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Employee> employees = employeeRepository.findByFirstOrLastName(name);
        if (employees.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Employee not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (Employee employee : employees) {
            List<VacantDays> vacantDaysList = vacantDaysRepository.findByEmployeeId(employee.getId());

            sb.append("Vacant Days for ").append(employee.getFirstName()).append(" ").append(employee.getLastName()).append(":\n");
            for (VacantDays vd : vacantDaysList) {
                sb.append("Start Date: ").append(vd.getStartDate()).append(", End Date: ").append(vd.getEndDate()).append("\n");
            }
            sb.append("\n");
        }
        resultArea.setText(sb.toString());
    }
}
