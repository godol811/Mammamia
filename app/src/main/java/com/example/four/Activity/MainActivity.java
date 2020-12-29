package com.example.four.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
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

        recyclerView = findViewById(R.id.rl_address);


        recyclerView.setHasFixedSize(true);


        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        //inwoo 추가
        //헤이! 여기 아이피만 교체해주세요!
        urlIp = "192.168.0.105";





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



        Log.v(TAG, "onResume");
        adapter.setOnItemClickListener(new AddressAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {



                Intent intent = new Intent(MainActivity.this, ListviewActivity.class);


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






}//------------------------------