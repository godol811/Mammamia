package com.example.four.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.four.Bean.AddressDto;
import com.example.four.ItemHelper.CustomDialog;
import com.example.four.ItemHelper.ItemTouchHelperListener;
import com.example.four.ItemHelper.OnDialogListener;
import com.example.four.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.MyViewHolder>
    //하진추가
        implements ItemTouchHelperListener, OnDialogListener {
    ////////////////////////

    final static String TAG = "어드레스어뎁터";


    Context mContext = null;
    int layout = 0;
    LayoutInflater inflater = null;
    private ArrayList<AddressDto> mDataset;
    String urlAddr = null;
    int pos = 0;

    String cal = null;

    public AddressAdapter(Context mContext, int layout, ArrayList<AddressDto> data) {
        this.mContext = mContext;
        this.layout = layout;
        this.mDataset = data;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //LayoutInflater를 이용해서 원하는 레이아웃을 띄워줌
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listlayout, parent, false);

        MyViewHolder vh = new MyViewHolder(v);


        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Log.d(TAG, mDataset.get(position).getAddrImagePath());

        //ItemViewHolder가 생성되고 넣어야할 코드들을 넣어준다.
        holder.addrTag.setText(mDataset.get(position).getAddrTag()); //position = 인덱스값
        holder.addrName.setText(mDataset.get(position).getAddrName()); //position = 인덱스값
        holder.addrAddr.setText(mDataset.get(position).getAddrAddr()); //position = 인덱스값
        holder.addrTel.setText(mDataset.get(position).getAddrTel()); //position = 인덱스값
        holder.addrProfile.setImageBitmap(BitmapFactory.decodeFile(mDataset.get(position).getAddrImagePath()));//사진


        if (mDataset.get(position).getAddrTag().equals("병원")) {
            holder.addrTagImg.setImageResource(R.drawable.tag_hospital);
        } else if (mDataset.get(position).getAddrTag().equals("유치원")) {
            holder.addrTagImg.setImageResource(R.drawable.tag_kindergaden);
        } else if (mDataset.get(position).getAddrTag().equals("키즈카페")) {
            holder.addrTagImg.setImageResource(R.drawable.tag_cafe);
        } else if (mDataset.get(position).getAddrTag().equals("기타")) {
            holder.addrTagImg.setImageResource(R.drawable.tag_user);
        } else {
            holder.addrTagImg.setImageResource(R.drawable.tag_user);
        }


    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    //인터페이스 선언
    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    private OnItemClickListener mListener = null;

    //메인에서 사용할 클릭메서드 선언
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View v, int position);
    }

    private OnItemLongClickListener mLongListener = null;

    public void setOnItemLongClickListener(OnItemLongClickListener longListener) {
        this.mLongListener = longListener;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView addrTag;
        public TextView addrName;
        public TextView addrTel;
        public TextView addrAddr;
        public ImageView addrProfile;
        public ImageView addrTagImg;

        //추가
        public TextView addrLike;


        MyViewHolder(View v) {

            super(v);
            addrAddr = v.findViewById(R.id.tv_address_listlayout);
            addrTag = v.findViewById(R.id.tv_tag_listlayout);
            addrName = v.findViewById(R.id.tv_name_listlayout);
            addrTel = v.findViewById(R.id.tv_tel_listlayout);
            addrProfile = v.findViewById(R.id.iv_profile_listlayout);
            addrTagImg = v.findViewById(R.id.iv_tag_listlayout);


            // 뷰홀더에서만 리스트 포지션값을 불러올 수 있음.


            //-----------------Click Event---------------------
            //-----------------Click Event---------------------
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();//어뎁터 포지션값
                    // 뷰홀더에서 사라지면 NO_POSITION 을 리턴
                    if (position != RecyclerView.NO_POSITION) {
                        if (mListener != null) {
                            mListener.onItemClick(view, position);
                        }
                    }
                }


            });


            //-----------------Click Event---------------------
            //-----------------Click Event---------------------


//            //-----------------LongClick Event---------------------
//            //-----------------LongClick Event---------------------
//            v.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View view) {
//                    int position = getAdapterPosition();//어뎁터 포지션값
//                    // 뷰홀더에서 사라지면 NO_POSITION 을 리턴
//                    if (position != RecyclerView.NO_POSITION) {
//                        if (mLongListener != null) {
//                            mLongListener.onItemLongClick(view, position);
//                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+cal));
//                            mContext.startActivity(intent);
//
//
//                        }
//                    }
//
//
//                    return true;
//                }
//            });
//
//            //-----------------LongClick Event---------------------
//            //-----------------LongClick Event---------------------


        }
    }
    @Override
    public boolean onItemMove(int from_position, int to_position) {
        //이동할 객체 저장
        AddressDto addressDto = mDataset.get(from_position);
        //이동할 객체 삭제
        mDataset.remove(from_position);
        //이동하고 싶은 position에 추가
        mDataset.add(to_position, addressDto);
        //Adapter에 데이터 이동알림
        notifyItemMoved(from_position, to_position);
        return true;
    }

    @Override
    public void onItemSwipe(int position) {
        mDataset.remove(position);
        notifyItemRemoved(position);
    }
    //왼쪽 버튼 누르면 수정할 다이얼로그 띄우기

    @Override
    public void onLeftClick(int position, RecyclerView.ViewHolder viewHolder) {
        //수정 버튼 클릭시 다이얼로그 생성
        CustomDialog dialog = new CustomDialog(mContext, position, mDataset.get(position));
        //화면 사이즈 구하기
        DisplayMetrics dm = mContext.getApplicationContext().getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        //다이얼로그 사이즈 세팅
        WindowManager.LayoutParams wm = dialog.getWindow().getAttributes();
        wm.copyFrom(dialog.getWindow().getAttributes());
        wm.width = (int) (width * 0.7);
        wm.height = height / 2;
        //다이얼로그 Listener 세팅
        dialog.setDialogListener(this);
        //다이얼로그 띄우기
        dialog.show();
    }

    //오른쪽 버튼 누르면 아이템 삭제
    @Override
    public void onRightClick(int position, RecyclerView.ViewHolder viewHolder) {
        //수정 버튼 클릭시 다이얼로그 생성
        CustomDialog dialog = new CustomDialog(mContext, position, mDataset.get(position));
        //화면 사이즈 구하기
        DisplayMetrics dm = mContext.getApplicationContext().getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        //다이얼로그 사이즈 세팅
        WindowManager.LayoutParams wm = dialog.getWindow().getAttributes();
        wm.copyFrom(dialog.getWindow().getAttributes());
        wm.width = (int) (width * 0.7);
        wm.height = height / 2;
        //다이얼로그 Listener 세팅
        dialog.setDialogListener(this);
        //다이얼로그 띄우기
        dialog.show();
    }



    @Override
    public void onFinish(int position, AddressDto addressDto) {
        mDataset.set(position, addressDto);
        notifyItemChanged(position);
    }
//    public ItemViewHolder(View itemView) {
//        super(itemView);
//        list_name = itemView.findViewById(R.id.list_name);
//        list_age = itemView.findViewById(R.id.list_age);
//        list_image = itemView.findViewById(R.id.list_image);
//    }
//
//    public void onBind(AddressDto addressDto) {
//        list_name.setText(addressDto.getName());
//        list_age.setText(String.valueOf(addressDto.getAge()));
//        list_image.setImageResource(addressDto.getImage());
//    }
}//-------------------------------
