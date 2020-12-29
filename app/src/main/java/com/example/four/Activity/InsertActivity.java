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


    final static String TAG = "인설트액티비티";

    //field
    String urlAddr = null;
    EditText insertName, insertTag, insertTel, insertAddr, insertDetail;
    Button addrinsertBtn;
    Button insertBackBtn;






    //-------------------------------------------------------
    //------------------onCreate start-----------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        //여기! 본인! ip! 설정! 하는 ! 곳 !
        urlAddr = "http://172.30.1.27:8080/test/mammamiaInsert.jsp?";


        //id 받아오기
        insertTag = findViewById(R.id.et_tagname_insert);
        insertName = findViewById(R.id.et_name_insert);
        insertTel = findViewById(R.id.et_tel_insert);
        insertAddr = findViewById(R.id.et_addr_insert);
        insertDetail = findViewById(R.id.et_detail_insert);
        addrinsertBtn = findViewById(R.id.btn_ok_insert);
        insertBackBtn = findViewById(R.id.btn_back_insert);

        addrinsertBtn.setOnClickListener(onClickListener);
        insertBackBtn.setOnClickListener(onClickListener1);
    }



    //------------------onCreate finish-----------------------
    //--------------------------------------------------------





    //---------------------------------------------------------
    //--------------------addrinsertBtn 클릭시 이벤트----------------
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //변수에 insert한거 넣어주기
            String addrTag = insertTag.getText().toString();
            String addrName = insertName.getText().toString();
            String addrTel = insertTel.getText().toString();
            String addrAddr = insertAddr.getText().toString();
            String addrDetail = insertDetail.getText().toString();


            //넣어준 변수 입력하기
            urlAddr = urlAddr +  "addrTag=" + addrTag + "&addrName=" + addrName + "&addrTel=" + addrTel +"&addrAddr=" + addrAddr + "&addrDetail=" + addrDetail;
            connectInsertData();
            //Insert 완료 후, MainActivity로 이동
            Intent intent = new Intent(InsertActivity.this,MainActivity.class);
            startActivity(intent);
        }
    };
    //------------------------------------------------------------
    //--------------------addrinsertBtn 클릭시 이벤트 끝----------------






    //---------------------------------------------------------------
    //--------------------insertBackBtn 클릭시 이벤트 -----------------
    View.OnClickListener onClickListener1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };

    //-------------------insertBackBtn 클릭시 이벤트 끝 ---------------
    //----------------------------------------------------------------








    //-----------------------------------------------------------------------
    //------------------method (connectInsertData) start------------------------
    private void connectInsertData() {
        try {
            NetworkTask insertworkTask = new NetworkTask(InsertActivity.this, urlAddr,"insert");
            insertworkTask.execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    //-----------------------------------------------------------------------
    //------------------method (connectInsertData) finish------------------------




}//-----------------끝--------------