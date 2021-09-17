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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

class ClassAdapter extends FirestoreRecyclerAdapter<CourseInfo,ClassAdapter.ClassViewHolder> {

    Context context;
    List<CourseInfo> classitems = new ArrayList<>();
    DocumentSnapshot documentSnapshot;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ClassAdapter(@NonNull FirestoreRecyclerOptions<CourseInfo> options) {
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
    public static String status = "";
    public final String PROF="Professional Account";
    public final String HT="Tutor Account";
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

    public static class ClassViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView classname;
        TextView course;
        ImageView edit;

        public ClassViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            classname = itemView.findViewById(R.id.coursetitle);
            course = itemView.findViewById(R.id.courseno);
            cardView = itemView.findViewById(R.id.classcard);
            edit = itemView.findViewById(R.id.editclass);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    onItemClickListener.onClick(ClassViewHolder.this.getAdapterPosition());
                    Intent intent = new Intent(v.getContext(),Sections_Inside_Courses.class);
                    intent.putExtra("Title",classname.getText());
                   // System.out.println(classname.getText());
                    v.getContext().startActivity(intent);
                }
            });
        }


    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_item, parent, false);
        context = parent.getContext();

        return new ClassViewHolder(itemView, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    protected void onBindViewHolder(@NonNull ClassViewHolder holder, int position, @NonNull CourseInfo model)  {
        holder.classname.setText(model.getCourseTitle());
        holder.course.setText(model.getCourseNo());
       // System.out.println(position);
      //  System.out.println(holder.getAdapterPosition());

       // setPos(holder.getAdapterPosition());

        //    holder.editcoursetitle.setText(classitems.get(position).getCourseTitle());
        // holder.editcourseno.setText(classitems.get(position).getCourseNo());

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.edit_class_layout);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                EditText editcourseno = (EditText) dialog.findViewById(R.id.edit_courseno);
               editcourseno.setText(model.getCourseNo());
                EditText editcoursetype= (EditText) dialog.findViewById(R.id.edit_coursetype);
                editcoursetype.setText(model.getCourseType());
                EditText editcredithour = (EditText) dialog.findViewById(R.id.edit_credithours);
                editcredithour.setText(model.getCredits());
              //  System.out.println(classitems.get(position).getCredits());
                Button update = (Button) dialog.findViewById(R.id.updatebutton);
                Button cancel = (Button) dialog.findViewById(R.id.cancelbutton);
                DocumentReference documentReference_for_status = firestore.collection("users").document(userID);

                documentReference_for_status.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot doc = task.getResult();
                        StringBuilder fields = new StringBuilder("");
                        status = fields.append(doc.get("choice")).toString();
                        System.out.println(status);
                    }
                }) .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

                if(status.equals("Professional teacher / Home tutor")){

                    documentReference = firestore.collection("users").document(userID).collection("All Files").document(PROF).collection("Courses").document(model.getCourseTitle());

                }
                else{
                    documentReference = firestore.collection("users").document(userID).collection("Courses").document(model.getCourseTitle());

                }
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ((isTypeChanged() && isNumberChanged() && isCreditChanged())||( isTypeChanged()&& isNumberChanged())|| (isCreditChanged()&& isTypeChanged())||
                                (isCreditChanged() && isNumberChanged()) || isTypeChanged() || isNumberChanged() || isCreditChanged()
                        ){
                            Toast.makeText(context,"Your Course has been updated!", Toast.LENGTH_SHORT).show();




                            //!(classitems.get(position).getCourseTitle()).equals(editcoursetitle.getText()) || !(classitems.get(position).getCourseNo()).equals(editcourseno.getText())
                            dialog.dismiss();

                        }

                    }

                    private boolean isCreditChanged() {
                        if(!((model.getCredits()).equals(editcredithour.getText())))
                        {
                            String cc = editcredithour.getText().toString();
                            //  classitems.get(position).setCourseTitle(cp);

                            documentReference.update("credits", cc).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                               //     Toast.makeText(context,"Your credits has been updated!", Toast.LENGTH_SHORT).show();

                                }
                            });
                            //  holder.classname.setText(cp);
                            return true;
                        }
                        else {
                            return false;
                        }
                    }

                    private boolean isTypeChanged() {
                     //   System.out.println(classitems.size());
                        System.out.println(model.getCourseType());
                        if(!((model.getCourseType()).equals(editcoursetype.getText().toString())))
                        {
                            String cp = editcoursetype.getText().toString();
                          //  classitems.get(position).setCourseTitle(cp);
                            documentReference.update("courseType", cp).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                  //  Toast.makeText(context,"Your coursetype has been updated!", Toast.LENGTH_SHORT).show();

                                }
                            });
                            return true;

                            //  holder.classname.setText(cp);
                        }
                        else {
                            return false;
                        }
                    }
                    private boolean isNumberChanged() {
                        if(!((model.getCourseNo()).equals(editcourseno.getText())))
                        {
                            String cq = editcourseno.getText().toString();
                        //    classitems.get(position).setCourseNo(cq);
                            documentReference.update("courseNo", cq).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                 //   Toast.makeText(context,"Your courseno has been updated!", Toast.LENGTH_SHORT).show();

                                }
                            });
                           // holder.course.setText(cq);
                            return true;
                        }
                        else{
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
