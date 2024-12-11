package com.example.budgetwisesolutions.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.budgetwisesolutions.model.Budget;

import java.util.ArrayList;
import java.util.List;

public class BudgetDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "BudgetManager.db";
    private static final int DATABASE_VERSION = 1;

    // Table name and columns
    private static final String TABLE_BUDGET = "budget";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_SOURCE = "source";
    private static final String COLUMN_START_DATE = "start_date";
    private static final String COLUMN_END_DATE = "end_date";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_NOTE = "note";
    private static final String COLUMN_AMOUNT = "amount";

    public BudgetDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_BUDGET + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SOURCE + " TEXT, " +
                COLUMN_START_DATE + " TEXT, " +
                COLUMN_END_DATE + " TEXT, " +
                COLUMN_CATEGORY + " TEXT, " +
                COLUMN_NOTE + " TEXT, " +
                COLUMN_AMOUNT + " REAL)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUDGET);
        onCreate(db);
    }

    // Add Budget
    public long addBudget(Budget budget) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SOURCE, budget.getSource());
        values.put(COLUMN_START_DATE, budget.getStartDate());
        values.put(COLUMN_END_DATE, budget.getEndDate());
        values.put(COLUMN_CATEGORY, budget.getCategory());  // Add category field
        values.put(COLUMN_NOTE, budget.getNote());
        values.put(COLUMN_AMOUNT, budget.getAmount());
        long id = db.insert(TABLE_BUDGET, null, values);
        db.close();
        return id;
    }


    // Get All Budgets
    public List<Budget> getAllBudgets() {
        List<Budget> budgets = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BUDGET, null, null, null, null, null, COLUMN_ID + " DESC");
        if (cursor.moveToFirst()) {
            do {
                Budget budget = new Budget(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SOURCE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_START_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_END_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT))
                );
                budgets.add(budget);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return budgets;
    }

    // Update Budget
    public int updateBudget(Budget budget) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SOURCE, budget.getSource());
        values.put(COLUMN_START_DATE, budget.getStartDate());
        values.put(COLUMN_END_DATE, budget.getEndDate());
        values.put(COLUMN_CATEGORY, budget.getCategory());
        values.put(COLUMN_NOTE, budget.getNote());
        values.put(COLUMN_AMOUNT, budget.getAmount());
        int rows = db.update(TABLE_BUDGET, values, COLUMN_ID + "=?", new String[]{String.valueOf(budget.getId())});
        db.close();
        return rows;
    }

    // Delete Budget
    public int deleteBudget(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_BUDGET, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rows;
    }
}
