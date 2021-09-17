package com.example.chalkboard_copy;

import android.content.Context;
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

import java.util.List;

class TutorialHomeTutorAdapter extends FirestoreRecyclerAdapter<TutorialsClass, TutorialHomeTutorAdapter.TutorialHomeTutorViewHolder> {

    Context context;
    List<TutorialsClass> tutorialsClassList;
       private OnItemClickListener onItemClickListener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public TutorialHomeTutorAdapter(@NonNull FirestoreRecyclerOptions<TutorialsClass> options) {
        super(options);
    }

    public interface OnItemClickListener {

        void onClick(int position);

    }
    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

   /* public TutorialAdapter(Context context, List<TutorialsClass> tutorialsClassList) {
        this.tutorialsClassList = tutorialsClassList;
        this.context = context;


    }*/


    public static class TutorialHomeTutorViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView link_title,link_saved;


        public TutorialHomeTutorViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            link_title = itemView.findViewById(R.id.link_hometutor);
            link_saved = itemView.findViewById(R.id.linkname_hometutor);
           /* itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(),InsideClassActivity.class);
                    intent.putExtra("section",section_name.getText());
                    intent.putExtra("Title",Sections_Inside_Courses.title);
                    v.getContext().startActivity(intent);

                }
            });
*/        }
    }

    @NonNull
    @Override
    public TutorialHomeTutorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tutorials_item_hometutor, parent, false);
        return new TutorialHomeTutorViewHolder(itemView, onItemClickListener);
    }


    @Override
    protected void onBindViewHolder(@NonNull TutorialHomeTutorViewHolder holder, int position, @NonNull TutorialsClass model) {
        holder.link_title.setText(model.getLink_title());
        holder.link_saved.setText(model.getSaved_link());
        Linkify.addLinks(holder.link_saved, Linkify.WEB_URLS);
        holder.link_saved.setLinkTextColor(Color.parseColor("#F8C491"));

    }



}
