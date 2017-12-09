package ZUTK.B5.OrgDat.Model.OrgManagement;

import edu.duke.*;
import java.util.ArrayList;
import java.io.File;
public class LogFile {
	public String readFile(String org_name,String date) {
	    try{
	        StringBuilder s = new StringBuilder("");
	        FileResource fr =new FileResource("~/OrgDat/webapps/Org-Dat/Log/"+org_name+"/"+date+".log");
	        for(String line : fr.lines()){
	            s.append(line+"\n");
	        }
	        return s.toString();
	    }catch(Exception e){
	        return null;
	    }
	}
	
	public String getLogFileNames(String org_name){
	    try{
	        ArrayList<String> logFileNameList= new ArrayList<String>();
	        File directoy = new File("~");
	        return logFileNameList.toString();
	    }catch(Exception e){
	        return null;
	    }
	}
	
	public boolean logFiledelete(String org_name,String date){
	    try{
	        return true;
	    }catch(Exception e){
	        return false;
	    }
	}
}