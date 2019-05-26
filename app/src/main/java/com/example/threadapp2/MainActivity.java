package com.example.threadapp2;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.lang.ref.WeakReference;

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

    /** déclaration statique pour éviter une référence vers l'activité.
     *  Si on déclare une classe interne non statique, une référence sur
     *  l'activité est implicitement conservée.
     *  On a la chaine : Thread -> Handler -> Activité
     */
    private static final class ThreadHandler extends Handler{

        private final WeakReference<MainActivity> wrAppCompat;

       public ThreadHandler(MainActivity mainActivity){
         wrAppCompat = new WeakReference<MainActivity>(mainActivity);
       }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MainActivity mainActivity = (MainActivity) wrAppCompat.get();
            if(mainActivity!=null) {
                switch (msg.what) {
                    case UPDATE_COUNT:
                        mainActivity.setProgressUpdate(msg.arg1);
                        break;
                    case END_COUNT:
                        mainActivity.endProgress();
                        break;
                    default:
                }
            }

        }

    }


    public void setProgressUpdate(int v) {
        mProgress.setProgress(v);
    }

    public void endProgress(){
        mProgress.setVisibility(GONE);
        mButton.setEnabled(true);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = findViewById(R.id.button);
        mProgress = findViewById(R.id.progressBar);
        mProgress.setMax(PROGRESS_MAX);
        mHandler = new ThreadHandler(this);


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
