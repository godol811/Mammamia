package com.example.four.Activity;


import androidx.fragment.app.FragmentActivity;


import android.app.AlertDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.four.R;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

// googleplaces 추가
// 12월 29일 추가
import noman.googleplaces.NRPlaces;
import noman.googleplaces.Place;
import noman.googleplaces.PlaceType;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;

public class MapDetailActivity extends FragmentActivity implements OnMapReadyCallback, PlacesListener{

    final static String TAG = "지도디테일액티비티";

    private GoogleMap mMap;
    private Geocoder geocoder;
    private Button button, buttonDistance;
    private EditText editText;

    String[] locationText;
    String name;
    String tagName;

    //지오코딩 변수
    double intentLat, intentLng;


    //검색정보를 마커로 찍기위한 배열 변수 선언해줌
    //12월 29일 추가
    List<Marker> previous_marker = null;
    LatLng currentPosition;
    //------------

    //12월 30일 추가
    LatLng currentmakerPosition = null;
    LatLng previousmakerPosition = null;

    Marker addedMarker = null;

    int tracking = 0;
    private Location location;
    Location mCurrentLocatiion;
    private Marker currentMarker = null;
    private Marker markerposition = null;
    //----------------

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

        name = intent.getStringExtra("name");

        tagName = intent.getStringExtra("tagName");

        intentLat = intent.getDoubleExtra("intentLat",0);
        intentLng = intent.getDoubleExtra("intentLng",0);

        Log.d(TAG, String.valueOf(intentLat));
        Log.d(TAG, String.valueOf(intentLng));

        //마커주변에 주변정보 띄워주는 메소드
        showPlaceInformation(currentPosition);

        //마커리스트 배열 초기화
        previous_marker = new ArrayList<Marker>();


        new AlertDialog.Builder(MapDetailActivity.this)
                .setTitle("도움말")
                .setMessage("주변정보를 불러옵니다.\n마커를 클릭하면 주소록에 저장된 주소에서 \n남은 거리를 표시합니다.")
                .setNegativeButton("확인",null)
                .show();


    }



    @Override
    public void onMapReady(final GoogleMap googleMap) {


        mMap = googleMap;
        geocoder = new Geocoder(this);


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){

            public boolean onMarkerClick(Marker marker) {


                previousmakerPosition = marker.getPosition();

                //거리를 잴 두 마커의 정보 입력 메소드
                double distance = getDistance(currentmakerPosition,previousmakerPosition);
                String[] strdistance = Double.toString(distance).split("\\.");
                String text = "현재 주소록 마커에서 선택한 마커까지의 거리는"
                        + strdistance + "m입니다";

                Log.d(TAG, String.valueOf(getDistance(currentmakerPosition,previousmakerPosition)));
                Toast.makeText(MapDetailActivity.this, text, Toast.LENGTH_SHORT).show();

                new AlertDialog.Builder(MapDetailActivity.this)
                        .setTitle("거리")
                        .setMessage(text)
                        .setNegativeButton("확인",null)
                        .show();


                return false;
            }
        });


        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {

                Log.v(TAG,"헤이!");
                mMap.clear();//지도 클리어
                // 검색창에서 텍스트를 가져온다
                String str = editText.getText().toString();
                Log.v(TAG,str);

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
        // 초기화면을 마커의 위도경도로 지정하고 카메라를 이동
        previous_marker.clear();

        LatLng detail = new LatLng(intentLat, intentLng);
        currentmakerPosition = detail;

        Log.d(TAG, "currentmakerPosition :" + String.valueOf(currentmakerPosition));

        MarkerOptions mOptions = new MarkerOptions();

        mOptions.title(name);
        mOptions.snippet(tagName);
        mOptions.position(detail);

        mMap.addMarker(mOptions);

        //시작할떄 줌하기
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(detail, 17));


    }





    // 구글맵 주소 검색 메서드
    protected void search(List<Address> addresses) {

        previous_marker.clear();

        Log.v(TAG,"여기가 안들어오나");
        Address address = addresses.get(0);
        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());


        Log.v(TAG, String.valueOf(address));

        editText.getText();


        MarkerOptions marker = new MarkerOptions();
        Log.v(TAG,"search1");
        marker.position(latLng);
        Log.v(TAG,"search2");

        marker.title(String.valueOf(editText.getText()));
        Log.v(TAG,"search3");

        mMap.addMarker(marker);
        Log.v(TAG,"search4");
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        Log.v(TAG,"search5");
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        Log.v(TAG,"search6");
    }

