package com.example.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    TextView tvHikersWatch,tvLatitude,tvLongitude,tvAccuracy,tvAltitude,tvAddress,tvRefresh;
    DecimalFormat decimal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        tvHikersWatch=findViewById(R.id.tvHikersWatch);
        tvLatitude=findViewById(R.id.tvLatitude);
        tvLongitude=findViewById(R.id.tvLongitude);
        tvAccuracy=findViewById(R.id.tvAccuracy);
        tvAltitude=findViewById(R.id.tvAltitude);
        tvAddress=findViewById(R.id.tvAddress);
        tvRefresh=findViewById(R.id.tvRefresh);
        decimal=new DecimalFormat("#0.0000000");


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

//                Log.i("LocationInfo",location.toString());
                updateLocationInfo(location);

                try {
                    updateAddressInfo(location);
                } catch (IOException e) {
                    e.printStackTrace();
                }

//                Geocoder geocoder=new Geocoder(getApplicationContext(),Locale.getDefault());
//                List<Address> addressList= null;
//                try {
//                    addressList = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
//                    String results="";
//
//                    if(addressList.get(0).getSubThoroughfare() != null)
//                    {
//                        results+=addressList.get(0).getSubThoroughfare()+", ";
//                    }
//                    if(addressList.get(0).getThoroughfare() != null)
//                    {
//                        results+=addressList.get(0).getThoroughfare()+", ";
//                    }
//                    if(addressList.get(0).getLocality() != null)
//                    {
//                        results+=addressList.get(0).getLocality()+", ";
//                    }
//                    if(addressList.get(0).getSubAdminArea() != null)
//                    {
//                        results+=addressList.get(0).getSubAdminArea()+", ";
//                    }
//                    if(addressList.get(0).getAdminArea() != null)
//                    {
//                        results+=addressList.get(0).getAdminArea()+", ";
//                    }
//                    if(addressList.get(0).getPostalCode() != null)
//                    {
//                        results+=addressList.get(0).getPostalCode()+", ";
//                    }
//                    if(addressList.get(0).getCountryName() != null)
//                    {
//                        results+=addressList.get(0).getCountryName();
//                    }
//
//                    tvAddress.setText("Address : "+results);
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED)
        {
//            ask for permission
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        else
        {
            // permission granted
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,0,locationListener);

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode ==1)
        {
            if(grantResults.length >0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED)
                {
                    // permission granted
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,0,locationListener);
                }
            }
        }
    }

    public void updateLocationInfo(Location location)
    {
        tvHikersWatch.setText("Address N Location");
        tvLatitude.setText("Latitude : "+ decimal.format(location.getLatitude()));
        tvLongitude.setText("Longitude : "+decimal.format(location.getLongitude()));
        tvAccuracy.setText("Accuracy : "+location.getAccuracy());
        tvAltitude.setText("Altitude : "+decimal.format(location.getAltitude()));
    }

    public void updateAddressInfo(Location location) throws IOException {
        Geocoder geocoder=new Geocoder(getApplicationContext(),Locale.getDefault());
        List<Address> addressList=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

        String results="";

        if(addressList.get(0).getSubThoroughfare() != null)
        {
            results+=addressList.get(0).getSubThoroughfare()+", ";
        }
        if(addressList.get(0).getThoroughfare() != null)
        {
            results+=addressList.get(0).getThoroughfare()+", ";
        }
        if(addressList.get(0).getLocality() != null)
        {
            results+=addressList.get(0).getLocality()+", ";
        }
        if(addressList.get(0).getSubAdminArea() != null)
        {
            results+=addressList.get(0).getSubAdminArea()+", ";
        }
        if(addressList.get(0).getAdminArea() != null)
        {
            results+=addressList.get(0).getAdminArea()+", ";
        }
        if(addressList.get(0).getPostalCode() != null)
        {
            results+=addressList.get(0).getPostalCode()+", ";
        }
        if(addressList.get(0).getCountryName() != null)
        {
            results+=addressList.get(0).getCountryName();
        }

        tvRefresh.setText(null);
        tvAddress.setText("Address : "+results);
    }
}