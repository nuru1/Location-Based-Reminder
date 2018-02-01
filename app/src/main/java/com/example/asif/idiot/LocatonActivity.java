package com.example.asif.idiot;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class LocatonActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText msg;
    TextView Location;
    Button setLocation,save;

    int Place_Picker_Request=1;
    int L_repeat;
    String L_type,S_Location="",S_message="";
    LatLng latLng;
    double S_lattitude,S_longitude;

    String[] Days = new String[]{
            "Once",
            "Daily",
            "Every Sunday",
            "Every Monday",
            "Every Tuesday",
            "Every Wednesday",
            "Every Thursday",
            "Every Friday",
            "Every Saturday"
    };

    String[] type=new String[]{};
    ArrayList T=new ArrayList();
    Fragment_Suggestions obj;
    FirebaseHelper_Suggestion helper_suggestion;
    ArrayList<Suggestions> suggestions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locaton);

        msg= (EditText)findViewById(R.id.add_reminder_msg_loc);
        Location=(TextView)findViewById(R.id.Location_loc);
        setLocation=(Button)findViewById(R.id.setLocation_loc);
        save=(Button)findViewById(R.id.save_loc);
        obj=new Fragment_Suggestions();
        helper_suggestion = new FirebaseHelper_Suggestion();
        suggestions = helper_suggestion.getSuggestions();
        set_spinners();
        checkPermissions();
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }

    }

    private void set_spinners() {

        Spinner spinner2 = (Spinner) findViewById(R.id.spinner_repeat_loc);
        spinner2.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Repeat, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter);

        type=obj.getLoc_type();
        Suggestions suggest;
        for(int i=0;i<suggestions.size();i++){
            suggest=suggestions.get(i);
            T.add(suggest.getType());
        }
        Spinner spinner_type2=(Spinner)findViewById(R.id.spinner_type_loc);
        spinner_type2.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> aa;
        aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,T);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_type2.setAdapter(aa);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Spinner sp=(Spinner)adapterView;
        if(sp.getId()==R.id.spinner_repeat_loc){
            //  Toast.makeText(getApplicationContext(), Days[i], Toast.LENGTH_LONG).show();
            L_repeat=i;
        }
        else if(sp.getId()==R.id.spinner_type_loc){
            //  Toast.makeText(getApplicationContext(), type[i], Toast.LENGTH_LONG).show();
            L_type=type[i];
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void PlacePicker(View view) {

        PlacePicker.IntentBuilder builder=new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(LocatonActivity.this),Place_Picker_Request);
        }
        catch (GooglePlayServicesRepairableException e){
            e.printStackTrace();
        }
        catch (GooglePlayServicesNotAvailableException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Place_Picker_Request){
            if(resultCode == RESULT_OK){
                Place place = PlacePicker.getPlace(LocatonActivity.this,data);
                S_Location= (String) place.getAddress();
                Location.setText(S_Location);
                latLng=place.getLatLng();
                Log.d("Place picker",latLng.toString());
                S_lattitude = latLng.latitude;
                S_longitude = latLng.longitude;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void save_location(View view) {
        S_message=msg.getText().toString();
        if(S_message=="Remind About" ){
            Toast.makeText(this,"Enter Reminder Title",Toast.LENGTH_SHORT).show();  }
        if(S_Location==""){
            Toast.makeText(this,"Please set a Location",Toast.LENGTH_SHORT).show(); }
        else {
            UniqueId id = new UniqueId();
            String uniqueID =id.generateRandomString();
            Reminder_Location s = new Reminder_Location();
            s.setLattitude(S_lattitude);
            s.setLongitude(S_longitude);
            s.setLocation(S_Location);
            s.setSuggestionType(L_type);
            s.setRepeat(L_repeat);
            s.setMessage(S_message);
            s.setStatus(1);
            s.setId(uniqueID);

            FirebaseHelper_Location helper = new FirebaseHelper_Location();
            if(helper.save_location(s)){
                onView();
                Toast.makeText(getApplicationContext(),"Saved!!",Toast.LENGTH_SHORT).show();
                }
            else {
                Toast.makeText(getApplicationContext(),"There is a Problem",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void onView() {
        msg.setText("");
        S_message="";
        S_Location="";
        Location.setText("Please Set a Location");
    }


}
