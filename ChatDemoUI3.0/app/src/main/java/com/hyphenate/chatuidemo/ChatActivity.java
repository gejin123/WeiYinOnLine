/*
package com.hyphenate.chatuidemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.FragmentHostCallback;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements EMMessageListener {
    protected static final int REQUEST_CODE_MAP = 1;
    protected static final int REQUEST_CODE_CAMERA = 2;
    protected static final int REQUEST_CODE_LOCAL = 3;
    protected static final int REQUEST_CODE_DING_MSG = 4;

    private boolean isimg_msg=false;
    private boolean isfile_msg=false;
    private boolean istext_msg=true;
    private String toChatUsername;
    private String nowUser;
    //private String username;
    private EditText msg_edit;
    private TextView right_msg;
    private TextView left_msg;
    private ListViewAdapter adapter=null;
    private ListView listview;
    private Button send_btn;
    private EMConversation conversation;
    private TextView toChatName;
    private EMMessageListener messageListener;
    private ImageView tab_more;
    private Boolean isRotate=false;
    private LinearLayout layoutPlus;
    private ImageView img_btn;
    FragmentHostCallback mHost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        messageListener=this;
        initView();
        adapter = new ListViewAdapter(this);
        listview.setAdapter(adapter);
        initConversation();


    }

    public void initView()
    {
        adapter=new ListViewAdapter(this);
        listview=(ListView) findViewById(R.id.list_view);
        msg_edit=(EditText) findViewById(R.id.msg_edit);
        right_msg=(TextView) findViewById(R.id.right_msg);
        send_btn=(Button) findViewById(R.id.send_msg);
        img_btn=(ImageView) findViewById(R.id.im_photo);
        img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                //intent.setType("**

/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent,1);
            }
        });
        send_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(istext_msg)
                {
                    sendTextMessage();
                }
                if(isimg_msg)
                {
                    sendImgMesage();
                }
                if(isfile_msg)
                {
                    //sendFileMessage();
                }

            }
        });
        toChatName=(TextView) findViewById(R.id.tochatname);
        tab_more=(ImageView) findViewById(R.id.tb_more);
        layoutPlus=(LinearLayout) findViewById(R.id.layout_plus);
        tab_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRotate)
                    tab_more.setRotation(0f);
                if (!isRotate)
                    tab_more.setRotation(45f);
                isRotate = false;
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                switch (layoutPlus.getVisibility()) {
                    case View.VISIBLE:
                        layoutPlus.setVisibility(View.GONE);
                        break;
                    case View.GONE:
                        layoutPlus.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
        msg_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutPlus.setVisibility(View.GONE);
                tab_more.setRotation(0f);
            }
        });

        VelocityTracker velocityTracker= VelocityTracker.obtain();
        //gbg7UvelocityTracker.addMovement();




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {//是否选择，没选择就不会继续
            Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);
            int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            actualimagecursor.moveToFirst();
            String img_path = actualimagecursor.getString(actual_image_column_index);
            File file = new File(img_path);
            Toast.makeText(ChatActivity.this, file.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    //發送消息


    protected void sendFileByUri(Uri uri){
        String filePath = null;
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = null;

            try {
                cursor = getApplication().getContentResolver().query(uri, filePathColumn, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            filePath = uri.getPath();
        }
        if (filePath == null) {
            return;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            Toast.makeText(ChatActivity.this, "文件不存在", Toast.LENGTH_LONG).show();
            //Toast.makeText(getActivity(), R.string.File_does_not_exist, Toast.LENGTH_SHORT).show();
            return;
        }
        sendFileMessage(filePath);
    }


    protected void sendMessage(EMMessage message){
        if (message == null) {
            return;
        }
        //Add to conversation
        EMClient.getInstance().chatManager().saveMessage(message);
        //refresh ui
    }












    public void sendTextMessage()
    {
        String content=msg_edit.getText().toString();
        //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
        adapter.addDataToAdapter(new Message(null,content));
        adapter.notifyDataSetChanged();
        listview.smoothScrollToPosition(listview.getCount()-1);
        EMClient.getInstance().chatManager().sendMessage(message);
        //msg_edit.setText("");
        Log.d("ChatActivity","消息发送成功");
    }
    public void sendImgMesage()
    {

    }
    protected void sendFileMessage(String filePath){
        EMMessage message = EMMessage.createFileSendMessage(filePath, toChatUsername);
        sendMessage(message);
    }


    private void initConversation() {

        nowUser= EMClient.getInstance().getCurrentUser();
        */
