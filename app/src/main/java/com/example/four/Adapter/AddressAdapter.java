package com.example.four.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.four.Activity.ListviewActivity;
import com.example.four.Bean.AddressDto;
import com.example.four.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;




public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.MyViewHolder> {


    final static String TAG = "어드레스어뎁터";


    Context mContext = null;
    int layout = 0;
    LayoutInflater inflater = null;
    private ArrayList<AddressDto> mDataset;
    String urlAddr = "http://172.30.1.27:8080/pictures/";
    ///////////////////////////////////////////////////////////////////////////////////////
    // Date : 2020.12.29
    //
    // Description:
    // -urlAddr은 사진 불러올라고 어쩔 수 없이 넣는 값이므로 이해 부탁
    //
    ///////////////////////////////////////////////////////////////////////////////////////

    int pos=0;

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
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listlayout, parent, false);

        MyViewHolder vh = new MyViewHolder(v);


        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.d(TAG,mDataset.get(position).getAddrImagePath());


        holder.addrTag.setText(mDataset.get(position).getAddrTag()); //position = 인덱스값
        holder.addrName.setText(mDataset.get(position).getAddrName()); //position = 인덱스값
        holder.addrAddr.setText(mDataset.get(position).getAddrAddr()); //position = 인덱스값
        holder.addrTel.setText(mDataset.get(position).getAddrTel()); //position = 인덱스값
//        holder.addrProfile.setImageURI(Uri.parse(urlAddr+mDataset.get(position).getAddrImagePath()));

        Log.d(TAG,mDataset.get(position).getAddrImagePath());


            Glide.with(holder.addrImagePath).load(urlAddr+mDataset.get(position).getAddrImagePath()).placeholder(R.drawable.shape_circle).override(120,120).apply(new RequestOptions().circleCrop()).into(holder.addrImagePath);

        ///////////////////////////////////////////////////////////////////////////////////////
        // Date : 2020.12.29
        //
        // Description:
        // -urlAddr은 사진 불러올라고 어쩔 수 없이 넣는 값이므로 이해 부탁 Picasso 사용
        //
        ///////////////////////////////////////////////////////////////////////////////////////


        Log.d(TAG,urlAddr+mDataset.get(position).getAddrImagePath());


        
        if(mDataset.get(position).getAddrTag().equals("병원")){
            holder.addrTagImg.setImageResource(R.drawable.tag_hospital);
        } else if(mDataset.get(position).getAddrTag().equals("유치원")){
            holder.addrTagImg.setImageResource(R.drawable.tag_kindergaden);
        } else if(mDataset.get(position).getAddrTag().equals("키즈카페")){
            holder.addrTagImg.setImageResource(R.drawable.tag_cafe);
        } else if(mDataset.get(position).getAddrTag().equals("기타")){
            holder.addrTagImg.setImageResource(R.drawable.tag_user);
        }else{
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
        public ImageView addrImagePath;
        public ImageView addrTagImg;

        //추가
        public TextView addrLike;



        MyViewHolder(View v) {

            super(v);
            addrAddr = v.findViewById(R.id.tv_address_listlayout);
            addrTag = v.findViewById(R.id.tv_tag_listlayout);
            addrName = v.findViewById(R.id.tv_name_listlayout);
            addrTel = v.findViewById(R.id.tv_tel_listlayout);
            addrImagePath = v.findViewById(R.id.iv_profile_listlayout);
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



}//-------------------------------

