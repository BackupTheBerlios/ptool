
package de.partysoke.psagent.util;

import java.io.*;

import de.partysoke.psagent.*;

public class OpenUrl
{   


    public static void openURL(String url)
    {
        boolean windows = isWindowsPlatform();
        String cmd = null;

        try
        {
            if (windows)
            {
                // cmd = 'rundll32 url.dll,FileProtocolHandler http://...'
                cmd = Define.getWIN_PATH() + " " + Define.getWIN_FLAG() + " " + url;
                Process p = Runtime.getRuntime().exec(cmd);
            }
            else
            {

                // cmd = 'netscape -remote openURL(http://www.javaworld.com)'
                cmd = Define.getUNIX_PATH() + " " + Define.getUNIX_FLAG() + "(" + url + ")";
                Process p = Runtime.getRuntime().exec(cmd);

                try
                {
                    // wait for exit code -- if it's 0, command worked,
                    // otherwise we need to start the browser up.
                    int exitCode = p.waitFor();

                    if (exitCode != 0)
                    {
                        // Command failed, start up the browser

                        // cmd = 'netscape http://www.javaworld.com'
                        cmd = Define.getUNIX_PATH() + " "  + url;
                        p = Runtime.getRuntime().exec(cmd);
                    }
                }
                catch(InterruptedException x)
                {
                    //System.err.println("Error bringing up browser, cmd='" +
                    //                   cmd + "'");
                    //System.err.println("Caught: " + x);
                }
            }
        }
        catch(IOException x)
        {
            //System.err.println("Could not invoke browser, command=" + cmd);
            if (!isWindowsPlatform()) useAltBrowser(url);
        }
    }

    public static void useAltBrowser(String url) {
        String cmd = null;

        try
        {
           // cmd = 'mozilla -remote openURL(http://www.javaworld.com)'
           cmd = Define.getUNIX_ALT_PATH() + " " + Define.getUNIX_FLAG() + "(" + url + ")";
           Process p = Runtime.getRuntime().exec(cmd);

           try
           {
                    // wait for exit code -- if it's 0, command worked,
                    // otherwise we need to start the browser up.
                    int exitCode = p.waitFor();

                    if (exitCode != 0)
                    {
                        // Command failed, start up the browser

                        // cmd = 'netscape http://www.javaworld.com'
                        cmd = Define.getUNIX_ALT_PATH() + " "  + url;
                        p = Runtime.getRuntime().exec(cmd);
                    }
                }
                catch(InterruptedException x)
                {
                    //System.err.println("Error bringing up browser, cmd='" +
                    //                   cmd + "'");
                    //System.err.println("Caught: " + x);
                }
        }
        catch(IOException x)
        {
            //System.err.println("Could not invoke browser, command=" + cmd);
            if (!isWindowsPlatform()) useAlt2Browser(url);
        }
        
    }
    
    public static void useAlt2Browser(String url) {
        String cmd = null;

        try
        {
           // cmd = 'mozilla -remote openURL(http://www.javaworld.com)'
           cmd = Define.getUNIX_ALT2_PATH() + " " + Define.getUNIX_FLAG() + "(" + url + ")";
           Process p = Runtime.getRuntime().exec(cmd);

           try
           {
                    // wait for exit code -- if it's 0, command worked,
                    // otherwise we need to start the browser up.
                    int exitCode = p.waitFor();

                    if (exitCode != 0)
                    {
                        // Command failed, start up the browser

                        // cmd = 'netscape http://www.javaworld.com'
                        cmd = Define.getUNIX_ALT2_PATH() + " "  + url;
                        p = Runtime.getRuntime().exec(cmd);
                    }
                }
                catch(InterruptedException x)
                {
                    //System.err.println("Error bringing up browser, cmd='" +
                    //                   cmd + "'");
                    //System.err.println("Caught: " + x);
                    
                }
        }
        catch(IOException x)
        {
            //System.err.println("Could not invoke browser, command=" + cmd);
            if (!isWindowsPlatform()) useAlt3Browser(url);
        }
        
    }
    
    public static void useAlt3Browser(String url) {
        String cmd = null;

        try
        {
           // cmd = 'mozilla -remote openURL(http://www.javaworld.com)'
           cmd = Define.getUNIX_ALT3_PATH() + " " + url ;
           Process p = Runtime.getRuntime().exec(cmd);

           try
           {
                    // wait for exit code -- if it's 0, command worked,
                    // otherwise we need to start the browser up.
                    int exitCode = p.waitFor();

                    if (exitCode != 0)
                    {
                        // Command failed, start up the browser

                        // cmd = 'netscape http://www.javaworld.com'
                        cmd = Define.getUNIX_ALT3_PATH() + " "  + url;
                        p = Runtime.getRuntime().exec(cmd);
                    }
                }
                catch(InterruptedException x)
                {
                    System.err.println("Error bringing up browser, cmd='" +
                                       cmd + "'");
                    System.err.println("Caught: " + x);
                    
                }
        }
        catch(IOException x)
        {
            System.err.println("Could not invoke browser, command=" + cmd);
        }
        
    }
    
    /**
     * Try to determine whether this application is running under Windows
     * or some other platform by examing the "os.name" property.
     *
     * @return true if this application is running under a Windows OS
     * @since 30.03
     */
    public static boolean isWindowsPlatform()
    {
        String os = System.getProperty("os.name");

        if ( os != null && os.startsWith(Define.getWIN_ID()))
            return true;
        else
            return false;
    }


}


