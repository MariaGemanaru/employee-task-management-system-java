package BusinessLogic;

import DataModel.Employee;
import DataModel.SimpleTask;
import DataModel.Task;

import java.util.*;

public class Utility {
    //metoda ce filtreaza angajatii cu durata de munca>40h, ii sortez si afisez numele
    public static void sortingEmployeesByWorkDuration(Map<Employee, List<Task>> taskMap, TasksManagement tasksManagement){
        //map cu angajatii si durata de munca
        Map<Employee, Integer> employeeWorkDurationMap=new HashMap<>();
        for(Employee employee: taskMap.keySet()){
            int totalDuration=tasksManagement.calculateEmployeeWorkDuration(employee.getIdEmployee());
            if(totalDuration>40){
                employeeWorkDurationMap.put(employee, totalDuration);
            }
        }
        //sortez angajatii dupa durata lor de munca
        List<Map.Entry<Employee, Integer>> sortedEmployees=new ArrayList<>(employeeWorkDurationMap.entrySet());
        sortedEmployees.sort(Map.Entry.comparingByValue());

        //afisarea numelor angajatilor sortati
        System.out.println("Employees with work duration>40h, sorted by duration work");
        for(Map.Entry<Employee, Integer> entry: sortedEmployees){
            System.out.println(entry.getKey().getName());
        }
    }

    //ii for each employee the num of completed and uncompleted tasks
    //returns a map where key=employee name and
    //the value= map<string, integer>
    //the key repr the possible status of tasks and the vakue=numb of tasks in that category
    public static Map<String, Map<String, Integer>> countTaskStatus(Map<Employee, List<Task>> taskMap){
        Map<String, Map<String, Integer>> result=new HashMap<>();
        for(Map.Entry<Employee, List<Task>> entry: taskMap.entrySet()){
            String employeeName=entry.getKey().getName();
            Map<String, Integer> statusCnt=new HashMap<>();
            statusCnt.put("Completed", 0);
            statusCnt.put("Uncompleted", 0);

            for(Task task: entry.getValue()){
               // statusCnt.put(task.getStatusTask(), statusCnt.get(task.getStatusTask())+1);
                if("Completed".equalsIgnoreCase(task.getStatusTask())){
                    statusCnt.put("Completed", statusCnt.get("Completed")+1);
                }else{
                    statusCnt.put("Uncompleted", statusCnt.get("Uncompleted")+1);
                }
            }
            result.put(employeeName, statusCnt);
        }
        return result;
    }

    //testing
    /*public static void main(String[] args){
        //creare obiecte employee si task
        Employee e1=new Employee(1, "Vali Gemanar");
        Employee e2=new Employee(2, "John Gemanar");

        Task t1= new SimpleTask(22, "Completed", 7, 11);
        Task t2= new SimpleTask(33, "Completed", 8, 10);
        Task t3= new SimpleTask(11, "Uncompleted", 12, 21);

        //creare map pt gestiunea task urilor
        Map<Employee, List<Task>> taskMap=new HashMap<>();
        taskMap.put(e1, Arrays.asList(t1, t2));
        taskMap.put(e2, Arrays.asList(t3));

        //creare taskManagement
        TasksManagement tasksManagement=new TasksManagement(taskMap);

        //testare metode
        sortingEmployeesByWorkDuration(taskMap, tasksManagement);
        Map<String, Map<String, Integer>> statusCnt=countTaskStatus(taskMap);
        System.out.println("Task statuses for employee: "+statusCnt);

    }*/

}
