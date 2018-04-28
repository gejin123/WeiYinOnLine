package com.hyphenate.chatuidemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends Activity implements
        android.view.View.OnClickListener {

    private ViewPager mViewPager;// 用来放置界面切换
    private PagerAdapter mPagerAdapter;// 初始化View适配器
    private List<View> mViews = new ArrayList<View>();// 用来存放Tab01-04
    // 四个Tab，每个Tab包含一个按钮
    private LinearLayout mIndex;
    //private LinearLayout mTabAddress;
    private LinearLayout mCar;
    private LinearLayout mMe;
    // 四个按钮
    private ImageView mIndexImg;
    //private ImageView mAddressImg;
    private ImageView mCarImg;
    private ImageView mMeImg;

    private EditText yy,rr;
    private ListView lv1;
    private ListView lv2;
    public ArrayList<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
    private ImageView headImg;
    private ImageView settingImg;
    private LinearLayout disfinishedly;
    private LinearLayout finishedly;
    //private Button loginBtn;
    String storeName;
    String storeDescribe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        //loginBtn=findViewById(R.id.btn_logintop3);


        initView();
        initViewPage();
        initEvent();


        View view= View.inflate(getApplicationContext(),R.layout.top_layout3,null);
        final Button loginBtn=(Button) view.findViewById(R.id.btn_logintop3);


    }

    private void initEvent() {

        mIndex.setOnClickListener(this);
        //mTabAddress.setOnClickListener(this);
        mCar.setOnClickListener(this);
        mMe.setOnClickListener(this);
        //loginBtn.setOnClickListener(this);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            /**
             *ViewPage左右滑动时
             */
            @Override
            public void onPageSelected(int arg0) {
                int currentItem = mViewPager.getCurrentItem();
                switch (currentItem) {
                    case 0:
                        resetImg();
                        mIndexImg.setImageResource(R.drawable.index02);
                        break;
                    case 1:
                        resetImg();
                        mCarImg.setImageResource(R.drawable.car02);
                        break;
                    case 2:
                        resetImg();
                        mMeImg.setImageResource(R.drawable.me02);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }
    public void initFirstPage()//初始化首页
    {
        list.clear();
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
                            String storename=object.getString("sid");
                            String pic=object.getString("face");
                            String storedescription=object.getString("name");
                            Log.i("TAG", pic);
                            map.put("storename", storename);
                            //map.put("pic", pic);
                            map.put("storedescription", storedescription);
                            list.add(map);

                        }
                        android.os.Message message = new android.os.Message();
                        message.what = 1;
                        handler.sendMessage(message);
                        // }else{
                   /* android.os.Message message = new android.os.Message();
                    message.what = 2;
                    handler.sendMessage(message);*/
                        // }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            public Handler handler=new Handler(){
                public void handleMessage(android.os.Message msg) {
                    switch (msg.what) {
                        case 1:
                            Mybaseadapter list_item=new Mybaseadapter();
                            lv1=(ListView) findViewById(R.id.his_lv1);
                            lv1.setAdapter(list_item);
                            list_item.notifyDataSetChanged();
                            break;
                        case 2:
                            Toast.makeText(MainActivity.this, "请输入正确的日期", Toast.LENGTH_LONG).show();
                            break;


                    }

                };
            };

            public void run() {
                /*String [] UrlArry={"http://www.yunprint.com/api/cse49910p/getservices/sid/huacai",
                        "http://www.yunprint.com/api/cse49910p/getservices/sid/kjldong",
                        "http://www.yunprint.com/api/cse49910p/getservices/sid/henyou",
                        "http://www.yunprint.com/api/cse49910p/getservices/sid/bhtsg",
                        "http://www.yunprint.com/api/cse49910p/getservices/sid/wenyin",
                        "http://www.yunprint.com/api/cse49910p/getservices/sid/haohaolai",
                        "http://www.yunprint.com/api/cse49910p/getservices/sid/dafuyin",
                        "http://www.yunprint.com/api/cse49910p/getservices/sid/xinguang",
                        "http://www.yunprint.com/api/cse49910p/getservices/sid/huacai"
                };*/
                String[] item = {"huacai", "kjldong", "henyou", "bhtsg", "wenyin", "haohaolai", "dafuyin", "xinguang"};
                for (int i = 0; i < item.length; i++) {
                    //yy = (EditText) findViewById(R.id.his_yy);
                   // rr = (EditText) findViewById(R.id.his_nn);
                    //String yue=yy.getText().toString().trim();
                    //String ri=rr.getText().toString().trim();
                    OkHttpClient okHttpClient = new OkHttpClient();
                    //服务器返回的地址

                    Request request = new Request.Builder()
                            .url("http://www.yunprint.com/api/cse49910p/getservices/sid/" + item[i] + "").build();

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

            }
        }).start();

    }
    public void initSecondPage()//初始化第二页
    {
        //list.clear();
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
                            String storename=object.getString("sid");
                            String pic=object.getString("face");
                            String storedescription=object.getString("name");
                            Log.i("TAG", pic);
                            map.put("storename", storename);
                            //map.put("pic", pic);
                            map.put("storedescription", storedescription);
                            list.add(map);

                        }
                        android.os.Message message = new android.os.Message();
                        message.what = 1;
                        handler.sendMessage(message);
                        // }else{
                   /* android.os.Message message = new android.os.Message();
                    message.what = 2;
                    handler.sendMessage(message);*/
                        // }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            public Handler handler=new Handler(){
                public void handleMessage(android.os.Message msg) {
                    switch (msg.what) {
                        case 1:
                            Mybaseadapter list_item=new Mybaseadapter();
                            lv2=(ListView) findViewById(R.id.his_lv2);
                            lv2.setAdapter(list_item);
                            list_item.notifyDataSetChanged();
                            break;
                        case 2:
                            Toast.makeText(MainActivity.this, "请输入正确的日期", Toast.LENGTH_LONG).show();
                            break;


                    }

                };
            };

            public void run() {
                /*String [] UrlArry={"http://www.yunprint.com/api/cse49910p/getservices/sid/huacai",
                        "http://www.yunprint.com/api/cse49910p/getservices/sid/kjldong",
                        "http://www.yunprint.com/api/cse49910p/getservices/sid/henyou",
                        "http://www.yunprint.com/api/cse49910p/getservices/sid/bhtsg",
                        "http://www.yunprint.com/api/cse49910p/getservices/sid/wenyin",
                        "http://www.yunprint.com/api/cse49910p/getservices/sid/haohaolai",
                        "http://www.yunprint.com/api/cse49910p/getservices/sid/dafuyin",
                        "http://www.yunprint.com/api/cse49910p/getservices/sid/xinguang",
                        "http://www.yunprint.com/api/cse49910p/getservices/sid/huacai"
                };*/
                String[] item = {"huacai", "kjldong", "henyou", "bhtsg", "wenyin", "haohaolai", "dafuyin", "xinguang"};
                for (int i = 0; i < item.length; i++) {
                    //yy = (EditText) findViewById(R.id.his_yy);
                    // rr = (EditText) findViewById(R.id.his_nn);
                    //String yue=yy.getText().toString().trim();
                    //String ri=rr.getText().toString().trim();
                    OkHttpClient okHttpClient = new OkHttpClient();
                    //服务器返回的地址

                    Request request = new Request.Builder()
                            .url("http://www.yunprint.com/api/cse49910p/getservices/sid/" + item[i] + "").build();

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

            }
        }).start();

    }
    public void initThirdPage()
    {
       // head=findViewById(R.id.head);
        headImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent=new Intent(MainActivity.this,LogIn.class);
                startActivity(loginIntent);
            }
        });
    }


    //listview1的适配器
    public class Mybaseadapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();

        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //String[] item = {"huacai", "kjldong", "henyou", "bhtsg", "wenyin", "haohaolai", "dafuyin", "xinguang"};
            int []index=new int[]{R.drawable.huacai,
                    R.drawable.kjldong,
                    R.drawable.xinghuang,
                    };

            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = getLayoutInflater().inflate(R.layout.history_item, null);
                viewHolder.storeName = (TextView) convertView.findViewById(R.id.storename);
                viewHolder.storeDescribe = (TextView) convertView.findViewById(R.id.storedescribe);
                viewHolder.storeImg = (ImageView) convertView.findViewById(R.id.store_img);
                viewHolder.lisetViewLinearLayout=(LinearLayout) convertView.findViewById(R.id.listview_item);
                viewHolder.lisetViewLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent =new Intent(MainActivity.this,StoreDetailActivity.class);

                        //String q=v.findViewById(R.id.his_des);
                        TextView storename=(TextView) v.findViewById(R.id.storename);
                        TextView storedescribe=(TextView) v.findViewById(R.id.storedescribe);
                        storeName=storename.getText().toString();
                        storeDescribe=storedescribe.getText().toString();
                        intent.putExtra("storename",storeName);
                        intent.putExtra("storedescription",com.hyphenate.chatuidemo.StoreDetailActivity.class);
                        startActivity(intent);
                    }
                });
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Map<String,Object> map=new HashMap<String,Object>();
            map.put("huacai",R.drawable.huacai);
            map.put("wenyin",R.drawable.wenyin);
            map.put("kjldong",R.drawable.kjldong);
            map.put("xinguang",R.drawable.xinghuang);
            viewHolder.storeName.setText(list.get(position).get("storename").toString());
            viewHolder.storeDescribe.setText(list.get(position).get("storedescription").toString());
            viewHolder.storeImg.setImageResource((int)map.get(list.get(position).get("storename")));
            //
            /*if (TextUtils.isEmpty(list.get(position).get("pic").toString())){
                Picasso
                        .with(MainActivity.this)
                        .cancelRequest(viewHolder.img);
                viewHolder.img.setImageDrawable(getResources().getDrawable(R.drawable.no));
            }else {
                //图片加载
                Picasso
                        .with(MainActivity.this)
                        .load(list.get(position).get("pic").toString())
                        .placeholder(R.drawable.jiazai)

                        .into(viewHolder.img);
            }*/
            return convertView;
        }
    }

    final static class ViewHolder {
        TextView storeName;
        TextView storeDescribe;
        ImageView storeImg;
        LinearLayout lisetViewLinearLayout;
    }




    /**
     * 初始化设置
     */
    private void initView() {
        mViewPager =  (ViewPager) findViewById(R.id.id_viewpage);
        // 初始化四个LinearLayout
        mIndex =  (LinearLayout) findViewById(R.id.index_layout);
        //mTabAddress = (LinearLayout) findViewById(R.id.id_tab_address);
        mCar = (LinearLayout)findViewById(R.id.car_layout);
        mMe =  (LinearLayout)findViewById(R.id.me_layout);
        // 初始化三个按钮
        mIndexImg = (ImageView) findViewById(R.id.index);
        //mAddressImg =  findViewById(R.id.id_tab_address_img);
        mCarImg = (ImageView) findViewById(R.id.car);
        mMeImg =(ImageView) findViewById(R.id.me);
    }
    /**
     * 初始化ViewPage
     */
    private void initViewPage() {//跨page调用控件在这儿！！！！
        // 初始化三个布局
        LayoutInflater mLayoutInflater = LayoutInflater.from(this);
        View tab01 = mLayoutInflater.inflate(R.layout.tab1, null);
        View tab02 = mLayoutInflater.inflate(R.layout.tab2, null);
        View tab03 = mLayoutInflater.inflate(R.layout.tab3, null);
        disfinishedly=(LinearLayout) tab03.findViewById(R.id.disfinishedlayout);
        disfinishedly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    if(EMClient.getInstance().getCurrentUser()!=null&& EMClient.getInstance().isConnected())
                    {
                        Intent intent=new Intent(MainActivity.this,DisFinishedActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this,"您还未登录，请先登录！", Toast.LENGTH_SHORT).show();
                        Intent gotoLonInIntent=new Intent(MainActivity.this,LogIn.class);
                        startActivity(gotoLonInIntent);
                    }
                }
                catch (NullPointerException e)
                {
                    Toast.makeText(MainActivity.this,"您还未登录，请先登录！", Toast.LENGTH_SHORT).show();
                    Intent gotoLonInIntent=new Intent(MainActivity.this,LogIn.class);
                    startActivity(gotoLonInIntent);
                }

            }
        });
        finishedly=(LinearLayout) tab03.findViewById(R.id.finishedlayout);
        finishedly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    if(EMClient.getInstance().getCurrentUser()!=null&& EMClient.getInstance().isConnected())
                    {
                        Intent intent=new Intent(MainActivity.this,FinishedActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this,"您还未登录，请先登录！", Toast.LENGTH_SHORT).show();
                        Intent gotoLonInIntent=new Intent(MainActivity.this,LogIn.class);
                        startActivity(gotoLonInIntent);
                    }
                }
                catch (NullPointerException e)
                {
                    Toast.makeText(MainActivity.this,"您还未登录，请先登录！", Toast.LENGTH_SHORT).show();
                    Intent gotoLonInIntent=new Intent(MainActivity.this,LogIn.class);
                    startActivity(gotoLonInIntent);
                }


            }
        });
        headImg=(ImageView) tab03.findViewById(R.id.head);
        headImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    if(EMClient.getInstance().getCurrentUser()!=null&& EMClient.getInstance().isConnected())
                    {
                        /*Intent gotoChatIntent=new Intent(StoreDetailActivity.this,ChatActivity.class);
                        startActivity(gotoChatIntent);*/
                        Toast.makeText(MainActivity.this,"您已登陆，不需要重复登录！", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Intent gotoLonInIntent=new Intent(MainActivity.this,LogIn.class);
                        startActivity(gotoLonInIntent);
                    }
                }
                catch (NullPointerException e)
                {

                    Intent gotoLonInIntent=new Intent(MainActivity.this,LogIn.class);
                    startActivity(gotoLonInIntent);
                }

            }
        });
        settingImg=(ImageView) tab03.findViewById(R.id.settingimg);
        settingImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent=new Intent(MainActivity.this,SettingActivity.class);
                startActivity(intent);
            }
        });

        mViews.add(tab01);
        mViews.add(tab02);
        mViews.add(tab03);
        // 适配器初始化并设置
        mPagerAdapter = new PagerAdapter() {
            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                container.removeView(mViews.get(position));
            }
            @Override
            public int getItemPosition(Object object) {
                return super.getItemPosition(object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = mViews.get(position);

                container.addView(view);
                //head=view.findViewById(R.id.head);
               // head=findViewById(R.id.head);
                /*head.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent loginIntent=new Intent(MainActivity.this,LogIn.class);
                        startActivity(loginIntent);
                    }
                });*/
                switch (position)
                {
                    case 0:
                    {
                        initFirstPage();
                        break;
                    }
                    case 1:
                    {
                        //initSecondPage();
                    }
                    case 2:
                    {
                       //initThirdPage();
                        break;
                    }
                }
                return view;
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {

                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return mViews.size();
            }
        };
        mViewPager.setAdapter(mPagerAdapter);
    }

    /**
     * 判断哪个要显示，及设置按钮图片
     */
    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.index_layout:
                mViewPager.setCurrentItem(0);
                resetImg();
                mIndexImg.setImageResource(R.drawable.index02);
                break;
            /*case R.id.id_tab_address:
                mViewPager.setCurrentItem(1);
                resetImg();
                mAddressImg.setImageResource(R.drawable.baobei);
                break;*/
            case R.id.car_layout:
                mViewPager.setCurrentItem(1);
                resetImg();
                mCarImg.setImageResource(R.drawable.car02);
                break;
            case R.id.me_layout:
                mViewPager.setCurrentItem(2);
                resetImg();
                mMeImg.setImageResource(R.drawable.me02);
                break;
            /*case  R.id.btn_logintop3:
                Intent loginIntent=new Intent(MainActivity.this,LogIn.class);
                startActivity(loginIntent);
                Log.d("main","botton出纳管理");*/
            default:
                break;
        }
    }

    /**
     * 把所有图片变暗
     */
    private void resetImg() {
        mIndexImg.setImageResource(R.drawable.index01);
        //mAddressImg.setImageResource(R.drawable.car02);
        mCarImg.setImageResource(R.drawable.car01);
        mMeImg.setImageResource(R.drawable.me01);
    }

}
