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

import java.util.ArrayList;
import java.util.List;

class NotesAdapter extends FirestoreRecyclerAdapter<NotesClass,NotesAdapter.NotesViewHolder> {

    Context context;
    List<NotesClass> notes;
    public static String title,section;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public NotesAdapter(@NonNull FirestoreRecyclerOptions<NotesClass> options) {
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

    public static String getNote_title() {
        return note_title;
    }

    public static void setNote_title(String note_title) {
        NotesAdapter.note_title = note_title;
    }

    public static String note_title;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();
    DocumentReference documentReference;

    private OnItemClickListener onItemClickListener;

    public void filterlist(ArrayList<NotesClass> filteredlist) {
        notes=filteredlist;
        notifyDataSetChanged();
    }

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

    public static class NotesViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;



        TextView note_title,note_subtitle,my_note,url,date;

        public NotesViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            note_title = itemView.findViewById(R.id.notes_title);
            note_subtitle = itemView.findViewById(R.id.notes_subtitle);
            my_note = itemView.findViewById(R.id.mynotes);
            url = itemView.findViewById(R.id.url_tv);
            date = itemView.findViewById(R.id.notes_date);
            cardView=itemView.findViewById(R.id.note_card);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(),EditNoteActivity.class);
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
                    Intent intent = new Intent(v.getContext(),EditNoteActivity.class);
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
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_item, parent, false);
        return new NotesViewHolder(itemView, onItemClickListener);
    }


    @Override
    protected void onBindViewHolder(@NonNull NotesViewHolder holder, int position, @NonNull NotesClass model) {
        holder.note_title.setText(model.getNoteTitle());
        note_title=model.getNoteTitle();
        MyNotes.setNote_title(model.getNoteTitle());
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
