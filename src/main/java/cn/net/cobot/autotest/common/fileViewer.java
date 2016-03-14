package cn.net.cobot.autotest.common;

import java.io.*;  
import java.util.*;  
  
/** 
 * 读取目录及子目录下指定文件名的路径, 返回一个List 
 */  
  
public class fileViewer {  
  
 /** 
  * @param path 
  *            文件路径 
  * @param suffix 
  *            后缀名, 为空则表示所有文件 
  * @param isdepth 
  *            是否遍历子目录 
  * @return list 
  */  
 public List<String> getListFiles(String path) {  
	  List<String> lstFileNames = new ArrayList<String>();  
	  File file = new File(path);  
	  return listFile(lstFileNames, file, "", false);  
 }  
  
 private List<String> listFile(List<String> lstFileNames, File f, String suffix, boolean isdepth) {  
	  // 若是目录, 采用递归的方法遍历子目录    
	  if (f.isDirectory()) {  
		   File[] t = f.listFiles();  
		     
		   for (int i = 0; i < t.length; i++) {  
			    if (isdepth || t[i].isFile()) {  
			    	listFile(lstFileNames, t[i], suffix, isdepth);  
			    }  
		   }     
	  } else {  
		   String filePath = f.getAbsolutePath();
		   int begIndex = filePath.lastIndexOf("."); // 最后一个.(即后缀名前面的.)的索引  
		   if (!suffix.equals("")) {  
			    
			    String tempsuffix = "";  
			  
			    if (begIndex != -1) {  
				     tempsuffix = filePath.substring(begIndex + 1, filePath.length());  
				     //System.out.println(tempsuffix);
				     if (tempsuffix.equals(suffix)) {  
				    	 lstFileNames.add(filePath);  
				    	 //System.out.println(filePath);
				     }  
			    }  
		   } else {  
			   if (begIndex != -1) {  
				     String tempsuffix = filePath.substring(begIndex + 1, filePath.length());  
				     //System.out.println(tempsuffix);
				     if(tempsuffix.equals("rar"))		System.out.println(filePath + "\t 为rar压缩包，不支持导入");
				     else		lstFileNames.add(filePath);
			   }
			   //System.out.println(filePath);
		   }  
	  }  
	  return lstFileNames;  
	 }  
} 
