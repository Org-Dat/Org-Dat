package zutk.b5.orgdat.model.orgmanagement;

import edu.duke.*;
import java.util.*;

import java.io.File;
import org.json.simple.*;
public class LogFile {
	public String readFile(String org_name,String date) {
	    try{
	        JSONArray s = new JSONArray();
	       // StringBuilder s = new StringBuilder("");
	        FileResource fr = new FileResource("Log/"+org_name+"/log_"+date+".log");
	        for(String line : fr.lines()){
	            s.add(line);
	        }
	        return s.toString();
	    }catch(Exception e){
	        e.printStackTrace();
	        return null;
	    }
	}
	
	public String getLogFileNames(String org_name){
	    try{
	        ArrayList<String> logFileNameList= new ArrayList<String>();
	        File directoy = new File("Log/"+org_name);
	        System.out.print("sdddfFWEFWSDWvvvvvfvvvefvefveveveve");
	        return  JSONArray.toJSONString(Arrays.asList(directoy.list()));
	    }catch(Exception e){
	        e.printStackTrace();
	        return null;
	    }
	}
	
	public String logFiledelete(String org_name,String date){
	    boolean work =false;
	    try{
	        work =  new File("Log/"+org_name+"/log_"+date+".log").delete();
	    }catch(Exception e){
	        work = false;
	    } finally {
	        if (work){
	            return "success";
	        } else {
	            return null;
	        }
	    }
	}
}
