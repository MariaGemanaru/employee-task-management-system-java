package GraphicalUserInterface;

import BusinessLogic.TasksManagement;
import BusinessLogic.Utility;
import DataAccess.Serialization;
import DataModel.Employee;
import DataModel.SimpleTask;
import DataModel.ComplexTask;
import DataModel.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationGui extends JFrame {
    private Map<Employee, List<Task>> employeeTaskMap;
    private List<Task> allTasks;
    private JTextArea displayArea;
    private TasksManagement tasksManagement;

    public ApplicationGui() {
        this.tasksManagement = Serialization.load();

        setTitle("Task Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 1));

        JButton addEmployeeButton = new JButton("Add Employee");
        JButton assignTaskButton = new JButton("Assign Task");
        JButton viewEmployeesButton = new JButton("View Employees");
        JButton viewAssignedTasksButton = new JButton("View Assigned Tasks");
        JButton calculateWorkDurationButton = new JButton("Calculate Work Duration");
        JButton modifyStatusButton = new JButton("Modify Task Status");
        JButton filterEmployeesButton = new JButton("Filter Employees (WorkDuration>40h)");
        JButton viewTaskStatisticsButton = new JButton("View Task Statistics");

        buttonPanel.add(addEmployeeButton);
        buttonPanel.add(assignTaskButton);
        buttonPanel.add(viewEmployeesButton);
        buttonPanel.add(viewAssignedTasksButton);
        buttonPanel.add(calculateWorkDurationButton);
        buttonPanel.add(modifyStatusButton);
        buttonPanel.add(filterEmployeesButton);
        buttonPanel.add(viewTaskStatisticsButton);

        add(buttonPanel, BorderLayout.WEST);

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        add(scrollPane, BorderLayout.CENTER);

        addEmployeeButton.addActionListener(e -> addEmployee());
        assignTaskButton.addActionListener(e -> assignTaskToEmployee());
        viewEmployeesButton.addActionListener(e -> viewEmployees());
        viewAssignedTasksButton.addActionListener(e -> viewAssignedTasks());
        calculateWorkDurationButton.addActionListener(e -> calculateEmployeeWorkDuration());
        modifyStatusButton.addActionListener(e -> modifyTaskStatus());
        filterEmployeesButton.addActionListener(e -> filterEmployeesByWorkDuration());
        viewTaskStatisticsButton.addActionListener(e -> viewTaskStatistics());

        setVisible(true);
    }

    private void saveData() {
        Serialization.save(tasksManagement);
    }

    private void addEmployee() {
        JPanel panel = new JPanel(new GridLayout(3, 2));
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        panel.add(new JLabel("Employee ID:"));
        panel.add(idField);
        panel.add(new JLabel("Employee Name:"));
        panel.add(nameField);
        int result = JOptionPane.showConfirmDialog(this, panel, "Add Employee", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                if (tasksManagement.getMap().containsKey(new Employee(id, name))) {
                    displayArea.append("Employee with ID " + id + " already exists.\n");
                    return;
                }
                tasksManagement.getMap().put(new Employee(id, name), new ArrayList<>());
                saveData();
                displayArea.append("Employee added: " + name + " (ID: " + id + ")\n");
            } catch (NumberFormatException e) {
                displayArea.append("Invalid input. Please enter a valid number for the Employee ID.\n");
            }
        }
    }

    private void viewAssignedTasks() {
        tasksManagement = Serialization.load();
        displayArea.setText("");
        Map<Employee, List<Task>> employeeTaskMap = tasksManagement.getMap();
        if (employeeTaskMap.isEmpty()) {
            displayArea.append("No employees found.\n");
            return;
        }
        for (Map.Entry<Employee, List<Task>> entry : employeeTaskMap.entrySet()) {
            Employee employee = entry.getKey();
            displayArea.append("Employee: " + employee.getName() + " (ID: " + employee.getIdEmployee() + ")\n");
            for (Task task : entry.getValue()) {
                displayArea.append("  Task ID: " + task.getIdTask() + ", Status: " + task.getStatusTask() + "\n");
                if (task instanceof ComplexTask) {
                    displaySubTasks((ComplexTask) task, 2); //indexare cu 3 spatii
                }
            }
        }
    }

    //metoda recursiva pt a afisa subtask urile identate
    private void displaySubTasks(ComplexTask complexTask, int indentLevel) {
        for (Task subTask : complexTask.getSubTasks()) {
            String indent = "  ".repeat(indentLevel); //identare bazata pe nivel
            displayArea.append(indent + "Subtask ID: " + subTask.getIdTask() + ", Status: " + subTask.getStatusTask() + "\n");
            if (subTask instanceof ComplexTask) {
                displaySubTasks((ComplexTask) subTask, indentLevel + 2); // recursiv
            }
        }
    }

    private void viewEmployees() {
        tasksManagement = Serialization.load();
        displayArea.setText("");
        for (Employee employee : tasksManagement.getMap().keySet()) {
            displayArea.append("Employee ID: " + employee.getIdEmployee() + ", Name: " + employee.getName() + "\n");
        }
    }

    private void viewTasks() {
        tasksManagement = Serialization.load();
        displayArea.setText("");
        for (Task task : tasksManagement.getAllTasks()) {
            displayArea.append("Task ID: " + task.getIdTask() + ", Status: " + task.getStatusTask() + "\n");
        }
    }
    private void assignTaskToEmployee() {
        JPanel panel = new JPanel(new GridLayout(7, 2));
        JTextField employeeIdField = new JTextField();
        JTextField taskIdField = new JTextField();
        JTextField startField = new JTextField();
        JTextField endField = new JTextField();

        JComboBox<String> taskStatus = new JComboBox<>(new String[]{"Completed", "Uncompleted"});
        JComboBox<String> taskTypeComboBox = new JComboBox<>(new String[]{"SimpleTask", "ComplexTask"});
        taskTypeComboBox.setSelectedIndex(0);

        panel.add(new JLabel("Employee ID:"));
        panel.add(employeeIdField);
        panel.add(new JLabel("Task ID:"));
        panel.add(taskIdField);
        panel.add(new JLabel("Task Status:"));
        panel.add(taskStatus);
        panel.add(new JLabel("Start Hour:"));
        panel.add(startField);
        panel.add(new JLabel("End Hour:"));
        panel.add(endField);
        panel.add(new JLabel("Task Type:"));
        panel.add(taskTypeComboBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add & Assign Task", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int employeeId = Integer.parseInt(employeeIdField.getText());
                int taskId = Integer.parseInt(taskIdField.getText());
                String status = (String) taskStatus.getSelectedItem();
                Task task;

                if ("ComplexTask".equals(taskTypeComboBox.getSelectedItem())) {
                    task = new ComplexTask(taskId, status, new ArrayList<>());
                    addSubTasks((ComplexTask) task);  //adaug recursiv subtask uri
                } else {
                    int startHour = Integer.parseInt(startField.getText());
                    int endHour = Integer.parseInt(endField.getText());
                    task = new SimpleTask(taskId, status, startHour, endHour);
                }

                tasksManagement.getAllTasks().add(task);
                tasksManagement.assignTaskToEmployee(employeeId, task);
                saveData();
                displayArea.append("Task ID " + taskId + " added and assigned to Employee ID " + employeeId + "\n");
            } catch (NumberFormatException e) {
                displayArea.append("Invalid input. Please enter valid numeric values.\n");
            }
        }
    }
    private void addSubTasks(ComplexTask parentTask) {
        while (true) {
            int option = JOptionPane.showConfirmDialog(this, "Would you like to add a subtask to Task ID: " + parentTask.getIdTask() + "?", "Add Subtask", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.NO_OPTION) {
                break;
            }

            JPanel subTaskPanel = new JPanel(new GridLayout(6, 2));
            JTextField subTaskIdField = new JTextField();
            JTextField startField = new JTextField();
            JTextField endField = new JTextField();

            JComboBox<String> subTaskStatus = new JComboBox<>(new String[]{"Completed", "Uncompleted"});
            JComboBox<String> subTaskTypeComboBox = new JComboBox<>(new String[]{"SimpleTask", "ComplexTask"});
            subTaskTypeComboBox.setSelectedIndex(0);

            subTaskPanel.add(new JLabel("SubTask ID:"));
            subTaskPanel.add(subTaskIdField);
            subTaskPanel.add(new JLabel("SubTask Status:"));
            subTaskPanel.add(subTaskStatus);
            subTaskPanel.add(new JLabel("Start Hour:"));
            subTaskPanel.add(startField);
            subTaskPanel.add(new JLabel("End Hour:"));
            subTaskPanel.add(endField);
            subTaskPanel.add(new JLabel("SubTask Type:"));
            subTaskPanel.add(subTaskTypeComboBox);

            int subTaskResult = JOptionPane.showConfirmDialog(this, subTaskPanel, "Add SubTask", JOptionPane.OK_CANCEL_OPTION);
            if (subTaskResult == JOptionPane.OK_OPTION) {
                try {
                    int subTaskId = Integer.parseInt(subTaskIdField.getText());
                    String status = (String) subTaskStatus.getSelectedItem();
                    Task subTask;

                    if ("ComplexTask".equals(subTaskTypeComboBox.getSelectedItem())) {
                        subTask = new ComplexTask(subTaskId, status, new ArrayList<>());
                        addSubTasks((ComplexTask) subTask);  // Recursive call for deeper levels
                    } else {
                        int startHour = Integer.parseInt(startField.getText());
                        int endHour = Integer.parseInt(endField.getText());
                        subTask = new SimpleTask(subTaskId, status, startHour, endHour);
                    }

                    parentTask.getSubTasks().add(subTask);
                    displayArea.append("Subtask ID " + subTaskId + " added to Complex Task ID " + parentTask.getIdTask() + "\n");

                } catch (NumberFormatException e) {
                    displayArea.append("Invalid input. Please enter valid numeric values.\n");
                }
            }
        }
    }

    //durata timpului de munca
    //tre sa iau in calcul si durata subtask urilor
    //clasa pt a calcula durata ce include si subtask urile
    private void calculateEmployeeWorkDuration() {
        String input = JOptionPane.showInputDialog(this, "Enter Employee ID:");
        if (input != null && !input.isEmpty()) {
            try {
                int idEmployee = Integer.parseInt(input);
                int totalDuration = tasksManagement.calculateEmployeeWorkDuration(idEmployee); //apelez tasksManag unde am metoda de calculare a timpului de lucru

                if (totalDuration > 0) { //afisare rezultat
                    displayArea.append("Employee with ID " + idEmployee + " worked for " + totalDuration + " hours.\n");
                } else {
                    displayArea.append("No tasks completed for Employee ID " + idEmployee + " or employee not found.\n");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for the Employee ID.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void modifyTaskStatus() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        JTextField employeeIdField = new JTextField();
        JTextField taskIdField = new JTextField();

        panel.add(new JLabel("Employee ID:"));
        panel.add(employeeIdField);
        panel.add(new JLabel("Task ID:"));
        panel.add(taskIdField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Modify Task Status", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int employeeId = Integer.parseInt(employeeIdField.getText());
                int taskId = Integer.parseInt(taskIdField.getText());
                tasksManagement.modifyTaskStatus(employeeId, taskId);
                saveData();
                displayArea.setText("Task status modified successfully.\n");
            } catch (NumberFormatException e) {
                displayArea.append("Please enter valid numeric values.\n");
            }
        }
    }

    private void filterEmployeesByWorkDuration() {
        displayArea.setText("");
        Map<Employee, List<Task>> taskMap = tasksManagement.getMap();
        Utility.sortingEmployeesByWorkDuration(taskMap, tasksManagement);

        displayArea.append("Employees with work duration > 40h, sorted by work duration:\n");
        for (Employee employee : taskMap.keySet()) {
            int totalDuration = tasksManagement.calculateEmployeeWorkDuration(employee.getIdEmployee());
            if (totalDuration > 40) {
                displayArea.append(employee.getName() + " - " + totalDuration + " hours\n");
            }
        }
    }

    private void viewTaskStatistics() {
        displayArea.setText("");
        Map<String, Map<String, Integer>> statusCount = Utility.countTaskStatus(tasksManagement.getMap());
        displayArea.append("Task status statistics:\n");

        for (Map.Entry<String, Map<String, Integer>> entry : statusCount.entrySet()) {
            String employeeName = entry.getKey();
            Map<String, Integer> statusMap = entry.getValue();
            displayArea.append("Employee: " + employeeName + "\n");
            displayArea.append("  Completed tasks: " + statusMap.getOrDefault("Completed", 0) + "\n");
            displayArea.append("  Uncompleted tasks: " + statusMap.getOrDefault("Uncompleted", 0) + "\n");
        }
    }
    public static void main(String[] args) {
        new ApplicationGui();
    }
}