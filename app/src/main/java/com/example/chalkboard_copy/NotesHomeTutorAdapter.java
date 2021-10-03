package com.example.chalkboard_copy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

class NotesHomeTutorAdapter extends FirestoreRecyclerAdapter<NotesClass, NotesHomeTutorAdapter.NotesHomeTutorViewHolder> {

    Context context;
    List<NotesClass> notes;
    public static String title,section;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public NotesHomeTutorAdapter(@NonNull FirestoreRecyclerOptions<NotesClass> options) {
        super(options);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSection(String section) {
        this.section = section;
    }

    String state = "";
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();
    DocumentReference documentReference;

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {

        void onClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }




    public void setState(String state) {
        this.state = state;
    }

    public static class NotesHomeTutorViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView note_title,note_subtitle,my_note,url,date;

        public NotesHomeTutorViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            note_title = itemView.findViewById(R.id.notes_title_ht);
            note_subtitle = itemView.findViewById(R.id.notes_subtitle_ht);
            my_note = itemView.findViewById(R.id.mynotes_ht);
            url = itemView.findViewById(R.id.url_tv_ht);
            date = itemView.findViewById(R.id.notes_date_ht);
            cardView=itemView.findViewById(R.id.note_card_ht);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(),EditNoteHomeTutorActivity.class);
                    intent.putExtra("title",title);
                    intent.putExtra("section",section);
                    intent.putExtra("note_name",note_title.getText());
                    v.getContext().startActivity(intent);
                }
            });

           /* itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    onItemClickListener.onClick(ClassViewHolder.this.getAdapterPosition());
                    Intent intent = new Intent(v.getContext(),EditNoteHT.class);
                    intent.putExtra("title",title);
                    intent.putExtra("section",section);
                    intent.putExtra("note_name",note_title.getText());

                    // System.out.println(classname.getText());
                    v.getContext().startActivity(intent);
                }
            });
*/
        }
    }

    @NonNull
    @Override
    public NotesHomeTutorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_item_hometutor, parent, false);
        return new NotesHomeTutorViewHolder(itemView, onItemClickListener);
    }


    @Override
    protected void onBindViewHolder(@NonNull NotesHomeTutorViewHolder holder, int position, @NonNull NotesClass model) {
        holder.note_title.setText(model.getNoteTitle());
        MyNotes_hometutor.setNote_title(model.getNoteTitle());
        holder.note_subtitle.setText(model.getSubtitle());
        holder.my_note.setText(model.getMynote());
        holder.url.setText(model.getUrl());
        holder.date.setText(model.getDate());
        if (model.getUrl().equals("No url added"))
        {
            holder.url.setVisibility(View.GONE);

        }
        else
        {
            holder.url.setVisibility(View.VISIBLE);
            Linkify.addLinks(holder.url, Linkify.WEB_URLS);
            holder.url.setLinkTextColor(Color.parseColor("#85BFB4"));


        }

    }


 }
