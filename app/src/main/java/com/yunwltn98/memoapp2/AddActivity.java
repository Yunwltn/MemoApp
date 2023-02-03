package com.yunwltn98.memoapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yunwltn98.memoapp2.data.DatabaseHandler;
import com.yunwltn98.memoapp2.model.Memo;

public class AddActivity extends AppCompatActivity {

    EditText editTitle;
    EditText editContent;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        editTitle = findViewById(R.id.editTitle);
        editContent = findViewById(R.id.editContent);
        btnSave = findViewById(R.id.btnSave);

        // 저장누르면 데이터베이스에 저장
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editTitle.getText().toString().trim();
                String content = editContent.getText().toString().trim();

                // 제목과 내용이 들어갔는지 확인
                if (title.isEmpty() || content.isEmpty() ) {
                    Toast.makeText(AddActivity.this, "내용을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 묶어서 처리할 memo 클래스를 만들고 객체를 하나 만든다
                Memo memo = new Memo(title, content);

                // DB에 저장한다
                DatabaseHandler db = new DatabaseHandler(AddActivity.this);
                db.addMemo(memo);

                // 유저한테 잘 저장되었다고 알려주고
                Toast.makeText(AddActivity.this,"저장되었습니다", Toast.LENGTH_SHORT).show();

                // 액티비티는 종료한다
                finish();
            }
        });
    }
}