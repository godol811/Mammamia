package com.example.four.ItemHelper;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.four.Bean.AddressDto;
import com.example.four.R;
//import com.example.swipe.OnDialogListener;
//import com.example.swipe.Person;
//import com.example.swipe.R;

public class CustomDialogRight extends Dialog {
    private OnDialogListener listener;
    private Context context;
    private Button mod_bt2,mod_bt2_cancle;


    String urlIp = null;
    String urlAddr = null;

    int addrNo;
    String addr;
    Button delebtn;

    final String TAG = "커스텀다이얼로그오른쪽";

    public CustomDialogRight(Context context, final int position, AddressDto addressDto) {
        super(context);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.customdialog_right);

        mod_bt2 = findViewById(R.id.mod_bt2);
        mod_bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urlAddr = "http://"+urlIp+":8080/test/mammamiaDelete.jsp?";
                urlAddr = urlAddr + "addrNo=" + addrNo;
                connectDeleteData();



            }
        });
        mod_bt2_cancle = findViewById(R.id.mod_bt2_cancle);
        mod_bt2_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();

            }
        });
    }



    private void connectDeleteData(){
        try {
           // NetworkTask deleteworkTask = new NetworkTask(CustomDialog2.this,urlAddr,"delete");
        // deleteworkTask.execute().get();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setDialogListener(OnDialogListener listener) {
        this.listener = listener;
    }
}



