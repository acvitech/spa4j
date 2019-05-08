package com.acvitech.spa4j.support;

import static java.lang.System.out;

/**
 *
 * @author avikarz
 */
public class DebugMgmt {

    /**
     * Variable debugEnabled is used to check whether application is opened in debug mode or not 
     */
    private static boolean debugEnabled = false;

    private static DebugMgmt debugMgmt;

    /**
     *
     */
    private static boolean buildDebugEnabled = false;

    public static boolean isDebugEnabled() {
        return debugEnabled;
    }

    public static void setDebugEnabled(boolean isDebugEnabled) {
        debugEnabled = isDebugEnabled;
    }

    public static boolean isBuildDebugEnabled() {
        return buildDebugEnabled;
    }

    public static void setBuildDebugEnabled(boolean isBuildDebugEnabled) {
        buildDebugEnabled = isBuildDebugEnabled;
    }

    
    public static DebugMgmt getDebugMgmt(){
        if(debugMgmt==null){
            debugMgmt = new DebugMgmt();
        }
        return debugMgmt;
    }
    
    
    /**
     * Method to show or print log messages (e.g. console.log() and sysout())
     * @param log
     */
    public static void log(Object msg) {
        out.println("Log: "+ msg);
    }

    
    /**
     * Method to show or print log messages (e.g. console.log() and sysout())
     * @param log
     */
    public static void debug(Object msg) {
        if (debugEnabled || buildDebugEnabled) {
            out.println("Debug: "+msg);
        }
    }
    
    /**
     * Method to show or print log messages (e.g. console.log() and sysout())
     * @param log
     */
    public static void warn(Object msg) {
        out.println("Warn: "+msg);
    }
    
    /**
     * Method to show or print log messages (e.g. console.log() and sysout())
     * @param log
     */
    public static void error(Object msg) {
        out.println("Error: "+msg);
    }
    
    
    private DebugMgmt() {
    }

}
