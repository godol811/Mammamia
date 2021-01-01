package com.example.four.ItemHelper;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.four.Activity.MainActivity;
import com.example.four.Activity.UpdateActivity;
import com.example.four.Bean.AddressDto;
import com.example.four.NetworkTask.NetworkTask;
import com.example.four.R;

//import com.example.swipe.OnDialogListener;
//import com.example.swipe.Person;
//import com.example.swipe.R;
public class CustomDialogLeft extends Dialog {
    private OnDialogListener listener;
    private Button ok, cancle;
    String urlAddr = null;
    String urlIp = null;
    private TextView tv;
    final String TAG = "커스텀다이얼로그왼쪽";

    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//
//       // Intent intent = getIntent();
//
//        //macIP = intent.getStringExtra("macIP");
//        //입력하는 데이터를 위해 ? 추가
//        urlAddr = "http://" + macIP + ":8080/test/mammamialikeupdate.jsp?";
//
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv = findViewById(R.id.tv_likedial_customleft);
        if (Variable.whereaddrlike.equals("0")) {//좋아요
            tv.setText("즐겨찾기 목록에 추가합니다.");

            connectUpdateData();
        } else if (Variable.whereaddrlike.equals("1")) {//좋아요 취소
            tv.setText("즐겨찾기를 취소합니다.");

        }
    }

    public CustomDialogLeft(Context context, final int position, AddressDto addressDto) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.customdialog_left);

        /////////////////-자기 아이피 챙기기-////////////////////////
        urlIp = "192.168.1.5"; //하진                //
//        urlIp = "172.30.1.27";//혜정                  //
//        urlIp = "222.106.89.206";//이누               //
//      urlIp = "192.168.0.105";//보람                  //
//        urlIp = "192.168.35.147";//종찬 아이피            //
/////////////////////////////////////////////////////////



        ok = findViewById(R.id.btn_Ok_customleft);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//확인버튼
                if (Variable.whereaddrlike.equals("0")) {//좋아요

                    urlAddr = "http://" + urlIp + ":8080/test/mammamiaLikeupdate.jsp?";
                    urlAddr = urlAddr + "addrLike=" + 1 + "&addrNo=" + Variable.publicaddrno;

                    connectUpdateData();
                } else if (Variable.whereaddrlike.equals("1")) {//좋아요 취소
                    urlAddr = "http://" + urlIp + ":8080/test/mammamiaLikeupdate.jsp?";
                    urlAddr = urlAddr + "addrLike=" + 0 + "&addrNo=" + Variable.publicaddrno;

                    connectUpdateData();
                }
                Variable.whereaddrlike = null;
                Variable.publicaddrno = 0;
                Intent intent = new Intent(getContext(), MainActivity.class);
                getContext().startActivity(intent);
                dismiss();


            }
        });
        cancle = findViewById(R.id.btn_cancle_customleft);//취소버튼 선언
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//취소버튼
                //Intent intent =new Intent(getContext()getcon, MainActivity.class);
                dismiss();

            }
        });

    }

    private void connectUpdateData() {
        try {
            NetworkTask updateworkTask = new NetworkTask(getContext(), urlAddr, "update");
            updateworkTask.execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDialogListener(OnDialogListener listener) {
        this.listener = listener;
    }
}













