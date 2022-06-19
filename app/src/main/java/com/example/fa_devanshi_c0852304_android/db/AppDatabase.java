package com.example.fa_devanshi_c0852304_android.db;


import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.fa_devanshi_c0852304_android.date_converter.DateTypeConverter;
import com.example.fa_devanshi_c0852304_android.db.dao.AddExpenseDao;
import com.example.fa_devanshi_c0852304_android.entities.AddExpense;

@Database(entities = {AddExpense.class}, version = 1,exportSchema = false)
@TypeConverters({DateTypeConverter.class})

public abstract class AppDatabase extends RoomDatabase {

    public abstract AddExpenseDao addExpenseDao();
}
