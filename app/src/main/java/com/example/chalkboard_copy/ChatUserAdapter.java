package com.example.chalkboard_copy;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

class ChatUserAdapter extends RecyclerView.Adapter<ChatUserAdapter.ChatUserViewHolder> {

    Context context;
    List<ChatUser> users;
    private boolean isChat;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();
    String the_last_message = "default";

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {

        void onClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ChatUserAdapter(Context context, List<ChatUser> users,boolean isChat) {
        this.users = users;
        this.context = context;
        this.isChat = isChat;


    }

    @NonNull
    @Override
    public ChatUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chats_user_item, parent, false);
        return new ChatUserViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatUserViewHolder holder, int position) {
        ChatUser chatUser= users.get(position);
        holder.chat_sername.setText(chatUser.getUsername());
        System.out.println(chatUser.getUsername());
        System.out.println(chatUser.getId());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,MessageActivity.class);
                intent.putExtra("username",chatUser.getUsername());
                intent.putExtra("uid",chatUser.getId());
                context.startActivity(intent);
            }
        });
        if(isChat)
        {
            lastmessage(chatUser.getId(),holder.last_message_textview);
        }
        else {
            holder.last_message_textview.setVisibility(View.GONE);
        }
        if(isChat)
        {
            if(chatUser.getActive_status().equals("online"))
            {
                holder.image_on.setVisibility(View.VISIBLE);
                holder.image_off.setVisibility(View.GONE);
            }
            else
            {
                holder.image_on.setVisibility(View.GONE);
                holder.image_off.setVisibility(View.VISIBLE);

            }
        }
        else
        {
            holder.image_on.setVisibility(View.GONE);
            holder.image_off.setVisibility(View.GONE);

        }
        // Log.d("hello",forSignupDatabase.getUsername());
            /*if(chatUser.getImageUrl().equals("default"))
            {
                holder.user_profile_pic.setImageResource(R.drawable.user);
            }
            else {
                Glide.with(context).load(chatUser.getImageUrl()).into(holder.user_profile_pic);
            }*/
    }

    public static class ChatUserViewHolder extends RecyclerView.ViewHolder {
        TextView chat_sername,last_message_textview;
        ImageView user_profile_pic,image_on,image_off;

        public ChatUserViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            chat_sername = itemView.findViewById(R.id.chat_username);
            user_profile_pic = itemView.findViewById(R.id.user_image);
            image_on = itemView.findViewById(R.id.image_on);
            image_off = itemView.findViewById(R.id.image_off);
            last_message_textview = itemView.findViewById(R.id.last_message);
            //itemView.setOnClickListener(v -> onItemClickListener.onClick(getAdapterPosition()));
        }

    }

    private void lastmessage(String id,TextView last_message)
    {
        CollectionReference collectionReference1 = firestore.collection("chats");
        collectionReference1.orderBy("time", Query.Direction.ASCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                {
                    ChatRecordClass object = documentSnapshot.toObject(ChatRecordClass.class);
                    if(object.getReciever().equals(userID) && object.getSender().equals(id)
                            || object.getReciever().equals(id) && object.getSender().equals(userID))
                    {
                        the_last_message = object.getMessage();
                    }

                }
                switch (the_last_message){
                    case "default" :
                        last_message.setText("No messages");
                        break;
                    default:
                        last_message.setText(the_last_message);
                        break;

                }
                the_last_message = "default";




           /*    classAdapter = new ClassAdapter(getApplicationContext(), classitems);
               List<CourseInfo> documentData = queryDocumentSnapshots.toObjects(CourseInfo.class);
*/
            }

        });
    }





    @Override
    public int getItemCount() {
        return users.size();
    }
}
