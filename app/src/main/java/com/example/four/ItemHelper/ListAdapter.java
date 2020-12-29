package com.example.four.ItemHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.four.Activity.MainActivity;
import com.example.four.Bean.AddressDto;
import com.example.four.NetworkTask.NetworkTask;
import com.example.four.R;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ItemViewHolder>
        implements ItemTouchHelperListener {

    final static String TAG = "리스트어댑터";
    ArrayList<AddressDto> items = new ArrayList<>();
    Context context;

    public ListAdapter(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //LayoutInflater를 이용해서 원하는 레이아웃을 띄워줌
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.listlayout, parent, false);
        return new ItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        //ItemViewHolder가 생성되고 넣어야할 코드들을 넣어준다.
        holder.onBind(items.get(position));
        Log.v(TAG, "bind");
    }

    //아이템의 개수로 사이즈 측정
    @Override
    public int getItemCount() {

        Log.v(TAG, "ItemCount");
        return items.size();
    }



    @Override
    public boolean onItemMove(int from_position, int to_position) {
        //이동할 객체 저장
        AddressDto addr = items.get(from_position);
        //이동할 객체 삭제
        items.remove(from_position);
        //이동하고 싶은 position에 추가
        items.add(to_position, addr);
        //Adapter에 데이터 이동알림
        notifyItemMoved(from_position, to_position);
        return true;
    }

    @Override
    public void onItemSwipe(int position) {
     items.remove(position);
     notifyItemRemoved(position);
    }


    //왼쪽버튼 누르면 즐겨찾기
    @Override
    public void onLeftClick(int position, RecyclerView.ViewHolder viewHolder) {
        //수정 버튼 클릭시 다이얼로그 생성
        Log.v(TAG, "수정");
        CustomDialog dialog = new CustomDialog(context, position, items.get(position));
        //화면 사이즈 구하기
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        //다이얼로그 사이즈 세팅
        WindowManager.LayoutParams wm = dialog.getWindow().getAttributes();
        wm.copyFrom(dialog.getWindow().getAttributes());
        wm.width = (int) (width * 0.7);
        wm.height = height / 2;
        //다이얼로그 Listener 세팅
        //dialog.setDialogListener(this);
        //다이얼로그 띄우기
        dialog.show();
    }


    //오른쪽 버튼 누르면 아이템 삭제
    @Override
    public void onRightClick(int position, RecyclerView.ViewHolder viewHolder) {




    }

//    @Override
//    public void onFinish(int position, AddressDto addr) {
//        items.set(position, addr);
//        notifyItemChanged(position);
//    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView list_tag, list_name, list_tel;
        ImageView list_image;

        public ItemViewHolder(View itemView) {
            super(itemView);
            //list_image = itemView.findViewById(R.id.addrlist_tagimg);
            list_tag = itemView.findViewById(R.id.tv_tag_listlayout);
            list_name = itemView.findViewById(R.id.tv_name_listlayout);
            list_tel = itemView.findViewById(R.id.tv_tel_listlayout);
            list_image = itemView.findViewById(R.id.tv_address_listlayout);
        }

        public void onBind(AddressDto addr) {
//            list_image.setImageResource(addr.getAddrProfile());
            list_tag.setText(addr.getAddrTag());
            list_name.setText(addr.getAddrName());
            list_tel.setText(addr.getAddrTel());

        }
    }

    String macIP;

    String urlAddr = null;


    View.OnClickListener onClickListener2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };







}

