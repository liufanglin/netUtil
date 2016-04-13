package com.example.netutil;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
/**
 * test fen zhi 
 * @author samsung
 *
 */

public class MainActivity extends Activity {
   //dsafasdfa
	private int  a =  0 ;
	private TextView tv_show;
	private Button btn_result;
	protected static final int FAILTURE = 0;	
	protected static final int SUCCESS=1;
	private Map<String, String> params=new HashMap<String, String>();
	private String filepath="file:///android_asset/aaa.png";
	private String path="http://192.168.216.43:8080/SpringMVC8/user/1/photo";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tv_show=(TextView) findViewById(R.id.tv_show);
		btn_result=(Button) findViewById(R.id.btn_post);
		params.put("id", "1");
		params.put("name", "zhangsan");
		params.put("type", "POST");
		params.put("pathsddfa", path);
		params.put("isupfile", filepath);
		
		//第一个参数是请求的路径,第一个参数是请求头的参数，第二个是上传文件用的，传一个file对象，传空代表我们不传文件,
		//第三个参数是代表我要发送的请求的类型，第四个是文件参数，第五个是实现的接口回调，通过里面的方法我们拿到返回的结果
		MyAsynctask task=new MyAsynctask(this,params,new BackResult() {
			
			@Override
			public void BackResult(Boolean flag, String json) {
				//如果请求成功则会返回true,请求失败返回false
				if (flag) {
					//返回真的话用户可以在这里做操作
					//tv_show.setText(json);
					Log.i("返回", "访问成功"+json);
					tv_show.setText(json);
				}else {
					Log.i("返回", "访问失败"+json);
					tv_show.setText(json);
				}
			}
		});
		task.execute();	

	}

}
