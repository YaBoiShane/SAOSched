package packager;

import java.io.*;
import java.util.*;

public class EmployeeManager {
    private List<Employee> employees;

    // Constructor accepting List<Employee>
    public EmployeeManager(List<Employee> employees) {
        this.employees = employees;
    }
    public EmployeeManager() {
        this.employees = new ArrayList<>();
        loadEmployeesFromFile();
    }

    // Method to load employees from a file on startup
    public void loadEmployeesFromFile() {
        File file = new File("employees.txt"); // Path to the file
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(", "); // Assuming comma is the delimiter
                    if (parts.length == 4) {
                        String name = parts[0];
                        int id = Integer.parseInt(parts[1]);
                        int startTime = Integer.parseInt(parts[2]);
                        int endTime = Integer.parseInt(parts[3]);

                        // Create new Employee and add to the list
                        Employee emp = new Employee(name, id, startTime, endTime);
                        employees.add(emp);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No employee data file found, starting with empty list.");
        }
    }

    // Method to save employees to a file
    public void saveEmployeesToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("employees.txt"))) {
            for (Employee emp : employees) {
                // Save each employee as a line in the file
                writer.write(emp.getName() + ", " + emp.getId() + ", " + emp.getStartTime() + ", " + emp.getEndTime());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to add employee
    public void addEmployee(String name, int startTime, int endTime) {
    	int id = 0; 
        Employee emp = new Employee(name, id, startTime, endTime);
        employees.add(emp);
        saveEmployeesToFile();  // Update file after adding employee
    }

    // Method to delete employee by ID
    public void deleteEmployeeById(int id) {
        employees.removeIf(emp -> emp.getId() == id);
        saveEmployeesToFile();  // Update the file after removal
    }

    // Getter for the list of employees
    public List<Employee> getEmployees() {
        return employees;
    }
}
