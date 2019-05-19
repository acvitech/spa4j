
package com.acvitech.spa4j.support;

/**
 *
 * @author avikarz
 */
public class ConfigManager {
    
    protected static String spaDebugURL = "http://localhost:4200";
    protected static String spaInternalURL = "/ui/index.html";
    protected static String applicationTitle = "SPA4J Window";//"Energy Components - Class Editor"
    protected static String applicationIconURI = "/ui/spa4j.png";// "/ui/assets/images/favicon.png"
    protected static String javafxModules="javafx.controls,javafx.web";
    
    protected static int winHeight = 600;
    protected static int winWidth = 900;
    protected static String winColor = "#666970";
    
    

    public static String getSpaDebugURL() {
        return spaDebugURL;
    }

    public static void setSpaDebugURL(String spaDebugURL) {
        ConfigManager.spaDebugURL = spaDebugURL;
    }

    public static String getSpaInternalURL() {
        return spaInternalURL;
    }

    public static void setSpaInternalURL(String spaInternalURL) {
        ConfigManager.spaInternalURL = spaInternalURL;
    }

    public static String getApplicationTitle() {
        return applicationTitle;
    }

    public static void setApplicationTitle(String applicationTitle) {
        ConfigManager.applicationTitle = applicationTitle;
    }

    public static String getApplicationIconURI() {
        return applicationIconURI;
    }

    public static void setApplicationIconURI(String applicationIconURI) {
        ConfigManager.applicationIconURI = applicationIconURI;
    }

    public static String getJavafxModules() {
        return javafxModules;
    }

    public static void setJavafxModules(String javafxModules) {
        ConfigManager.javafxModules = javafxModules;
    }

    public static int getWinHeight() {
        return winHeight;
    }

    public static void setWinHeight(int winHeight) {
        ConfigManager.winHeight = winHeight;
    }

    public static int getWinWidth() {
        return winWidth;
    }

    public static void setWinWidth(int winWidth) {
        ConfigManager.winWidth = winWidth;
    }

    public static String getWinColor() {
        return winColor;
    }

    public static void setWinColor(String winColor) {
        ConfigManager.winColor = winColor;
    }
        
    
    
    
}
