package com.neu.edu.schedule.data;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;

import com.neu.edu.schedule.Main2Activity;
import com.neu.edu.schedule.R;


public class AlarmAlert extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String remindMsg = bundle.getString("remindMsg");
        if (bundle.getBoolean("ring")) {
            Main2Activity.mediaPlayer = MediaPlayer.create(this, R.raw.ring);
            try {
                Main2Activity.mediaPlayer.setLooping(true);
                Main2Activity.mediaPlayer.prepare();
            } catch (Exception e) {
                setTitle(e.getMessage());
            }
            Main2Activity.mediaPlayer.start();
        }
        if (bundle.getBoolean("shake")) {
            Main2Activity.vibrator = (Vibrator) getApplication().getSystemService(
                    Service.VIBRATOR_SERVICE);
            Main2Activity.vibrator.vibrate(new long[] { 1000, 100, 100, 1000 }, -1);//按照指定的模式去震动
        }
        new AlertDialog.Builder(AlarmAlert.this)
                .setIcon(R.drawable.icon)
                .setTitle("提醒")
                .setMessage(remindMsg)
                .setPositiveButton("关 闭",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                AlarmAlert.this.finish();
                                if (Main2Activity.mediaPlayer != null)
                                    Main2Activity.mediaPlayer.stop();
                                if (Main2Activity.vibrator != null)
                                    Main2Activity.vibrator.cancel();
                            }
                        }).show();

    }
}