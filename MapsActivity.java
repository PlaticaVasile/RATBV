package edu.unitbv.ratbv;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import java.io.InputStream;
import java.util.ArrayList;
import jxl.Sheet;
import jxl.Workbook;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private Button b;
    private FusedLocationProviderClient fpc ;
    private final static int Request_Code=100;
    ArrayList<LatLng> Coord = new ArrayList<LatLng>();
    ArrayList<String> Nume_Statii=new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_activity);
       mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.ErsteMap);
        mapFragment.getMapAsync(this);
        atribuire();//adaugarea de componente in arraylist-uri, unul ce tine de coordonate, altul tine de numele statiilor
        fpc = LocationServices.getFusedLocationProviderClient(MapsActivity.this);
        b=(Button)findViewById(R.id.my_location);b.setText("\n  Unde mă aflu ?  ");
        b.setTextColor(Color.parseColor("#FFFFFF"));b.setBackgroundColor(Color.parseColor("#018786"));
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getMyLocation();
            }
        });
    }



    @SuppressLint("MissingPermission")
    private void getMyLocation() {
        if(verificare()) {
                fpc.flushLocations();
                fpc.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            mapFragment.getMapAsync(new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(@NonNull GoogleMap googleMap) {
                                    LatLng l_l = new LatLng(location.getLatitude(), location.getLongitude());
                                    MarkerOptions mo = new MarkerOptions().position(l_l).title("Sunt aici").
                                            icon(BitmapFromVector(getApplicationContext(), R.drawable.me_icon));
                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(l_l, 15));
                                    mMap.addMarker(mo);
                                }
                            });

                        }
                    }
                });
            } else {
                askPermission();
            }

    }
    private boolean verificare(){
        boolean b=false;
        if ( Build.VERSION.SDK_INT >= 23 &&
                (ContextCompat.checkSelfPermission( MapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission( MapsActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            b=true ;
        }
        return b;
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(MapsActivity.this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION},Request_Code);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(Request_Code==requestCode){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                getMyLocation();
        }else{
            Toast.makeText(this,"Permisiune necesara",Toast.LENGTH_SHORT).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


        public void atribuire(){
        try{
            int i,j;
            AssetManager am=getAssets();
            InputStream is=am.open("CoordonatePlan.xls");
            Workbook wb= Workbook.getWorkbook(is);
            Sheet s=wb.getSheet(0);
            int row=s.getRows(), col=s.getColumns();
            for(j=0;j<col-1;j++){//mergem pe coloana, apoi pe linie
                for(i=0;i<row;i++){
                    if(j==0) Nume_Statii.add(s.getCell(j,i).getContents());
                    else Coord.add(new LatLng(Double.parseDouble(s.getCell(j,i).getContents()),
                            Double.parseDouble(s.getCell(j+1,i).getContents())));
                }
            }
        }
        catch(Exception e){}
    }
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        for (int i = 0; i < Coord.size(); i++) {
            mMap.addMarker(new MarkerOptions().position(Coord.get(i)).title(Nume_Statii.get(i)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(Coord.get(i)));
            //aici se pune acel mic text box de deasupra iconului si se stabileste titlul
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker){
                String markertitle=marker.getTitle();
                Intent in = new Intent(MapsActivity.this,Info.class);
                in.putExtra("title",markertitle);
                startActivity(in);
                return false;
            }
        });
        }
    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        // below line is use to set bounds to our vector drawable.
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
    }

