package packager;

public class Employee {
    private String name;
    private int id;
    private int startTime;
    private int endTime;

    // Constructor for Employee class
    public Employee(String name, int id, int startTime, int endTime) {
        this.name = name;
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getter for Name
    public String getName() {
        return name;
    }

    // Setter for Name
    public void setName(String name) {
        this.name = name;
    }

    // Getter for ID (change this to getId())
    public int getId() {
        return id;
    }

    // Setter for ID
    public void setId(int id) {
        this.id = id;
    }

    // Getter for Start Time (change this to getStartTime())
    public int getStartTime() {
        return startTime;
    }

    // Setter for Start Time
    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    // Getter for End Time (change this to getEndTime())
    public int getEndTime() {
        return endTime;
    }

    // Setter for End Time
    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    // Method to convert time to string format (HH:mm)
    public static String formatTime(int time) {
        int hours = time / 100;
        int minutes = time % 100;
        return String.format("%02d:%02d", hours, minutes);
    }

    @Override
    public String toString() {
        return "Employee{name='" + name + "', id=" + id + ", startTime=" + startTime + ", endTime=" + endTime + "}";
    }
}
