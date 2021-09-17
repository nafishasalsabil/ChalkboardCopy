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

class BatchAdapter extends FirestoreRecyclerAdapter<BatchClass,BatchAdapter.BatchViewHolder> {

    Context context;
    List<SectionClass> section;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();
    DocumentReference documentReference;

    private OnItemClickListener onItemClickListener;
   static String title;

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public BatchAdapter(@NonNull FirestoreRecyclerOptions<BatchClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull BatchViewHolder holder, int position, @NonNull BatchClass model) {
        holder.batch_title.setText(model.getBatchName());
        holder.batch_days.setText(model.getBatchDays());
        holder.batch_time.setText(model.getBatchTime());

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


    public static class BatchViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView batch_title,batch_days,batch_time;



        public BatchViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            batch_title = itemView.findViewById(R.id.batchname);
            batch_days = itemView.findViewById(R.id.batch_days);
            batch_time = itemView.findViewById(R.id.batch_time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(),Inside_class_home_tutor.class);
                    intent.putExtra("section",batch_title.getText());
                    intent.putExtra("title",title);

                    v.getContext().startActivity(intent);

                }

            });
        }
    }

    @NonNull
    @Override
    public BatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.batch_item, parent, false);
        return new BatchViewHolder(itemView, onItemClickListener);

    }

    /*  @Override
      public void onBindViewHolder(@NonNull SectionViewHolder holder, int position) {
          holder.section_name.setText(section.get(position).getSection());


      }
  */


   /* @Override
    public int getItemCount() {
        return section.size();
    }*/
}
