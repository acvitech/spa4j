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
import com.acvitech.spa4j.util.Bean;
import com.acvitech.spa4j.util.SPA4JLogger;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import javafx.application.HostServices;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSException;
import netscape.javascript.JSObject;

/**
 *
 * @author avikarz
 */
public class BrowserRegion extends Region {


    private HostServices hostServices;
    private JSObject window;
    private Stage stage;
    private WebView webView = new WebView();
   

    
    
    /**
     * Method to enables Firebug Lite for debugging a webEngine.
     *
     * @param engine the webEngine for which debugging is to be enabled.
     */
    private static void enableFirebug(final WebEngine engine) {
        engine.executeScript("if (!document.getElementById('FirebugLite')){"
                + "E = document['createElement' + 'NS'] && document.documentElement.namespaceURI;"
                + "E = E ? document['createElement' + 'NS'](E, 'script') :"
                + "document['createElement']('script');"
                + "E['setAttribute']('id', 'FirebugLite');"
                + "E['setAttribute']('src', 'https://getfirebug.com/' + 'firebug-lite.js' + '#startOpened');"
                + "E['setAttribute']('FirebugLite', '4');"
                + "(document['getElementsByTagName']('head')[0] || document['getElementsByTagName']('body')[0]).appendChild(E);"
                + "E = new Image;E['setAttribute']('src', 'https://getfirebug.com/' + '#startOpened');}"
        );
    }


    public BrowserRegion(Stage stage,HostServices hostServices){
        this();
        this.stage = stage;
        this.hostServices = hostServices;
    }

    public boolean isCloseAllowed(){ 
        boolean isAllowed = true;
        
        try{
            isAllowed = (Boolean) window.eval("isCloseAllowed()");
        }catch(JSException ex){
            SPA4JLogger.debug("isCloseAllowed() is not defined in HTML DOM."
                    + " Define it if you wish to take a decision on whether to "
                    + "allow a user to close the window or not.");
        }catch(Exception ex){
            SPA4JLogger.error(ex);
        }
        
        if(isAllowed){
            RuntimeSettings.getBeans().entrySet().forEach((Map.Entry<String, Bean> es) -> {
                try{
                    es.getValue().destroy();
                }catch(Exception ex){
                    SPA4JLogger.error("Error calling destroy() on "+es.getKey()+" bean\n"+ex);
                }
            });             
        }
        return isAllowed;    
    }
    
    
    
    private BrowserRegion() {
        getStyleClass().add("browser");
        URL url = null;

        getWebEngine().getLoadWorker().stateProperty().addListener((ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) -> {

            SPA4JLogger.debug("Browser Event:" + observable);

            if(observable.getValue() == observable.getValue().SUCCEEDED){
                
                SPA4JLogger.debug("Setting up Java-JS bridge");
                
                window = (JSObject) getWebEngine().executeScript("window");
                window.setMember("logger", SPA4JLogger.getLogger());

                RuntimeSettings.getBeanClasses().entrySet().forEach((Map.Entry<String, Class> es) -> {

                    try {
                        Bean beanObject = (Bean) es.getValue().getDeclaredConstructor().newInstance(); 
                        window.setMember(es.getKey(), beanObject);
                        beanObject.init();
                        RuntimeSettings.addBean(es.getKey(), beanObject);
                    } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                        SPA4JLogger.error(ex);
                    }

                });

                if(SPA4JLogger.isDebugEnabled() || SPA4JLogger.isBuildDebugEnabled()){
                    
                    getWebEngine().executeScript(
                        "console_log = console.log;"+
                        "console.log = function(message){ console_log(message); debugManager.log('Log: '+message);};"
                    );
                    getWebEngine().executeScript(
                        "console_error = console.error;"+
                        "console.error = function(message){ console_error(message); debugManager.error('Error: '+message); };"
                    );
                    getWebEngine().executeScript(
                        "console_debug = console.debug;"+
                        "console.debug = function(message){console_debug(message); debugManager.debug('Debug: '+message); };"
                    );
                    getWebEngine().executeScript(
                        "console_warn = console.warn;"+
                        "console.warn= function(message){console_warn(message); debugManager.warn('Warn: '+message); };"
                    );
                    
                    enableFirebug(getWebEngine());

                }else{
                    getWebEngine().executeScript(
                        "console.log = function(message){};"+
                        "console.error = function(message){};"+
                        "console.debug = function(message){};"+
                        "console.warn = function(message){};"
                    );         
                }
            }
        });

        try {
            if (SPA4JLogger.isDebugEnabled()) {
                url = new URL(RuntimeSettings.getSpaDebugURL());  
            } else {
                url = getClass().getResource(RuntimeSettings.getSpaInternalURL());
            }

        } catch (MalformedURLException ex) {
            SPA4JLogger.log(ex);
        }

        if (url != null) {
            String urlToLoad = url.toExternalForm();
            SPA4JLogger.log("Loding HTML application from :"+ urlToLoad);
            getWebEngine().load(urlToLoad);
        } else {
            
        }
        getChildren().add(webView);
    }

   

     /*
      * Method to define the layout of childre(BrowserRegion)
      * */
    @Override
    protected void layoutChildren() {
        double w = getWidth();
        double h = getHeight();
        layoutInArea(webView, 0, 0, w, h, 0, HPos.CENTER, VPos.CENTER);
    }

    /*
     * (non-Javadoc)
     * @see javafx.scene.layout.Region#computePrefWidth(double)
     */
    @Override
    protected double computePrefWidth(double height) {
        return 1_024;
    }

    /*
     * (non-Javadoc)
     * @see javafx.scene.layout.Region#computePrefHeight(double)
     */
    @Override
    protected double computePrefHeight(double width) {
        return 768;
    }

    public HostServices getHostServices() {
        return hostServices;
    }

    public JSObject getWindow() {
        return window;
    }

    public Stage getStage() {
        return stage;
    }

    public WebView getWebView() {
        return webView;
    }

    public WebEngine getWebEngine() {
        return webView.getEngine();
    }
    
    
}
