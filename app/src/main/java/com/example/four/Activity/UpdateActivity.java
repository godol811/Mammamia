package com.example.four.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.four.NetworkTask.NetworkTask;
import com.example.four.R;

public class UpdateActivity extends Activity {


    String tagName, addrName, addrTel, addrDetail;
    final static String TAG = "업데이트액티비티";
    //addr1 추가
    String tag1, name1, tel1, detail1, addr1;
    int num;
    EditText tag, name, tel, detail, addr;

    String urlAddr = null;
    String urlIp = null;

    Button okbtn;
    Button backbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);





        Intent intent = getIntent();

        urlIp = intent.getStringExtra("urlIp");

        tag1 = intent.getStringExtra("addrTag");
        name1 = intent.getStringExtra("addrName");
        tel1 = intent.getStringExtra("addrTel");
        addr1 = intent.getStringExtra("addrAddr");
        detail1 = intent.getStringExtra("addrDetail");
        num = intent.getIntExtra("addrNo",0);



        urlAddr = "http://"+urlIp+":8080/test/mammamiaUpdate.jsp?";




        tag = findViewById(R.id.et_tagname_update);
        name = findViewById(R.id.et_name_update);
        tel = findViewById(R.id.et_tel_update);
        addr = findViewById(R.id.et_addr_update);
        detail = findViewById(R.id.et_detail_update);
        okbtn = findViewById(R.id.btn_ok_update);
        backbtn = findViewById(R.id.btn_back_update);


        tag.setText(tag1);
        name.setText(name1);
        tel.setText(tel1);
        addr.setText(addr1);
        detail.setText(detail1);

        okbtn.setOnClickListener(onClickListener);
        backbtn.setOnClickListener(onClickListener1);



    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String st_tag = tag.getText().toString();
            String st_name = name.getText().toString();
            String st_tel = tel.getText().toString();
            String st_addr = addr.getText().toString();
            String st_detail = detail.getText().toString();


            urlAddr = urlAddr +"addrNo="+num+"&addrName=" + st_name + "&addrTel=" + st_tel + "&addrAddr=" + st_addr + "&addrDetail=" + st_detail + "&addrTag=" + st_tag;
            connectUpdateData();

            Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
            Toast.makeText(UpdateActivity.this,"수정이완료돼싸",Toast.LENGTH_SHORT).show();
            startActivity(intent);




        }
    };




    View.OnClickListener onClickListener1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };

    private void connectUpdateData() {
        try {
            NetworkTask updateworkTask = new NetworkTask(UpdateActivity.this, urlAddr,"update");
            updateworkTask.execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}//-----------------