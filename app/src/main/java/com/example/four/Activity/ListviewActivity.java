package com.example.four.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.four.NetworkTask.NetworkTask;
import com.example.four.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListviewActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    final static String TAG = "리스트뷰액티비티";

    String urlIp = null;
    String urlAddr = null;
    TextView addrTag, addrName, addrTel, addrDetail, addrAddr;
    ImageView profileImage;
    ArrayList<Address> members;
    Button backbtn, upbtn;
    int addrNo;
    String tagName;
    String tel;
    String name;
    String detail;
    String imagePath;
    String addr;
    Button delebtn;
    double intentLat, intentLng;
    private GoogleMap mMap;

    private RecyclerView recyclerView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ActivityCompat.requestPermissions(ListviewActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE); //사용자에게 사진 사용 권한 받기 (가장중요함)


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fg_map_listview);//Fragment 가져오기

        mapFragment.getMapAsync(this);//onMapReady 메소드 호출
        Intent intent = getIntent();
        urlIp = intent.getStringExtra("urlIp");


        addrNo = intent.getIntExtra("addrNo", 0);
        name = intent.getStringExtra("addrName");
        tel = intent.getStringExtra("addrTel");
        tagName = intent.getStringExtra("addrTag");
        detail = intent.getStringExtra("addrDetail");
        addr = intent.getStringExtra("addrAddr");
        imagePath = intent.getStringExtra("addrImagePath");

        Log.d(TAG,imagePath);


        addrName = findViewById(R.id.tv_name_listview);
        addrTag = findViewById(R.id.tv_tagname_listview);
        addrTel = findViewById(R.id.tv_tel_listview);
        //addrAddr추가
        addrAddr = findViewById(R.id.tv_addr_listview);
        //------------------

        //addrimage 추가
        profileImage = findViewById(R.id.iv_profile_listview);

        String urlAddr = "http://" + urlIp + ":8080/pictures/";
//        Picasso.get().load(urlAddr+imagePath).into(profileImage);
        Glide.with(ListviewActivity.this).
                load(urlAddr + imagePath).
                override(300, 300).
                placeholder(R.drawable.shape_circle).
                apply(new RequestOptions().circleCrop()).into(profileImage);


        addrDetail = findViewById(R.id.tv_detail_listview);
        upbtn = findViewById(R.id.btn_update_listview);
        delebtn = findViewById(R.id.btn_delete_listview);
        addrName.setText(name);
        addrDetail.setText(detail);
        addrAddr.setText(addr);
        addrTel.setText(tel);
        addrTag.setText(tagName);
        upbtn.setOnClickListener(onClickListener);
        delebtn.setOnClickListener(onClickListener2);
        geocoding();//지오코딩해주는 메소드
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent1 = new Intent(ListviewActivity.this, UpdateActivity.class);
            intent1.putExtra("urlIp", urlIp); //ip값 보내기
            intent1.putExtra("urlAddr", urlAddr);
            intent1.putExtra("addrNo", addrNo);
            intent1.putExtra("addrName", name);
            intent1.putExtra("addrTag", tagName);
            intent1.putExtra("addrTel", tel);
            intent1.putExtra("addrDetail", detail);
            intent1.putExtra("addrAddr", addr);
            intent1.putExtra("addrImagePath", imagePath);   //imagepath
            startActivity(intent1);
        }
    };

    View.OnClickListener onClickListener2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            urlAddr = "http://" + urlIp + ":8080/test/mammamiaDelete.jsp?";
            urlAddr = urlAddr + "addrNo=" + addrNo;
            connectDeleteData();
            Log.v("헤이~", urlAddr);
            Toast.makeText(ListviewActivity.this, "삭제가 완료되었습니다 ", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ListviewActivity.this, MainActivity.class);
            startActivity(intent);
        }
    };

    private void connectDeleteData() {
        try {
            NetworkTask deleteworkTask = new NetworkTask(ListviewActivity.this, urlAddr, "delete");
            deleteworkTask.execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {//OnMapReadyCallback 인터페이스의 onMapReady 메소드를 구현해줘야 한다.
                                                 //맵이 사용할 준비가 되었을 때(NULL이 아닌 GoogleMap 객체를 파라미터로 제공해 줄 수 있을 때) 호출되어지는 메소드
        mMap = googleMap;  //다시 변환후 넣어줘야됨

        LatLng markerPosition = new LatLng(intentLat, intentLng);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(markerPosition);//마커 표시 위치
        //여기에 주소록 이름 넣어줄꺼야
        markerOptions.title(name);//마커 타이틀
        //여기에 주소록 태그 이름 넣어줄꺼야
        markerOptions.snippet(tagName);//마커 클릭시 보여지는 말풍선
        mMap.addMarker(markerOptions);//마커 추가
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerPosition, 16));
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                //mapdetail로 이동
                Intent intent = new Intent(ListviewActivity.this, MapDetailActivity.class);

                intent.putExtra("name", name);
                intent.putExtra("tagName", tagName);

                //주소값 보내주기 마커위치에서 시작하기 위해서
                intent.putExtra("intentLat", intentLat);
                intent.putExtra("intentLng", intentLng);
                Log.d(TAG, addr);
                startActivity(intent);
            }
        });
    }

    //지오코딩 해주는 메소드
    private void geocoding() {
        // 주소 -> 좌표 (지오코딩)
        //지오 코딩 작업을 수행하는 객체 생성
        //Locale 객체를 매개변수로 하여 Geocoder객체를 사용하면 주소 결과를 사용자의 지역에 맞게 가져올 수 있다.
        Geocoder geocoder = new Geocoder(this, Locale.KOREA);
        //지오코더에게 지오코딩작업 요청
        try {

            //getFromLocationName : 주소로 부터 가져온 위도와 경도 값
            //maxResults : 반환받고싶은 주소의 최대 개수
            List<Address> addresses = geocoder.getFromLocationName(addr, 3); //최대 3개까지 받는데, 0~3개까지 있으면 받는다.
            //StringBuffer객체 생성
            StringBuffer buffer = new StringBuffer();
            for (android.location.Address t : addresses) {
                buffer.append(t.getLatitude() + ", " + t.getLongitude() + "\n");
            }
            //다이얼로그로 좌표들 보여주기
            //주소록에 입력하는 주소값을 좌표로 저장해놓자! 핀으로 찍어서 보여줘야지
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //좌표값 저장
            intentLat = addresses.get(0).getLatitude();
            intentLng = addresses.get(0).getLongitude();
            Log.v(TAG, "intentLat : " + String.valueOf(intentLat));
            Log.v(TAG, "intentLng : " + String.valueOf(intentLng));



        } catch (IOException e) {
            Toast.makeText(this, "검색 실패", Toast.LENGTH_SHORT).show();
        }
    }
}//------------------------------