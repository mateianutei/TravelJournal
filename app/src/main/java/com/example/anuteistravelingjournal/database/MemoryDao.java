package com.example.anuteistravelingjournal.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.anuteistravelingjournal.entities.Memory;

import java.util.List;

@Dao
public interface MemoryDao {
    @Query("SELECT * FROM memories ORDER BY id DESC")
    List<Memory> getAllMemories();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMemory(Memory m);

    @Delete
    void deleteMemory(Memory m);


}
