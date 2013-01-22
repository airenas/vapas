package com.aireno.vapas.gui;

import com.aireno.base.ApplicationContextProvider;
import com.aireno.utils.ADateUtils;
import com.aireno.vapas.gui.main.MainPresenter;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.*;
import java.util.Date;

public class MainApp extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    public void start(Stage stage) throws Exception
    {
        ApplicationContextProvider.init();

        Image ico = new Image("images/Dog.png");
        stage.getIcons().add(ico);

        MainPresenter mainPresenter = ApplicationContextProvider.getProvider().getBean("MainPresenter", MainPresenter.class);
        mainPresenter.setStage(stage);
        mainPresenter.show(Constants.MAIN_PRESENTER);

        Scene scene = new Scene(mainPresenter.getView());
        scene.getStylesheets().add("styles.css");
        scene.getStylesheets().add("/jfxtras/labs/internal/scene/control/CalendarTextField.css");
        stage.setScene(scene);

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());


        stage.show();
        // backup db
        backupDb();
    }

    private void backupDb() {
        backupDb("vapas.script");
        backupDb("vapas.log");
        backupDb("vapas.properties");
    }

    private void backupDb(String file) {
        String fileName = String.format("kopija/%s_%s", file, ADateUtils.dateToString(new Date()));
        String fileNameFrom = String.format("db/%s", file);
        copyFile(fileNameFrom, fileName);
    }

    private void copyFile(String fileNameFrom, String fileName) {
         try{
            File f1 = new File(fileNameFrom);
            File f2 = new File(fileName);
            InputStream in = new FileInputStream(f1);

            //For Append the file.
//  OutputStream out = new FileOutputStream(f2,true);
            //For Overwrite the file.
            OutputStream out = new FileOutputStream(f2);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0){
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            System.out.println("File copied.");
        }
        catch(FileNotFoundException ex){
            System.out.println(ex.getMessage() + " in the specified directory.");
            //System.exit(0);
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
}
