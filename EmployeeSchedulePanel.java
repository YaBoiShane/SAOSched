package packager;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class EmployeeSchedulePanel extends JPanel {
    private List<Employee> employees;

    public EmployeeSchedulePanel(List<Employee> employees) {
        this.employees = employees;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Define the width and height of the panel
        int panelHeight = 200;
        int timelineWidth = getWidth();

        // Draw the timeline grid (hours)
        int hours = 24;
        int hourWidth = timelineWidth / hours;
        
        // Draw the hourly labels
        for (int i = 0; i < hours; i++) {
            g.drawLine(i * hourWidth, 0, i * hourWidth, panelHeight); // vertical grid line
            g.drawString(String.format("%02d:00", i), i * hourWidth + 5, 15); // hour labels
        }

        // Draw each employee's availability as a colored block
        for (Employee employee : employees) {
            int start = employee.getStartTime(); // Start time in minutes
            int end = employee.getEndTime(); // End time in minutes
            int startX = (start / 60) * hourWidth + (start % 60) * hourWidth / 60;
            int width = (end - start) * hourWidth / 60;

            // Draw a colored block for the employee's availability
            g.setColor(Color.GREEN); // Set block color
            g.fillRect(startX, 30, width, panelHeight - 50); // Draw block

            // Draw employee's name inside the block
            g.setColor(Color.BLACK);
            g.drawString(employee.getName(), startX + 5, 60); // Place name inside the block
        }
    }
}