package com.abner.ming.base.upload.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;

import com.abner.ming.base.R;

import java.io.File;


/**
 * Created by Teprinciple on 2017/11/3.
 */

public class UpdateAppReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        int notifyId = 1;
        int progress = intent.getIntExtra("progress", 0);
        String title = intent.getStringExtra("title");

        NotificationManager notifManager = null;
        if (UpdateAppUtils.showNotification) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationCompat.Builder builder;
                String id="001";
                notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel mChannel = notifManager.getNotificationChannel(id);
                if (mChannel == null) {
                    mChannel = new NotificationChannel(id, title, importance);
//                    mChannel.enableVibration(true);
                    notifManager.createNotificationChannel(mChannel);
                }
                builder = new NotificationCompat.Builder(context, id);
                builder.setContentTitle("正在下载 " + title)
                        .setSmallIcon(R.drawable.logo)
                        .setAutoCancel(true)
                        .setProgress(100, progress, false);

                notifManager.notify(notifyId, builder.build());
            } else {

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                builder.setContentTitle("正在下载 " + title);
                builder.setSmallIcon(R.drawable.logo);
                builder.setProgress(100, progress, false);

                Notification notification = builder.build();
                notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notifManager.notify(notifyId, notification);
            }
        }


        if (progress == 100) {
            if (notifManager != null) notifManager.cancel(notifyId);

            if (DownloadAppUtils.downloadUpdateApkFilePath != null) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                File apkFile = new File(DownloadAppUtils.downloadUpdateApkFilePath);
                if (UpdateAppUtils.needFitAndroidN && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri contentUri = FileProvider.getUriForFile(
                            context, "com.ming.weidushop", apkFile);
                    i.setDataAndType(contentUri, "application/vnd.android.package-archive");
                } else {
                    i.setDataAndType(Uri.fromFile(apkFile),
                            "application/vnd.android.package-archive");
                }
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        }
    }
}
