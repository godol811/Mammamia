package com.example.four.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.four.NetworkTask.NetworkTask;
import com.example.four.R;

public class InsertActivity extends Activity {


    String urlAddr = null;

    EditText insertName, insertTag, insertTel, insertAddr, insertDetail;
    Button addrinsertBtn;
    Button insertBackBtn;

    final static String TAG = "인설트액티비티";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        urlAddr = "http://192.168.0.105:8080/test/mammamiaInsert.jsp?";



        insertTag = findViewById(R.id.et_tagname_insert);
        insertName = findViewById(R.id.et_name_insert);
        insertTel = findViewById(R.id.et_tel_insert);

        //주소입력 추가 -----------
        insertAddr = findViewById(R.id.et_addr_insert);
        //----------------------

        insertDetail = findViewById(R.id.et_detail_insert);
        addrinsertBtn = findViewById(R.id.btn_ok_insert);
        insertBackBtn = findViewById(R.id.btn_back_insert);

        addrinsertBtn.setOnClickListener(onClickListener);
        insertBackBtn.setOnClickListener(onClickListener1);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String addrTag = insertTag.getText().toString();
            String addrName = insertName.getText().toString();
            String addrTel = insertTel.getText().toString();
            //주소입력 추가 -----------
            String addrAddr = insertAddr.getText().toString();
            //----------------------
            String addrDetail = insertDetail.getText().toString();

            //addrAddr추가
            urlAddr = urlAddr +  "addrTag=" + addrTag + "&addrName=" + addrName + "&addrTel=" + addrTel +"&addrAddr=" + addrAddr + "&addrDetail=" + addrDetail;
            connectInsertData();
            Intent intent = new Intent(InsertActivity.this,MainActivity.class);
            startActivity(intent);
        }
    };
    View.OnClickListener onClickListener1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };


    private void connectInsertData() {
        try {
            NetworkTask insertworkTask = new NetworkTask(InsertActivity.this, urlAddr,"insert");
            insertworkTask.execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}//-------------------------------