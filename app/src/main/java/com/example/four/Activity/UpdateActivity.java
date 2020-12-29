package com.example.four.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.four.NetworkTask.NetworkTask;
import com.example.four.R;

public class UpdateActivity extends Activity {

    String imagePath;
    String imageName;
    private String img_path = new String();
    private Bitmap image_bitmap_copy = null;
    private Bitmap image_bitmap = null;
    private final int REQ_CODE_SELECT_IMAGE = 100;

    String tagName, addrName, addrTel, addrDetail;
    final static String TAG = "업데이트액티비티";
    //addr1 추가
    String tag1, name1, tel1, detail1, addr1;
    int num;
    EditText tag, name, tel, detail, addr;

    ImageView profileImage;

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


        tag.setText(tag1);
        name.setText(name1);
        tel.setText(tel1);
        addr.setText(addr1);
        detail.setText(detail1);

        profileImage.setImageBitmap(BitmapFactory.decodeFile(imagePath));//가져온 경로를 imageView에 올리기

        //--------------------이미지 올리기---------------------------
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
            }
        });


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


            urlAddr = urlAddr + "addrNo=" + num + "&addrName=" + st_name + "&addrTel=" + st_tel + "&addrAddr=" + st_addr + "&addrDetail=" + st_detail + "&addrTag=" + st_tag + "&addrImagePath=" + imagePath;
            connectUpdateData();

            Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
            Toast.makeText(UpdateActivity.this, "수정이완료돼싸", Toast.LENGTH_SHORT).show();
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
            NetworkTask updateworkTask = new NetworkTask(UpdateActivity.this, urlAddr, "update");
            updateworkTask.execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //----------------------이미지 관련 메소드----------------------------------------------
    //
    //고종찬 = 바지사장
    //
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
                    ImageView image = (ImageView) findViewById(R.id.iv_profile_update);  //이미지를 띄울 위젯 ID값
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
        Toast.makeText(UpdateActivity.this, "이미지 이름 : " + imgName, Toast.LENGTH_SHORT).show();
        this.imageName = imgName;
        this.imagePath = imgPath;

        return imgPath;
    }//end of getImagePathToUri()
    //----------------------이미지 관련 메소드----------------------------------------------
    //
    //고종찬 = 바지사장
    //
    //---------------------------------------------------------------------------------


}//-----------------