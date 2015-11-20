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
import java.util.Map;
import java.util.UUID;
import android.os.AsyncTask;
import android.util.Log;
/**
 * 
 * @author samsung
 *����һ����ʵ���첽����
 *��һ������������Ҫ����path
 *�ڶ����ǽ��ȸ��µ�
 *�������Ƿ���ֵ
 */
public class MyAsynctask extends AsyncTask<String, Integer, String>{
	private Map<String, String> params;
	private String type;
	private BackResult br;
	protected static final String GET="GET";
	protected static final String POST="POST";
	protected static final String SEND="SEND";
	private File file;
	private String json;
	private boolean flag=true;
	private String TAG="��Ϣ��ӡ";
	private String name;
	
	//��һ������������ͷ�Ĳ������ڶ������ϴ��ļ��õģ���һ��file���󣬴��մ������ǲ����ļ�,
	//�����������Ǵ�����Ҫ���͵���������ͣ����ĸ����ļ��������������ʵ�ֵĽӿڻص���ͨ������ķ��������õ����صĽ��
	public MyAsynctask(Map<String, String> params,File file,String type,String name,BackResult br){
		this.params=params;
		this.type=type;
		this.br=br;
		this.file=file;
		this.name=name;
	}
	/**
	 * ��������ڵ���excuteʱ�����,������ʼ��UI�õ�
	 */
	 @Override  
     protected void onPreExecute() {  
     }  
	 /**
	  * �����������������ʱ��Ĳ����Ļ���onPreExecute()����֮���Զ�����
	  * @param params
	  * @return ����ֵ������Ҫ�Ľ�� �������������������json����
	  */
     @Override  
     protected String doInBackground(String... path) {  
    	 if(file==null){
    	 if(type.equals(GET)){
 			try {
 				URL url = new URL(path[0]);
 				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
 				conn.setRequestMethod("GET");
 				conn.setConnectTimeout(3000);
 				int code = conn.getResponseCode();
 				
 				if (200 == code) {
 					InputStream is = conn.getInputStream();
 					json=StreamTools.readStream(is);
 					flag=true;
 					} else {
 						flag=false;
 						json="����ʧ��";
 							}
 						} catch (Exception e) {
 							e.printStackTrace();
 						}
 					}
 	    if (type.equals(POST)) {
         StringBuilder sb = new StringBuilder();  
         if(params!=null &params.size()!=0){  
             for (Map.Entry<String, String> entry : params.entrySet()) {  
                    try {
 					sb.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "utf-8"));
                     sb.append("&");   
                    } catch (UnsupportedEncodingException e) {
    					e.printStackTrace();
    				}  
             }  
             sb.deleteCharAt(sb.length()-1);  
         }  
         byte[] entity = sb.toString().getBytes();  
         try {
          URL url = new URL(path[0]);
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
        	flag=false;
        	json="����ʧ��";
          }  
         } catch (Exception e) {
 			e.printStackTrace();
 		}  
      }  
  }else {
	  	 String BOUNDARY = UUID.randomUUID().toString(); 
	     String PREFIX = "--" ;
	     String LINE_END = "\r\n"; 
         StringBuilder sb = new StringBuilder();  
     if(params!=null &params.size()!=0){  
         for (Map.Entry<String, String> entry : params.entrySet()) {  
         	  String value = entry.getValue();  
              sb.append("--" + BOUNDARY + "\r\n");  
              sb.append("Content-Disposition: form-data; name=\"" + entry.getKey()+"\"\r\n");  
              sb.append("\r\n");  
              sb.append(URLEncoder.encode(value) + "\r\n");
         }  
     byte[] entity = sb.toString().getBytes();  
     try {
           URL url=new URL(path[0]);
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
          buffer.append("Content-Disposition: form-data; name=\"" + name  
                  + "\"; filename=\"" + URLEncoder.encode(file.getName()) + LINE_END);   
          buffer.append("Content-Type: application/octet-stream; charset="+"UTF-8"+LINE_END);   
          //����Ҫ�ϴ����������ļ�����application/octet-stream
          buffer.append(LINE_END);   
          //д����ͷ
          dos.write(buffer.toString().getBytes());    
          InputStream is = new FileInputStream(path[0]);   
          byte[] bytes = new byte[1024];    
          int len = 0;  
         // д�����������ļ�
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
      System.out.println("������"+conn.getResponseCode());
      if (conn.getResponseCode() == 200) {  
     	InputStream is1 = conn.getInputStream();
     	StringBuilder builder=new StringBuilder();
 		BufferedReader reader=new BufferedReader(new InputStreamReader(is1));
 		String line=null;
 		while (((line=reader.readLine()))!=null) {
 			builder.append(line);
         }  
 		reader.close();
 		json=reader.toString();
 		flag=true;
 	
      }
     } catch (Exception e) {
    	 flag=false;
    	 json="ͼƬ�ϴ�����ʧ��";
		e.printStackTrace();
		}  
     }
   
  }
         return json;  
}  
    
     /**
      * ��doInBackground()������ִ�е���publichProgress()����ʱ���ִ��
      * @param progresses
      */
     @Override  
     protected void onProgressUpdate(Integer... progresses) {  
     }  
   /**
    * ����ǽ�����Ĳ�����ui�߳���
    * @param result
    */
     @Override  
     protected void onPostExecute(String result) {  
    	 br.BackResult(flag,json);
     }  
 /**
  * ȡ������
  */
     @Override  
     protected void onCancelled() {  
     }  
     
 }  
