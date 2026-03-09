package DataModel;

public non-sealed class SimpleTask extends Task{
    private int startHour;
    private int endHour;

    public SimpleTask(int idTask, String statusTask, int startHour, int endHour) {
        super(idTask, statusTask);
        this.startHour = startHour;
        this.endHour = endHour;
    }
    public int estimateDuration(){
        return endHour-startHour;
    }

    public int getStartHour() {
        return startHour;
    }

    public int getEndHour() {
        return endHour;
    }

    @Override
    public String toString() {
        return "SimpleTask{" +
                "idTask=" +  getIdTask() +
                ", statusTask="+ getStatusTask()+ '\''+
                ", startHour=" + startHour +
                ", endHour=" + endHour +
                '}';
    }
}
