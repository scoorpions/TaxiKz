package kz.taxikz.google;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import java.util.Calendar;

import kz.taxikz.data.db.TinyDB;
import kz.taxikz.taxi4.R;
import kz.taxikz.ui.news.NewsActivity;
import kz.taxikz.ui.orders.OrdersActivity;

public class OrderGcmListenerService extends GcmListenerService {

    private TinyDB tinydb;
    private String orderId;
    private LocalBroadcastManager broadcaster;
    static final public String COPA_RESULT = "com.example.marat.myapplication.gcm.REQUEST_PROCESSED";
    private String stateId="";
    private String newsTitle;

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {

        broadcaster = LocalBroadcastManager.getInstance(this);

        String gcmType = data.getString("gcm_type");
        assert gcmType != null;
        if(gcmType.equals("0")){
            Log.d("MTEST","GCM "+data.toString());
            String carColor = data.getString("car_color");
            String carMark = data.getString("car_mark");
            String carNumber = data.getString("car_number");
            stateId = data.getString("state_id");
            orderId = data.getString("order_id");

            String notifyBody = carColor + " " + carMark + getString(R.string.car_number_caption) + carNumber;

            if(stateId.equals("20")){
                startTimerService();
            }else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    headsUpNotification(notifyBody, stateId);
                } else {
                    sendNotification(notifyBody, stateId);
                }
            }


        }else if(gcmType.equals("1")){

            newsTitle = data.getString("title");
            String newsDesc = data.getString("description");
            sendNotification(newsDesc, stateId);
        }


            sendResult();

    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param notifContentText GCM message received.
     */
    private void sendNotification(String notifContentText, String stateId) {
        Intent intent = new Intent(this, OrdersActivity.class);
        if(stateId.equals("")){
            intent = new Intent(this, NewsActivity.class);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getContentTitle(stateId))
                .setContentText(notifContentText)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void headsUpNotification(String contentText, String stateId) {
        Intent intent = new Intent(this, OrdersActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        NotificationCompat.Builder actionBuilder = new NotificationCompat.Builder(this)

                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getContentTitle(stateId))
                .setContentText(contentText)
                .setContentIntent(pendingIntent)
                .setLargeIcon(largeIcon);
        //Heads-up
        actionBuilder.setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, actionBuilder.build());

    }

    private String getContentTitle(String stateId) {
        String currentStateMessage;
        switch (stateId) {
            case "12":
                currentStateMessage = getString(R.string.notification_title_state_12);
                stopTimerService();
                break;
            case "28":
                currentStateMessage = getString(R.string.notification_title_arrived);
                startTimerService();
                break;
            case "61":
                currentStateMessage = getString(R.string.notification_title_arrived);
                startTimerService();
                break;
            default:
                currentStateMessage = newsTitle;
                break;
        }
        return currentStateMessage;
    }

    private void startTimerService() {
        if (tinydb == null) {
            tinydb = new TinyDB(getApplicationContext());
        }
        Calendar currentTime =  Calendar.getInstance();
        tinydb.putLong(orderId, currentTime.getTimeInMillis());
    }
    private void stopTimerService() {
        if (tinydb == null) {
            tinydb = new TinyDB(getApplicationContext());
        }
        tinydb.remove(orderId);
    }
    public void sendResult() {
        Intent intent = new Intent(COPA_RESULT);
        broadcaster.sendBroadcast(intent);
    }
}