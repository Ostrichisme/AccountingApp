package com.matianyi.accountingapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.matianyi.accountingapp.R;
import com.matianyi.accountingapp.bean.RecordBean;
import com.matianyi.accountingapp.util.DateUtil;
import com.matianyi.accountingapp.util.GlobalUtil;

import java.util.LinkedList;

public class ListViewAdapter extends BaseAdapter {

    private LinkedList<RecordBean> records = new LinkedList<>();

    private LayoutInflater mInflater;

    private final String TAG = "ListViewAdapter";

    public ListViewAdapter(Context context){
        Log.d(TAG, "ListViewAdapter: mContext " + context);
        mInflater = LayoutInflater.from(context);
    }

    public void setData(LinkedList<RecordBean> records){
        this.records = records;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public Object getItem(int position) {
        return records.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ListViewHolder holder;


        convertView = mInflater.inflate(R.layout.cell_list_view, null);

        RecordBean recordBean = (RecordBean) getItem(position);
        holder = new ListViewHolder(convertView, recordBean);

        convertView.setTag(holder);

        Log.d(TAG, "getView: converView position is: " + position);


        return convertView;
    }
}

class ListViewHolder {

    ListViewHolder(View itemView, RecordBean record){
        TextView remarkTV = itemView.findViewById(R.id.textView_remark);
        TextView amountTV = itemView.findViewById(R.id.textView_amount);
        TextView timeTV = itemView.findViewById(R.id.textView_time);
        ImageView categoryIcon = itemView.findViewById(R.id.imageView_category);

        remarkTV.setText(record.getRemark());

        if (record.getType() == 1){
            amountTV.setText(String.format("- %s", record.getAmount()));
        }else{
            amountTV.setText(String.format("+ %s", record.getAmount()));
        }

        timeTV.setText(DateUtil.getFormattedTime(record.getTimeStamp()));

        categoryIcon.setImageResource(GlobalUtil.getInstance().getResourceIcon(record.getCategory()));
    }

}


