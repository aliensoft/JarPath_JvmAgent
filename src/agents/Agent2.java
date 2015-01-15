package agents;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Agent2 {
    @SuppressWarnings("rawtypes")
    public static void agentmain(String args, Instrumentation inst) throws IOException, URISyntaxException{
        
        CodeSource codeSource =  Agent2.class.getProtectionDomain().getCodeSource();
        File file = new File(codeSource.getLocation().toURI().getPath());
        String path = file.getAbsolutePath();
        System.out.println("load jar: " + path);
        
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(new Date()));
        String logFileName = file.getPath().replace("JvmAgent.jar", "") + "/" + df.format(new Date()) + ".txt";
        System.out.println("logFileName: " + logFileName);
        
        FileWriter fileWriter = new FileWriter(logFileName);
        
        Class[] classes = inst.getAllLoadedClasses();
        for(Class cls :classes){
        	ClassLoader clor = cls.getClassLoader();
        	if(clor != null) {
	        	String className1 = cls.getCanonicalName();
	        	if(className1 != null) {
		        	String className = className1.replace(".", "/") + ".class";
		        	System.out.println(className);
		            URL r = clor.getResource(className);
		            System.out.println(r);
		            if(r != null)
		            	fileWriter.write(r.toString() + "\n");
	        	}
        	}
        }
        fileWriter.flush();
        fileWriter.close();
    }
}