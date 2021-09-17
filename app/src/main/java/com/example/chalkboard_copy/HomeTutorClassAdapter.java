package com.example.chalkboard_copy;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

class HomeTutorClassAdapter extends FirestoreRecyclerAdapter<CourseInfoHomeTutorClass, HomeTutorClassAdapter.HomeTutorClassViewHolder> {

    Context context;
    List<CourseInfo> classitems;

    DocumentSnapshot documentSnapshot;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public HomeTutorClassAdapter(@NonNull FirestoreRecyclerOptions<CourseInfoHomeTutorClass> options) {
        super(options);
    }



    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    String p = "", q = "";
    int pos;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();
    DocumentReference documentReference;

    private OnItemClickListener onItemClickListener;


    public interface OnItemClickListener {

        void onClick(int position);

    }
    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /*public ClassAdapter(Context context, List<CourseInfo> classitems) {
        this.classitems = classitems;
        this.context = context;


    }
*/

    public static class HomeTutorClassViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView classname;
        TextView course;
        ImageView edit;

        public HomeTutorClassViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            course = itemView.findViewById(R.id.coursetitle_hometutor);
            classname = itemView.findViewById(R.id.classname_text);
            cardView = itemView.findViewById(R.id.classcard);
            edit = itemView.findViewById(R.id.editclass_hometutor);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    onItemClickListener.onClick(ClassViewHolder.this.getAdapterPosition());
                    Intent intent = new Intent(v.getContext(),Batch_inside_courses_home_tutor.class);
                    intent.putExtra("className",course.getText());
                   // System.out.println(classname.getText());
                    v.getContext().startActivity(intent);
                }
            });
        }


    }

    @NonNull
    @Override
    public HomeTutorClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_item_hometutor, parent, false);
        context = parent.getContext();

        return new HomeTutorClassViewHolder(itemView, onItemClickListener);
    }

    @Override
    protected void onBindViewHolder(@NonNull HomeTutorClassViewHolder holder, int position, @NonNull CourseInfoHomeTutorClass model) {
        holder.classname.setText(model.getClassName());
        holder.course.setText(model.getCourseName());
       // System.out.println(position);
      //  System.out.println(holder.getAdapterPosition());

       // setPos(holder.getAdapterPosition());

        //    holder.editcoursetitle.setText(classitems.get(position).getCourseTitle());
        // holder.editcourseno.setText(classitems.get(position).getCourseNo());

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.edit_class_layout_hometutor);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                EditText editclassname = (EditText) dialog.findViewById(R.id.edit_classname_hometutor);
               editclassname.setText(model.getClassName());
                 //  System.out.println(classitems.get(position).getCredits());
                Button update = (Button) dialog.findViewById(R.id.updatebutton_hometutor);
                Button cancel = (Button) dialog.findViewById(R.id.cancelbutton_hometutor);

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isClassNameChanged()){
                            Toast.makeText(context,"Your profile has been updated!", Toast.LENGTH_SHORT).show();
                            documentReference = firestore.collection("users").document(userID).collection("Courses").document(model.getCourseName());

                            //!(classitems.get(position).getCourseTitle()).equals(editcoursetitle.getText()) || !(classitems.get(position).getCourseNo()).equals(editcourseno.getText())
                            dialog.dismiss();

                        }

                    }



                    private boolean isClassNameChanged() {
                        if(!((model.getClassName()).equals(editclassname.getText())))
                        {
                            documentReference = firestore.collection("users").document(userID).collection("Courses").document(model.getCourseName());

                            String cp = editclassname.getText().toString();
                          //  classitems.get(position).setCourseTitle(cp);
                            documentReference.update("className", cp).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(context,"Your course has been updated!", Toast.LENGTH_SHORT).show();

                                }
                            });
                            return true;

                            //  holder.classname.setText(cp);
                        }
                        else {
                            return false;
                        }
                    }

                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }


        });

       /* int cardcolors[] = context.getResources().getIntArray(R.array.classcolors);
        int randomAndroidColor = cardcolors[new Random().nextInt(cardcolors.length)];
        //  int remainder = getAdapterPosition() % cardcolors.size();
        holder.cardView.setBackgroundColor(randomAndroidColor);
        holder.cardView.setRadius(6);*/

    }







}
