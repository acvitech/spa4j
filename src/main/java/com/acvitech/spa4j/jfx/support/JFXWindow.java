/*
 * Copyright 2019 acvitech.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.acvitech.spa4j.jfx.support;

import com.acvitech.spa4j.util.RuntimeSettings;
import static com.acvitech.spa4j.util.SPA4JLogger.debug;
import static com.acvitech.spa4j.util.SPA4JLogger.error;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import static javafx.scene.paint.Color.web;
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
            stage.setTitle(RuntimeSettings.getApplicationTitle());
            stage.getIcons().add(new Image(getClass().getResourceAsStream(RuntimeSettings.getApplicationIconURI())));
        } catch (Exception ex) {
            error(ex);
        }
        BrowserRegion browser = new BrowserRegion(stage, getHostServices());
        Scene scene = new Scene(browser, RuntimeSettings.getWinWidth(), RuntimeSettings.getWinHeight(), web(RuntimeSettings.getWinColor()));
        stage.setScene(scene);

        stage.setOnCloseRequest((event) -> {
            boolean isCloseAllowed = browser.isCloseAllowed();
            debug("isCloseAllowed() = " + isCloseAllowed);
            if (!isCloseAllowed) {
                event.consume();
            } else {
                debug("Closing the application...");
            }
        });
        RuntimeSettings.initBrowserObjects(browser,stage,scene);
        stage.show();
    }

}
