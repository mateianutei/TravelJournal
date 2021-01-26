package com.example.anuteistravelingjournal.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.anuteistravelingjournal.entities.Memory;

@Database(entities= Memory.class,version = 1,exportSchema = false)
public abstract class MemoriesDatabase extends RoomDatabase
{
    private static MemoriesDatabase memoriesDatabase;
    public static    synchronized  MemoriesDatabase getDatase(Context context){
        if(memoriesDatabase==null){
            memoriesDatabase= Room.databaseBuilder(context,MemoriesDatabase.class,"memories_db").build();
        }
        return memoriesDatabase;
    }

    public abstract MemoryDao memoryDao();
}
