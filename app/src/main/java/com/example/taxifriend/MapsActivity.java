package com.example.taxifriend;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import directionhelpers.FetchURL;
import directionhelpers.TaskLoadedCallback;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, TaskLoadedCallback {

    private GoogleMap mMap;
    MarkerOptions mOptions;
    Marker fromMarker = null,toMarker = null;
    LocationManager locationManager;
    LocationListener listener;
    private Polyline currentPolyline;
    LatLng coord = null,coord2 = null;
    List<Address> addressList;
    EditText content,content2;
    String strContent,strContent2;
    Double perMileValue = 0.5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                content = findViewById(R.id.fromLocation);
                strContent = content.getText().toString();
                coord = new LatLng(location.getLatitude(),location.getLongitude());
                System.out.println(coord);

                if (fromMarker == null) {
                    fromMarker = mMap.addMarker(new MarkerOptions().position((coord)));
                    fromMarker.setTitle(getString(R.string.Current_Location));
                }else if(strContent.toLowerCase().equals(getString(R.string.Current))){
                    fromMarker.setPosition(coord);
                    fromMarker.setTitle(getString(R.string.Current_Location));
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);

            }
        };
        configureLocation();


        Button btn = (Button) findViewById(R.id.buttonMap);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = findViewById(R.id.fromLocation);
                strContent = content.getText().toString();
                content2 = findViewById(R.id.toLocation);
                strContent2 = content2.getText().toString();
                if(strContent.equals("") || strContent2.equals("")){
                    new AlertDialog.Builder(MapsActivity.this).setTitle("Validation!")
                            .setMessage("You need to enter two locations")
                            .setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .show();
                }else{
                    Location loc1 = new Location(""),loc2 = new Location("");
                    content = findViewById(R.id.fromLocation);
                    content2 = findViewById(R.id.toLocation);
                    strContent = content.getText().toString();
                    strContent2 = content2.getText().toString();
                    coord = getLocation(strContent);
                    LatLng fromLocationMarker = coord;
                    loc1.setLatitude(fromLocationMarker.latitude);
                    loc1.setLongitude(fromLocationMarker.longitude);
                    coord2 = getLocation(strContent2);
                    LatLng toLocationMarker = coord2;
                    loc2.setLatitude(toLocationMarker.latitude);
                    loc2.setLongitude(toLocationMarker.longitude);
                    float distanceTo = loc1.distanceTo(loc2);
                    double totalPaymentMiles = distanceTo / 1609.344;
                    String totalPayment = String.format("%.2f",totalPaymentMiles * perMileValue * 3.25);
                    System.out.println(coord);
                    System.out.println(distanceTo);
                    Intent intent = new Intent(MapsActivity.this,PaymentActivity.class);
                    intent.putExtra("fromLocation",strContent);
                    intent.putExtra("toLocation",strContent2);
                    intent.putExtra("payment",totalPayment);
                    startActivity(intent);
                }
            }
        });

        content = findViewById(R.id.toLocation);
        content.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH ||
                        i == EditorInfo.IME_ACTION_DONE ||
                        keyEvent != null &&
                                keyEvent.getAction() == KeyEvent.ACTION_DOWN &&
                                keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (keyEvent == null || !keyEvent.isShiftPressed()) {
                        content = findViewById(R.id.fromLocation);
                        strContent = content.getText().toString();
                        if (!strContent.toLowerCase().equals(getString(R.string.Current))) {
                            coord = getLocation(strContent);
                            LatLng fromLocationMarker = coord;
                            if (fromMarker == null) {
                                fromMarker = mMap.addMarker(new MarkerOptions().position(fromLocationMarker).title(strContent));
                            } else {
                                fromMarker.setPosition(fromLocationMarker);
                            }

                        }
                        content = findViewById(R.id.toLocation);
                        strContent = content.getText().toString();
                        coord = getLocation(strContent);
                        LatLng toLocationMarker = coord;
                        if(toMarker == null) {
                            toMarker = mMap.addMarker(new MarkerOptions().position(toLocationMarker).title(strContent));
                        }else{
                            toMarker.setPosition(toLocationMarker);
                        }
                        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(toLocationMarker, 5.0f));
                        CameraUpdateFactory.zoomTo(15);

                        new FetchURL(MapsActivity.this).execute(getUrl(fromMarker.getPosition(), toMarker.getPosition(), "driving"), "driving");

                        InputMethodManager inputManager = (InputMethodManager) textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(textView.getWindowToken(),0);
                        return true; // consume.
                    }
                }
                return false;
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
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(53.5127483,-2.2114427), 5.0f) );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                configureLocation();
                break;
            default:
                break;
        }
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

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

    public void configureLocation(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
                return;
            }
        }
        if (fromMarker == null) {
            locationManager.requestLocationUpdates("gps", 100, 0, listener);
        }
    }

    public float getDistance(double lat1, double lon1, double lat2, double lon2) {
        String result_in_kms = "";
        String url = "http://maps.google.com/maps/api/directions/xml?origin=" + lat1 + "," + lon1 + "&destination=" + lat2 + "," + lon2 + "&sensor=false&units=metric";
        String tag[] = {"text"};
        HttpResponse response = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(url);
            response = httpClient.execute(httpPost, localContext);
            InputStream is = response.getEntity().getContent();
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(is);
            if (doc != null) {
                NodeList nl;
                ArrayList args = new ArrayList();
                for (String s : tag) {
                    nl = doc.getElementsByTagName(s);
                    if (nl.getLength() > 0) {
                        Node node = nl.item(nl.getLength() - 1);
                        args.add(node.getTextContent());
                    } else {
                        args.add(" - ");
                    }
                }
                result_in_kms =String.valueOf( args.get(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Float f=Float.valueOf(result_in_kms);
        return f*1000;
    }
}
