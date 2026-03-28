package com.spacECE.spaceceedu;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class ActivityAdapter extends BaseAdapter {

    final String TAG = "ActivityAdapter";
    Context context;
    List<ActivityData> activityDataList;


    public ActivityAdapter(Context context,List<ActivityData> activityDataList){
        this.context = context;
        this.activityDataList = activityDataList;
    }

    @Override
    public int getCount() {
        return activityDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return activityDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.item_activity_card,parent,false);

        ActivityData currActivityData = (ActivityData) getItem(position);
        
        TextView textViewActivityId = convertView.findViewById(R.id.text_activity_id);
        TextView textViewActivityTitle = convertView.findViewById(R.id.text_activity_title);
        Button btnView = convertView.findViewById(R.id.btn_view);

        if (currActivityData.getData() != null && !currActivityData.getData().isEmpty()) {
            Data data = currActivityData.getData().get(0);
            if (textViewActivityId != null) {
                textViewActivityId.setText("Activity ID : " + data.getActivityNo());
            }
            if (textViewActivityTitle != null) {
                textViewActivityTitle.setText("Activity Name : " + data.getActivityName());
            }
        }

        if (btnView != null) {
            btnView.setOnClickListener(v -> {
                Intent intent = new Intent(context, ActivityDetailsActivity.class);
                intent.putExtra("EXTRA_ACTIVITY", currActivityData);
                context.startActivity(intent);
            });
        }

        return convertView;
    }
}
