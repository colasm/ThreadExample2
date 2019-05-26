package com.example.threadapp2;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import static android.view.View.GONE;
import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {

    private Button mButton;
    private Thread mThread;
    private Handler mHandler;
    private ProgressBar mProgress;

    private final static int UPDATE_COUNT = 100;
    private final static int END_COUNT = 101;
    private final static int PROGRESS_MAX = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = findViewById(R.id.button);
        mProgress = findViewById(R.id.progressBar);
        mProgress.setMax(PROGRESS_MAX);

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case UPDATE_COUNT :
                        mProgress.setProgress(msg.arg1);
                        break;
                    case END_COUNT :
                        mProgress.setVisibility(GONE);
                        mButton.setEnabled(true);
                        break;
                    default :
                }
            }
        };


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButton.setEnabled(false);
                mProgress.setVisibility(View.VISIBLE);
                mThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // un thread qui dure 10 secondes
                        for(int k=1;k<11;k++){
                            Message message = mHandler.obtainMessage();
                            message.what = UPDATE_COUNT;
                            message.arg1 = k;
                            mHandler.sendMessage(message);

                            try{
                                // update progress

                                sleep(1000);
                            } catch(InterruptedException ie){
                            }

                        }
                        Message message = mHandler.obtainMessage();
                        message.what = END_COUNT;
                        mHandler.sendMessage(message);
                    }
                });
                mThread.start();
            }
        });

    }
}
