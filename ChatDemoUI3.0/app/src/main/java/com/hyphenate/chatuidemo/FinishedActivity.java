package com.hyphenate.chatuidemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chatuidemo.ui.BaseActivity;
import com.hyphenate.chatuidemo.ui.ChatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FinishedActivity extends BaseActivity {
    private String storename;
    private String storedescription;
    private TextView nametext;
    private TextView desctext;
    private String name;
    private int img;
    private String des;
    private String descr;
    private ImageView imageView;
    private ImageView connectSeller;
    private ImageView gotomapImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);
        Intent intent=getIntent();
        storename=intent.getStringExtra("storename");
        desctext=(TextView) findViewById(R.id.sid);
        //textView.setText(textviewdata1);
        storedescription=intent.getStringExtra("storedescription");
        nametext=(TextView) findViewById(R.id.name);
        //textView.setText(textviewdata2);
        imageView=(ImageView) findViewById(R.id.imageshow);
        Map<String,Object> drawablemap=new HashMap<String,Object>();
        Map<String,Object> locationmap=new HashMap<String,Object>();
        drawablemap.put("huacai",R.drawable.huacai);
        drawablemap.put("wenyin",R.drawable.wenyin);
        drawablemap.put("kjldong",R.drawable.kjldong);
        drawablemap.put("xinguang",R.drawable.xinghuang);
        /*map.put("wenyin",R.drawable.back);
        map.put("wenyin",R.drawable.back);
        map.put("wenyin",R.drawable.back);
        map.put("wenyin",R.drawable.back);
        map.put("wenyin",R.drawable.back);*/
        initView();
        //imageView.setBackgroundResource((int)drawablemap.get(storename));
        new Thread(new Runnable() {
            private void jsonJX(String date) {
                if(date!=null){
                    try {
                        //JSONObject jsonObject = new JSONObject(date);
                        JSONArray jsonArray=new JSONArray(date);
                        //String code=jsonObject.getString("error_code");
                        // if (code.equals("0")) {
                        // JSONArray json=jsonObject.getJSONArray("result");
                        for(int i=0;i<jsonArray.length();i++){
                            Map<String, Object> map=new HashMap<String, Object>();
                            JSONObject object=jsonArray.getJSONObject(i);
                            name=object.getString("sid");//这个字段是店铺名称
                            //img=object.getInt("face");
                            //des=object.getString("descrip");
                            descr=object.getString("name");//这个字段是店铺介绍
                        }
                        android.os.Message message = new android.os.Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            public Handler handler=new Handler(){
                public void handleMessage(android.os.Message msg) {
                    switch (msg.what) {
                        case 1:
                            nametext.setText(name);
                            desctext.setText(descr);
                            //desctext.setText(name);
                            //imageView.setImageResource(img);
                            break;
                    }
                };
            };
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                //服务器返回的地址
                Request request = new Request.Builder()
                        .url("http://www.yunprint.com/api/cse49910p/getservices/sid/" + storename + "").build();
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    //获取到数据
                    String date = response.body().string();
                    //把数据传入解析josn数据方法
                    jsonJX(date);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void initView()
    {
        connectSeller=(ImageView) findViewById(R.id.connectseller);
        connectSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    if(EMClient.getInstance().getCurrentUser()!=null&& EMClient.getInstance().isConnected())
                    {
                        Intent gotoChatIntent=new Intent(FinishedActivity.this,ChatActivity.class);
                        gotoChatIntent.putExtra("storename",name);
                        startActivity(gotoChatIntent);
                    }
                    else
                    {
                        Toast.makeText(FinishedActivity.this,"您还未登录，请先登录！", Toast.LENGTH_SHORT).show();
                        Intent gotoLonInIntent=new Intent(FinishedActivity.this,LogIn.class);
                        startActivity(gotoLonInIntent);
                    }
                }
                catch (NullPointerException e)
                {
                    Toast.makeText(FinishedActivity.this,"您还未登录，请先登录！", Toast.LENGTH_SHORT).show();
                    Intent gotoLonInIntent=new Intent(FinishedActivity.this,LogIn.class);
                    startActivity(gotoLonInIntent);
                }
            }
        });
        gotomapImg=(ImageView) findViewById(R.id.gotomap);
        gotomapImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent gotoMapIntent=new Intent(FinishedActivity.this,GoToMapActivity.class);
                //startActivity(gotoMapIntent);
                //Toast.makeText()
            }
        });
    }
}
