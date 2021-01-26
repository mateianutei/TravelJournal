package com.example.anuteistravelingjournal.adapters;

import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anuteistravelingjournal.R;
import com.example.anuteistravelingjournal.entities.Memory;
import com.example.anuteistravelingjournal.listeners.MemoryListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MemoryAdapter extends RecyclerView.Adapter<MemoryAdapter.MemoryViewHolder> {
    private List<Memory> list;
    private MemoryListener memoryListener;
    private Timer timer;
    private List<Memory> memorySource;
    public MemoryAdapter(List<Memory> list, MemoryListener memoryListener) {
        this.list = list; this.memoryListener=memoryListener;
        memorySource=list;
    }


    //it takes the linear layout as an entity
    @NonNull
    @Override
    public MemoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MemoryViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.container_memory,parent,false)

        );
    }

    @Override
    public void onBindViewHolder(@NonNull MemoryViewHolder holder, int position) {
        holder.setMemory(list.get(position));
        holder.layoutMemory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memoryListener.onMemoryClicked(list.get(position),position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    static class MemoryViewHolder extends RecyclerView.ViewHolder{


        TextView memorytitle,memorysubtitle,DateTime;
        LinearLayout layoutMemory;
        ImageView imagememory;
        public MemoryViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutMemory=itemView.findViewById(R.id.layoutMemory);
            memorytitle=itemView.findViewById(R.id.memorytitle);
            memorysubtitle=itemView.findViewById(R.id.memorysubtitle);
            DateTime=itemView.findViewById(R.id.DateTime);
            imagememory=itemView.findViewById(R.id.memoryimagerecycler);

        }

        void setMemory(Memory m){
            memorytitle.setText(m.getTitle());
            memorysubtitle.setText(m.getSubtitle());
            DateTime.setText(m.getDateTime());
            if( m.getImagePath()==null) {
                imagememory.setVisibility(View.GONE);
            }
            else{
                imagememory.setImageBitmap(BitmapFactory.decodeFile(m.getImagePath()));
                imagememory.setVisibility(View.VISIBLE);

            }

        }
    }

    public void searchMemory(final String searchKeyword){
        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(searchKeyword.trim().isEmpty()){
                    list=memorySource;
                }
                else{
                    ArrayList<Memory> temp=new ArrayList<>();
                    for(Memory m :memorySource){
                        if(m.getTitle().toLowerCase().contains(searchKeyword.toLowerCase()))
                            temp.add(m);

                    }
                    list=temp;
                }
                new Handler(Looper.getMainLooper()).post(new Runnable(){
                    @Override
                            public void run(){
                        notifyDataSetChanged();
                    }
                });
            }
        },500);
    }

    public void cancelTimer(){
        if(timer!=null)
            timer.cancel();
    }
}
