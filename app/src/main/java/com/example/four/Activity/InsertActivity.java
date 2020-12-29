package com.example.four.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.four.NetworkTask.NetworkTask;
import com.example.four.R;

public class InsertActivity extends Activity {


    String urlAddr = null;
    String urlIp = null;

    EditText insertName, insertTag, insertTel, insertAddr, insertDetail;
    Button addrinsertBtn;
    Button insertBackBtn;
    Button tagSelectBtn;

    //Tag 추가-------------------------------------
    boolean[] tagSelect = {false,false,false,false};
    //---------------------------------------------

    final static String TAG = "인설트액티비티";


    //-------------------------------------------------------
    //------------------onCreate start-----------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        urlAddr = "http://172.30.1.27:8080/test/mammamiaInsert.jsp?";



        //받아오는 ip값
        Intent intent = getIntent();

        urlIp = intent.getStringExtra("urlIp");

        urlAddr = "http://"+urlIp+":8080/test/mammamiaInsert.jsp?";

        //id 받아오기
        insertTag = findViewById(R.id.et_tagname_insert);
        insertName = findViewById(R.id.et_name_insert);
        insertTel = findViewById(R.id.et_tel_insert);
        insertAddr = findViewById(R.id.et_addr_insert);

        //태그 선택 버튼---------------------------------------
        tagSelectBtn = findViewById(R.id.btn_tagselect_insert);
        tagSelectBtn.setOnClickListener(tagselectClick);
        //----------------------------------------------------

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

            String addrTag = insertTag.getText().toString();
            String addrName = insertName.getText().toString();
            String addrTel = insertTel.getText().toString();
            //주소입력 추가 -----------
            String addrAddr = insertAddr.getText().toString();
            //----------------------
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



    View.OnClickListener tagselectClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(InsertActivity.this)
                    .setTitle("태그를 선택하세요")
                    .setIcon(R.mipmap.ic_icon)
                    .setMultiChoiceItems(R.array.tag, tagSelect,
                            new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                    tagSelect[which] = isChecked;
                                }
                            }
                    )
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String[] tag = getResources().getStringArray(R.array.tag);
                            TextView text = findViewById(R.id.et_tagname_insert);
                            String result = "";
                            for (int i=0; i<tagSelect.length; i++){
                                if (tagSelect[i]){
                                    result += tag[i]+ " ";
                                }
                            }
                            text.setText(result);
                        }
                    })
                    .setNegativeButton("취소", null)
                    .show();

        }
    };






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