package com.tranthikieutrinh.bookdb;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.BitmapFactory;

import androidx.annotation.Nullable;



public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "book.sqlite";
    public static final int DB_VERSION = 1;
    public static final String TBL_NAME = "Books";
    public static final String COL_ID = "BookId";
    public static final String COL_NAME = "BookName";
    public static final String COL_PUBLISHER = "Publisher";
    public static final String COL_EDITION = "Edition";
    public static final String COL_PRICE = "Price";
    public static final String COL_THUMB = "Thumb";

    public DataBaseHelper(@Nullable Context context) {
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //CREATE TABLE IF NOT EXISTS TBL_NAME (COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, COL_NAME VARCHAR(50),
        //COL_PUBLISHER VARCHAR(30), COL_EDITION INTEGER, COL_PRICE REAL, COL_THUMB BLOB) INTEGER PRIMARY KEY AUTOINCREMENT,

        String sql = "CREATE TABLE IF NOT EXISTS " + TBL_NAME + " ( " + COL_ID + " INTEGER PRIMARY KEY , "
                + COL_NAME + " VARCHAR(50), "
                + COL_EDITION + " INTEGER, "
                + COL_PUBLISHER + " VARCHAR(30), "
                + COL_PRICE + " REAL, "
                + COL_THUMB + " BLOB)";
        sqLiteDatabase.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TBL_NAME);
        onCreate(sqLiteDatabase);

    }
    public Cursor getData(String sql){
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(sql,null);

    }
    //INSERT, UPDATE, DELETE
    public void execSql(String sql){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
    }
    public void deleteRow(int id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TBL_NAME,COL_ID + " = ?",new String[]{String.valueOf(id)});
    }
    public void updateRow(int id, String name,int edition, String publisher,  double price, byte[] thumb){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "UPDATE " + TBL_NAME + " SET " + COL_NAME + " = ?, "+ COL_EDITION + " = ?, " + COL_PUBLISHER + " = ?, "  + COL_PRICE + " = ?, " + COL_THUMB + " = ? WHERE " + COL_ID + " = ?";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1,name);
        statement.bindLong(2,edition);
        statement.bindString(3,publisher);
        statement.bindDouble(4,price);
        statement.bindBlob(5,thumb);
        statement.bindLong(6,id);
        statement.execute();
        db.close();
    }
    public int getNumOfRows(){
        Cursor cursor = getData("SELECT * FROM "+ TBL_NAME);
        int numOfRows = cursor.getCount();
        cursor.close();
        return numOfRows;
    }
    public void insertRow(int id, String name, int edition,String publisher,  double price, byte[] thumb){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "INSERT OR REPLACE INTO " + TBL_NAME + " VALUES(?,?,?,?,?,?)";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.clearBindings();
        statement.bindLong(1,id);
        statement.bindString(2,name);
        statement.bindLong(3,edition);
        statement.bindString(4,publisher);
        statement.bindDouble(5,price);
        statement.bindBlob(6,thumb);
        statement.executeInsert();

    }

}