/*if(nowUser.equals("gejin1"))
        {
            toChatUsername="gejin2";
            toChatName.setText(toChatUsername);

        }
        if(nowUser.equals("gejin2"))
        {
            toChatUsername="gejin1";
            toChatName.setText(toChatUsername);
        }*//*

        Intent getIntent=getIntent();
        String storeName=getIntent.getStringExtra("storename");
        toChatName.setText(storeName);
        toChatUsername=storeName;

        conversation = EMClient.getInstance().chatManager().getConversation(toChatUsername, null, true);
        if (conversation != null) {
            conversation.markAllMessagesAsRead();
            int count = conversation.getAllMessages().size();
            if (count < conversation.getAllMsgCount() && count < 20) {
                String msgId = conversation.getAllMessages().get(0).getMsgId();
                conversation.loadMoreMsgFromDB(msgId, 20 - count);
            }
            for (EMMessage message : conversation.getAllMessages()) {
                String content = ((EMTextMessageBody) message.getBody()).getMessage();
                adapter.addDataToAdapter(new Message(content, null));
                adapter.notifyDataSetChanged();
                listview.smoothScrollToPosition(listview.getCount() - 1);
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    EMMessage message = (EMMessage) msg.obj;
                    EMTextMessageBody body = (EMTextMessageBody) message.getBody();
                    adapter.addDataToAdapter(new Message(body.getMessage(), null));
                    adapter.notifyDataSetChanged();
                    listview.smoothScrollToPosition(listview.getCount() - 1);
                    //msg_edit.setText("");
                    break;
            }
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
    }
    @Override
    public void onMessageReceived(List<EMMessage> messages) {
        for (EMMessage message : messages) {
            if (message.getFrom().equals(toChatUsername)) {
                conversation.markMessageAsRead(message.getMsgId());
                android.os.Message msg = handler.obtainMessage();
                msg.what = 0;
                msg.obj = message;
                handler.sendMessage(msg);
            }
        }
    }

    public void onCmdMessageReceived(List<EMMessage> list) {
        for (int i = 0; i < list.size(); i++) {
            EMMessage cmdMessage = list.get(i);
            EMCmdMessageBody body = (EMCmdMessageBody) cmdMessage.getBody();
        }
    }

    @Override
    public void onMessageDelivered(List<EMMessage> list) {

    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object o) {

    }

    @Override
    public void onMessageRead(List<EMMessage> list) {

    }

    @Override
    public void onMessageRecalled(List<EMMessage> list) {

    }

    public class ListViewAdapter extends BaseAdapter {
        private List<Message> MessageDatas = new ArrayList<>();
        private Context context;
        private ViewHolder viewHolder;
        public void addDataToAdapter(Message e) {
            MessageDatas.add(e);
        }


        public ListViewAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return MessageDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return MessageDatas.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater= LayoutInflater.from(context);
                convertView=inflater.inflate(R.layout.adapter_chat,null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }
            else
            {
                viewHolder=(ViewHolder) convertView.getTag();
            }
            String left=MessageDatas.get(position).getLeftMsg();
            String right=MessageDatas.get(position).getRightMsg();
            if(left==null)
            {
                viewHolder.right_Message.setText(right);
                viewHolder.left.setVisibility(View.INVISIBLE);
                viewHolder.right.setVisibility(View.VISIBLE);
            }
            if(right==null)
            {
                viewHolder.left_Message.setText(left);
                viewHolder.left.setVisibility(View.VISIBLE);
                viewHolder.right.setVisibility(View.INVISIBLE);
            }
            return convertView;
        }
        public class ViewHolder{
            public View rootView;
            public TextView left_Message;
            public TextView right_Message;
            public LinearLayout left;
            public LinearLayout right;
            public ViewHolder(View rootView){
                this.rootView=rootView;
                this.left_Message=(TextView) rootView.findViewById(R.id.left_msg);
                this.left=(TextView)rootView.findViewById(R.id.left);
                this.right=rootView.findViewById(R.id.right);
                this.right_Message=rootView.findViewById(R.id.right_msg);

            }
        }
    }

}
*/
