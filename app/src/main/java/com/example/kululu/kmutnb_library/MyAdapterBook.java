package com.example.kululu.kmutnb_library;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class MyAdapterBook extends BaseAdapter {
    private static final String TAG = "LogServer";
    private List<Data> mDatas;
    private LayoutInflater mLayoutInflater;
    Context mContext;

    private String iconBaseUrl = "http://10.40.23.5/Android/Book_Pic/" ;

    public MyAdapterBook(Context context, List<Data> aList){
        mDatas = aList;
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }
    static class ViewHolder{
        TextView tvId;
        TextView tvTitle;
        TextView tvDesc;
        TextView tvType;
        TextView tvWritten;
    }
    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null){
            view = mLayoutInflater.inflate(R.layout.layout_book,viewGroup,false);
            holder = new ViewHolder();
            holder.tvTitle = (TextView)view.findViewById(R.id.title);
            holder.tvDesc = (TextView)view.findViewById(R.id.desc);
            holder.tvId = (TextView)view.findViewById(R.id.id);
            holder.tvType = (TextView)view.findViewById(R.id.type);
            holder.tvWritten = (TextView)view.findViewById(R.id.written);
            view.setTag(holder);
        }else {
            holder = (ViewHolder)view.getTag();
        }
        String title = mDatas.get(position).getBook_name();
        holder.tvTitle.setText(title);
        holder.tvDesc.setText(mDatas.get(position).getBook_desc());
        holder.tvType.setText(mDatas.get(position).getBook_type());
        holder.tvWritten.setText(mDatas.get(position).getBook_written());
        holder.tvId.setText(String.valueOf(mDatas.get(position).getId()));
        return view;
    }


//        String imgUrl = iconBaseUrl + mDatas.get(position).getmIcon();
//
//        Picasso.with(mContext)
//                .load(imgUrl)
//                .placeholder(R.mipmap.ic_launcher)
//                .fit()
//                .error(R.mipmap.ic_launcher)
//                .into(holder.img);
//

}
