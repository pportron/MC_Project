package com.mobilecomputing.mc_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Reminder> arraylist;
    private final ClickListener listener;


    public RecyclerViewAdapter(Context context, ArrayList<Reminder> arraylist,  ClickListener listener) {
        this.context = context;
        this.arraylist = arraylist;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_reminder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.message.setText(arraylist.get(position).getMessage());
        holder.date.setText(arraylist.get(position).getReminder_time());
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView message;
        private TextView date;
        private ImageButton btn;
        private WeakReference<ClickListener> listenerRef;

        public ViewHolder(@NonNull View itemview) {
            super(itemview);

            listenerRef = new WeakReference<>(listener);
            message = (TextView) itemView.findViewById(R.id.txtMessage);
            date = (TextView) itemView.findViewById(R.id.txtDate);
            btn = (ImageButton) itemView.findViewById(R.id.BtnDeleteRmd);
            btn.setOnClickListener(this);

        }
        @Override
        public void onClick(View v) {
            listenerRef.get().onPositionClicked(getAdapterPosition());

        }



    }
}
