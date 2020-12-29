package com.example.four.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.four.Adapter.AddressAdapter;
import com.example.four.Bean.AddressDto;
import com.example.four.NetworkTask.NetworkTask;
import com.example.four.R;

import java.util.ArrayList;

public class MainActivity extends Activity {

    final static String TAG = "메인";

    //field
    String urlAddr = null;
    ArrayList<AddressDto> members;
    AddressAdapter adapter = null;
    private RecyclerView recyclerView = null;
    private RecyclerView.LayoutManager layoutManager = null;

    //-------------------------------------------------------
    //------------------onCreate start-----------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //리사이클러뷰 레이아웃 가져오기
        recyclerView = findViewById(R.id.rl_address);
        recyclerView.setHasFixedSize(true); //Adapter Item View의 내용이 변경되어도 RecyclerView의 크기는 고정


        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        //여기! 본인 ! ip ! 수정 ! 하는 ! 곳!
        urlAddr = "http://172.30.1.27:8080/test/mammamia.jsp";


        //Insert 버튼 실행
        findViewById(R.id.btn_insert_listview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //버튼 클릭시 InsertActivity로 이동
                Intent intent = new Intent(MainActivity.this, InsertActivity.class);
                startActivity(intent);
            }
        });


    }

    //------------------onCreate finish-----------------------
    //--------------------------------------------------------




    //--------------------------------------------------------
    //------------------onResume start------------------------
    @Override
    protected void onResume() {
        super.onResume();


        connectGetData(); // data 갱신

        Log.v(TAG, "onResume"); //태그 선언


        //리사이클러뷰는 직접 onClick,LongClick 이 불가해서 adapter 에서 가져온다.
        adapter.setOnItemClickListener(new AddressAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                //Item Click 시 ListViewActivity로 이동
                Intent intent = new Intent(MainActivity.this, ListviewActivity.class);

                //intent 해줄 값
                intent.putExtra("urlAddr", urlAddr);
                intent.putExtra("addrNo", members.get(position).getAddrNo());
                intent.putExtra("addrName", members.get(position).getAddrName());
                intent.putExtra("addrTag", members.get(position).getAddrTag());
                intent.putExtra("addrTel", members.get(position).getAddrTel());
                intent.putExtra("addrDetail", members.get(position).getAddrDetail());
                intent.putExtra("addrAddr", members.get(position).getAddrAddr());


                startActivity(intent);


            }
        });
    }

    //------------------onResume finish------------------------
    //---------------------------------------------------------


    //-----------------------------------------------------------------------
    //------------------method (connectGetData) start------------------------

    private void connectGetData() {
        try {

            NetworkTask networkTask = new NetworkTask(MainActivity.this, urlAddr, "select");
            Object obj = networkTask.execute().get();
            members = (ArrayList<AddressDto>) obj;


            adapter = new AddressAdapter(MainActivity.this, R.layout.listlayout, members);
            recyclerView.setAdapter(adapter);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //-----------------method (connectGetData) finish----------------------
    //---------------------------------------------------------------------


}   //---------------끝---------------