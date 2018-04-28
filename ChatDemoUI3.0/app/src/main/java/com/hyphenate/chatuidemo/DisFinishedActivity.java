package com.hyphenate.chatuidemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chatuidemo.ui.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DisFinishedActivity extends BaseActivity {
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


    String oid;
    String uid;
    String servid;
    String filenum;
    TextView oid_tv;
    TextView uid_tv;
    TextView servid_tv;
    TextView filenum_tv;
    String bindway,state,credit,totalamount,paidway,paidamount,delivery;

    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dis_finished);
        /*Intent intent=getIntent();
        storename=intent.getStringExtra("storename");
        desctext=findViewById(R.id.sid);
        //textView.setText(textviewdata1);
        storedescription=intent.getStringExtra("storedescription");
        nametext=findViewById(R.id.name);
        //textView.setText(textviewdata2);
        imageView=findViewById(R.id.imageshow);
        Map<String,Object>drawablemap=new HashMap<String,Object>();
        Map<String,Object>locationmap=new HashMap<String,Object>();
        drawablemap.put("huacai",R.drawable.huacai);
        drawablemap.put("wenyin",R.drawable.wenyin);
        drawablemap.put("kjldong",R.drawable.kjldong);
        drawablemap.put("xinguang",R.drawable.xinghuang);
        initView();*/
        //imageView.setBackgroundResource((int)drawablemap.get(storename));
        initView();
        initData();
    }
    public void initView()
    {
        oid_tv=(TextView) findViewById(R.id.oid);
        uid_tv=(TextView) findViewById(R.id.uid);
        servid_tv=(TextView) findViewById(R.id.serveid);
        filenum_tv=(TextView) findViewById(R.id.filenum);
    }
    public void initData()
    {
        userName= EMClient.getInstance().getCurrentUser();
        Log.d("aa","看看你卡"+userName);
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
                            //name=object.getString("sid");//这个字段是店铺名称
                            //img=object.getInt("face");
                            //des=object.getString("descrip");
                            //descr=object.getString("name");//这个字段是店铺介绍
                            oid=object.getString("oid");
                            uid=object.getString("uid");
                            servid=object.getString("servid");
                            filenum=object.getString("filenum");
                            bindway=object.getString("bindway");
                            Log.d("","看虾米那内容");
                            Log.d("",oid);
                            Log.d("",uid);
                            //Log.d("",servid);
                            //Log.d("",filenum);
                            Log.d("",bindway);
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
                            oid_tv.setText(oid);
                            uid_tv.setText(uid);
                            servid_tv.setText(servid);
                            filenum_tv.setText(filenum);
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
                        .url("http://www.yunprint.com/api/cse49910p/hangorders/uid/chengyong").build();
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


}
