package com.example.anuteistravelingjournal.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.anuteistravelingjournal.R;
import com.example.anuteistravelingjournal.activities.CreateMemoryActivity;
import com.example.anuteistravelingjournal.adapters.MemoryAdapter;
import com.example.anuteistravelingjournal.database.MemoriesDatabase;
import com.example.anuteistravelingjournal.entities.Memory;
import com.example.anuteistravelingjournal.listeners.MemoryListener;

import java.util.ArrayList;
import java.util.List;

public class PrincipalInterfaceActivity extends AppCompatActivity implements MemoryListener {

    public static final int REQUEST_CODE_ADD_MEMORY=1;
    public static final int REQUEST_CODE_UPDATE_MEMORY=2;
    public static final int REQUEST_CODE_SHOW_MEMORY=3;
    private RecyclerView memoriesRecyclerView;
    private List<Memory> memoryList;
    private MemoryAdapter memoryAdapter;
    // to find out which item from the recycler view was selected
    private int memoryClickedPosition=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_interface);
        //add button + listener
        ImageView addMemory=findViewById(R.id.addmemory);
        addMemory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivityForResult(

                        // it starts an activity with the request code of add memory
                       new Intent(getApplicationContext(), CreateMemoryActivity.class),REQUEST_CODE_ADD_MEMORY
               );

            }
        });

        //setup for recycler view
        memoriesRecyclerView=findViewById(R.id.memoriesRecyclerView);
        memoriesRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        memoryList=new ArrayList<Memory>();
        memoryAdapter=new MemoryAdapter(memoryList,this);
        memoriesRecyclerView.setAdapter(memoryAdapter);
        // the show code is to put all the entities from the database in te recyclerview
        getmemories(REQUEST_CODE_SHOW_MEMORY);

        //edit text for the search input
        EditText inputSearchText=findViewById(R.id.inputSearchText);
        inputSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                memoryAdapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable s) {
            if(memoryList.size()!=0){
                memoryAdapter.searchMemory(s.toString());
            }
            }
        });
    }

    @Override
    public void onMemoryClicked(Memory m, int position) {
        memoryClickedPosition=position;
        Intent intent=new Intent (getApplicationContext(),CreateMemoryActivity.class);
        intent.putExtra("isViewOrUpdate",true);
        intent.putExtra("memory",m);
        startActivityForResult(intent,REQUEST_CODE_UPDATE_MEMORY);
    }

    private void getmemories(final int requestCode) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Memory>   list = MemoriesDatabase.getDatase(getApplicationContext()).memoryDao().getAllMemories();
                if(requestCode==REQUEST_CODE_SHOW_MEMORY)
                {
                    for(Memory m:list)
                        memoryList.add(m);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            memoryAdapter.notifyDataSetChanged();

                        }
                    });

                }
                else if(requestCode==REQUEST_CODE_ADD_MEMORY)
                {

                    memoryList.add(0,list.get(0));
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            memoryAdapter.notifyItemInserted(0);

                        }
                    });
                    memoriesRecyclerView.smoothScrollToPosition(0);

                }
                else if(requestCode==REQUEST_CODE_UPDATE_MEMORY){
                    memoryList.remove(memoryClickedPosition);
                    memoryList.add(memoryClickedPosition,list.get(memoryClickedPosition));
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            memoryAdapter.notifyItemChanged(memoryClickedPosition);

                        }
                    });
                }


            }
        }).start();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if the request code is add it calls the getmemories method to add a memory
        if(requestCode==REQUEST_CODE_ADD_MEMORY  && resultCode==RESULT_OK)
            getmemories(REQUEST_CODE_ADD_MEMORY);
        //if the request code is update it calls the getmemories method to update a memory
        else if(requestCode==REQUEST_CODE_UPDATE_MEMORY && resultCode==RESULT_OK)
        {  if(data!=null) getmemories(REQUEST_CODE_UPDATE_MEMORY);}

    }
}
