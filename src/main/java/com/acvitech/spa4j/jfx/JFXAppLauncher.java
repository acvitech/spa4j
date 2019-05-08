/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acvitech.spa4j.jfx;

import com.acvitech.spa4j.support.ConfigManager;
import com.acvitech.spa4j.support.DebugMgmt;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.prefs.Preferences;

/**
 *
 * @author avikarz
 */
public class JFXAppLauncher {

    /**
     * @param args the command line arguments
     */
    public static void launch(String args[]) {

        if (!isValidJavaFXLocation(getJavaFXLocation())) {
            setJavaFXLocation("");
        }

        if (args.length > 0) {

            if (args[0].trim().equalsIgnoreCase("debug") || (args.length > 1 && args[1].trim().equalsIgnoreCase("debug"))) {
                DebugMgmt.setBuildDebugEnabled(true);
            } else if (args[0].trim().equalsIgnoreCase("debug-build") || (args.length > 1 && args[1].trim().equalsIgnoreCase("debug-build"))) {
                DebugMgmt.setBuildDebugEnabled(true);
            } else if (args[0].trim().equalsIgnoreCase("reset-jfx")) {
                setJavaFXLocation("");
            }

            if (args[0].trim().equalsIgnoreCase("execReady")) {
                JFXWindow.launch(args);
                System.exit(0);
            }

        }

        if (JAVA_VERSION >= 1.8 && JAVA_VERSION < 11) {
            JFXWindow.launch(args);
            System.exit(0);
        }

        if (JAVA_VERSION >= 11) {

            String jfxLocation = getJavaFXLocation();

            if (!(jfxLocation != null && !"".equals(jfxLocation) && isValidJavaFXLocation(jfxLocation))) {

                /* Set the Nimbus look and feel */
                //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
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
                    DebugMgmt.log(ex);
                }
                //</editor-fold>

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

                        String commandToRun = "java " + "--module-path=\"" + jfxLocation + "\"" + " " + "--add-modules=" + ConfigManager.getJavafxModules() + " -jar \"" + jarPath + "\"";
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
                                    DebugMgmt.error(ex);
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
                                DebugMgmt.log(new String(bytes, 0, i));
                            }
                            ps.destroy();
                        } catch (IOException ex) {
                            DebugMgmt.error(ex);
                        }
                    } else {
                        DebugMgmt.error("The Executable at " + jarPath + " is not found");
                    }
                    System.exit(0);
                } catch (UnsupportedEncodingException ex) {
                    DebugMgmt.error(ex);
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
    
    
        public static String getJavaFXLocation() {
        Preferences pref;
        pref = Preferences.userNodeForPackage(BrowseJFXDialog.class);
        String ojfLocation = System.getenv("JFX_HOME");
        if (ojfLocation == null || "".equals(ojfLocation.trim())) {
            ojfLocation = pref.get("JavaFXModuleLocation", "");
        }
        return ojfLocation;
    }

    public static void setJavaFXLocation(String location) {
        Preferences pref;
        pref = Preferences.userNodeForPackage(BrowseJFXDialog.class);
        pref.put("JavaFXModuleLocation", location);
    }

    

    public static String confirmJavaFXLocation(String fileLocText) {
        if (fileLocText != null && !"".equals(fileLocText.trim())) {
            if (new File(fileLocText + File.separator + "javafx.web.jar").exists() && (new File(fileLocText + File.separator + "javafx.web.jar")).exists()) {
                return new File(fileLocText + File.separator + "..").getAbsolutePath();
            } else if (new File(fileLocText + File.separator + "lib" + File.separator + "javafx.web.jar").exists()
                    && (new File(fileLocText + File.separator + "lib" + File.separator + "javafx.web.jar")).exists()) {
                return new File(fileLocText).getAbsolutePath();
            }
        }
        return fileLocText;
    }

    public static boolean isValidJavaFXLocation(String fileLocText) {
        if (fileLocText != null && !"".equals(fileLocText.trim())) {
            String fileLocation1 = fileLocText + File.separator + "lib" + File.separator + "javafx.web.jar";
            String fileLocation2 = fileLocText + File.separator + "lib" + File.separator + "javafx.controls.jar";
            return (new File(fileLocation1)).exists() && (new File(fileLocation2)).exists();
        }
        return false;
    }

    public static String getValidJFXLoction(String fileLocText) {
        return fileLocText + File.separator + "lib";
    }

    public static double JAVA_VERSION = getVersion();
    private static Process ps;
}
