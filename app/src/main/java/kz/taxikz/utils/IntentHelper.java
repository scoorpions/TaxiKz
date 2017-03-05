package kz.taxikz.utils;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import kz.taxikz.taxi4.R;

public class IntentHelper {

    private static final String PLAY_REFER = "http://play.google.com/store/apps/details?id=kz.taxi.taxi4";

    public static void share(@NonNull Activity activity) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, activity.getText(R.string.action_share_content) + "\n" + PLAY_REFER);
        sendIntent.setType("text/plain");
        activity.startActivity(sendIntent);
    }


    public static void feedback(@NonNull Activity activity) {
        String []emails = activity.getResources().getStringArray(R.array.emails);
        Intent Email = new Intent(Intent.ACTION_SEND);
        Email.setType("text/email");
        Email.putExtra(Intent.EXTRA_EMAIL, emails);
        Email.putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.review_subject));
        Email.putExtra(Intent.EXTRA_TEXT, activity.getString(R.string.review_pre_text));
        activity.startActivity(Intent.createChooser(Email, activity.getString(R.string.review_chooser_title)));
    }
}

