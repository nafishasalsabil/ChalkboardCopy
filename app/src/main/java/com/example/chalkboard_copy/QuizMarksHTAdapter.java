package com.example.chalkboard_copy;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

class QuizMarksHTAdapter extends RecyclerView.Adapter<QuizMarksHTAdapter.QuizMarksHTViewHolder> {

    Context context;
    List<QuizNameClass> quizitems;

     FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();
    DocumentReference documentReference;
    public static String sec = "",title = "";

    public String getSec() {
        return sec;
    }

    public void setSec(String sec) {
        this.sec = sec;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {

        void onClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public QuizMarksHTAdapter(Context context, List<QuizNameClass> quizitems) {
        this.quizitems = quizitems;
        this.context = context;


    }


    public static class QuizMarksHTViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView quiz_title;
        TextView quiz_marks;

        public QuizMarksHTViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            quiz_title = itemView.findViewById(R.id.quiztitle_ht);
            quiz_marks = itemView.findViewById(R.id.quiz_marks_total_ht);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    onItemClickListener.onClick(ClassViewHolder.this.getAdapterPosition());
                    Intent intent = new Intent(v.getContext(),QuizMarksRecordHT.class);
                    intent.putExtra("title",title);
                    intent.putExtra("section",sec);
                    intent.putExtra("quiz",quiz_title.getText());

                    // System.out.println(classname.getText());
                   v.getContext().startActivity(intent);

                }
            });
        }
    }

    @NonNull
    @Override
    public QuizMarksHTViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_item_ht, parent, false);
        return new QuizMarksHTViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizMarksHTViewHolder holder, int position) {
        holder.quiz_title.setText(quizitems.get(position).getQuiz());
        holder.quiz_marks.setText(Integer.toString(quizitems.get(position).getQuiz_total_marks()));


    }


    @Override
    public int getItemCount() {
        return quizitems.size();
    }
}
