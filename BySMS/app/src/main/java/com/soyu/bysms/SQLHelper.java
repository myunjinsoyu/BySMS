package com.soyu.bysms;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by soyu on 2017. 7. 16..
 */

public class SQLHelper extends SQLiteOpenHelper {
    private Context context;
    public SQLHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

        this.context = context;
    }

    private void initDB(SQLiteDatabase sqLiteDatabase) {
        String createSql = "create table number ("
                + "idx integer primary key autoincrement, "
                + "recv text, "
                + "send text)";
        sqLiteDatabase.execSQL(createSql);
    }
    private void dropTable(SQLiteDatabase sqLiteDatabase) {
        String dropSql = "drop table if exists number";
        sqLiteDatabase.execSQL(dropSql);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        initDB(sqLiteDatabase);
        Toast.makeText(context, "DB is opened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        dropTable(sqLiteDatabase);
        initDB(sqLiteDatabase);
        Toast.makeText(context, "DB is upgrade", Toast.LENGTH_SHORT).show();
    }

    // 데이터 검색
    public Cursor selectData(SQLiteDatabase sqLiteDatabase) {
        String sql = "select * from number;";
        Cursor result = sqLiteDatabase.rawQuery(sql, null);

        return result;
    }
    // 데이터 검색
    public Cursor selectData(SQLiteDatabase sqLiteDatabase, String recv) {
        String sql = "select * from number where recv = '" + recv + "'; ";
        Cursor result = sqLiteDatabase.rawQuery(sql, null);

        return result;
    }

    // 데이터 추
    public void insertData(SQLiteDatabase sqLiteDatabase, String recv, String send) {
        String sql = "insert into number values(NULL, '"
                + recv + "', '" + send + "');";
        sqLiteDatabase.execSQL(sql);
    }

    // 데이터 갱신
    public void updateData(SQLiteDatabase sqLiteDatabase, String recv, String send, int idx) {
        String sql = "update number set recv = '" + recv + "', send = '" + send + "' where idx = " + idx
                + ";";
        sqLiteDatabase.execSQL(sql);
    }
}
