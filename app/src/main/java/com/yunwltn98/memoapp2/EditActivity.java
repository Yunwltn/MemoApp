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

public class EditActivity extends AppCompatActivity {

    TextView txtTitle;
    EditText editTitle;
    EditText editContent;
    Button btnSave;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        txtTitle = findViewById(R.id.txtTitle);
        editTitle = findViewById(R.id.editTitle);
        editContent = findViewById(R.id.editContent);
        btnSave = findViewById(R.id.btnSave);

        Memo memo = (Memo) getIntent().getSerializableExtra("memo");
        id = memo.id;
        editTitle.setText(memo.title);
        editContent.setText(memo.content);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editTitle.getText().toString().trim();
                String content = editContent.getText().toString().trim();

                if (title.isEmpty() || content.isEmpty()) {
                    Toast.makeText(EditActivity.this, "필수항목 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                // db에 저장한다
                DatabaseHandler db = new DatabaseHandler(EditActivity.this);

                // 넘겨줄정보 : 아이디, 제목, 내용
                Memo memo = new Memo(id, title, content);
                db.updateMemo(memo);

                // 액티비티 종료
                finish();

            }
        });
    }
}