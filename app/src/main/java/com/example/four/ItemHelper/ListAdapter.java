package com.example.four.ItemHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    final static String TAG = "여기보세요_ListAdapter";
    ArrayList<AddressDto> items = new ArrayList<>();
    Context context;


    int addrNo;

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
    }

    //아이템의 개수로 사이즈 측정
    @Override
    public int getItemCount() {
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
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.remove(position);
                notifyItemRemoved(position);
            }
        };


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

