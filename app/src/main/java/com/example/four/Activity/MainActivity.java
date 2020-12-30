//Copyrightⓒ2008 by HOTPANTSCEO. All pictures cannot be copied without permission.
//
//(모든 저작권은 XX에게 있습니다. 이곳의 모든 사진들은 허가없이 복사할수 없습니다.)


package com.example.four.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.four.Adapter.AddressAdapter;
import com.example.four.Bean.AddressDto;
import com.example.four.ItemHelper.ItemTouchHelperCallback;

import com.example.four.NetworkTask.NetworkTask;
import com.example.four.R;

import java.util.ArrayList;

public class MainActivity extends Activity {

    final static String TAG = "메인";


    String urlAddr = null;

    String urlIp = null;
    //-----------------
    ArrayList<AddressDto> members;
    AddressAdapter adapter = null;
    private RecyclerView recyclerView = null;


    private RecyclerView.LayoutManager layoutManager = null;


    ItemTouchHelper helper;

    ImageButton ivSearchActivity;//검색버튼

    ImageButton Iblikelistbtn;//라이크리스트버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE); //사용자에게 사진 사용 권한 받기 (가장중요함)


        recyclerView = findViewById(R.id.rl_address);


        recyclerView.setHasFixedSize(true);


        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

/////////////////-자기 아이피 챙기기-////////////////////////
        urlIp = "192.168.1.5";//하진                //
//        urlIp = "172.30.1.27";//혜정                  ////
//        urlIp = "192.168.0.13";//이누               //\
//        urlIp = "192.168.0.105";//보람                  //
//        urlIp = "192.168.35.147";//종찬 아이피            //
/////////////////////////////////////////////////////////


        urlAddr = "http://" + urlIp + ":8080/test/mammamia.jsp";

        ivSearchActivity = findViewById(R.id.btn_search_main);//검색 인텐트로 이동하기 위해 버튼 선언
        ivSearchActivity.setOnClickListener(searchClickListener);

        Iblikelistbtn =findViewById((R.id.btn_likelist_main));
        Iblikelistbtn.setOnClickListener(likelistClickListener);

        findViewById(R.id.btn_insert_listview).setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InsertActivity.class);


                intent.putExtra("urlIp", urlIp);//ip주소 보내기
                startActivity(intent);
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();


        connectGetData();
        registerForContextMenu(recyclerView);

        Log.v(TAG, "onResume"); //태그 선언


        Log.v(TAG, "onResume");
        adapter.setOnItemClickListener(new AddressAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                Intent intent = new Intent(MainActivity.this, ListviewActivity.class);//리스트 클릭시 리스트뷰 넘어가기
                intent.putExtra("urlAddr", urlAddr);
                intent.putExtra("urlIp",urlIp);
                intent.putExtra("addrNo", members.get(position).getAddrNo());
                intent.putExtra("addrName", members.get(position).getAddrName());
                intent.putExtra("addrTag", members.get(position).getAddrTag());
                intent.putExtra("addrTel", members.get(position).getAddrTel());
                intent.putExtra("addrDetail", members.get(position).getAddrDetail());
                intent.putExtra("addrAddr", members.get(position).getAddrAddr());
                intent.putExtra("addrImagePath", members.get(position).getAddrImagePath());


                startActivity(intent);


            }
        });
    }


    View.OnClickListener searchClickListener = new View.OnClickListener() { //돋보기 버튼 클릭 - 검색 인텐트로 이동
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
            intent.putExtra("urlIp", urlIp);
            startActivity(intent);
        }
    };


    //하진추가- 라이크 리스트 버튼--------
    View.OnClickListener likelistClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(),LikelistActivity.class);
            intent.putExtra("urlIp", urlIp);
            startActivity(intent);
        }
    };
    //----------------------


    private void connectGetData() {
        try {

            NetworkTask networkTask = new NetworkTask(MainActivity.this, urlAddr, "select");
            Object obj = networkTask.execute().get();
            members = (ArrayList<AddressDto>) obj;


            adapter = new AddressAdapter(MainActivity.this, R.layout.listlayout, members);
            recyclerView.setAdapter(adapter);


            helper = new ItemTouchHelper(new ItemTouchHelperCallback(adapter)); //ItemTouchHelper 생성
            helper.attachToRecyclerView(recyclerView);//RecyclerView에 ItemTouchHelper 붙이기




        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setUpRecyclerView() {
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                helper.onDraw(c, parent, state);
            }
        });
    }

    //배경 터치 시 키보드 사라지게
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        InputMethodManager imm;
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }


}//------------------------------