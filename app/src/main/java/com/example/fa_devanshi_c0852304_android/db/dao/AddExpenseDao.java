package com.example.fa_devanshi_c0852304_android.db.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fa_devanshi_c0852304_android.entities.AddExpense;

import java.util.List;

@Dao
public interface AddExpenseDao {

    @Query("SELECT * FROM AddExpense")
    List<AddExpense> getAll();

    @Insert
    void insert(AddExpense addExpense);

    @Delete
    void delete(AddExpense addExpense);

    @Update
    void update(AddExpense addExpense);
}
