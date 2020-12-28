package com.example.four.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.four.R;
import com.example.four.SqliteDB.MemberInfo;

public class LoginActivity extends Activity {

    final static String TAG = "로그인";

    EditText etUserName, etUserTel, etUserAddrDetail;
    TextView tvUserAddr;
    Button btnLoginButton;
    MemberInfo memberInfo;
    CheckBox cbAgree;

    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        memberInfo = new MemberInfo(LoginActivity.this);

        etUserName = findViewById(R.id.et_name_login);
        etUserTel = findViewById(R.id.et_tel_login);
        tvUserAddr = findViewById(R.id.et_addr_login);
        etUserAddrDetail = findViewById(R.id.et_addrDetail_login);
        cbAgree = findViewById(R.id.cb_agree_login);

        btnLoginButton = findViewById(R.id.btn_login_login);

        btnLoginButton.setEnabled(false);

        findViewById(R.id.btn_login_login).setOnClickListener(mClickListener);
        selectAction();//데이터 베이스에 값이 있으면 그냥 메인페이지로 가기

        //정보 값 채워졌는지 확인하는 함수----------------------------------------------------


        cbAgree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    btnLoginButton.setEnabled(validation());
                }
            }
        });

        etUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s != null){
                    btnLoginButton.setEnabled(validation());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etUserTel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s != null){
                    btnLoginButton.setEnabled(validation());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        tvUserAddr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s != null){
                    btnLoginButton.setEnabled(validation());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etUserAddrDetail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s != null){
                    btnLoginButton.setEnabled(validation());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });









        //정보 값 채워졌는지 확인하는 함수----------------------------------------------------





//주소검색 API--------------------------------------------------------------
        tvUserAddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, AddressWebViewActivity.class);
                startActivityForResult(i, SEARCH_ADDRESS_ACTIVITY);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case SEARCH_ADDRESS_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    String data = intent.getExtras().getString("data");
                    if (data != null) {
                        tvUserAddr.setText(data);
                    }
                }
                break;
        }
//주소검색 API--------------------------------------------------------------

    }//onCreate

//로그인 버튼 ---------------------------------------------------------------

    View.OnClickListener mClickListener = new View.OnClickListener() {
        SQLiteDatabase DB;


        @Override
        public void onClick(View v) {
            String strUserName = etUserName.getText().toString().trim();
            String strUserAddr = tvUserAddr.getText().toString().trim();
            String strUserTel = etUserTel.getText().toString().trim();
            String strUserAddrDetail = etUserAddrDetail.getText().toString().trim();
            switch (v.getId()) {
                case R.id.btn_login_login:

                    try {
                        DB = memberInfo.getWritableDatabase();
                        String query = "INSERT INTO MEMBER (userName, userAddr, userTel) VALUES ('" + strUserName + "', '" + strUserAddr + " " + strUserAddrDetail + "', '" + strUserTel + "');";
                        DB.execSQL(query);
                        memberInfo.close();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("USERNAME", strUserName);
                        intent.putExtra("USERTEL", strUserTel);
                        intent.putExtra("USERADDR", strUserAddr);
                        startActivity(intent);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        }
    };
    //로그인 버튼 ---------------------------------------------------------------



    //만약에 데이터 베이스 값이 있으면 그대로 유지 하되 다음 페이지로 넘기기
    public void selectAction() {
        Log.d(TAG, "selectAction()");
        SQLiteDatabase DB;
        try {
            DB = memberInfo.getReadableDatabase();
            String query = "select userName, userAddr, userTel From member LIMIT 1;";
            Cursor cursor = DB.rawQuery(query, null);


            StringBuffer stringBuffer = new StringBuffer();//여기다가 데이터 담음
            while (cursor.moveToNext()) {
                if (cursor.toString().trim().length() != 0) {
                    String getUserName = cursor.getString(0);
                    String getUserAddr = cursor.getString(1);
                    String getUserTel = cursor.getString(2);
                    cursor.close();
                    memberInfo.close();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("USERNAME", getUserName);
                    intent.putExtra("USERTEL", getUserTel);
                    intent.putExtra("USERADDR", getUserAddr);
                    startActivity(intent);
                } else {
                    cursor.close();
                    memberInfo.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public boolean validation(){//빈칸 다 채워졌는지 확인하는 메소드 true이면 빈칸이 없고 false면 빈칸이 있다.
        return etUserName.getText().toString().trim().length() !=0 && etUserTel.getText().toString().trim().length() !=0&&
                tvUserAddr.getText().toString().trim().length() !=0&& etUserAddrDetail.getText().toString().trim().length() !=0
                && cbAgree.isChecked();

    }

}