package com.mapgenerator;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mapgenerator.handlers.Controller;
import com.mapgenerator.handlers.GlobalVariables;
import com.mapgenerator.handlers.ServicesHandler;

/**
 * This class holds the main view of the app.
 */
public class MainActivity extends AppCompatActivity implements Controller.FunctionPasser {
    private Controller controller;
//    private GLSurfaceView mGLView;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private TextView locationAddedTv;
    private TextView roundTv;
    private Button newLocationButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
//        mGLView = new MyGLSurfaceView(this);
//        setContentView(mGLView);
        setContentView(R.layout.activity_main);
        newLocationButton = findViewById(R.id.new_location_button);
        locationAddedTv = findViewById(R.id.location_added_tv);
        roundTv = findViewById(R.id.round_tv);

        controller = new Controller(this, this);
        controller.start();
        newLocationButton.setOnClickListener(v ->
        {
            newLocationButton.setEnabled(false);
            controller.gatherLocation();
        });
    }

    /**
     * Called when a result of granting permission is asked.
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ServicesHandler.PERMISSION_REQUEST:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    controller.permissionGranted();
                } else {
                    Toast.makeText(this, "Permissions to GPS must be granted.", Toast.LENGTH_LONG);
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;
        }
    }

    /**
     * This is called when an activity is being called only for result and the result is delivered
     * to here.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ServicesHandler.GPS_LOCATION_PROVIDER_ASKED:
                controller.permissionGranted();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        controller.stop();
        super.onDestroy();
    }

    @Override
    public void addRoute(int x1, int y1, int x2, int y2) {
        //Log.e(GlobalVariables.APPLICATION_TAG, "new route: (" + x1 + "," + y1 + ") -> (" + x2 + "," + y2 + ")");
        locationAddedTv.setText(locationAddedTv.getText() + "\nnew route: (" + x1 + "," + y1 + ") -> (" + x2 + "," + y2 + ")");
    }

    @Override
    public void newLocationAdded(int x, int y) {
        //Log.e(GlobalVariables.APPLICATION_TAG, "added: (" + x + "," + y + ")");
        locationAddedTv.setText(locationAddedTv.getText() + "\n(" + x + "," + y + ")");
//        Toast.makeText(this, "new location is added!", Toast.LENGTH_LONG);
        newLocationButton.setEnabled(true);
    }

    @Override
    public void updateRound(int round, float acc) {
        roundTv.setText("round: " + round + " acc: " + acc);
    }
}
