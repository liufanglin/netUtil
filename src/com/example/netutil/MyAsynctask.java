package com.example.netutil;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
/**
 * 
 * @author samsung
 *这是一个是实现异步的类
 *第一个参数是我们要传的path
 *第二个是进度更新的
 *第三个是返回值
 */
public class MyAsynctask extends AsyncTask<String, Integer, String>{
	private Map<String, String> params;
	private BackResult br;
	protected static final String GET="GET";
	protected static final String POST="POST";
	protected static final String SEND="SEND";
	private String json;
	private boolean flag=true;
	private String TAG="信息打印";
	private Context context;
	
	//第一个参数是请求头的参数，第二个是上传文件用的，传一个file对象，传空代表我们不传文件,
	//第三个参数是代表我要发送的请求的类型，第四个是文件参数，第五个是实现的接口回调，通过里面的方法我们拿到返回的结果
	public MyAsynctask(Context context,Map<String, String> params,BackResult br){
		this.params=params;
		this.br=br;
		this.context=context;
	}
	
	public MyAsynctask(Map<String, String> params,BackResult br){
		this.params=params;
		this.br=br;
	}
	/**
	 * 这个方法在调用excute时候调用,用来初始化UI用的
	 */
	 @Override  
     protected void onPreExecute() {  
     }  
	 /**
	  * 这个方法是用来做耗时间的操作的会在onPreExecute()方法之后自动调用
	  * @param params
	  * @return 返回值是我们要的结果 我们这个可以用来返回json数据
	  */
     @Override  
     protected String doInBackground(String... path) {  
    	 if(params.get("isupfile")==null){
    		
    	 if(params.get("type").equals(GET)){
    	
 			try {
 				
 				StringBuilder sb = new StringBuilder();  
 		        sb.append(params.get("path")).append("?");  
 		  	
 		        if (params != null && params.size() != 0) {  
 		        	Set<String> keys=params.keySet();
 		        	Iterator<String> iter=keys.iterator();
 		        	while(iter.hasNext()){
 		        		String key=iter.next();
 		        		if (!key.equals("path")&&!key.equals("type")) {
 		        			String value=params.get(key);
 		        			sb.append(key).append("=").append(URLEncoder.encode(value, "utf-8"));
 		        			sb.append("&");
						}
 		       	}
 		            sb.deleteCharAt(sb.length() - 1);  
 		            System.out.println(sb.toString()+"--------");
 		        }  
 				URL url = new URL(sb.toString());
 				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
 				conn.setRequestMethod("GET");
 				conn.setConnectTimeout(3000);
 				int code = conn.getResponseCode();
 				 System.out.println("------------------"+code);
 				if (200 == code) {
 					InputStream is = conn.getInputStream();
 					
 					json=StreamTools.readStream(is);
 					flag=true;
 					} else {
 						flag=false;
 						json="请求失败";
 							}
 						} catch (Exception e) {
 							flag=false;
 							json=e.getMessage();
 							e.printStackTrace();
 						}
 					}
 	    if (params.get("type").equals(POST)) {
         StringBuilder sb = new StringBuilder(); 
         try {
         if(params!=null &params.size()!=0){  
        	 
        		Set<String> keys=params.keySet();
		        	Iterator<String> iter=keys.iterator();
		        	while(iter.hasNext()){
		        		String key=iter.next();
		        		if (!key.equals("path")&&!key.equals("type")) {
		        			String value=params.get(key);
		        			sb.append(key).append("=").append(URLEncoder.encode(value, "utf-8"));
		        			sb.append("&");
					}
		       	}
             sb.deleteCharAt(sb.length()-1);  
         }  
         byte[] entity = sb.toString().getBytes();  
         
          URL url = new URL(params.get("path"));
          HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
          conn.setConnectTimeout(2000);  
          conn.setRequestMethod("POST");  
          conn.setDoOutput(true);  
          conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");  
          conn.setRequestProperty("Content-Length", entity.length+"");  
          OutputStream out = conn.getOutputStream();  
          out.write(entity);  
          out.flush();    
          out.close();  
          System.out.println(conn.getResponseCode());
          Log.i(TAG, conn.getResponseCode()+"====================");
          if (conn.getResponseCode() == 200) {
        	 flag=true;
         	 InputStream is=conn.getInputStream();
         	 json=StreamTools.readStream(is);
         	 
          }else {
        	InputStream is=conn.getInputStream();
          	json=StreamTools.readStream(is);
        	flag=false;
          }  
         } catch (Exception e) {
        	 flag=false;
        	 json=e.getMessage().toString();
 			e.printStackTrace();
 		}  
      }  
  }else {
	  	 String BOUNDARY = UUID.randomUUID().toString(); 
	     String PREFIX = "--" ;
	     String LINE_END = "\r\n"; 
         StringBuilder sb = new StringBuilder();  
     if(params!=null &params.size()!=0){  
    	Set<String> keys=params.keySet();
     	Iterator<String> iter=keys.iterator();
     	while(iter.hasNext()){
     		String key=iter.next();
     		if (!key.equals("path")&&!key.equals("type")&&!key.equals("isupfile")) {
     			String value=params.get(key);
     			sb.append("--" + BOUNDARY + "\r\n");  
                sb.append("Content-Disposition: form-data; name=\"" + key+"\"\r\n");  
                sb.append("\r\n");  
                sb.append(URLEncoder.encode(value) + "\r\n");
			}
    	 
         }  
     }
     byte[] entity = sb.toString().getBytes();  
     try {
           URL url=new URL(params.get("path"));
           File file =new File(params.get("isupfile"));
     
           Log.i(TAG, params.get("isupfile"));
           HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
          conn.setDoOutput(true);  
          conn.setUseCaches(false);  
          conn.setConnectTimeout(2000); 
          conn.setRequestMethod("POST");  
          conn.setRequestProperty("connection", "keep-alive");    
          conn.setRequestProperty("Content-Type", "multipart/form-data"+ ";boundary=" + BOUNDARY);   
      
          OutputStream outputSteam=conn.getOutputStream();    
      	  DataOutputStream dos= new DataOutputStream(outputSteam);   
          dos.write(entity); 
          StringBuffer buffer = new StringBuffer();    
          buffer.append(PREFIX);    
          buffer.append(BOUNDARY); 
          buffer.append(LINE_END);    
          buffer.append("Content-Disposition: form-data; name=\"imgFile\"; filename=\"" + URLEncoder.encode(file.getName()) + LINE_END);   
          buffer.append("Content-Type: application/octet-stream; charset="+"UTF-8"+LINE_END);   
          //代表要上传的数据是文件类型application/octet-stream
          buffer.append(LINE_END);   
          //写请求头
          dos.write(buffer.toString().getBytes());   
         
          InputStream is = context.getAssets().open(file.getName());   
          byte[] bytes = new byte[1024];    
          int len = 0;  
         // 写二进制数据文件
          while((len=is.read(bytes))!=-1)    
          {    
             dos.write(bytes, 0, len);    
          }    
          is.close();    
          ///r/n
          dos.write(LINE_END.getBytes());    
        //  -------------------------------7db372eb000e2--/r/n
          byte[] end_data = (PREFIX+BOUNDARY+PREFIX+LINE_END).getBytes();    
          dos.write(end_data);    
          dos.flush();  
          dos.close(); 
      System.out.println("返回码"+conn.getResponseCode());
      if (conn.getResponseCode() == 200) {  
     	InputStream is1 = conn.getInputStream();
		json=StreamTools.readStream(is1);
     	/*StringBuilder builder=new StringBuilder();
 		BufferedReader reader=new BufferedReader(new InputStreamReader(is1));
 		String line=null;
 		while (((line=reader.readLine()))!=null) {
 			builder.append(line);
         }  
 		reader.close();
 		json=reader.toString();*/
 		flag=true;
 	
      }
     } catch (Exception e) {
    	 flag=false;
    	 json="上传请求失败";
		e.printStackTrace();
		}  
     }
   
  
         return json;  
}  
    
     /**
      * 在doInBackground()方法中执行调用publichProgress()方法时候会执行
      * @param progresses
      */
     @Override  
     protected void onProgressUpdate(Integer... progresses) {  
     }  
   /**
    * 这个是结束后的操作在ui线程中
    * @param result
    */
     @Override  
     protected void onPostExecute(String result) {  
    	 br.BackResult(flag,json);
     }  
 /**
  * 取消操作
  */
     @Override  
     protected void onCancelled() {  
     }  
     
 }  
