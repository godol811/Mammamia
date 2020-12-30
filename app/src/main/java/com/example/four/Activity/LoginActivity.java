package com.example.four.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.four.R;
import com.example.four.SqliteDB.MemberInfo;

import java.util.regex.Pattern;

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


//주소검색 API--------------------------------------------------------------
        tvUserAddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, AddressWebViewActivity.class);
                startActivityForResult(i, SEARCH_ADDRESS_ACTIVITY);
            }
        });


        etUserTel.addTextChangedListener(new TextWatcher() {//자동으로 "-" 생성해서 전화번호에 붙여주기


            private int beforeLenght = 0;
            private int afterLenght = 0;

            //입력 혹은 삭제 전의 길이와 지금 길이를 비교하기 위해 beforeTextChanged에 저장
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeLenght = s.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //아무글자도 없는데 지우려고 하면 로그띄우기 에러방지
                if (s.length() <= 0) {
                    Log.d("addTextChangedListener", "onTextChanged: Intput text is wrong (Type : Length)");
                    return;
                }

                //특수문자 입력 방지
                char inputChar = s.charAt(s.length() - 1);
                if (inputChar != '-' && (inputChar < '0' || inputChar > '9')) {
                    etUserTel.getText().delete(s.length() - 1, s.length());
                    Log.d("addTextChangedListener", "onTextChanged: Intput text is wrong (Type : Number)");
                    return;
                }

                afterLenght = s.length();


                if (beforeLenght < afterLenght) {// 타자를 입력 중이면
                    if (afterLenght == 4 && s.toString().indexOf("-") < 0) { //subSequence로 지정된 문자열을 반환해서 "-"폰을 붙여주고 substring

                        etUserTel.setText(s.toString().subSequence(0, 3) + "-" + s.toString().substring(3, s.length()));
                        Log.v(TAG, String.valueOf(s.toString().substring(3, s.length())));
                    } else if (afterLenght == 9) {
                        etUserTel.setText(s.toString().subSequence(0, 8) + "-" + s.toString().substring(8, s.length()));
                        Log.v(TAG, String.valueOf(s.toString().substring(8, s.length())));
                    }
                }
                etUserTel.setSelection(etUserTel.length());

            }

            @Override
            public void afterTextChanged(Editable s) {
                // 생략
            }

        });//자동으로 전화번호 누르기 끝




        etUserName.setFilters(new InputFilter[]{new InputFilter() {//특수문자 제한
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                //한글 영어로 문자 제한
                Pattern ps = Pattern.compile("^[a-zA-Z-가-힣ㄱ-ㅎㅏ-ㅣ\\u318D\\u119E\\u11A2\\u2022\\u2025a\\u00B7\\uFE55]+$");
//                source.equals("")백스페이스 허용 처리
                if (source.equals("") || ps.matcher(source).matches()) {
                    return source;
                }
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("알림")
                        .setMessage("한글, 영문만 입력 가능합니다.")
                        .setNegativeButton("확인",null)
                        .setCancelable(false)
                        .show();
                return "";
            }
        }, new InputFilter.LengthFilter(5)});//특수문자 제한

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

    }

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
}//-------------------------------------