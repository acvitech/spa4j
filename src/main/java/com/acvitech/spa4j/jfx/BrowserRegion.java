package com.acvitech.spa4j.jfx;


import com.acvitech.spa4j.support.DebugMgmt;
import com.acvitech.spa4j.support.ConfigManager;
import java.net.MalformedURLException;
import java.net.URL;
import javafx.application.HostServices;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
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
    private WebEngine webEngine = webView.getEngine();
   

    
    
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
        return (Boolean) window.eval("isCloseAllowed()");   
    }
    
    
    
    private BrowserRegion() {
        getStyleClass().add("browser");
        URL url = null;
        try {
            if (DebugMgmt.isDebugEnabled()) {
                url = new URL(ConfigManager.getSpaDebugURL());  
            } else {
                url = getClass().getResource(ConfigManager.getSpaInternalURL());
            }

        } catch (MalformedURLException ex) {
            DebugMgmt.log(ex);
        }

        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {


            window = (JSObject) webEngine.executeScript("window");
            window.setMember("debugManager", DebugMgmt.getDebugMgmt());
            
            if(DebugMgmt.isDebugEnabled() || DebugMgmt.isBuildDebugEnabled()){

                DebugMgmt.log("Browser Event:" + observable);
                enableFirebug(webEngine);

//                webEngine.executeScript(
//                    "console.log = function(message){ debugManager.log('Log: '+message);};"
//                );
//                webEngine.executeScript(
//                    "console.error = function(message){ debugManager.error('Error: '+message); };"
//                );
//                webEngine.executeScript(
//                    "console.debug = function(message){ debugManager.debug('Debug: '+message); };"
//                );
//                webEngine.executeScript(
//                    "console.warn= function(message){ debugManager.warn('Warn: '+message); };"
//                );
            }
        });

        if (url != null) {
            webEngine.load(url.toExternalForm());
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
        return webEngine;
    }
    
    
}
