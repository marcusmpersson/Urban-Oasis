package Controllers;

import entities.WidgetEntity;

import java.io.*;
import java.util.ArrayList;

public class LocalFileHandler {

    public ArrayList<WidgetEntity> readLocalFile(String username) {
        try (FileInputStream fis = new FileInputStream("Local_Saves/" + username + ".dat");
             ObjectInputStream ois = new ObjectInputStream(fis)){

            return (ArrayList<WidgetEntity>) ois.readObject();

        } catch (FileNotFoundException e){
            System.out.println("no file found for user: " + username);

        } catch (IOException e) {
            throw new RuntimeException(e);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public void updateLocalFile(ArrayList<WidgetEntity> widgets, String username) {
        try (FileOutputStream fos = new FileOutputStream("Local_Saves/" + username + ".dat");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(widgets);

        } catch (FileNotFoundException e) {
            System.out.println("no file found for user: " + username);
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}