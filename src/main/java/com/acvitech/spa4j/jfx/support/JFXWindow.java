package com.acvitech.spa4j.jfx;

import static com.acvitech.spa4j.support.DebugMgmt.debug;
import static com.acvitech.spa4j.support.DebugMgmt.error;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import static javafx.scene.paint.Color.web;
import com.acvitech.spa4j.support.ConfigManager;


import javafx.stage.Stage;

/**
 *
 * @author avikarz
 */
public class JFXWindow extends Application {


    public static void launch(String args[]){
        Application.launch(args);
    }
    
  
    @Override
    public void start(Stage stage) {
        try {
            stage.setTitle(ConfigManager.getApplicationTitle());
            stage.getIcons().add(new Image(getClass().getResourceAsStream(ConfigManager.getApplicationIconURI())));
        } catch (Exception ex) {
            error(ex);
        }
        browser = new BrowserRegion(stage, getHostServices());
        Scene scene = new Scene(browser, ConfigManager.getWinWidth(), ConfigManager.getWinHeight(), web(ConfigManager.getWinColor()));
        stage.setScene(scene);

        stage.setOnCloseRequest((event) -> {

            debug("isCloseAllowed() = " + browser.isCloseAllowed());
            if (!browser.isCloseAllowed()) {
                event.consume();
            } else {
                debug("Closing the application...");
            }
        });

        stage.show();
    }

    public static BrowserRegion getBrowser() {
        return browser;
    }

    private static BrowserRegion browser;

}
