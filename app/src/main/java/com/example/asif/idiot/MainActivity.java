package com.example.asif.idiot;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.provider.Settings.Secure;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, GeoQueryEventListener {

    static String name;
    static String url;
    private BottomNavigationView bottomNavigationView;
    static DatabaseReference db;
    static DatabaseReference db_location;
    Context ctx;

    static ArrayList<Reminder_Location> locations_list;
    static ArrayList<Reminder_Time> reminder_time;
    static ArrayList<Suggestions> suggestions;

    private static final String TAG = "MainActivity";
    static FirebaseHelper_Location helper_location;
    private static GoogleApiClient mGoogleApiClient = null;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;

    private static int UPDATE_INTERVAL=10000;
    private static int FASTEST_INTERVAL=3000;
    private static int DISPLACEMENT=10;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST=4321;
    private static final int MY_PERMISSION_REQUEST_CODE=1234;
    private double longitude,lattitude;

    static GeoFire geoFire;
    static GeoQuery geoQuery=null;
    static DatabaseReference db_geofire;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        name = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);
        System.out.println("ID " + name);
        ctx = getApplicationContext();

        db_geofire= FirebaseDatabase.getInstance().getReference("Location Based Reminder/"+name+"/GeoFire/");
        geoFire = new GeoFire(db_geofire);
        final Fragment Add_Reminder_Fragment = new Fragment_Add_Reminders();
        final Fragment Reminders_Fragment = new Fragment_Reminders();
        final Fragment Suggestions_Fragment = new Fragment_Suggestions();

        if (savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, Add_Reminder_Fragment).commit();
        }

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                FragmentManager fragmentManager = getSupportFragmentManager();

                if (item.getItemId() == R.id.add_reminder) {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainer, Add_Reminder_Fragment).commit();
                } else if (item.getItemId() == R.id.reminders) {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainer, Reminders_Fragment).commit();
                } else if (item.getItemId() == R.id.suggestions) {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainer, Suggestions_Fragment).commit();
                }
                return true;
            }
        });

        SendNotification("mesg","Adding Suggestion");

        final FirebaseHelper helper = new FirebaseHelper();
        db = helper.getDb();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                helper.retrieve();
                reminder_time = helper.getReminders();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                helper.retrieve();
                reminder_time = helper.getReminders();
            }
        });


        final FirebaseHelper_Suggestion helper_suggestion = new FirebaseHelper_Suggestion();
        db = helper_suggestion.getDb();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                helper_suggestion.retrieve();
                suggestions = helper_suggestion.getSuggestions();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                helper_suggestion.retrieve();
                suggestions = helper_suggestion.getSuggestions();
            }
        });

        helper_location = new FirebaseHelper_Location();
        db_location = helper_location.getDb();
        db_location.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                helper_location.retrieve_loc();
                locations_list = helper_location.getReminders_locations();
                updateGeoQuery(locations_list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                helper.retrieve();
                locations_list = helper_location.getReminders_locations();
            }

        });

        geoQuery=null;
        setupLocation();
        updateGeoQuery(locations_list);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSION_REQUEST_CODE:
                if(grantResults.length> 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                    if(checkPlayServices()){
                        buildAPiClient();
                        createLocationRequest();
                        displayLocation();
                    }
                }
                break;
        }
    }

    private void setupLocation() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_REQUEST_CODE);

        } else {
            if (checkPlayServices()) {
                buildAPiClient();
                createLocationRequest();
                displayLocation();
            }
        }

    }

    private void displayLocation() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLastLocation != null){
            longitude = mLastLocation.getLongitude();
            lattitude = mLastLocation.getLatitude();
            Log.d(TAG,"Location changed    "+longitude+" : "+lattitude);

            Reminder_Location locs;
            if(locations_list!=null){
                for (int i = 0; i < locations_list.size(); i++) {
                    locs = locations_list.get(i);
                    String id = locs.getId();
                    Log.d("ADDGEOQUERY", "adding query :" + id);
                    geoFire.setLocation(id, new GeoLocation(lattitude, longitude));
                    if(geoQuery==null){
                        updateGeoQuery(locations_list);
                    }
                }
            }
        }
        else {
            Log.d(TAG,"cannot get Location ");
        }
    }

    private  void updateGeoQuery(ArrayList<Reminder_Location> Locations_list){
        Log.d(TAG,"......................IN ADD geoquery");
        Reminder_Location locs;
        if(geoQuery!=null)
            geoQuery.removeAllListeners();
        if(locations_list==null)
            locations_list=helper_location.getReminders_locations();
        for (int i = 0; i < locations_list.size(); i++) {
            locs = locations_list.get(i);
            Log.d("ADDGEOQUERY", "adding query+++++----++++----+++++");
            double lat = locs.getLattitude();
            double lon = locs.getLongitude();
            geoQuery = geoFire.queryAtLocation(new GeoLocation(lat, lon), 0.1);
            geoQuery.addGeoQueryEventListener(this);

            }
    }

    private void createLocationRequest() {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);

    }

    private void buildAPiClient() {
         mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

    }

    public void launch_TimeActivity(View view) {
        Intent i = new Intent(this, TimeActivity.class);
        startActivity(i);
    }

    public static String getName() {
        return name;
    }

    public void launch_LocationActivity(View view) {
        Intent i = new Intent(this, LocatonActivity.class);
        startActivity(i);
    }

    public Context getCtx() {
        return ctx;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "ONConnected");
        displayLocation();
        startLocationUpdates();
    }

    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
         return;  }
         LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended   ");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, 1);
            } catch (IntentSender.SendIntentException e) {
                Log.e(TAG, "Exception while resolving connection error.", e);
            }
        } else {
            int errorCode = connectionResult.getErrorCode();
            Log.e(TAG, "Connection to Google Play services failed with error code " + errorCode);
        }
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "Onstart");
        super.onStart();
        mGoogleApiClient.reconnect();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "Onstop");
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(), "This device is not supported", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Google Play services is unavailable.");
            }
            return false;
        }
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        displayLocation();
    }

    public void SendNotification(String Content,String suggestion){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_explore_black_24dp)
                .setContentTitle(Content)
                .setContentText(suggestion);
        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(this,MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;

        manager.notify(001,notification);
    }


    @Override
    public void onKeyEntered(String key, GeoLocation location) {
        Log.d("Geo Fire KEY ENTERED --","ENTERED KEY"+key+"+--+-+-+---+++---+++---+");
        //Toast.makeText(getApplicationContext(),"ENTERED KEY+--+-+-+---+++---+++---+ ",Toast.LENGTH_SHORT).show();
        Reminder_Location reminder_location;
        Suggestions suggest;
        Calendar calendar = Calendar.getInstance();
        int dOw=calendar.get(calendar.DAY_OF_WEEK);
        Log.d(TAG,"DAY DOW++++++++++++++++++++++++ :"+dOw);
        for (int i=0;i<locations_list.size();i++){
            reminder_location = locations_list.get(i);
            String id =reminder_location.getId();
            if(key==id){
                int stat=reminder_location.getStatus();
                int repeat=reminder_location.getRepeat();
                int sett=reminder_location.getSetter();
                String sugg="   ";String typ=reminder_location.getSuggestionType();
                Log.d(TAG,"status "+stat+"  repeat "+repeat+"  setter  "+sett);
                if(stat==1 && (repeat==0 || repeat ==1 || repeat-1 ==dOw ) && sett==0){
                String mesg=reminder_location.getMessage();
                for(int j=0;j<suggestions.size();j++){
                    suggest=suggestions.get(i);
                    String t=suggest.getType();
                    if(typ.equalsIgnoreCase(t)){
                        sugg=suggest.getSuggestion();
                        break;
                    }

                }
                SendNotification(mesg,sugg);
                reminder_location.setSetter(dOw);
                if(repeat == 0)
                    reminder_location.setStatus(0);
                    helper_location.save_location(reminder_location);

                }
            }
        }
    }

    @Override
    public void onKeyExited(String key) {

    }

    @Override
    public void onKeyMoved(String key, GeoLocation location) {

    }

    @Override
    public void onGeoQueryReady() {

    }

    @Override
    public void onGeoQueryError(DatabaseError error) {

    }
}

