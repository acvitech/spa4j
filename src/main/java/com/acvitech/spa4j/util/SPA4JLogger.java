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

import static java.lang.System.out;

/**
 *
 * @author avikarz
 */
public class SPA4JLogger {

    /**
     * Variable debugEnabled is used to check whether application is opened in debug mode or not 
     */
    private static boolean debugEnabled = false;

    private static SPA4JLogger logger;

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

    
    public static SPA4JLogger getLogger(){
        if(logger==null){
            logger = new SPA4JLogger();
        }
        return logger;
    }
    
    
    /**
     * Method to show or print log messages (e.g. console.log() and sysout()) in case of normal log
     * @param msg this is the information that will be printed
     */
    public static void log(Object msg) {
        out.println("Log: "+ msg);
    }

    
    /**
     * Method to show or print log messages (e.g. console.log() and sysout()) in case of debug
     * @param msg  this is the information that will be printed
     */
    public static void debug(Object msg) {
        if (debugEnabled || buildDebugEnabled) {
            out.println("Debug: "+msg);
        }
    }
    
    /**
     * Method to show or print log messages (e.g. console.log() and sysout()) in case of Warning
     * @param msg this is the information that will be printed
     */
    public static void warn(Object msg) {
        out.println("Warn: "+msg);
    }
    
    /**
     * Method to show or print log messages (e.g. console.log() and sysout()) in case of Error
     * @param msg this is the information that will be printed
     */
    public static void error(Object msg) {
        out.println("Error: "+msg);
    }
    
    
    private SPA4JLogger() {
    }

}
