package com.learnitbro.testing.tool.file;

import java.net.URL;

public class URLHandler {
	
	 /* Returns true if url is valid */
    public static boolean isURLValid(String url) 
    { 
        /* Try creating a valid URL */
        try { 
            new URL(url).toURI(); 
            
            if(url.startsWith("http") && url.contains("."))
            	return true; 
            else 
            	return false;
        } 
          
        // If there was an Exception 
        // while creating URL object 
        catch (Exception e) { 
            return false; 
        } 
    } 

}
