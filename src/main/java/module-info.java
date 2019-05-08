/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

module com.acvitech.spa4j {
    
    exports com.acvitech.spa4j.jfx;
    exports com.acvitech.spa4j.support;
    
    
    
    requires javafx.controlsEmpty;
    requires javafx.controls;
    requires javafx.graphicsEmpty;
    requires javafx.graphics;
    requires javafx.baseEmpty;
    requires javafx.base;
    requires javafx.webEmpty;
    requires javafx.web;
    requires javafx.mediaEmpty;
    requires javafx.media;
    requires java.desktop;
    requires java.prefs;
    requires jdk.jsobject;
}
