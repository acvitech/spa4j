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

package com.acvitech.spa4j.jfx;

import com.acvitech.spa4j.util.RuntimeSettings;
import com.acvitech.spa4j.jfx.support.BrowseJFXDialog;
import static com.acvitech.spa4j.jfx.support.BrowseJFXDialog.getJavaFXLocation;
import static com.acvitech.spa4j.jfx.support.BrowseJFXDialog.getValidJFXLoction;
import static com.acvitech.spa4j.jfx.support.BrowseJFXDialog.isValidJavaFXLocation;
import static com.acvitech.spa4j.jfx.support.BrowseJFXDialog.setJavaFXLocation;
import com.acvitech.spa4j.jfx.support.JFXWindow;
import com.acvitech.spa4j.util.SPA4JLogger;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author avikarz
 */
public class JFXApplication {

    /**
     * This method starts the JavaFX Application
     * @param args not null command line arguments received in main()
     */
    public void launch(String args[]) {

        if (!isValidJavaFXLocation(getJavaFXLocation())) {
            setJavaFXLocation("");
        }

        if (args!=null && args.length > 0) {

            if (args[0].trim().equalsIgnoreCase("debug") || (args[0].trim().equalsIgnoreCase("execReady") && (args.length > 1 && args[1].trim().equalsIgnoreCase("debug")))) {
                SPA4JLogger.setDebugEnabled(true);
            } else if (args[0].trim().equalsIgnoreCase("debug-build") || (args[0].trim().equalsIgnoreCase("execReady") && (args.length > 1 && args[1].trim().equalsIgnoreCase("debug-build")))) {
                SPA4JLogger.setBuildDebugEnabled(true);
            } else if (args[0].trim().equalsIgnoreCase("reset-jfx")) {
                setJavaFXLocation("");
            }

        }

        if ((args!=null && args.length > 0 && args[0].trim().equalsIgnoreCase("execReady")) 
                ||(JAVA_VERSION >= 1.8 && JAVA_VERSION < 11) 
                || RuntimeSettings.isJFXClassesAvailable()) {
            JFXWindow.launch(args);
            System.exit(0);
        }

        if (JAVA_VERSION >= 11) {

            String jfxLocation = getJavaFXLocation();

            if (!(jfxLocation != null && !"".equals(jfxLocation) && isValidJavaFXLocation(jfxLocation))) {

                /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
                 * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
                 */
                try {
                    for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                        if ("Nimbus".equals(info.getName())) {
                            javax.swing.UIManager.setLookAndFeel(info.getClassName());
                            break;
                        }
                    }
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
                    SPA4JLogger.log(ex);
                }
                
                BrowseJFXDialog dialog = new BrowseJFXDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);

            }

            jfxLocation = getJavaFXLocation();

            if (jfxLocation != null && !"".equals(jfxLocation) && isValidJavaFXLocation(jfxLocation)) {

                try {
                    jfxLocation = getValidJFXLoction(jfxLocation);
                    String jarPath = BrowseJFXDialog.class.getProtectionDomain().getCodeSource().getLocation().getPath();
                    jarPath = java.net.URLDecoder.decode(jarPath, StandardCharsets.UTF_8.name());
                    if (System.getProperty("os.name").toLowerCase().contains("win")) {
                        jarPath = jarPath.substring(1);
                    }

                    File jarLocation = new File(jarPath);

                    if (jarLocation.exists()) {

                        String commandToRun = "java " + "--module-path=\"" + jfxLocation + "\"" + " " + "--add-modules=" + RuntimeSettings.getRequiredModule() + " -jar \"" + jarPath + "\"";
                        String argsList = "";
                        for (String arg : args) {
                            argsList += " " + arg;
                        }

                        commandToRun += " execReady " + argsList;

                        final String finalCommand = commandToRun;

                        new Thread() {

                            @Override
                            public void run() {
                                try {
                                    ps = Runtime.getRuntime().exec(finalCommand);
                                } catch (IOException ex) {
                                    SPA4JLogger.error(ex);
                                }
                            }

                        }.start();

                        try {
                            int i;

                            int retries = 20;
                            while (ps == null) {
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException ex) {
                                }
                                if (retries-- < 0) {
                                    System.exit(0);
                                }
                            }

                            InputStream ins = ps.getInputStream();
                            byte[] bytes = new byte[1024];
                            while ((i = ins.read(bytes)) > -1) {
                                SPA4JLogger.log(new String(bytes, 0, i));
                            }
                            ps.destroy();
                        } catch (IOException ex) {
                            SPA4JLogger.error(ex);
                        }
                    } else {
                        SPA4JLogger.error("The Executable at " + jarPath + " is not found");
                    }
                    System.exit(0);
                } catch (UnsupportedEncodingException ex) {
                    SPA4JLogger.error(ex);
                }
            }
        }
    }

    
    
    
    static double getVersion() {
        String version = System.getProperty("java.version");
        int pos = version.indexOf('.');
        if (pos > -1) {
            pos = version.indexOf('.', pos + 1);
        } else {
            pos = version.length();
        }
        return Double.parseDouble(version.substring(0, pos));
    }

    private JFXApplication(){
    
    }
    
    public static JFXApplication prepare(){
        return JFX_APPLICATION;
    }
    
    public JFXApplication setIcon(String url){
        RuntimeSettings.setApplicationIconURI(url);
        return JFX_APPLICATION;
    }
    
    public JFXApplication setTitle(String title){
        RuntimeSettings.setApplicationTitle(title);
        return JFX_APPLICATION;
    }
    
    public JFXApplication setStartUrl(String url){
        RuntimeSettings.setSpaInternalURL(url);
        return JFX_APPLICATION;
    }
    
    public JFXApplication addBean(String beanName,Class clazz){
        RuntimeSettings.setupBean(beanName, clazz);
        return JFX_APPLICATION;
    }
    
    public JFXApplication setDebugURL(String debugURL){
        RuntimeSettings.setSpaDebugURL(debugURL);
        return JFX_APPLICATION;
    }
    
    public JFXApplication setColor(String color){
        RuntimeSettings.setWinColor(color);
        return JFX_APPLICATION;
    }
    
    
    public JFXApplication setWidth(int width){
        RuntimeSettings.setWinWidth(width);
        return JFX_APPLICATION;
    }
    
    public JFXApplication setHeight(int height){
        RuntimeSettings.setWinHeight(height);
        return JFX_APPLICATION;
    }
    
    
    private static final JFXApplication JFX_APPLICATION = new JFXApplication();
    private final static double JAVA_VERSION = getVersion();
    private static Process ps;
    
    
    
}

