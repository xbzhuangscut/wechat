package com.example.xiaobin.wechat;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class LoginActivity extends ActionBarActivity {
    private EditText accountEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private ImageView iconImage;

    private String userName;
    private String passWord;

    private ProgressDialog dialog;
    String imagePath = "http://192.168.2.111:8080/myhttp/servlet/LoginAction";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        accountEditText = (EditText) this.findViewById(R.id.accountEditText);
        passwordEditText = (EditText) this.findViewById(R.id.passwordEditText);
        loginButton = (Button) this.findViewById(R.id.loginButton);
        iconImage = (ImageView) this.findViewById(R.id.iconImage);

        dialog = new ProgressDialog(this);
        dialog.setTitle("提示信息");
        dialog.setMessage("正在登录，请稍后...");
        //   new MyTask().execute(imagePath);
        //     new MyTask().execute(imagePath);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyTask().execute(imagePath);
            }
        });


    }

    //第一个参数 表示要执行的任务，通常是网络的路径
    //第二个参数 表示进度的刻度
    //第三个表示 任务执行的返回结果
    public class MyTask extends AsyncTask<String, Void, Integer> {
        //表示之前的操作
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();

        }

        @Override
        protected Integer doInBackground(String... params) {
            //使用网络连接类HttpClient类完成对网络数据的提取
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(params[0]);
            Integer integer = 0;
            String string = null;

            //传递参数
            List<NameValuePair> loginParams = new ArrayList<NameValuePair>();
            loginParams.add(new BasicNameValuePair("username", accountEditText.getText().toString()));
            loginParams.add(new BasicNameValuePair("password", passwordEditText.getText().toString()));
            try {
                //发送参数到服务器
                HttpEntity entity = new UrlEncodedFormEntity(loginParams, "UTF-8");
                httpPost.setEntity(entity);
                //获得返回数据
                HttpResponse httpResponse = httpClient.execute(httpPost);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    HttpEntity httpEntity = httpResponse.getEntity();
                    string = EntityUtils.toString(httpEntity);
                    if (string.trim().equals("login successfully"))
                        integer = 1;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.printf("%s", string);
            System.out.printf("%d\n", integer);
            return integer;
        }

        //更新UI操作
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (result == 1)
                //     dialog.show();
                // if (result != null)
                // iconImage.setImageBitmap(result);
                //  dialog.show();
                dialog.setMessage("登录成功!");
            else
                dialog.setMessage("登录失败 请重新输入");
            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    dialog.dismiss();
                }
            };
            timer.schedule(timerTask, 1000);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
