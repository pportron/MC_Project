package com.mobilecomputing.mc_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {
    Context context;
    String RmdName[];
    String RmdDate[];
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, String[] RmdName, String[] RmdDate) {
        this.context = context;
        this.RmdName = RmdName;
        this.RmdDate = RmdDate;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return RmdName.length;
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.list_view, null);
        TextView Name = (TextView) view.findViewById(R.id.ReminderName);
        TextView Date = (TextView) view.findViewById(R.id.DueDate);
        Button BtnDelete = (Button) view.findViewById(R.id.DeleteBtn);
        Button BtnEdit = (Button) view.findViewById(R.id.EditBtn);
        Name.setText(RmdName[i]);
        Date.setText(RmdDate[i]);
        return view;
    }

}
