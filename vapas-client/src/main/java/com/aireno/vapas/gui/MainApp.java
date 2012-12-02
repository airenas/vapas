package com.aireno.vapas.gui;

import com.aireno.base.ApplicationContextProvider;
import com.aireno.vapas.gui.main.MainPresenter;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    public void start(Stage stage) throws Exception
    {
        ApplicationContextProvider.init();

        MainPresenter mainPresenter = ApplicationContextProvider.getProvider().getBean("MainPresenter", MainPresenter.class);
        mainPresenter.setStage(stage);
        mainPresenter.show(Constants.MAIN_PRESENTER);

        Scene scene = new Scene(mainPresenter.getView());
        scene.getStylesheets().add("styles.css");
        stage.setScene(scene);
        //stage.setTitle("Vaistų apskaita - VApas");
        stage.show();
    }
}
