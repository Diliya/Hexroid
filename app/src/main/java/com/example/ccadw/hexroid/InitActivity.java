package com.example.ccadw.hexroid;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class InitActivity extends AppCompatActivity {
    private PackageManager mPackageManager;//用于判断termux是否存在
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        //先检测是否第一次使用，项目是否生成

        //设置启动图片

        //检测是否安装了termux
        mPackageManager = this.getPackageManager();
        if(!isInstallApp(getApplicationContext(),"com.termux"))
        {
            //termux不存在
            //Toast.makeText(getApplicationContext(), "termux donnot exist!",
                    //Toast.LENGTH_SHORT).show();

        }
        else
        {
            //Toast.makeText(getApplicationContext(), "termux exist!",
            //        Toast.LENGTH_SHORT).show();

        }

        //计时跳转至新页面
        Timer timer=new Timer();
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                Intent StartIntent =new Intent(InitActivity.this,StartActivity.class);
                startActivity(StartIntent);
                InitActivity.this.finish();
            }
        };
        //timer.schedule(timerTask,5000);
    }
    /**
     * 判断相对应的APP是否存在
     *
     * @param context
     * @param packageName(包名)(若想判断QQ，则改为com.tencent.mobileqq，若想判断微信，则改为com.tencent.mm)
     * @return
     */
    public boolean isInstallApp(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();

        //获取手机系统的所有APP包名，然后进行一一比较
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if (((PackageInfo) pinfo.get(i)).packageName
                    .equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }
}
