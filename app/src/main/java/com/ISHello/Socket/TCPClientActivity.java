package com.ISHello.Socket;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ISHello.base.base.BaseActivity;
import com.example.ishelloword.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TCPClientActivity extends BaseActivity {
    private final String TAG = TCPClientActivity.class.getSimpleName();
    private static final int MESSAGE_RECEIVE_NEW_MSG = 1;
    private static final int MESSAGE_SOCKET_CONNECTED = 2;

    private Button mSendButton;
    private TextView mMessageTextView;
    private EditText mMessageEditText;

    private PrintWriter mPrintWriter;
    private Socket mClientSocket;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_RECEIVE_NEW_MSG: {
                    mMessageTextView.setText(mMessageTextView.getText() + (String) msg.obj);
                    break;
                }

                case MESSAGE_SOCKET_CONNECTED: {
                    mSendButton.setEnabled(true);
                    break;
                }
                default: {
                    break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcpclient);
        findViews();
        init();
    }

    private void findViews() {
        mSendButton = findViewById(R.id.send);
        mMessageTextView = findViewById(R.id.msg_container);
        mMessageEditText = findViewById(R.id.msg);
    }

    private void init() {
        Intent intent = new Intent(this, TCPServerService.class);
        startService(intent);
        new Thread() {
            @Override
            public void run() {
                connectTCPServer();
            }
        }.start();
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String msg = mMessageEditText.getText().toString();
                if (!TextUtils.isEmpty(msg) && mPrintWriter != null) {
                    mPrintWriter.println(msg);
                    mMessageEditText.setText("");
                    String time = formatDateTime(System.currentTimeMillis());
                    final String showedMsg = "self " + time + ":" + msg + "\n";
                    mMessageTextView.setText(mMessageTextView.getText() + showedMsg);
                }
            }
        });

    }

    private void connectTCPServer() {
        Socket socket = null;
        while (socket == null) {
            try {
                socket = new Socket("localhost", 8688);
                mClientSocket = socket;
                mPrintWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter
                        (socket.getOutputStream())), true);
                mHandler.sendEmptyMessage(MESSAGE_SOCKET_CONNECTED);
                Log.i(TAG, "--->connect server success");

            } catch (IOException e) {
                SystemClock.sleep(500);
                Log.i(TAG, "--->connect server failed,try retry");
            }
        }

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (!TCPClientActivity.this.isFinishing()) {
                String msg = br.readLine();
                Log.e(TAG, "--->reveived:" + msg);
                if (msg != null) {
                    String time = formatDateTime(System.currentTimeMillis());
                    final String showedMsg = "server " + time + ":" + msg + "\n";
                    mHandler.obtainMessage(MESSAGE_RECEIVE_NEW_MSG, showedMsg).sendToTarget();
                }
            }
            Log.e(TAG, "--->quit....");
            if (mPrintWriter != null) {
                mPrintWriter.close();
                mPrintWriter = null;
            }
            if (br != null) {
                br.close();
                br = null;
            }
            socket.close();
            socket = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String formatDateTime(long time) {
        return new SimpleDateFormat("(HH:mm:ss)").format(new Date(time));
    }

    @Override
    protected void onDestroy() {
        if (mClientSocket != null) {
            try {
                mClientSocket.shutdownInput();
                mClientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        super.onDestroy();
    }
}
