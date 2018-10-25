package com.together.kimdog91.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<MarkerOptions> markerOptionsArrayList = new ArrayList<MarkerOptions>(); // 첫번째 마커 : 모임 장소 기준점

    private NetworkManager nm;

    TextView titleTextView;
    Button myPosBtn;
    Button moimPosBtn;

    int my_uid; // 내 uid
    int my_marker_index; // 내 marker 리스트 index

    int mid; // 현재 모임
    String mname;
    String mlon;
    String mlat;

    BitmapDrawable moim_marker_drawable;
    BitmapDrawable person_marker_drawable;
    BitmapDrawable my_marker_drawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // 내 uid
        my_uid = MoimActivity.uid;

        // 모임 정보 세팅
        Intent intent = getIntent();
        mid = intent.getIntExtra("mid", -1);
        mname = intent.getStringExtra("title");
        mlon = intent.getStringExtra("mlon");
        mlat = intent.getStringExtra("mlat");

        // 네트워크 매니저
        nm = new NetworkManager();
        nm.setActivity(this);

        moim_marker_drawable = (BitmapDrawable)getResources().getDrawable(R.drawable.moim_marker);
        person_marker_drawable = (BitmapDrawable)getResources().getDrawable(R.drawable.person_marker);
        my_marker_drawable = (BitmapDrawable)getResources().getDrawable(R.drawable.my_marker);

        // 모임 기준점 세팅
        LatLng latLngMoim = new LatLng(Double.parseDouble(mlat), Double.parseDouble(mlon));
        markerOptionsArrayList.add( new MarkerOptions()
                .position(latLngMoim)
                .title(mname)
                .snippet("모임 기준점")
                .icon(BitmapDescriptorFactory.fromBitmap(pngToBitmap(moim_marker_drawable, 100, 100))) );

        // 모임 멤버 로드
        loadMoimMembers();

        titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(mname);

        myPosBtn = (Button) findViewById(R.id.myPosBtn);
        moimPosBtn = (Button) findViewById(R.id.moimPosBtn);

        // 내 위치로
        myPosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.animateCamera(CameraUpdateFactory.newLatLng( markerOptionsArrayList.get(my_marker_index).getPosition() ));
            }
        });

        // 모임의 위치로
        moimPosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.animateCamera(CameraUpdateFactory.newLatLng( markerOptionsArrayList.get(0).getPosition() ));
            }
        });


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


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
        // 모든 마커 추가
        for(int i=0; i < markerOptionsArrayList.size(); i++) {
            Log.d("1", "loop: " + i);
            MarkerOptions cur = markerOptionsArrayList.get(i);
            if( i == 0 ) {
                mMap.addMarker(cur).showInfoWindow();
            }
            else {
                mMap.addMarker(cur);
            }
        }

        // 모임 기준점으로 카메라 위치
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerOptionsArrayList.get(0).getPosition(),15.0f));

    }

    private void loadMoimMembers() {
        // 데이터 세팅
        String[] colums = { "mid" };
        String[] data = { String.valueOf(mid) };
        try {
            // 서버와 통신
            JSONArray result = nm.executePost("/api/moim", colums, data);

            for(int i=0; i<result.length(); i++) {
                JSONObject resultObject = result.getJSONObject(i);

                int uid = resultObject.getInt("uid");
                String id = resultObject.getString("id");
                String name = resultObject.getString("name");
                String phone = resultObject.getString("phone");
                double lon = resultObject.getDouble("lon");
                double lat = resultObject.getDouble("lat");
                String comment = resultObject.getString("comment");
                String time = resultObject.getString("time");

                Log.d("1", uid +": "+id+"/"+name+"/"+phone+"/"+lon+"/"+lat+"/"+comment+"/"+time);

                // 내 정보일 경우
                if( uid == my_uid ) {
                    my_marker_index = i+1;
                    markerOptionsArrayList.add(new MarkerOptions()
                            .position(new LatLng(lat, lon))
                            .title(name)
                            .snippet(comment + "\n" + time)
                            .icon(BitmapDescriptorFactory.fromBitmap(pngToBitmap(my_marker_drawable, 100, 80))));
                }
                else {
                    // 리스트에 추가
                    markerOptionsArrayList.add(new MarkerOptions()
                            .position(new LatLng(lat, lon))
                            .title(name)
                            .snippet(comment + "\n" + time)
                            .icon(BitmapDescriptorFactory.fromBitmap(pngToBitmap(person_marker_drawable, 100, 100))));
                }

            }

        } catch ( Exception e ) {
            e.printStackTrace();
        } finally {

        }
    }

    private Bitmap pngToBitmap(BitmapDrawable png, int width, int height) {
        Bitmap b = png.getBitmap();
        return Bitmap.createScaledBitmap(b, width, height, false);
    }
}
