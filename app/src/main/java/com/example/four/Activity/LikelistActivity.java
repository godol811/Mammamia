package com.example.four.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.four.Adapter.AddressAdapter;
import com.example.four.Bean.AddressDto;
import com.example.four.NetworkTask.NetworkTask;
import com.example.four.R;

import java.util.ArrayList;

public class LikelistActivity extends Activity {

    final static String TAG = "서치액티비티";


    String urlAddr = null;
    String urlIp = null;
    ArrayList<AddressDto> members;
    AddressAdapter adapter = null;
    private RecyclerView recyclerView = null;

    private RecyclerView.LayoutManager layoutManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likelist);



        recyclerView = findViewById(R.id.rl_address);


        //recyclerView.setHasFixedSize(true);


        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



        //받아오는 ip값
        Intent intent = getIntent();

        urlIp = intent.getStringExtra("urlIp");


        urlAddr = "http://"+urlIp+":8080/test/mammamiaLikelist.jsp";





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



                Intent intent = new Intent(LikelistActivity.this, ListviewActivity.class);


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




    private void connectGetData() {
        try {

            NetworkTask networkTask = new NetworkTask(LikelistActivity.this, urlAddr,"select");
            Object obj = networkTask.execute().get();
            members = (ArrayList<AddressDto>) obj;


            adapter = new AddressAdapter(LikelistActivity.this, R.layout.listlayout, members);
            recyclerView.setAdapter(adapter);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
