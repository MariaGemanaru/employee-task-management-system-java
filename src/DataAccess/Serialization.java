package DataAccess;

import java.io.*;

import BusinessLogic.TasksManagement;

public class Serialization {

    public static void save(TasksManagement tasksManagement) {
        try (FileOutputStream fileOutputStream = new FileOutputStream("tasks_info.ser");
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(tasksManagement);
        } catch (IOException e) {
            System.out.println("Eroare la salvarea datelor: " + e.getMessage());
        }
    }

    public static TasksManagement load() {
        try (FileInputStream fileInputStream = new FileInputStream("tasks_info.ser");
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            return (TasksManagement) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            return new TasksManagement();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Eroare la încărcarea datelor: " + e.getMessage());
            return new TasksManagement();
        }
    }
}
