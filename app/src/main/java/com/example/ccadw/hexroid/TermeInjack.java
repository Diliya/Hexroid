package com.example.ccadw.hexroid;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by natas on 2017/7/4.
 */
public class TermeInjack {
    private TermeSusession terme_session;
    private Process su;
    static String checkEnv = "date;uname -a;hexo --version;git --version;";

    //root
    public TermeInjack() throws IOException, InterruptedException {
        Process su = Runtime.getRuntime().exec("su");
        DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());
        DataInputStream response = new DataInputStream(su.getInputStream());
        this.terme_session = new TermeSusession();
        this.terme_session.outputStream = outputStream;
        this.terme_session.response = response;
        this.su = su;
    }

    //    初始化termux环境
    public void initTermuxEnv() throws IOException {
        String cmdStr = "cd /data/data/com.termux/files/home;export PATH=/data/data/com.termux/files/usr/bin:/data/data/com.termux/files/usr/bin/applets:$PATH;export LD_LIBRARY_PATH=/data/data/com.termux/files/usr/lib:$LD_LIBRARY_PATH;tmpuid=`stat -c \"%U\" /data/data/com.termux/files/home`;\n";
        Log.v("initTermuxEnv", "initTermuxEnv");
        this.terme_session.outputStream.writeBytes(cmdStr);
    }

    private void runCmdSuTermuxUser(String cmdstr) throws IOException, InterruptedException {
        String raw_cmd = "su - $tmpuid -c 'export PATH=/data/data/com.termux/files/usr/bin:/data/data/com.termux/files/usr/bin/applets:$PATH;export LD_LIBRARY_PATH=/data/data/com.termux/files/usr/lib:$LD_LIBRARY_PATH;%s';exit\n";
        String cmdFine = String.format(raw_cmd, cmdstr);
        this.runCmd(cmdFine);
    }

    private void runCmd(String cmdStr) throws IOException, InterruptedException {
        cmdStr = cmdStr.concat("\n");
        Log.v("cmdstr", cmdStr);
        DataOutputStream outputStream = this.terme_session.outputStream;
        outputStream.writeBytes(cmdStr);
        outputStream.writeBytes("exit\n");
        outputStream.flush();
    }

    // 使用termux环境运行
    public String TermuxRunCmd(String cmdStr) throws IOException, InterruptedException {
        this.initTermuxEnv();
        this.runCmdSuTermuxUser(cmdStr);
        String res = TermeInjack.readFully(this.terme_session.response);
        Log.v("response", res);
        return res;
    }

    //用su权限执行
    public String SuRunCmd(String cmdStr) throws IOException, InterruptedException {
        this.runCmd(cmdStr);
        String res = TermeInjack.readFully(this.terme_session.response);
        Log.v("response", res);
        return res;
    }

    // 读取命令执行的输出
    public static String readFully(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
//        int count = 0;
        while ((length = is.read(buffer)) != -1) {
//            Log.v("length", new StringBuilder().append(length).toString());
//            Log.v("count", new StringBuilder().append(++count).toString());
            baos.write(buffer, 0, length);
        }
        return baos.toString("UTF-8");
    }
}

class TermeSusession {
    DataOutputStream outputStream;
    InputStream response;
}