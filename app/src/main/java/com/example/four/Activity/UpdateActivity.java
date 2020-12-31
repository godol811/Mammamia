package com.example.four.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.four.NetworkTask.NetworkTask;
import com.example.four.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UpdateActivity extends Activity {


    //field
    String imagePath = null;
    String imageName = null;
    private String img_path = new String();
    private Bitmap image_bitmap_copy = null;
    private Bitmap image_bitmap = null;
    private final int REQ_CODE_SELECT_IMAGE = 100;
    final static String TAG = "업데이트액티비티";
    String tag1, name1, tel1, detail1, addr1;
    int num;
    EditText tag, name, tel, detail, addr;
    ImageView profileImage;
    String urlAddr = null;
    String urlIp = null;
    Button okbtn;
    Button backbtn;
    Button tagSelectBtn;

    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;//12월 30일 주소 api 변수 추가

    final int[] selectedIndex = {0}; //tag 추가


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());//쓰레드 사용시 문제 없게 하는 용도

        ActivityCompat.requestPermissions(UpdateActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                , MODE_PRIVATE); //사용자에게 사진 사용 권한 받기 (가장중요함)


        Intent intent = getIntent();

        urlIp = intent.getStringExtra("urlIp");

        tag1 = intent.getStringExtra("addrTag");
        name1 = intent.getStringExtra("addrName");
        tel1 = intent.getStringExtra("addrTel");
        addr1 = intent.getStringExtra("addrAddr");
        detail1 = intent.getStringExtra("addrDetail");

        imagePath = intent.getStringExtra("addrImagePath");

        num = intent.getIntExtra("addrNo", 0);


        urlAddr = "http://" + urlIp + ":8080/test/mammamiaUpdate.jsp?";


        profileImage = findViewById(R.id.iv_profile_update);
        tag = findViewById(R.id.et_tagname_update);
        name = findViewById(R.id.et_name_update);
        tel = findViewById(R.id.et_tel_update);
        addr = findViewById(R.id.et_addr_update);
        detail = findViewById(R.id.et_detail_update);
        okbtn = findViewById(R.id.btn_ok_update);
        backbtn = findViewById(R.id.btn_back_update);

        //tag btn 추가
        tagSelectBtn = findViewById(R.id.btn_tagselect_update);

        tag.setText(tag1);
        name.setText(name1);
        tel.setText(tel1);
        addr.setText(addr1);
        detail.setText(detail1);


        Glide.with(UpdateActivity.this).load("http://" + urlIp + ":8080/pictures/" + imagePath)
                .override(300, 300)
                .placeholder(R.drawable.noimg)
                .apply(new RequestOptions().circleCrop())
                .into(profileImage);
        Log.d(TAG, "http://" + urlIp + ":8080/pictures/" + imagePath);


        profileImage.setOnClickListener(new View.OnClickListener() {//이미지 올리기
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);

            }
        });


        okbtn.setOnClickListener(onClickListener); //수정완료 버튼 리스너
        backbtn.setOnClickListener(onClickListener1); //뒤로가기 버튼 리스너

        //update tag 버튼 추가
        tagSelectBtn.setOnClickListener(tagselectClick);
        //12월 29일 인우 추가
        //자동으로 "-" 생성해서 전화번호에 붙여주기------------------------
        tel.addTextChangedListener(new TextWatcher() {


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
                    tel.getText().delete(s.length() - 1, s.length());
                    Log.d("addTextChangedListener", "onTextChanged: Intput text is wrong (Type : Number)");
                    return;
                }

                afterLenght = s.length();


                // 타자를 입력 중이면
                if (beforeLenght < afterLenght) {
                    if (afterLenght == 4 && s.toString().indexOf("-") < 0) {
                        //subSequence로 지정된 문자열을 반환해서 "-"폰을 붙여주고 substring
                        tel.setText(s.toString().subSequence(0, 3) + "-" + s.toString().substring(3, s.length()));
                        Log.v(TAG, String.valueOf(s.toString().substring(3, s.length())));
                    } else if (afterLenght == 9) {
                        tel.setText(s.toString().subSequence(0, 8) + "-" + s.toString().substring(8, s.length()));
                        Log.v(TAG, String.valueOf(s.toString().substring(8, s.length())));
                    }
                }
                tel.setSelection(tel.length());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 생략
            }

        });

        //주소검색 API----------------------
        addr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UpdateActivity.this, AddressWebViewActivity.class);
                startActivityForResult(i, SEARCH_ADDRESS_ACTIVITY);
            }
        });


        name.setFilters(new InputFilter[]{new InputFilter() {//특수문자 제한
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                //한글 영어로 문자 제한
                Pattern ps = Pattern.compile("^[a-zA-Z-가-힣ㄱ-ㅎㅏ-ㅣ\\u318D\\u119E\\u11A2\\u2022\\u2025a\\u00B7\\uFE55]+$");
                //source.equals("")백스페이스 허용 처리
                if (source.equals("") || ps.matcher(source).matches()) {
                    return source;
                }
                new androidx.appcompat.app.AlertDialog.Builder(UpdateActivity.this)
                        .setTitle("알림")
                        .setMessage("한글, 영문만 입력 가능합니다.")
                        .setNegativeButton("확인", null)
                        .setCancelable(false)
                        .show();
                return "";
            }
            //글자수 제한
        }, new InputFilter.LengthFilter(5)});//특수문자 제한

    }//-----------------onCreate


    View.OnClickListener tagselectClick = new View.OnClickListener() {//태그 선택했을경우
        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(UpdateActivity.this)
                    .setTitle("태그를 선택하세요")
                    .setIcon(R.mipmap.ic_icon)
                    .setSingleChoiceItems(R.array.tag, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            selectedIndex[0] = which;
                        }
                    })
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String[] tag = getResources().getStringArray(R.array.tag);
                            TextView text = findViewById(R.id.et_tagname_update);
                            text.setText(tag[selectedIndex[0]]);
                        }
                    })
                    .setNegativeButton("취소", null)
                    .show();
        }
    };//태그 선택 끝


    //수정완료 버튼 리스너 클릭시 이벤트
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(UpdateActivity.this)
                    .setTitle("알림")
                    .setMessage("수정 하시겠습니까?")
                    .setCancelable(false)
                    .setIcon(R.mipmap.ic_icon)
                    .setPositiveButton("취소", null)
                    .setNegativeButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    doMultiPartRequest();//사진 넣는 okHttp3 메소드}}}
                                }
                            }).start();


                            String st_tag = tag.getText().toString();
                            String st_name = name.getText().toString();
                            String st_tel = tel.getText().toString();
                            String st_addr = addr.getText().toString();
                            String st_detail = detail.getText().toString();

                            Calendar calendar = Calendar.getInstance();
                            java.util.Date date = calendar.getTime();
                            String today = (new SimpleDateFormat("yyyyMMddHHmmss").format(date));

                            imageName = today + "_" + imageName;

                            if (img_path.trim().length() == 0) {//사진 둥록을 해서 들어오는 값이 없으면 기존의 데이터를 그냥 다시 씌우기
                                urlAddr = urlAddr + "addrNo=" + num + "&addrName=" + st_name + "&addrTel=" + st_tel + "&addrAddr=" + st_addr + "&addrDetail=" + st_detail + "&addrTag=" + st_tag + "&addrImagePath=" + imagePath;
                            } else {//사진 등록되면 걍 넣기
                                urlAddr = urlAddr + "addrNo=" + num + "&addrName=" + st_name + "&addrTel=" + st_tel + "&addrAddr=" + st_addr + "&addrDetail=" + st_detail + "&addrTag=" + st_tag + "&addrImagePath=" + imageName;
                            }


                            connectUpdateData();
                            Log.d(TAG, urlAddr);

                            Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
                            startActivity(intent);


                        }
                    })
                    .show();

        }
    };

    //뒤로가기 버튼 리스너
    View.OnClickListener onClickListener1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };


    private void connectUpdateData() {
        try {
            NetworkTask updateworkTask = new NetworkTask(UpdateActivity.this, urlAddr, "update");
            updateworkTask.execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//주석

        super.onActivityResult(requestCode, resultCode, data);//주소 검색 api 추가
        switch (requestCode) {
            case SEARCH_ADDRESS_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    String data1 = data.getExtras().getString("data");
                    if (data1 != null) {
                        addr.setText(data1);
                    }
                }
                break;
        }//주소 검색 api 추가


        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    img_path = getImagePathToUri(data.getData()); //이미지의 URI를 얻어 경로값으로 반환
                    image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    ImageView image = (ImageView) findViewById(R.id.iv_profile_update);  //이미지를 띄울 위젯 ID값
                    image.setImageBitmap(image_bitmap_copy);

                    Glide.with(UpdateActivity.this).load(img_path)
                            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .placeholder(R.drawable.noimg)
                            .apply(new RequestOptions().circleCrop())
                            .into(profileImage);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }//end of onActivityResult()

    public String getImagePathToUri(Uri data) {//사용자가 선택한 이미지의 정보를 받아옴

        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String imgPath = cursor.getString(column_index);//이미지의 경로 값
        Log.d("test", imgPath);//이미지 경로 확인해서 데이터 값 넘기기
        String imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1); //이미지의 이름 값
        this.imageName = imgName;

        return imgPath;
    }//end of getImagePathToUri()


    private void doMultiPartRequest() {//파일 변환

        File f = new File(img_path);

        DoActualRequest(f);
    }


    private void DoActualRequest(File file) {//서버 보내기
        OkHttpClient client = new OkHttpClient();
        String url = "http://" + urlIp + ":8080/test/multipartRequest.jsp";

        Calendar calendar = Calendar.getInstance();
        java.util.Date date = calendar.getTime();
        String today = (new SimpleDateFormat("yyyyMMddHHmmss").format(date));


        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", today + "_" + file.getName(), RequestBody.create(MediaType.parse("image/jpeg"), file))
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
    }//서버 보내기 끝

    //----------------------이미지 관련 메소드----------------------------------------------
    //
    //고종찬 = 바지사장
    //
    //---------------------------------------------------------------------------------


    public boolean dispatchTouchEvent(MotionEvent ev) {//배경 터치 시 키보드 사라지게
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
}//-----------------