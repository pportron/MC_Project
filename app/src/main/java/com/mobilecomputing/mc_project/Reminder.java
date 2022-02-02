package com.mobilecomputing.mc_project;

import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class Reminder extends BaseAdapter {
    Context context;
    String Message[];
    String reminder_time[];
    String creation_time[];
    String creator_id[];
    int reminder_seen[];
    double location_x[];
    double location_y[];
    LayoutInflater inflter;


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String[] getMessage() {
        return Message;
    }

    public void setMessage(String[] message) {
        this.Message = message;
    }

    public String[] getReminder_time() {
        return reminder_time;
    }

    public void setReminder_time(String[] reminder_time) {
        this.reminder_time = reminder_time;
    }

    public String[] getCreation_time() {
        return creation_time;
    }

    public void setCreation_time(String[] creation_time) {
        this.creation_time = creation_time;
    }

    public String[] getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(String[] creator_id) {
        this.creator_id = creator_id;
    }

    public int[] getReminder_seen() {
        return reminder_seen;
    }

    public void setReminder_seen(int[] reminder_seen) {
        this.reminder_seen = reminder_seen;
    }

    public double[] getLocation_x() {
        return location_x;
    }

    public void setLocation_x(double[] location_x) {
        this.location_x = location_x;
    }

    public double[] getLocation_y() {
        return location_y;
    }

    public void setLocation_y(double[] location_y) {
        this.location_y = location_y;
    }

    public LayoutInflater getInflter() {
        return inflter;
    }

    public void setInflter(LayoutInflater inflter) {
        this.inflter = inflter;
    }

    public Reminder(Context context, String[] message, String[] reminder_time, String[] creation_time, String[] creator_id, int[] reminder_seen, double[] location_x, double[] location_y) {
        this.context = context;
        this.Message = message;
        this.reminder_time = reminder_time;
        this.creation_time = creation_time;
        this.creator_id = creator_id;
        this.reminder_seen = reminder_seen;
        this.location_x = location_x;
        this.location_y = location_y;
    }

    @Override
    public int getCount() {
        return Message.length;
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
        TextView Name = (TextView) view.findViewById(R.id.ReminderMessage);
        TextView Date = (TextView) view.findViewById(R.id.ReminderTime);
        Name.setText(Message[i]);
        Date.setText(reminder_time[i]);
        return view;
    }

}