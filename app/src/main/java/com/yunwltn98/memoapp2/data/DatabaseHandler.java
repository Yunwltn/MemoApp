package com.yunwltn98.memoapp2.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.yunwltn98.memoapp2.AddActivity;
import com.yunwltn98.memoapp2.model.Memo;
import com.yunwltn98.memoapp2.util.Util;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler(@Nullable Context context) {
        super(context, Util.DB_NAME, null, Util.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // 테이블 생성
        String CREATE_MEMO_TABLE = "create table memo( id integer primary key, title text, content text )";
        // 쿼리문 실행
        sqLiteDatabase.execSQL(CREATE_MEMO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // 기존의 테이블을 삭제하고 새 테이블을 다시 만든다
        String DROP_TAVLE = "drop table memo";
        // 쿼리문 실행
//        sqLiteDatabase.execSQL(DROP_TAVLE);
        sqLiteDatabase.execSQL(DROP_TAVLE, new String[]{Util.DB_NAME});

        // 테이블 재생성
        onCreate(sqLiteDatabase);
    }

    // 앱 동작시키는데 필요한 CRUD 관련된 SQL문이 들어간 메소드를 만들기

    // 메모 저장
    public void addMemo(Memo memo) {
        // 1. 데이터베이스를 가져온다
        SQLiteDatabase db = this.getWritableDatabase();
        // 2. 저장가능한 형식으로 만들어준다
        ContentValues values = new ContentValues();
        values.put(Util.KEY_TITLE, memo.title);
        values.put(Util.KEY_CONTENT, memo.content);
        // 3. insert 한다
        db.insert(Util.TABLE_NAME, null, values);
        // 4. DB사용이 끝나면 닫아준다
        db.close();
    }
    public ArrayList<Memo> getAllMemos() {
        // 데이터베이스를 가져와 쿼리문 만든다
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from memo";
        Cursor cursor = db.rawQuery(query, null);

        // 여러 데이터를 저장할 어레이리스트를 만든다
        ArrayList<Memo> MemoArrayList = new ArrayList<>();

        // 커서에서 데이터를 뽑아낸다
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String content = cursor.getString(2);

                Memo memo = new Memo(id, title, content);
                MemoArrayList.add(0, memo);

            } while (cursor.moveToNext());
        }
        db.close();
        return MemoArrayList;
    }

    public void updateMemo(Memo memo) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "update memo " +
                "set title = ?, content = ?" +
                "where id = ?";
        db.execSQL(query, new String[] {memo.title, memo.content, memo.id+""});
        db.close();
    }

    public void deleteMemo(Memo memo) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "delete from memo " +
                "where id = ?";
        String[] args = new String[] {memo.id+""};
        db.execSQL(query, args);
        db.close();
    }
    public ArrayList<Memo> getSearchMemos(String keyword) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select * from memo " +
                "where content like '%"+keyword+"%' or title like '%"+keyword+"%'";
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<Memo> MemoArrayList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String content = cursor.getString(2);

                Memo memo = new Memo(id, title, content);
                MemoArrayList.add(0,memo);

            } while (cursor.moveToNext());
        }
        db.close();
        return MemoArrayList;
    }

}
