package com.zoovisitors.cl.notifications;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.callbacks.GetObjectInterface;

/**
 * Created by Gili on 19/06/2018.
 */

public class ZooFirbaseIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        GlobalVariables.firebaseToken = FirebaseInstanceId.getInstance().getToken();
        if (GlobalVariables.firebaseToken != null){
            int notificationPref = readFromPreferences(R.string.notification_preferences, R.integer.notification_not_instantiate);
            if (notificationPref == -1) {
                notificationPref = 1;
                writeToPreferences(R.string.notification_preferences, 1);
            }
            if (notificationPref == 1){
                GlobalVariables.bl.subscribeToNotification(new GetObjectInterface() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.e("TOKEN", "Successfully subscribe");
                    }

                    @Override
                    public void onFailure(Object response) {
                        Log.e("TOKEN", "subscribe failed");
                    }
                });
            }
        }
    }

    private int readFromPreferences(int stringId, int integerId){
        SharedPreferences sharedPref = GlobalVariables.appCompatActivity.getPreferences(Context.MODE_PRIVATE);
        int integerNotInstantiate = getResources().getInteger(integerId);
        return sharedPref.getInt(getString(stringId), integerNotInstantiate);
    }

    private void writeToPreferences(int stringId, int write){
        SharedPreferences sharedPref = GlobalVariables.appCompatActivity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(stringId), write);
        editor.commit();
    }





//    writeToPreferences(R.string.notification_preferences, 1);
//            GlobalVariables.bl.subscribeToNotification(new GetObjectInterface() {
//        @Override
//        public void onSuccess(Object response) {
//            Toast.makeText(GlobalVariables.appCompatActivity, "Notifications: On", Toast.LENGTH_LONG).show();
//        }
//
//        @Override
//        public void onFailure(Object response) {
//            Toast.makeText(GlobalVariables.appCompatActivity, "Something went wrong...", Toast.LENGTH_LONG).show();
//        }
//    });
}
