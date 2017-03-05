package kz.taxikz.google;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import javax.inject.Inject;

import kz.taxikz.TaxiKzApp;
import kz.taxikz.controllers.google.GoogleController;
import kz.taxikz.taxi4.R;

public class RegistrationIntentService extends IntentService {

    @Inject
    GoogleController googleController;

    private static final String TAG = "RegIntentService";

    public RegistrationIntentService() {
        super(TAG);
        TaxiKzApp.get().component().inject(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            // [START register_for_gcm]
            // Initially this call goes out to the network to retrieve the token, subsequent calls
            // are local.
            // [START get_token]

            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_sender_id),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.i(TAG, "GCM Registration Token: " + token);


            sendRegistrationToServer(token);

            // Subscribe to topic channels

            // You should store a boolean that indicates whether the generated token has been
            // sent to your server. If the boolean is false, send the token to your server,
            // otherwise your server should have already received the token.
            sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, true).apply();
            // [END register_for_gcm]
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false).apply();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(QuickstartPreferences.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Persist registration to third-party servers.
     * <p>
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.

        Log.d("MTEST","googleController "+googleController);
        if(TaxiKzApp.storage.isNews()){
            googleController.signUpGoogle(token);
        }else{
            googleController.orderGoogle(token, TaxiKzApp.storage.getOrderId());
        }
    }

    // Уведомления для новостей
//    private void newsGCM(final String token){
//        new RestClient().getRestAdapter().create(GCMService.class).signUpGCM(
//                token,
//                new Callback<Code>() {
//                    @Override
//                    public void success(Code code, Response response) {
//                        // Успешно зарегистрирован
//                    }
//
//                    @Override
//                    public void failure(RetrofitError error) {
//                        if (error.getCause().toString().contains("SocketTimeoutException")) {
//                            boolean isMHWorking = TaxiKzApp.storage.isMainHostWorking();
//                            TaxiKzApp.storage.setMainHostWorks(!isMHWorking);
//                            newsGCM(token);
//                        }
//                    }
//                }
//        );
//
//    }

    // Уведомления для заказа
//    private void orderGCM(final String token){
//        new RestClient().getRestAdapter().create(GCMService.class).orderGCM(
//                token,
//                orderId,
//                new Callback<Code>() {
//                    @Override
//                    public void success(Code code, Response response) {
//                        // Успешно зарегистрирован
//                    }
//
//                    @Override
//                    public void failure(RetrofitError error) {
//                        if (error.getCause().toString().contains("SocketTimeoutException")) {
//                            boolean isMHWorking = TaxiKzApp.storage.isMainHostWorking();
//                            TaxiKzApp.storage.setMainHostWorks(!isMHWorking);
//                            orderGCM(token);
//                        }
//                    }
//                }
//        );
//
//    }

}