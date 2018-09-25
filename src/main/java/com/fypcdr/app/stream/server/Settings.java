package com.fypcdr.app.stream.server;

/**
 *
 * @author Lahiru Kaushalya
 */
public interface Settings {
    
    //Server ip address
    final String ipAddress = "localhost";
    
    //Server port
    final int port = 8080;
    
    //request timeout 60s
    final long timeout = 60000;
    
    //JSON object separator
    final String jsonSeparator = "/";
}
