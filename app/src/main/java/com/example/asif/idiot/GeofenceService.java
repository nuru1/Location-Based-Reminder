package com.example.asif.idiot;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;


public class GeofenceService extends IntentService {

    public static final String TAG ="GeofenceService";



    public GeofenceService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        GeofencingEvent event = GeofencingEvent.fromIntent(intent);
        if(event.hasError()){

        }
        else {
            int transition = event.getGeofenceTransition();
            List<Geofence> geofences = event.getTriggeringGeofences();
            Geofence geofence = geofences.get(0);
            String requestID=geofence.getRequestId();
            if(transition == Geofence.GEOFENCE_TRANSITION_ENTER)
            {
                Log.d(TAG,"Geofence Enter"+requestID);
            }
            else if(transition == Geofence.GEOFENCE_TRANSITION_EXIT){
                Log.d(TAG,"Geofence EXIT"+requestID);
            }
        }
    }
}
