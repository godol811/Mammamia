package com.example.four.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.four.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapDetailActivity extends FragmentActivity implements OnMapReadyCallback {

    final static String TAG = "지도디테일액티비티";

    private GoogleMap mMap;
    private Geocoder geocoder;
    private Button button;
    private EditText editText;
    private TextView locationText;

    //지오코딩 변수
    double intentLat, intentLng;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_detail);

        editText = findViewById(R.id.et_search_mapdetail);
        button = findViewById(R.id.btn_search_mapdetail);



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fg_map_mapdetail);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        intentLat = intent.getDoubleExtra("intentLat",0);
        intentLng = intent.getDoubleExtra("intentLng",0);

        Log.d(TAG, String.valueOf(intentLat));
        Log.d(TAG, String.valueOf(intentLng));

    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        geocoder = new Geocoder(this);
        Toast.makeText(MapDetailActivity.this, "latLng", Toast.LENGTH_SHORT).show();
        // 맵 터치 이벤트 구현 //
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
            @Override
            public void onMapClick(LatLng point) {
                MarkerOptions mOptions = new MarkerOptions();

                // 마커 타이틀
                mOptions.title("마커 좌표");

                Double latitude = point.latitude; // 위도
                Double longitude = point.longitude; // 경도

                // 마커의 스니펫(간단한 텍스트) 설정
                mOptions.snippet(latitude.toString() + ", " + longitude.toString());
                // LatLng: 위도 경도 쌍을 나타냄
                mOptions.position(new LatLng(latitude, longitude));
                // 마커(핀) 추가
                googleMap.addMarker(mOptions);
            }
        });
        ////////////////////


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){

            public boolean onMarkerClick(Marker marker) {
                String text = "[마커 클릭 이벤트] latitude ="
                        + marker.getPosition().latitude + ", longitude ="
                        + marker.getPosition().longitude;
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG)
                        .show();
                return false;
            }
        });


        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                // 검색창에서 텍스트를 가져온다
                String str = editText.getText().toString();

                Geocoder geocoder = new Geocoder(getBaseContext());
                List<Address> addresses = null;

                try {
                    addresses = geocoder.getFromLocationName(str, 10);
                    if (addresses != null && !addresses.equals(" ")) {
                        search(addresses);
                    }
                } catch(Exception e) {

                }

            }
        });
        ////////////////////

        // 맵이 준비 후 시작될떄 실행
        // 초기화면을 서울 위도경도로 지정하고 카메라를 이동
        LatLng detail = new LatLng(intentLat, intentLng);

        MarkerOptions mOptions = new MarkerOptions();
        mOptions.title("search result");
//        mOptions.snippet(address);
//        mOptions.position(point);

        //시작할떄 줌하기
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(detail, 17));
    }



    // 구글맵 주소 검색 메서드
    protected void search(List<Address> addresses) {
        Address address = addresses.get(0);
        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

        String addressText = String.format("%s, %s", address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : " ", address.getFeatureName());

        locationText.setVisibility(View.VISIBLE);
        locationText.setText(address.getLongitude() + "\n" + addressText.split(","));

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(addressText);

        mMap.clear();
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

}