package packager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.*;
import java.util.*;

public class EmployeeGUI {

    private JFrame frame;
    private JTextField nameField;
    private JTextField startTimeField;
    private JTextField endTimeField;
    private JButton addButton, changeButton, deleteButton, generateScheduleButton;
    private JTable employeeTable;
    private EmployeeManager employeeManager;

    public EmployeeGUI() {
        employeeManager = new EmployeeManager(); // Initialize employee manager to load from file
        createGUI();
    }

    private void createGUI() {
        frame = new JFrame("Employee Scheduler");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        // Buttons for managing employees
        addButton = new JButton("Add Employee");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEmployee();
            }
        });

        changeButton = new JButton("Change Employee");
        changeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeEmployee();
            }
        });

        deleteButton = new JButton("Delete Employee");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteEmployee();
            }
        });

        generateScheduleButton = new JButton("Generate Schedule");
        generateScheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateSchedule();
            }
        });

        // Table to display employees
        String[] columnNames = {"Name", "ID", "Start Time", "End Time"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        employeeTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(employeeTable);

        // Layout components
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(changeButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(generateScheduleButton);

        // Arrange components in layout
        frame.setLayout(new BorderLayout());
        frame.add(tableScrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Add the schedule panel for the timeline visualization
        frame.add(new EmployeeSchedulePanel(employeeManager.getEmployees()), BorderLayout.NORTH);  // Timeline above the table

        frame.setVisible(true);
        loadEmployeeData();  // Load saved employee data from file when GUI is created
    }

    private void loadEmployeeData() {
        // Load employees from the manager
        DefaultTableModel tableModel = (DefaultTableModel) employeeTable.getModel();
        for (Employee employee : employeeManager.getEmployees()) {
            tableModel.addRow(new Object[] {
                employee.getName(),
                employee.getId(),
                formatTime(employee.getStartTime()),
                formatTime(employee.getEndTime())
            });
        }
    }

    private void addEmployee() {
        // Show dialog to input name and time range for the new employee
        String name = JOptionPane.showInputDialog(frame, "Enter Employee Name:");
        if (name == null || name.isEmpty()) {
            return; // If no name is provided, do nothing
        }

        String startTimeStr = JOptionPane.showInputDialog(frame, "Enter Start Time (HH:mm):");
        String endTimeStr = JOptionPane.showInputDialog(frame, "Enter End Time (HH:mm):");

        if (startTimeStr == null || endTimeStr == null || startTimeStr.isEmpty() || endTimeStr.isEmpty()) {
            return; // If no time is provided, do nothing
        }

        int startTime = convertTimeToInt(startTimeStr);
        int endTime = convertTimeToInt(endTimeStr);

        // Add employee to the manager (ID is handled automatically by EmployeeManager)
        employeeManager.addEmployee(name, startTime, endTime);

        // Add employee data to the table model
        DefaultTableModel tableModel = (DefaultTableModel) employeeTable.getModel();
        Employee employee = employeeManager.getEmployees().get(employeeManager.getEmployees().size() - 1);
        tableModel.addRow(new Object[]{employee.getName(), employee.getId(), formatTime(employee.getStartTime()), formatTime(employee.getEndTime())});

        JOptionPane.showMessageDialog(frame, "Employee added!");
    }

    private void changeEmployee() {
        JOptionPane.showMessageDialog(frame, "Change employee functionality is not yet implemented.");
    }

    private void deleteEmployee() {
        int row = employeeTable.getSelectedRow();
        if (row != -1) {
            DefaultTableModel tableModel = (DefaultTableModel) employeeTable.getModel();
            // Get the ID of the employee to delete
            int employeeId = (int) tableModel.getValueAt(row, 1);
            employeeManager.deleteEmployeeById(employeeId); // Remove from EmployeeManager
            tableModel.removeRow(row); // Remove from the table

            JOptionPane.showMessageDialog(frame, "Employee deleted!");
        } else {
            JOptionPane.showMessageDialog(frame, "Please select an employee to delete.");
        }
    }

    private void generateSchedule() {
        ScheduleManager scheduleManager = new ScheduleManager();
        List<String> schedule = scheduleManager.generateSchedule(employeeManager.getEmployees());
        StringBuilder scheduleText = new StringBuilder();
        for (String entry : schedule) {
            scheduleText.append(entry).append("\n");
        }
        JOptionPane.showMessageDialog(frame, scheduleText.toString(), "Generated Schedule", JOptionPane.INFORMATION_MESSAGE);
    }

    // Method to convert time string (e.g., "8:45") into an integer (e.g., 845)
    private static int convertTimeToInt(String timeStr) {
        timeStr = timeStr.trim(); // Remove any extra spaces
        String[] timeParts = timeStr.split(":"); // Split by colon

        // Parse hours and minutes
        int hours = Integer.parseInt(timeParts[0]);
        int minutes = (timeParts.length > 1) ? Integer.parseInt(timeParts[1]) : 0; // Default to 0 if no minutes part

        // Combine hours and minutes into HHmm format
        return hours * 100 + minutes;
    }

    // Method to format time back from int to HH:mm string
    private static String formatTime(int time) {
        int hours = time / 100;
        int minutes = time % 100;
        return String.format("%02d:%02d", hours, minutes);
    }

    public static void main(String[] args) {
        new EmployeeGUI();  // Create the EmployeeGUI instance
    }

    // Panel to display employee schedule timeline
    private class EmployeeSchedulePanel extends JPanel {
        private List<Employee> employees;

        public EmployeeSchedulePanel(List<Employee> employees) {
            this.employees = employees;
            setPreferredSize(new Dimension(800, 200)); // Adjust panel size as necessary
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawTimeline(g);
        }

        private void drawTimeline(Graphics g) {
            int panelWidth = getWidth();
            int panelHeight = getHeight();

            // Draw time axis (24-hour timeline)
            g.setColor(Color.BLACK);
            g.drawLine(50, 20, panelWidth - 50, 20);  // Horizontal line for timeline

            // Draw time labels (0 - 24 hours)
            for (int i = 0; i <= 24; i++) {
                int xPosition = 50 + (i * (panelWidth - 100) / 24); // Space each hour evenly
                g.drawString(String.valueOf(i), xPosition, 35);  // Hour labels
            }

            // Draw employee availability blocks
            for (Employee employee : employees) {
                // Calculate the start and end position of the block
                int startX = 50 + (employee.getStartTime() * (panelWidth - 100) / 24);
                int endX = 50 + (employee.getEndTime() * (panelWidth - 100) / 24);
                int yPosition = 50 + (employees.indexOf(employee) * 30); // Vertical position for each employee

                // Draw the block for the employee's availability
                g.setColor(new Color(0, 128, 255, 150));  // Semi-transparent blue
                g.fillRect(startX, yPosition, endX - startX, 20);

                // Draw the employee's name and ID
                g.setColor(Color.BLACK);
                g.drawString(employee.getName() + " (ID: " + employee.getId() + ")", startX, yPosition - 5);
            }
        }
    }
}
