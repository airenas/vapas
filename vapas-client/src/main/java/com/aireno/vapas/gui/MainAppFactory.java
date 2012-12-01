package com.aireno.vapas.gui;

import com.aireno.vapas.gui.base.GuiPresenter;
import com.aireno.vapas.gui.main.MainPresenter;
import javafx.fxml.FXMLLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.io.IOException;

@Configuration
public class MainAppFactory
{
    @Bean
    public MainPresenter mainPresenter()
    {
        return loadPresenter("/fxml/main/Main.fxml");
    }

    @Bean
    public GuiPresenter createPane(String path)
    {
        return loadPresenter(path);
    }

    private <T> T loadPresenter(String fxmlFile)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.load(getClass().getResourceAsStream(fxmlFile));
            return (T) loader.getController();
        }
        catch (IOException e)
        {
            throw new RuntimeException(String.format("Unable to load FXML file '%s'", fxmlFile), e);
        }
    }
}
