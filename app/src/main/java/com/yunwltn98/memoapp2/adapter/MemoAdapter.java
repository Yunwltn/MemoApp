package com.yunwltn98.memoapp2.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.yunwltn98.memoapp2.EditActivity;
import com.yunwltn98.memoapp2.R;
import com.yunwltn98.memoapp2.data.DatabaseHandler;
import com.yunwltn98.memoapp2.model.Memo;

import java.util.ArrayList;
import java.util.List;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.ViewHolder>{

    Context context;
    List<Memo> memoList;
    int deleteIndex;

    public MemoAdapter(Context context, List<Memo> memoList) {
        this.context = context;
        this.memoList = memoList;
    }

    @NonNull
    @Override
    public MemoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // xml파일을 연결하는 작업(파일명(contact_row)과 클래스명(ContactAdapter)만 바꿔서 사용하면 된다)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.memo_row, parent, false);
        return new MemoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemoAdapter.ViewHolder holder, int position) {
        // 뷰에 데이터를 셋팅한다
        Memo memo = memoList.get(position);
        holder.txtTitle.setText(memo.title);
        holder.txtContent.setText(memo.content);
    }

    @Override
    public int getItemCount() {
        return memoList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtTitle;
        TextView txtContent;
        ImageView imgDelete;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtContent = itemView.findViewById(R.id.txtContent);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            cardView = itemView.findViewById(R.id.cardView);

            // 카드뷰를 클릭했을때의 이벤트
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 인텐트에 유저가 어떤 행을 눌렀는지 파악하여 정보 전달
                    int index = getAdapterPosition();
                    Memo memo = memoList.get(index);

                    // 수정 액티비티를 띄운다
                    Intent intent = new Intent(context, EditActivity.class);
                    intent.putExtra("memo", memo);
                    context.startActivity(intent);
                }
            });

            // 이미지 딜리트를 클릭했을때의 이벤트
            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 1. 어느 주소록을 삭제할 것인지 삭제할 주소록을 가져온다
                    deleteIndex = getAdapterPosition();

                    // 2. 알러트 다이얼로그가 나온다
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("메모 삭제");
                    builder.setMessage("정말 삭제하시겠습니까");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // 3. 알러트 다이얼로그에서 Yes 눌렀을때 데이터 베이스에서 삭제
                            DatabaseHandler db = new DatabaseHandler(context);
                            Memo memo = memoList.get(deleteIndex);
                            db.deleteMemo(memo);

                            // 즉 디비에 저장된 데이터 삭제했으니 메모리에 저장된 데이터도 삭제한다
                            memoList.remove(deleteIndex);
                            // 데이터가 변경되었으니 화면 갱신하라고 함수 호출
                            notifyDataSetChanged();
                        }
                    });
                    builder.setNegativeButton("NO", null);
                    builder.show();
                }
            });
        }
    }

}
