package com.example.taxifriend;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    MarkerOptions mOptions;
    Marker fromMarker = null,toMarker = null;
    PolylineOptions polyOptions;
    LatLng coord = null;
    LatLng coord2 = null;
    List<Address> addressList;
    List<Address> addressList2;
    EditText content,content2;
    String strContent,strContent2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Button btn = (Button) findViewById(R.id.buttonMap);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = findViewById(R.id.fromLocation);
                strContent = content.getText().toString();
                content2 = findViewById(R.id.toLocation);
                strContent2 = content2.getText().toString();
                Intent intent = new Intent(MapsActivity.this,PaymentActivity.class);
                intent.putExtra("fromLocation",strContent);
                intent.putExtra("toLocation",strContent2);
                startActivity(intent);
            }
        });

        Button btn1 = (Button) findViewById(R.id.buttonFromOk);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = findViewById(R.id.fromLocation);
                strContent = content.getText().toString();
                coord = getLocation(strContent);
                LatLng fromLocationMarker = coord;
                if (fromMarker == null) {
                    fromMarker = mMap.addMarker(new MarkerOptions().position(fromLocationMarker).title("From Location"));
                }else{
                    fromMarker.setPosition(fromLocationMarker);
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLng(fromLocationMarker));

            }
        });

        Button btn2 = (Button) findViewById(R.id.buttonToOk);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = findViewById(R.id.toLocation);
                strContent = content.getText().toString();
                coord = getLocation(strContent);
                LatLng toLocationMarker = coord;
                if(toMarker == null) {
                    toMarker = mMap.addMarker(new MarkerOptions().position(toLocationMarker).title("To Location"));
                }else{
                    toMarker.setPosition(toLocationMarker);
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLng(toLocationMarker));
            }
        });



    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //LatLng curr = new LatLng();
        String address = "London";
        String address2 = "Manchester";

        /**
        try {
            addressList = coder.getFromLocationName(address,5);
            addressList2 = coder.getFromLocationName(address2,5);
            Address location = addressList.get(0);
            Address location2 = addressList2.get(0);
            coord = new LatLng(location.getLatitude(),location.getLongitude());
            coord2 =  new LatLng(location2.getLatitude(),location2.getLongitude());
        } catch (IOException e) {
            e.printStackTrace();
        }

        MarkerOptions mOptions = new MarkerOptions();

        LatLng manchester = coord2;
        mMap.addMarker(new MarkerOptions().position(manchester).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(manchester));

        LatLng london = coord;
        mMap.addMarker(new MarkerOptions().position(london).title("Marker in London"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(london));

         **/
    }

    public void onLocationChanged(Location location){
        mOptions = new MarkerOptions();
        mOptions.position(new LatLng(location.getLatitude(),location.getLongitude()));
    }

    public LatLng getLocation(String string){
        Geocoder coder = new Geocoder(this);
        try{
            addressList = coder.getFromLocationName(string,5);
            Address location = addressList.get(0);
            coord = new LatLng(location.getLatitude(),location.getLongitude());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return coord;
    }

}
