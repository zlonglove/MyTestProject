package com.ISHello.Socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class TCPServerService extends Service {
    private final String TAG = TCPServerService.class.getSimpleName();
    private boolean mIsServerDestoryed = false;
    private String[] mDefineMessage = new String[]{
            "你好，哈哈",
            "请问你叫什么名字啊？",
            "今天北京的天气不错啊，shy",
            "你知道吗？我可是可以和多人同时聊天的哦",
            "给你讲个笑话吧，据说爱笑的人运气不会太差"
    };

    public TCPServerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new TcpServer()).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIsServerDestoryed = true;
    }

    private class TcpServer implements Runnable {

        @Override
        public void run() {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(8688);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            while (!mIsServerDestoryed) {
                try {
                    final Socket client = serverSocket.accept();
                    Log.i(TCPServerService.class.getSimpleName(), "--->accept");
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                responseClient(client);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void responseClient(Socket socket) throws IOException {
        /**
         * 用于接收客户端消息
         */
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        /**
         * 用户向客户端发送消息
         */
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()
        )), true);
        out.println("欢迎来到聊天室！");

        while (!mIsServerDestoryed) {
            String str = in.readLine();
            Log.i(TAG, "--->msg from client==" + str);
            if (str == null) {
                break;
            }
            int i = new Random().nextInt(mDefineMessage.length);
            String msg = mDefineMessage[i];
            out.println(msg);
        }
        Log.i(TAG, "--->clent quiet");
        /**
         * 关闭流
         */
        if (out != null) {
            out.close();
            out = null;
        }
        if (in != null) {
            in.close();
            in = null;
        }
        socket.close();

    }
}
