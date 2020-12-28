package com.example.four.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.four.Bean.AddressDto;
import com.example.four.R;
import java.util.ArrayList;




public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.MyViewHolder> {


    final static String TAG = "어드레스어뎁터";


    Context mContext = null;
    int layout = 0;
    LayoutInflater inflater = null;
    private ArrayList<AddressDto> mDataset;
    String urlAddr = null;
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

        holder.addrTag.setText(mDataset.get(position).getAddrTag()); //position = 인덱스값
        holder.addrName.setText(mDataset.get(position).getAddrName()); //position = 인덱스값
        holder.addrAddr.setText(mDataset.get(position).getAddrAddr()); //position = 인덱스값
        holder.addrTel.setText(mDataset.get(position).getAddrTel()); //position = 인덱스값




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
        //추가
        public TextView addrAddr;
        public ImageView addrTagImg;


        MyViewHolder(View v) {

            super(v);
            addrTag = v.findViewById(R.id.tv_tag_listlayout);
            addrName = v.findViewById(R.id.tv_name_listlayout);
            addrTel = v.findViewById(R.id.tv_tel_listlayout);


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

