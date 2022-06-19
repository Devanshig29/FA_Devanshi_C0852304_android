package com.example.fa_devanshi_c0852304_android.activities;

import android.app.Dialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.textfield.TextInputEditText;
import com.example.fa_devanshi_c0852304_android.R;
import com.example.fa_devanshi_c0852304_android.db.DatabaseClient;
import com.example.fa_devanshi_c0852304_android.entities.AddExpense;
import com.example.fa_devanshi_c0852304_android.preference.SharedPreference;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback{


    @BindView(R.id.menuImg)
    LinearLayout mMenuImg;

    @BindView(R.id.addressEt)
    TextInputEditText mAddressEt;

    @BindView(R.id.currentPositionIMg)
    ImageView currentPositionIMg;

    @BindView(R.id.mMapTypeBtn)
    LinearLayout mMapTypeBtn;

    @BindView(R.id.mCurrentLocationBtn)
    LinearLayout mCurrentLocationBtn;
    @BindView(R.id.home)
    LinearLayout home;
    @BindView(R.id.favouriteplace)
    LinearLayout favouriteplace;

    private GoogleMap mMap;
    double lat, lng;
    FusedLocationProviderClient fusedLocationProviderClient;
    SharedPreference sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mapactivity_homescreen);
        ButterKnife.bind(this);


        sharedPreference=new SharedPreference(MapActivity.this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            lat = bundle.getDouble("Lat");
            lng = bundle.getDouble("Lng");
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.myMap);
        mapFragment.getMapAsync(this);

        mAddressEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
                //Create Intent
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(MapActivity.this);
                startActivityForResult(intent, 100);

            }
        });

        mMapTypeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMapTypeDialog();
            }
        });

        mCurrentLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fetchLocation();

                mMap.clear();

                LatLng latLng = new LatLng(Double.parseDouble(SharedPreference.getLatitude()), Double.parseDouble(SharedPreference.getLongitude()));
                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("I am here!");
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
                mMap.addMarker(markerOptions);

                mAddressEt.invalidate();
                mAddressEt.setText(getCompleteAddressString(Double.parseDouble(SharedPreference.getLatitude()), Double.parseDouble(SharedPreference.getLongitude())));

            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });
        favouriteplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MapActivity.this,FavouritePlaceActivity.class));
            }
        });
    }


    private void showMapTypeDialog() {
        Dialog alertDialog = new Dialog(MapActivity.this);
        ;
        View view = getLayoutInflater().inflate(R.layout.dialoglayout, null);

        TextView mHybrid = view.findViewById(R.id.type_hybrid);
        TextView mTerrain = view.findViewById(R.id.type_terrain);
        TextView mSatellite = view.findViewById(R.id.type_satellite);
        TextView mRoadmap = view.findViewById(R.id.type_normal);
        TextView mNone = view.findViewById(R.id.type_none);

        mHybrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                alertDialog.dismiss();
            }
        });

        mTerrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                alertDialog.dismiss();
            }
        });

        mSatellite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                alertDialog.dismiss();
            }
        });

        mRoadmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                alertDialog.dismiss();

            }
        });
        mNone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                alertDialog.dismiss();

            }
        });


        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //this line MUST BE BEFORE setContentView
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(true);
        alertDialog.setContentView(view);
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        LatLng latLng = new LatLng(lat, lng);
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("I am here!");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
        googleMap.addMarker(markerOptions);

        mAddressEt.invalidate();
        mAddressEt.setText(getCompleteAddressString(lat, lng));

        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                Log.e("Drag Start", marker.getPosition().latitude + "..." + marker.getPosition().longitude);
            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                // TODO Auto-generated method stub
                Log.e("Drag End", marker.getPosition().latitude + "..." + marker.getPosition().longitude);

                mAddressEt.invalidate();
                mAddressEt.setText(getCompleteAddressString(marker.getPosition().latitude, marker.getPosition().longitude));

                lat = marker.getPosition().latitude;
                lng = marker.getPosition().longitude;

                mMap.addMarker(new MarkerOptions()
                        .position(marker.getPosition())
                        .title(getCompleteAddressString(lat,lng))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

                saveToFavourites(getCompleteAddressString(lat, lng),lat,lng);
            }
        });

        //Initialise Places Api
        Places.initialize(MapActivity.this, getString(R.string.google_maps_key));
        mAddressEt.setFocusable(false);

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                double lat = latLng.latitude;
                double lng = latLng.longitude;
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(getCompleteAddressString(lat,lng))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

                saveToFavourites(getCompleteAddressString(lat, lng),lat,lng);


            }
        });

    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.e("Current Address", strReturnedAddress.toString());
            } else {
                Log.e("Current Address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MapActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return strAdd;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            //initialise place
            Place place = Autocomplete.getPlaceFromIntent(data);
            mAddressEt.invalidate();
            mAddressEt.setText(place.getAddress());
            LatLng queriedLocation = place.getLatLng();
            lat = queriedLocation.latitude;
            lng = queriedLocation.longitude;
            Log.v("Latitude is", "" + queriedLocation.latitude);
            Log.v("Longitude is", "" + queriedLocation.longitude);

            mMap.clear();
            MarkerOptions markerOptions = new MarkerOptions().position(place.getLatLng()).title(place.getAddress()).draggable(false);
            mMap.animateCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15));
            mMap.addMarker(markerOptions);

            saveToFavourites(place.getAddress(),lat,lng);


        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            //initialise status
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(MapActivity.this, status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void saveToFavourites(String address, double lat, double lng) {
        class SaveExpense extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                AddExpense addIncome = new AddExpense(address,String.valueOf(lat),String.valueOf(lng),"false");

                //adding to database
                DatabaseClient.getInstance(MapActivity.this).getAppDatabase()
                        .addExpenseDao()
                        .insert(addIncome);
                return null;

            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(MapActivity.this, "Your data has been saved successfully", Toast.LENGTH_LONG).show();

            }
        }

        SaveExpense saveExpense = new SaveExpense();
        saveExpense.execute();

    }


}