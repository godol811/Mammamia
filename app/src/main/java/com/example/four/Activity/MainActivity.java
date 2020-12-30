package com.example.four.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.example.four.ItemHelper.ListAdapter;
import com.example.four.NetworkTask.NetworkTask;
import com.example.four.R;

import java.util.ArrayList;

public class MainActivity extends Activity {

    final static String TAG = "메인";


    String urlAddr = null;
    //ip 변수 추가
    String urlIp = null;
    //-----------------
    ArrayList<AddressDto> members;
    AddressAdapter adapter = null;
    private RecyclerView recyclerView = null;


    private RecyclerView.LayoutManager layoutManager = null;

    //여기서부터 하진추가
    ///////////////////////////////////////////////
    ListAdapter adapter2;
    ItemTouchHelper helper;
    ///////////////////////////////////////////////

    //---------------검색 버튼
    ImageButton ivSearchActivity;
    //------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE); //사용자에게 사진 사용 권한 받기 (가장중요함)


        recyclerView = findViewById(R.id.rl_address);


        recyclerView.setHasFixedSize(true);


        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        //inwoo 추가
        //헤이! 여기 아이피만 교체해주세요!
//        urlIp = "192.168.0.105";
        urlIp = "222.106.89.206";//종찬 아이피






        urlAddr = "http://"+urlIp+":8080/test/mammamia.jsp";

        //검색 인텐트로 이동하기 위해 버튼 선언--------------------
        ivSearchActivity = findViewById(R.id.btn_search_main);
        ivSearchActivity.setOnClickListener(searchClickListener);
        //--------------------------------------------------------


        //여기서부터 하진추가
        //////////////////////////////////////////////////////
        //ItemTouchHelper 생성
        helper = new ItemTouchHelper(new ItemTouchHelperCallback(adapter2));

        //RecyclerView에 ItemTouchHelper 붙이기
        helper.attachToRecyclerView(recyclerView);

        findViewById(R.id.btn_insert_listview).setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,InsertActivity.class);


                //ip주소 보내기
                intent.putExtra("urlIp", urlIp);
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



                Intent intent = new Intent(MainActivity.this, ListviewActivity.class);



                intent.putExtra("urlIp", urlIp);


                intent.putExtra("urlAddr", urlAddr);

                intent.putExtra("addrNo", members.get(position).getAddrNo());
                intent.putExtra("addrName", members.get(position).getAddrName());
                intent.putExtra("addrTag", members.get(position).getAddrTag());
                intent.putExtra("addrTel", members.get(position).getAddrTel());
                intent.putExtra("addrDetail", members.get(position).getAddrDetail());
                intent.putExtra("addrAddr", members.get(position).getAddrAddr());
                intent.putExtra("addrImagePath",members.get(position).getAddrImagePath());


                startActivity(intent);


            }
        });
    }


    //돋보기 버튼 클릭 - 검색 인텐트로 이동
    View.OnClickListener searchClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(),SearchActivity.class);
            intent.putExtra("urlIp", urlIp);
            startActivity(intent);
        }
    };


    private void connectGetData() {
        try {

            NetworkTask networkTask = new NetworkTask(MainActivity.this, urlAddr,"select");
            Object obj = networkTask.execute().get();
            members = (ArrayList<AddressDto>) obj;


            adapter = new AddressAdapter(MainActivity.this, R.layout.listlayout, members);
            recyclerView.setAdapter(adapter);



        } catch (Exception e) {
            e.printStackTrace();
        }
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