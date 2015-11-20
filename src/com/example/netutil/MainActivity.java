package com.example.netutil;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	private TextView tv_show;
	private Button btn_result;
	private String type="POST";
	private String name;
	protected static final int FAILTURE = 0;	
	protected static final int SUCCESS=1;
	private Map<String, String> params=new HashMap<>();
	
	private String path="http://www.weather.com.cn/adat/sk/101110101.html";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tv_show=(TextView) findViewById(R.id.tv_show);
		btn_result=(Button) findViewById(R.id.btn_post);
		//��һ������������ͷ�Ĳ������ڶ������ϴ��ļ��õģ���һ��file���󣬴��մ������ǲ����ļ�,
		//�����������Ǵ�����Ҫ���͵���������ͣ����ĸ����ļ��������������ʵ�ֵĽӿڻص���ͨ������ķ��������õ����صĽ��
		MyAsynctask task=new MyAsynctask(params,null,type,name,new BackResult() {
			
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
				}
			}
		});
		task.execute(path);	

	}

}
