package rs.elfak.mosis.stele.myplaces;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import rs.elfak.mosis.stele.myplaces.classes.MyPlace;
import rs.elfak.mosis.stele.myplaces.classes.MyPlacesData;

public class MyPlacesMapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    static final int PERMISSION_ACCESS_FIME_LOCATION = 1;

    public static final int NEW_PLACE = 1;
    public static final int SHOW_MAP = 0;
    public static final int CENTER_PLACE_ON_MAP = 1;
    public static final int SELECT_COORDINATES = 1;

    private int state = 0;
    private boolean selCoorsEnabled = false;
    private LatLng placeLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Intent mapIntent = getIntent();
            Bundle mapBundle = mapIntent.getExtras();
            if(mapBundle != null)
            {
                state = mapBundle.getInt("state");
                if(state == CENTER_PLACE_ON_MAP)
                {
                    String placeLat = mapBundle.getString("lat");
                    String placeLon = mapBundle.getString("lon");
                    placeLoc = new LatLng(Double.parseDouble(placeLat), Double.parseDouble(placeLon));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_my_places_maps);

        FloatingActionButton fab = findViewById(R.id.fab);

        if(state != SELECT_COORDINATES) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MyPlacesMapsActivity.this, EditMyPlaceActivity.class);
                    startActivityForResult(i, NEW_PLACE);
                }
            });
        } else {
            ViewGroup view = (ViewGroup) fab.getParent();
            if(view != null)
            {
                view.removeView(fab);
            }
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        if(state == SELECT_COORDINATES && !selCoorsEnabled)
        {
            menu.add(0, 1, 1, "Select Coordinates");
            menu.add(0, 2, 2, "Cancel");
            return super.onCreateOptionsMenu(menu);
        } else {
            inflater.inflate(R.menu.maps_menu, menu);
            return super.onCreateOptionsMenu(menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(state == SELECT_COORDINATES && !selCoorsEnabled)
        {
            switch (id) {
                case 1:
                    selCoorsEnabled = true;
                    Toast.makeText(this, "Select Coordinates", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                    break;
            }
        } else {
            Intent i = null;
            switch (id) {
                case R.id.new_place_item:
                    i = new Intent(this, EditMyPlaceActivity.class);
                    startActivityForResult(i, 1);
                    break;
                case R.id.about_item:
                    i = new Intent(this, AboutActivity.class);
                    startActivity(i);
                    break;
                case android.R.id.home:
                    finish();
                    break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_FIME_LOCATION:
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLoc, 15));
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(state == SHOW_MAP)
                        mMap.setMyLocationEnabled(true);
                    else if(state == CENTER_PLACE_ON_MAP)
                        setOnMapClickListener();
                    else
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLoc, 15));
                }
            return;
        }
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
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ACCESS_FIME_LOCATION);
        } else {
            if(state == SHOW_MAP)
                mMap.setMyLocationEnabled(true);
            else if(state == CENTER_PLACE_ON_MAP)
                setOnMapClickListener();
            else
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLoc, 15));
        }
    }
    private Map<Marker, Integer> markers;

    private void addMyPlaceMarkers() {
        ArrayList<MyPlace> myPlaces = MyPlacesData.getInstance().getMyPlaces();
        markers = new HashMap<Marker, Integer>((int) ((double)myPlaces.size()*1.2));
        for(int i = 0; i < myPlaces.size(); i++) {
            MyPlace place = myPlaces.get(i);
            String lat = place.getLatitude();
            String lon = place.getLongitude();
            LatLng loc = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
            MarkerOptions options = new MarkerOptions();
            options.position(loc);
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_beenhere));
            options.title(place.getName());
            Marker marker = mMap.addMarker(options);
            markers.put(marker, i);
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Intent intent = new Intent(MyPlacesMapsActivity.this, ViewMyPlaceActivity.class);
                int i = markers.get(marker);
                intent.putExtra("position", 1);
                startActivity(intent);
                return true;
            }
        });
    }

    private void setOnMapClickListener() {
        if(mMap != null) {
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    if(state == SELECT_COORDINATES)
                    {
                        String lon = Double.toString(latLng.longitude);
                        String lat = Double.toString(latLng.latitude);
                        Intent locationIntent = new Intent();
                        locationIntent.putExtra("lon", lon);
                        locationIntent.putExtra("lat", lat);
                        setResult(Activity.RESULT_OK, locationIntent);
                        finish();
                    }
                }
            });
        }
    }
}
