package com.example.four.ItemHelper;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.four.Bean.AddressDto;
import com.example.four.R;
//import com.example.swipe.OnDialogListener;
//import com.example.swipe.Person;
//import com.example.swipe.R;

public class CustomDialogLeft extends Dialog {
    private OnDialogListener listener;
    private Button mod_bt;

    String urlIp = null;
    String urlAddr = null;

    int addrNo;




    final String TAG = "커스텀다이얼로그왼쪽";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customdialog_left);



        //macIP = intent.getStringExtra("macIP");
        //입력하는 데이터를 위해 ? 추가
        addrNo= Variable.publicaddrno;
        urlAddr = "http://"+urlIp+":8080/test/mammamiaLikeupdate.jsp?";
        urlAddr = urlAddr + "addrNo=" + addrNo;
        connectLike();

    }

    public CustomDialogLeft(Context context, final int position, AddressDto addressDto) {
        super(context);




        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.customdialog_left);

        mod_bt = findViewById(R.id.mod_bt);
        mod_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }


    private void connectLike(){
        try {
            DialogNetworkTask networkTask = new DialogNetworkTask(CustomDialogLeft.this,urlAddr,"like");
            networkTask.execute().get();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void setDialogListener(OnDialogListener listener) {
        this.listener = listener;
    }
}



