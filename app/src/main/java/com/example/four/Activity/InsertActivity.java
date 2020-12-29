package com.example.four.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
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

    //field

    EditText insertName, insertTag, insertTel, insertAddr, insertDetail;
    Button addrinsertBtn;
    Button insertBackBtn;
    Button tagSelectBtn;

    //Tag 추가-------------------------------------
    boolean[] tagSelect = {false,false,false,false};
    //---------------------------------------------
    private final int REQ_CODE_SELECT_IMAGE = 100;

    final static String TAG = "인설트액티비티";


    //-------------------------------------------------------
    //------------------onCreate start-----------------------
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
}





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

            //addrAddr추가
            //imgaepath 추가 - 종찬
            urlAddr = urlAddr + "addrTag=" + addrTag + "&addrName=" + addrName + "&addrTel=" + addrTel + "&addrAddr=" + addrAddr + "&addrDetail=" + addrDetail + "&addrImagePath=" + imageName;
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


    //----------------------이미지 관련 메소드----------------------------------------------
    //
    //고종찬 = 바지사장
    //Multipart 사진 서버에 입력하는 부분 성공
    //---------------------------------------------------------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

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

}//-------------------------------