package com.example.four.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.four.Adapter.AddressAdapter;
import com.example.four.Bean.AddressDto;
import com.example.four.NetworkTask.NetworkTask;
import com.example.four.R;

import java.util.ArrayList;

public class LikelistActivity extends  Activity {

    final static String TAG = "라이크액티비티";

    String urlAddr = null;
    String urlIp = null;
    ArrayList<AddressDto> members;

    AddressAdapter adapter = null;
    private RecyclerView recyclerView = null;
    private RecyclerView.LayoutManager layoutManager = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        recyclerView = findViewById(R.id.rv_likelist);


        recyclerView.setHasFixedSize(true);


        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //받아오는 ip값
        Intent intent = getIntent();

        urlIp = intent.getStringExtra("urlIp");


        urlAddr = "http://"+urlIp+":8080/test/mammamialikelist.jsp? addrLike= 1";

    }

    private void connectGetData() {

        try {

            NetworkTask networkTask = new NetworkTask(LikelistActivity.this, urlAddr,"Likelist");
            Object obj = networkTask.execute().get();
            members = (ArrayList<AddressDto>) obj;


            adapter = new AddressAdapter(LikelistActivity.this, R.layout.activity_likelist, members);
            recyclerView.setAdapter(adapter);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
