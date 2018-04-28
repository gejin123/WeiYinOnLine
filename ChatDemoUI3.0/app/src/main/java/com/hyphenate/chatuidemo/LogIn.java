package com.hyphenate.chatuidemo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.chatuidemo.ui.BaseActivity;


/**
 * Created by gejin-PC on 2018/3/3.
 */

public class LogIn extends BaseActivity{
    private EditText account;
    private EditText passWord;
    private Button loginBtn;
    private ProgressDialog dialog;
    private String userName;
    private String password;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        account=(EditText) findViewById(R.id.account);
        passWord=(EditText) findViewById(R.id.password);
        loginBtn=(Button) findViewById(R.id.btn_login);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(account.getText().toString().trim().isEmpty()||passWord.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(LogIn.this,"用户名或者密码不能为空", Toast.LENGTH_SHORT).show();
                }
                else
                signIn();
            }
        });
        EMOptions options = new EMOptions();
// 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
// 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
        options.setAutoTransferMessageAttachments(true);
// 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
        options.setAutoDownloadThumbnail(true);
//初始化

        EMClient.getInstance().init(this, options);
//在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);


    }
    public void signIn()
    {
        dialog=new ProgressDialog(this);
        dialog.setMessage("登录中，请稍后...");

        userName=account.getText().toString().trim();
        password=passWord.getText().toString().trim();
        EMClient.getInstance().login(userName,password,new EMCallBack() {//回调

            @Override
            public void onSuccess() {
                Log.d("main", "登录聊天服务器成功！");
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                //finish();
                //Toast.makeText(LogIn.this, "1111111", Toast.LENGTH_SHORT).show();
                //Toast.makeText(LogIn.this, "登录成功", Toast.LENGTH_SHORT).show();
                /*Intent intent = new Intent(LogIn.this, MainActivity.class);
                startActivity(intent);*/
                //PublicbianliangClass.

                //String loginusername=userName;
                /*Intent intent1=new Intent(LogIn.this,ChatActivity.class);
                intent1.putExtra("extra_loginusername",userName);//将登陆的用户名传递给下一个activity
                startActivity(intent1);*/
                finish();
            }


            @Override
            public void onError(final int i, final String s) {//ylzf0000 880210
                //Toast.makeText(LogIn.this, "登录hh", Toast.LENGTH_SHORT).show();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();

                        switch (i) {
                            // 网络异常 2
                            case EMError.NETWORK_ERROR:
                                Toast.makeText(LogIn.this, "网络错误 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 无效的用户名 101
                            case EMError.INVALID_USER_NAME:
                                Toast.makeText(LogIn.this, "无效的用户名 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 无效的密码 102
                            case EMError.INVALID_PASSWORD:
                                Toast.makeText(LogIn.this, "无效的密码 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 用户认证失败，用户名或密码错误 202
                            case EMError.USER_AUTHENTICATION_FAILED:
                                Toast.makeText(LogIn.this, "用户认证失败，用户名或密码错误 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 用户不存在 204
                            case EMError.USER_NOT_FOUND:
                                Toast.makeText(LogIn.this, "用户不存在 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 无法访问到服务器 300
                            case EMError.SERVER_NOT_REACHABLE:
                                Toast.makeText(LogIn.this, "无法访问到服务器 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 等待服务器响应超时 301
                            case EMError.SERVER_TIMEOUT:
                                Toast.makeText(LogIn.this, "等待服务器响应超时 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 服务器繁忙 302
                            case EMError.SERVER_BUSY:
                                Toast.makeText(LogIn.this, "服务器繁忙 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 未知 Server 异常 303 一般断网会出现这个错误
                            case EMError.SERVER_UNKNOWN_ERROR:
                                Toast.makeText(LogIn.this, "未知的服务器异常 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            default:
                                Toast.makeText(LogIn.this, "ml_sign_in_failed code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });
            }
            @Override
            public void onProgress(int progress, String status) {

            }

        });
    }


}
