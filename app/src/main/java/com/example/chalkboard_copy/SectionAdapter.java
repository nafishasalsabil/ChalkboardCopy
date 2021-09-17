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

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

class SectionAdapter extends FirestoreRecyclerAdapter<SectionClass,SectionAdapter.SectionViewHolder> {

    Context context;
    List<SectionClass> section;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();
    DocumentReference documentReference;

    private OnItemClickListener onItemClickListener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public SectionAdapter(@NonNull FirestoreRecyclerOptions<SectionClass> options) {
        super(options);
    }

    public interface OnItemClickListener {

        void onClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

   /* public SectionAdapter(Context context, List<SectionClass> section) {
        this.section = section;
        this.context = context;


    }*/
   public void deleteItem(int position) {
       getSnapshots().getSnapshot(position).getReference().delete();
   }


    public static class SectionViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView section_name;


        public SectionViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            section_name = itemView.findViewById(R.id.sectiontitle);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(),InsideClassActivity.class);
                    intent.putExtra("section",section_name.getText());
                    intent.putExtra("Title",Sections_Inside_Courses.title);
                    v.getContext().startActivity(intent);

                }
            });
        }
    }

    @NonNull
    @Override
    public SectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_item, parent, false);
        return new SectionViewHolder(itemView, onItemClickListener);
    }

  /*  @Override
    public void onBindViewHolder(@NonNull SectionViewHolder holder, int position) {
        holder.section_name.setText(section.get(position).getSection());


    }
*/
    @Override
    protected void onBindViewHolder(@NonNull SectionViewHolder holder, int position, @NonNull SectionClass model) {
        holder.section_name.setText(model.getSection());

    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }
/* @Override
    public int getItemCount() {
        return section.size();
    }*/
}
