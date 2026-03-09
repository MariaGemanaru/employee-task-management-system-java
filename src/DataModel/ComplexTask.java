package DataModel;

import java.util.ArrayList;
import java.util.List;

public non-sealed class ComplexTask extends Task{
    private List<Task> subTasks;

    public ComplexTask(int idTask, String statusTask, List<Task> subTasks) {
        super(idTask, statusTask);
        this.subTasks = new ArrayList<>();
    }
    public void addTask(Task task){
        subTasks.add(task);
    }
    public void deleteTask(Task task){
        subTasks.remove(task);
    }

    @Override
    public int estimateDuration() {
        int totalDuration=0;
        for(Task task: subTasks){
            totalDuration +=task.estimateDuration();
        }
        return totalDuration;
    }

    @Override
    public String toString() {
        return "ComplexTask{" +
                "idTask=" + getIdTask() +
                ", statusTask='" + getStatusTask() + '\'' +
                "subTasks=" + subTasks +
                '}';
    }

    public List<Task> getSubTasks() {
        return subTasks;
    }
}
