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
		
		//��һ�������������·��,��һ������������ͷ�Ĳ������ڶ������ϴ��ļ��õģ���һ��file���󣬴��մ������ǲ����ļ�,
		//�����������Ǵ�����Ҫ���͵���������ͣ����ĸ����ļ��������������ʵ�ֵĽӿڻص���ͨ������ķ��������õ����صĽ��
		MyAsynctask task=new MyAsynctask(this,params,new BackResult() {
			
			@Override
			public void BackResult(Boolean flag, String json) {
				//�������ɹ���᷵��true,����ʧ�ܷ���false
				if (flag) {
					//������Ļ��û�����������������
					//tv_show.setText(json);
					Log.i("����", "���ʳɹ�"+json);
					tv_show.setText(json);
				}else {
					Log.i("����", "����ʧ��"+json);
					tv_show.setText(json);
				}
			}
		});
		task.execute();	

	}

}
