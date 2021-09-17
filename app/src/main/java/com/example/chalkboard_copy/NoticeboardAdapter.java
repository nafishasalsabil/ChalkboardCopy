package com.example.chalkboard_copy;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class NoticeboardAdapter extends RecyclerView.Adapter<NoticeboardAdapter.NoticeboardViewHolder> {

    Context context;
    List<NoticeClass> quizitems;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();
    DocumentReference documentReference;
    public static String sec = "",title = "";
    int count = 0;

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

    public NoticeboardAdapter(Context context, List<NoticeClass> quizitems) {
        this.quizitems = quizitems;
        this.context = context;


    }


    public static class NoticeboardViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView title;
        TextView description;
        TextView time,likes,name;
        ImageView profilepic;
        Button like;
        Button contact;


        public NoticeboardViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            title = itemView.findViewById(R.id.postTitleShowing);
            description = itemView.findViewById(R.id.postDescriptionShowing);
            time = itemView.findViewById(R.id.postTimeNotice);
            likes = itemView.findViewById(R.id.postLikes);
            name = itemView.findViewById(R.id.userNameNotice);
            profilepic = itemView.findViewById(R.id.userPictureNotice);
            like = itemView.findViewById(R.id.likeBtn);
            contact = itemView.findViewById(R.id.msgBtn_ht);


        }
    }

    @NonNull
    @Override
    public NoticeboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.noticeboard_item, parent, false);
        return new NoticeboardViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeboardViewHolder holder, int position) {
       /* holder.quiz_title.setText(quizitems.get(position).getQuiz());
        holder.quiz_marks.setText(Integer.toString(quizitems.get(position).getQuiz_total_marks()));
*/
       holder.title.setText(quizitems.get(position).getTitle());
       holder.description.setText(quizitems.get(position).getDescription());
       holder.name.setText(quizitems.get(position).getName());
       holder.time.setText(quizitems.get(position).getPosted_at());
       holder.likes.setText(Integer.toString(quizitems.get(position).getLike()));
        if(quizitems.get(position).getImageUrl().equals("not_selected"))
        {
            holder.profilepic.setBackgroundResource(R.drawable.profile);
        }
        else
        {
            Picasso.get().load(quizitems.get(position).getImageUrl()).into(holder.profilepic);

        }
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count==0)
                {
                    count = 1;
                    DocumentReference documentReference1 = firestore.collection("Notices").document(quizitems.get(position).getTitle());
                    documentReference1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            NoticeClass noticeClass = documentSnapshot.toObject(NoticeClass.class);
                            Map<String,Object> user = new HashMap<>();
                            user.put("like",quizitems.get(position).getLike()+1);
                            documentReference1.update(user);
                            holder.likes.setText(Integer.toString(quizitems.get(position).getLike()+1));


                        }
                    });

                }
                else
                {

                }



            }
        });

        holder.contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String un = quizitems.get(position).getName();
                System.out.println(un);
                if(un.equals(userID))
                {
                    Toast.makeText(v.getContext(),"This is your own post!",Toast.LENGTH_SHORT);
                    System.out.println("This is your own post!");
                }
                else
                {
                    Intent i = new Intent(v.getContext(),MessageActivityHomeTutor.class);
                    i.putExtra("username",un);
                    i.putExtra("uid",quizitems.get(position).getId());
                    v.getContext().startActivity(i);
                }

            }
        });
    }


    @Override
    public int getItemCount() {
        return quizitems.size();
    }
}
