package BusinessLogic;
import DataModel.Employee;
import DataModel.Task;

import java.io.Serializable;
import java.util.*;

public class TasksManagement implements Serializable {
    private Map<Employee, List<Task>> map;

    public TasksManagement() {
        map = new HashMap<>();
    }

    private Employee findEmployeeById(int idEmployee){
        for(Employee employee: map.keySet()){
            if(employee.getIdEmployee()==idEmployee){
                return employee;
            }
        }
        return null;
    }
    //assign task to an employee
    public void assignTaskToEmployee(int idEmployee, Task task){
        Employee employee=findEmployeeById(idEmployee);
        if(employee !=null){
            //daca cheia nu e deja in map, p adaug cu o noua lista goala
            map.putIfAbsent(employee, new ArrayList<>());
            //adaug sarcina la lista existenta
            map.get(employee).add(task);
        }else{
            System.out.println("Employee with ID "+idEmployee+ " not found");
        }
    }

    public int calculateEmployeeWorkDuration(int idEmployee){
        Employee employee=findEmployeeById(idEmployee);
        if(employee!=null && map.containsKey(employee)){
            int totalDuration=0;
            for(Task task: map.get(employee)){
                //folosesc ignore case ca sa se ia in calcul cuvantul indiferent cum ar fi scris
                if("Completed".equalsIgnoreCase(task.getStatusTask())){
                    totalDuration += task.estimateDuration();
                }
            }
            return totalDuration;
        }else{
            System.out.println("Employee with ID "+ idEmployee+" not found or without tasks");
            return 0;
        }
    }

    public  void modifyTaskStatus(int idEmployee, int idTask){
        Employee employee=findEmployeeById(idEmployee);
        if(employee !=null && map.containsKey(employee)){
            for(Task task: map.get(employee)){
                if(task.getIdTask()==idTask){
                    //completed or incompleted
                    String newStatus=task.getStatusTask().equalsIgnoreCase("Completed")? "Uncompleted": "Completed";
                    task.setStatusTask(newStatus);
                    System.out.println("Task ID "+ idTask +" new status is "+ newStatus);
                    return;
                }
            }
            System.out.println("Task with ID " + idTask + " not exist for employee " + idEmployee);
        }
        else{
            System.out.println("Employee with ID "+ idEmployee+" not found ");
        }
    }

    @Override
    public String toString() {
        return "TasksManagement{" +
                "map=" + map +
                '}';
    }

    public List<Task> getAllTasks() {
        List<Task> allTasks = new ArrayList<>();
        for (List<Task> tasks : map.values()) {
            allTasks.addAll(tasks);
        }
        return allTasks;
    }

    public Map<Employee, List<Task>> getMap() {
        return map;
    }

    public void setMap(Map<Employee, List<Task>> map) {
        this.map = map;
    }

    public void addEmployee(Employee employee) {

    }
}
