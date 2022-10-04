package tk.anysoft.uc.freeflow.hook;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import tk.anysoft.uc.freeflow.R;


public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static FreeMode mode;

    enum FreeMode{
        FISH(1,"Ali_Fish"),
        ANT(2,"Ali_Ant");

        private int id;
        private String name;


        FreeMode(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);
        EditText et_ip = findViewById(R.id.et_ip);
        EditText et_uid = findViewById(R.id.et_uid);
        EditText et_token = findViewById(R.id.et_token);
        EditText et_domain = findViewById(R.id.et_domain);
        EditText et_proxy_auth = findViewById(R.id.et_proxy_auth);
        File freeflow = new File(IniFileUtils.FILE_PATH);
        IniFileUtils ini = new IniFileUtils(freeflow);
        if (null !=ini.get(IniFileUtils.ALI_FISH_TAG,"token")){
            mode = FreeMode.FISH;
            String token = ini.get(IniFileUtils.ALI_FISH_TAG,"token").toString();
            String orderId = ini.get(IniFileUtils.ALI_FISH_TAG,"orderId").toString();
            String proxyIp = ini.get(IniFileUtils.ALI_FISH_TAG,"proxyIp").toString();
            String proxyPort = ini.get(IniFileUtils.ALI_FISH_TAG,"proxyPort").toString();
            et_uid.setText(orderId);
            et_token.setText(token);
            et_ip.setText(proxyIp + ":" + proxyPort);
        }
        if (null !=ini.get(IniFileUtils.ALI_ANT_TAG,"ip")){
            mode = FreeMode.ANT;
            String ip = ini.get(IniFileUtils.ALI_ANT_TAG,"ip").toString();
            if(!ip.startsWith("uc")){
                ip = "uc" + ip;
            }
            String password = ini.get(IniFileUtils.ALI_ANT_TAG,"password").toString();
            et_uid.setText(ip);
            et_token.setText(password);
            et_domain.setVisibility(View.GONE);
            TextView tv_domain = findViewById(R.id.tv_domain);
            tv_domain.setVisibility(View.GONE);
        }

        Button btn_calc = findViewById(R.id.btn_calc);
        Button btn_copy = findViewById(R.id.btn_copy);

        btn_calc.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onClick(View v) {
                if (null == mode) return;
                switch(mode){
                    case FISH:
                        if (TextUtils.isEmpty(et_domain.getText().toString())){
                            Toast.makeText(getApplicationContext(),"请输入服务器域名或者IP后再计算",Toast.LENGTH_LONG).show();
                            return;
                        }
                        et_proxy_auth.setText("Proxy-Authorization: " + calc(et_uid.getText().toString(),
                                et_token.getText().toString(),
                                et_domain.getText().toString()));
                        break;
                    case ANT:
                        et_proxy_auth.setText("Proxy-Authorization: " + Base64.getEncoder().encodeToString((et_uid.getText().toString()+":"+et_token.getText().toString()).getBytes(StandardCharsets.UTF_8)));
                        break;

                    default:

                        break;
                }




            }
        });
        btn_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(et_proxy_auth.getText().toString())){
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    // 将文本内容放到系统剪贴板里。
                    cm.setText(et_proxy_auth.getText());
                    Toast.makeText(getApplicationContext(),"认证信息已复制",Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
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
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle("关于 UC免流助手");
            dialog.setIcon(android.R.drawable.sym_action_chat);
            dialog.setMessage("本软件是基于Xposed框架运行，通过拦截UC部分方法获取阿里鱼卡和蚂蚁宝卡的免流认证信息，供配置免流脚本。本工具不提供任何免流服务，仅研究学习认证信息。\n\n作者:anysoft\nE-Mail:anysoft@yeah.net");
            dialog.setCancelable(false);

            dialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            dialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public static String rQ(String str) {
        if (str == null || str.length() == 0) {
            return "";
        }
        if (str.indexOf("://") < 0) {
            str = "http://" + str;
        }
        try {
            return new URL(str).getHost();
        } catch (Throwable th) {
            return "";
        }
    }

    public String calc(String uid,String token,String domain) {
        char[] cQe = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

        String str2 = uid + "|" + token + "|" + rQ(domain);
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.reset();
            instance.update(str2.getBytes());
            byte[] digest = instance.digest();
            String str = "";
            StringBuilder sb = new StringBuilder();
            for (byte b2 : digest) {
                char c = cQe[(b2 & 240) >> 4];
                char c2 = cQe[b2 & 15];
                sb.append(c);
                sb.append(c2).append(str);
            }
            return "1|" + uid + "|com.UCMobile|" + sb.toString().toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.MANAGE_EXTERNAL_STORAGE" };

    //然后通过一个函数来申请
    @RequiresApi(api = Build.VERSION_CODES.R)
    public void verifyStoragePermissions(Activity activity) {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            int permission2 = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.READ_EXTERNAL_STORAGE");
            int permission3 = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.MANAGE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED || permission2 != PackageManager.PERMISSION_GRANTED || permission3 != PackageManager.PERMISSION_GRANTED ) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getApplicationContext().getOpPackageName()));
                startActivityForResult(intent, 3);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
