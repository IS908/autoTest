package cn.net.cobot.autotest.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class out2log {
	//The defaulted root path of SSLVPN installation 
    private static String rootPath;
    //variable for creating new line
    private final static String enter = System.getProperty("line.separator");
    private static SimpleDateFormat sdf = 
        new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private final static String FILENAME = (new SimpleDateFormat("yyMMdd")).format(new Date()).toString();
    
    public static synchronized void log(String logMessage)
    {
        try
        {
        	rootPath =  System.getProperty("user.dir");
            //System.out.println(rootPath);
            File folder = new File(rootPath);
            if(!folder.exists())
            {
             folder.mkdir();
            }
            String filePath = null;
            if(System.getProperty("os.name").contains("Windows"))
            	filePath = rootPath + "\\" + FILENAME + ".log";
            else 
            	filePath = rootPath + "/" + FILENAME + ".log";
            //System.out.println(filePath);
            File file = new File(filePath);
            if(!file.exists())
            {
             file.createNewFile();
            }
            BufferedReader in = new BufferedReader(new FileReader(file));
            String str = "";
            String strToal = "";
            while ((str = in.readLine()) != null)
            {
                strToal += (str + enter);
            }     
            strToal = strToal + (sdf.format(new Date()) + " " + logMessage + enter);
            in.close();
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write(strToal);
            out.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
