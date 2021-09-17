package com.example.chalkboard_copy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();
    Context context;
    List<ChatRecordClass> chats;


    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {

        void onClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public MessageAdapter(Context context, List<ChatRecordClass> chats) {
        this.chats = chats;
        this.context = context;


    }


    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView show_message;
        ImageView user_profile_pic;
        TextView txt_seen;

        public MessageViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            show_message = itemView.findViewById(R.id.showmessage);
            user_profile_pic = itemView.findViewById(R.id.user_image);
            txt_seen = itemView.findViewById(R.id.txt_seen);
            //itemView.setOnClickListener(v -> onItemClickListener.onClick(getAdapterPosition()));
        }
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==MSG_TYPE_RIGHT)
        {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right, parent, false);
            return new MessageViewHolder(itemView, onItemClickListener);
        }
        else {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left, parent, false);
            return new MessageViewHolder(itemView, onItemClickListener);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {

        ChatRecordClass chatRecordClass = chats.get(position);
        holder.show_message.setText(chats.get(position).getMessage());
        if(position==chats.size()-1)
        {
            if (chatRecordClass.isIsseen())
            {
                holder.txt_seen.setText("Seen");
            }
            else {
                holder.txt_seen.setText("Delivered");

            }
        }
        else
        {
            holder.txt_seen.setVisibility(View.GONE);
        }
    }




    @Override
    public int getItemCount() {
        return chats.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(chats.get(position).getSender().equals(userID))
        {
            return MSG_TYPE_RIGHT;
        }
        else
        {
            return MSG_TYPE_LEFT;
        }
    }
}
