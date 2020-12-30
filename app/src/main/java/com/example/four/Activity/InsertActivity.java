package com.example.four.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.four.NetworkTask.NetworkTask;
import com.example.four.R;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class InsertActivity extends Activity {


    String urlAddr = null;
    String urlIp = null;
    String imagePath;
    String imageName;
    private String img_path = new String();
    private Bitmap image_bitmap_copy = null;
    private Bitmap image_bitmap = null;

    EditText insertName, insertTag, insertTel, insertDetail;
    TextView insertAddr;
    Button addrinsertBtn;
    Button insertBackBtn;
    Button tagSelectBtn;

    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;


    //Tag 추가-------------------------------------
    boolean[] tagSelect = {false,false,false,false};
    //---------------------------------------------
    private final int REQ_CODE_SELECT_IMAGE = 100;

    final static String TAG = "인설트액티비티";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);


        //받아오는 ip값
        Intent intent = getIntent();

        urlIp = intent.getStringExtra("urlIp");

        urlAddr = "http://" + urlIp + ":8080/test/mammamiaInsert.jsp?";

        //id 받아오기
        insertTag = findViewById(R.id.et_tagname_insert);
        insertName = findViewById(R.id.et_name_insert);
        insertTel = findViewById(R.id.et_tel_insert);


        //주소입력 추가 -----------
        insertAddr = findViewById(R.id.et_addr_insert);
        //----------------------

        //tag----------------------
        tagSelectBtn = findViewById(R.id.btn_tagselect_insert);
        tagSelectBtn.setOnClickListener(tagselectClick);
        //==-----------------------


        insertDetail = findViewById(R.id.et_detail_insert);
        addrinsertBtn = findViewById(R.id.btn_ok_insert);
        insertBackBtn = findViewById(R.id.btn_back_insert);

        addrinsertBtn.setOnClickListener(onClickListener);
        insertBackBtn.setOnClickListener(onClickListener1);

//---------------------------------------사진 불러오기 onclick-----------------------
        findViewById(R.id.iv_image_insert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);

            }
        });

//---------------------------------------사진 불러오기 onclick-----------------------

        //12월 29일 추가
        //주소검색 API--------------------------------------------------------------

        insertAddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(InsertActivity.this, AddressWebViewActivity.class);
                startActivityForResult(i, SEARCH_ADDRESS_ACTIVITY);
            }
        });


        //12월 29일 인우 추가
        //자동으로 "-" 생성해서 전화번호에 붙여주기------------------------
        insertTel.addTextChangedListener(new TextWatcher() {


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
                    insertTel.getText().delete(s.length() - 1, s.length());
                    Log.d("addTextChangedListener", "onTextChanged: Intput text is wrong (Type : Number)");
                    return;
                }

                afterLenght = s.length();

                // 타자를 입력 중이면
                if (beforeLenght < afterLenght) {
                    if (afterLenght == 4 && s.toString().indexOf("-") < 0) {
                      //subSequence로 지정된 문자열을 반환해서 "-"폰을 붙여주고 substring
                        insertTel.setText(s.toString().subSequence(0, 3) + "-" + s.toString().substring(3, s.length()));
                        Log.v(TAG, String.valueOf(s.toString().substring(3, s.length())));
                    } else if (afterLenght == 9) {
                        insertTel.setText(s.toString().subSequence(0, 8) + "-" + s.toString().substring(8, s.length()));
                        Log.v(TAG, String.valueOf(s.toString().substring(8, s.length())));
                    }
                }
                insertTel.setSelection(insertTel.length());

            }

            @Override
            public void afterTextChanged(Editable s) {
                // 생략
            }

        });

    }

////자동으로 "-" 생성해서 전화번호에 붙여주기-------------------------------------------------------



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
            //imgaepath 추가 - 종찬
            urlAddr = urlAddr + "addrTag=" + addrTag + "&addrName=" + addrName + "&addrTel=" + addrTel + "&addrAddr=" + addrAddr + "&addrDetail=" + addrDetail + "&addrImagePath=" + imageName;
            connectInsertData();
            Intent intent = new Intent(InsertActivity.this, MainActivity.class);
            startActivity(intent);
        }
    };
    View.OnClickListener onClickListener1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };

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



    private void connectInsertData() {
        try {
            NetworkTask insertworkTask = new NetworkTask(InsertActivity.this, urlAddr,"insert");
            insertworkTask.execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //----------------------이미지 관련 메소드----------------------------------------------
    //
    //고종찬 = 바지사장
    //Multipart 사진 서버에 입력하는 부분 성공
    //---------------------------------------------------------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        //12월 29일 추가
        //주소 api  인서트에 추가
        //-----------------------------------------------------
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case SEARCH_ADDRESS_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    String data1 = data.getExtras().getString("data");
                    if (data1 != null) {
                        insertAddr.setText(data1);
                    }
                }
                break;
        }
        //-----------------------------------------------------


        Toast.makeText(getBaseContext(), "resultCode : " + data, Toast.LENGTH_SHORT).show();

        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    img_path = getImagePathToUri(data.getData()); //이미지의 URI를 얻어 경로값으로 반환.
                    Toast.makeText(getBaseContext(), "img_path : " + img_path, Toast.LENGTH_SHORT).show();
                    //이미지를 비트맵형식으로 반환
                    image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                    //사용자 단말기의 width , height 값 반환
                    int reWidth = (int) (getWindowManager().getDefaultDisplay().getWidth());
                    int reHeight = (int) (getWindowManager().getDefaultDisplay().getHeight());

                    //image_bitmap 으로 받아온 이미지의 사이즈를 임의적으로 조절함. width: 400 , height: 300
                    image_bitmap_copy = Bitmap.createScaledBitmap(image_bitmap, 400, 300, true);
                    ImageView image = (ImageView) findViewById(R.id.iv_image_insert);  //이미지를 띄울 위젯 ID값
                    image.setImageBitmap(image_bitmap_copy);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }//end of onActivityResult()

    public String getImagePathToUri(Uri data) {
        //사용자가 선택한 이미지의 정보를 받아옴
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        //이미지의 경로 값
        String imgPath = cursor.getString(column_index);
        Log.d("test", imgPath);//이미지 경로 확인해서 데이터 값 넘기기

        //이미지의 이름 값
        String imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1);
        Toast.makeText(InsertActivity.this, "이미지 이름 : " + imgName, Toast.LENGTH_SHORT).show();
        this.imageName = imgName;
//        this.imagePath = imgPath;

        return imgPath;
    }//end of getImagePathToUri()

    //파일 변환
    private void doMultiPartRequest() {

        File f = new File(img_path);

        DoActualRequest(f);
    }

    //서버 보내기
    private void DoActualRequest(File file) {
        OkHttpClient client = new OkHttpClient();
        String url = "http://"+urlIp+":8080/test/multipartRequest.jsp";

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", file.getName(),
                        RequestBody.create(MediaType.parse("image/jpeg"), file))

                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try {
            Response response = client.newCall(request).execute();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }









    //----------------------이미지 관련 메소드----------------------------------------------
    //
    //고종찬 = 바지사장
    //
    //---------------------------------------------------------------------------------
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
}//-------------------------------