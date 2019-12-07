package com.ming.weidushop.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.abner.ming.base.BaseAppCompatActivity;
import com.ming.weidushop.R;
import com.ming.weidushop.utils.AppUtils;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * author:AbnerMing
 * date:2019/9/15
 */
public class StartActivity extends BaseAppCompatActivity {
    private int[] mIds = {
            R.drawable.start_0, R.drawable.start_1, R.drawable.start_2,
            R.drawable.start_3, R.drawable.start_4, R.drawable.start_5,
    };
    private ImageView mStartImage;
    private TextView mStartTime;
    private int mTime = 4;
    private Timer mTimer;
    private TimerTask mTask;

    private TimerTask getTimerTask() {
        mTask = new TimerTask() {
            @Override
            public void run() {
                mTime--;
                if (mTime == 1) {
                    start();

                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mStartTime.setText(String.valueOf(mTime));
                    }
                });
            }
        };
        return mTask;
    }

    private void start() {
        mTimer.cancel();
        mTask.cancel();
        AppUtils.start(StartActivity.this, MainActivity.class);
        finish();
    }

    @Override
    protected void initData() {
        //创建Random类对象
        Random random = new Random();
        //产生随机数
        int number = random.nextInt(6);
        mStartImage.setImageResource(mIds[number]);
        mTimer = new Timer();
        mTimer.schedule(getTimerTask(), 0, 1000);
        get(R.id.tv_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });
    }

    @Override
    protected void initView() {
        mStartImage = (ImageView) get(R.id.start_image);
        mStartTime = (TextView) get(R.id.start_time);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_start;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
            mTask.cancel();
        }
    }
}
