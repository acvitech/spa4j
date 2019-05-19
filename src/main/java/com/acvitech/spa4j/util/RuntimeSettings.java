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


package com.acvitech.spa4j.util;

import com.acvitech.spa4j.jfx.support.BrowserRegion;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javafx.application.HostServices;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

/**
 * This class holds default and application specific settings for the application.
 * @author avikarz
 */
public class RuntimeSettings {
    
    protected static String spaDebugURL = "http://localhost:4200";
    protected static String spaInternalURL = "/ui/index.html";
    protected static String applicationTitle = "SPA4J Window";//"Energy Components - Class Editor"
    protected static String applicationIconURI = "/ui/spa4j.png";// "/ui/assets/images/favicon.png"
    protected static String javafxModules="javafx.controls,javafx.web";
    protected static int winHeight = 600;
    protected static int winWidth = 900;
    protected static String winColor = "#666970";
    protected static BrowserRegion browser;
    protected static Stage stage;
    protected static Scene scene;
    
    public static BrowserRegion getBrowser() {
        return browser;
    }
    
    public static void initBrowserObjects(BrowserRegion browser,Stage stage, Scene scene) {
      RuntimeSettings.browser = browser;
      RuntimeSettings.stage = stage;
      RuntimeSettings.scene =  scene;
    }
    
    public static HostServices getHostServices() {
        return browser.getHostServices();
    }    
    
    public static JSObject getJavaScriptWindow() {
        return browser.getWindow();
    }
    
    public static Stage getStage() {
        return stage;
    }
    
    public static Scene getScene(){
        return scene;
    }

    public static WebView getWebView() {
        return browser.getWebView();
    }

    
    protected static Map<String,Class> beanClasses = new LinkedHashMap<>();
    protected static Map<String,Bean> beans = new LinkedHashMap<>();
    protected static List<String> modules = new ArrayList(); 
        
    public static void addRequiredModule(String moduelName){
        if(moduelName!=null && !moduelName.isEmpty()){
            modules.add(moduelName);
        }
    }
        
    public static StringBuilder getRequiredModule(){
        StringBuilder str = new StringBuilder("javafx.controls,javafx.web");
        modules.forEach(item->str.append(",").append(item));
        return str;
    }
        
    public static Map<String, Class> getBeanClasses() {
        return beanClasses;
    }
    
    public static void setupBean(String beanName, Class beanClass) {
        RuntimeSettings.beanClasses.put(beanName, beanClass);
    }
    
    public static Bean getBean(String key) {
        return beans.get(key);
    }
    
    public static void addBean(String key, Bean bean) {
         beans.put(key, bean);
    }
    
    public static Map<String,Bean> getBeans(){
        return beans;
    }
    

    public static String getSpaDebugURL() {
        return spaDebugURL;
    }

    public static void setSpaDebugURL(String spaDebugURL) {
      RuntimeSettings.spaDebugURL = spaDebugURL;
    }

    public static String getSpaInternalURL() {
        return spaInternalURL;
    }

    public static void setSpaInternalURL(String spaInternalURL) {
        RuntimeSettings.spaInternalURL = spaInternalURL;
    }

    public static String getApplicationTitle() {
        return applicationTitle;
    }

    public static void setApplicationTitle(String applicationTitle) {
        RuntimeSettings.applicationTitle = applicationTitle;
    }

    public static String getApplicationIconURI() {
        return applicationIconURI;
    }

    public static void setApplicationIconURI(String applicationIconURI){
        RuntimeSettings.applicationIconURI = applicationIconURI;
    }

    public static String getJavafxModules() {
        return javafxModules;
    }

    public static void setJavafxModules(String javafxModule){
        RuntimeSettings.javafxModules = javafxModule;
    }

    public static int getWinHeight() {
        return winHeight;
    }

    public static void setWinHeight(int winHeight){
            RuntimeSettings.winHeight = winHeight;
    }

    public static int getWinWidth() {
        return winWidth;
    }

    public static void setWinWidth(int winWidth){
        RuntimeSettings.winWidth = winWidth;
    }
    
    public static String getWinColor(){
       return RuntimeSettings.winColor;
    }
    
    public static void setWinColor(String winColor){
        RuntimeSettings.winColor = winColor;      
    }
    
    
    public static boolean isJFXClassesAvailable() {
        try {
            Class.forName("javafx.scene.web.WebView", false, RuntimeSettings.class.getClassLoader());
        } catch (LinkageError | ClassNotFoundException e) {
            return false;
        }
        return true;
    }
}
