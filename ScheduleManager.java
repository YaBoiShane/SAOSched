package packager;

import java.util.*;

public class ScheduleManager {

    // Method to generate a simple schedule for employees based on their availability
    public List<String> generateSchedule(List<Employee> employees) {
        List<String> schedule = new ArrayList<>();

        // Sort employees by start time of their availability
        employees.sort((e1, e2) -> Integer.compare(e1.getStartTime(), e2.getStartTime()));

        // Generate a simple schedule
        for (Employee employee : employees) {
            String entry = employee.getName() + " (ID: " + employee.getId() + ") - Start: " + formatTime(employee.getStartTime()) + " End: " + formatTime(employee.getEndTime());
            schedule.add(entry);
        }

        return schedule;
    }

    // Helper method to format time back from integer to HH:mm format
    private String formatTime(int time) {
        int hours = time / 100;
        int minutes = time % 100;
        return String.format("%02d:%02d", hours, minutes);
    }
}