//  googleplaces
//  12월 29일 추가
    @Override
    public void onPlacesFailure(PlacesException e) {

    }

    @Override
    public void onPlacesStart() {

    }

    @Override
    public void onPlacesSuccess(List<Place> places) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (noman.googleplaces.Place place : places) {

                    LatLng latLng
                            = new LatLng(place.getLatitude()
                            , place.getLongitude());

                    String markerSnippet = getCurrentAddress(latLng);

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title(place.getName());
                    markerOptions.snippet(markerSnippet);
                    //주변 위치 표시 마커 아이콘 변경
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                    Marker item = mMap.addMarker(markerOptions);
                    previous_marker.add(item);

                }

                //중복 마커 제거
                HashSet<Marker> hashSet = new HashSet<Marker>();
                hashSet.addAll(previous_marker);
                previous_marker.clear();
                previous_marker.addAll(hashSet);

            }
        });

    }

    @Override
    public void onPlacesFinished() {

    }


    public void showPlaceInformation(LatLng location) {

        //맵이 시작됨과 동시에 마커위치를 표시해줄것이므로 메소드에서 초기화 하면 안돼!
//            mMap.clear();//지도 클리어
        if (previous_marker != null) {
            previous_marker.clear();//지역정보 마커 클리어
        }



        Log.d(TAG, tagName);




            Log.d(TAG, "병원" + tagName);
            new NRPlaces.Builder()
                    .listener(MapDetailActivity.this)
                    .key("AIzaSyAEAJPO9fRiNPqBPFbvaiKasj7XCYJPl1U")//api키 입력
                    .latlng(intentLat, intentLng)//현재 마커 위치
                    .radius(500) //500 미터 내에서 검색
                    .build()
                    .execute();


// 지우지 말기----------------------------------
//        if (tagName == "키즈카페") {
//            Log.d(TAG, "키 : " + tagName);
//            new NRPlaces.Builder()
//                    .listener(MapDetailActivity.this)
//                    .key("AIzaSyAEAJPO9fRiNPqBPFbvaiKasj7XCYJPl1U")//api키 입력
//                    .latlng(intentLat, intentLng)//현재 마커 위치
//                    .radius(500) //500 미터 내에서 검색
//                    .type(PlaceType.CAFE)
//                    .type(PlaceType.BAKERY)
//                    .build()
//                    .execute();
//        }
//
//            case "유치원":
//                Log.d(TAG, "유치원 : " + tagName);
//                new NRPlaces.Builder()
//                        .listener(MapDetailActivity.this)
//                        .key("AIzaSyAEAJPO9fRiNPqBPFbvaiKasj7XCYJPl1U")//api키 입력
//                        .latlng(intentLat, intentLng)//현재 마커 위치
//                        .radius(500) //500 미터 내에서 검색
//                        .type(PlaceType.SCHOOL)
//                        .type(PlaceType.UNIVERSITY)
//                        .build()
//                        .execute();
//                break;
//
//            case "기타":
//                new NRPlaces.Builder()
//                        .listener(MapDetailActivity.this)
//                        .key("AIzaSyAEAJPO9fRiNPqBPFbvaiKasj7XCYJPl1U")//api키 입력
//                        .latlng(intentLat, intentLng)//현재 마커 위치
//                        .radius(500) //500 미터 내에서 검색
//                        .build()
//                        .execute();
//                break;
//
//        }

    }


    public String getCurrentAddress(LatLng latlng) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latlng.latitude,
                    latlng.longitude,
                    1);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }


        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0).toString();
        }

    }
//12월 29일 추가 끝----------------------------------------------

    public double getDistance(LatLng LatLng1, LatLng LatLng2) {

        double distance = 0;
        Location locationA = new Location("A");
        locationA.setLatitude(LatLng1.latitude);
        locationA.setLongitude(LatLng1.longitude);
        Location locationB = new Location("B");
        locationB.setLatitude(LatLng2.latitude);
        locationB.setLongitude(LatLng2.longitude);
        distance = locationA.distanceTo(locationB);

        return distance;

    }


//----------------------------------------
}