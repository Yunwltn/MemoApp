package com.yunwltn98.memoapp2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.yunwltn98.memoapp2.adapter.MemoAdapter;
import com.yunwltn98.memoapp2.data.DatabaseHandler;
import com.yunwltn98.memoapp2.model.Memo;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText editSearch;
    ImageView imgDelete;
    ImageView imgSearch;
    Button btnAdd;
    RecyclerView recyclerView;
    MemoAdapter adapter;
    ArrayList<Memo> MemoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editSearch = findViewById(R.id.editSearch);
        imgSearch = findViewById(R.id.imgSearch);
        imgDelete = findViewById(R.id.imgDelete);
        btnAdd = findViewById(R.id.btnAdd);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keyword = editSearch.getText().toString().trim();

                // 빈 검색어는 검색이 안되게 리턴
                if (keyword.isEmpty()) {
                    editSearch.setText("");
                    Toast.makeText(MainActivity.this,"내용을 입력해주세요", Toast.LENGTH_LONG).show();
                    return;
                }

                // db의 content 부분에 search에 입력한 검색어가 포함된 내용만 가져오기
                DatabaseHandler db = new DatabaseHandler(MainActivity.this);
                MemoList = db.getSearchMemos(keyword);
                db.close();

                adapter = new MemoAdapter(MainActivity.this, MemoList);
                recyclerView.setAdapter(adapter);
            }
        });

        // 검색 버튼을 없애고 검색어 입력시 검색어 포함된 내용 가져오기
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // 검색어 가져오기
                String keyword = editSearch.getText().toString().trim();
                // 2글자 이상 입력했을때만 검색이 되도록한다
                if (keyword.length() < 2 && keyword.length() > 0) {
                    return;
                }
                // db의 content 부분에 search에 입력한 검색어가 포함된 내용만 가져오기
                DatabaseHandler db = new DatabaseHandler(MainActivity.this);
                MemoList = db.getSearchMemos(keyword);
                db.close();

                adapter = new MemoAdapter(MainActivity.this, MemoList);
                recyclerView.setAdapter(adapter);

            }
        });


        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHandler db = new DatabaseHandler(MainActivity.this);
                MemoList = db.getAllMemos();
                db.close();
                adapter = new MemoAdapter(MainActivity.this, MemoList);
                recyclerView.setAdapter(adapter);

                editSearch.setText("");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // DB에서 데이터를 가져와서 리사이클러뷰에 표시하기
        DatabaseHandler db = new DatabaseHandler(MainActivity.this);
        MemoList = db.getAllMemos();
        db.close();

        // 어댑터 만들어 리사이클러뷰에 셋팅
        adapter = new MemoAdapter(MainActivity.this, MemoList);
        recyclerView.setAdapter(adapter);
    }
}