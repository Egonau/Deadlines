package com.example.deadlines;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class AcceptanceAdapter extends RecyclerView.Adapter<AcceptanceAdapter.DeadlineViewHolder> {
    private LayoutInflater inflater;
    private ArrayList<HashMap<String,Object>> deadlinesForAdapter;
    private Context context;

    public AcceptanceAdapter(Context context,ArrayList<HashMap<String,Object>> arr){
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.deadlinesForAdapter = arr;
    }
    @NonNull
    @Override
    public DeadlineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item_acceptance_view, parent, false);
        return new DeadlineViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DeadlineViewHolder holder, int position) {
        //наполнение каждого элемента интерфейса
        holder.deadline_acceptance_name.setText(deadlinesForAdapter.get(position).get("DeadlineName").toString());
        holder.deadline_acceptance_priority.setText(deadlinesForAdapter.get(position).get("DeadlinePriority").toString());
        holder.deadline_acceptance_description.setText(deadlinesForAdapter.get(position).get("DeadlineDescription").toString());
        String date = deadlinesForAdapter.get(holder.getAdapterPosition()).get("DeadlineDateInfo").toString();
        String[] dateM= date.split("_");
        String year = dateM[0];
        String month = dateM[1];
        String day = dateM[2];
        String number = dateM[3];
        holder.deadline_acceptance_date.setText(day+"/"+String.valueOf(Integer.valueOf(month)+1)+"/"+String.valueOf(Integer.valueOf(year)+1900));
        if (deadlinesForAdapter.get(holder.getAdapterPosition()).get("Type").toString().equals("NonSchool")){
            holder.deadline_acceptance_lesson.setText("Не школьный дедлайн");
        }
        else {
            holder.deadline_acceptance_lesson.setText(deadlinesForAdapter.get(holder.getAdapterPosition()).get("Lesson").toString());
        }
        //отказ от дедлайна, удаление его ветки из БД
        holder.rejectImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                FirebaseAuth auth = FirebaseAuth.getInstance();
                rootRef.child(Objects.requireNonNull(auth.getUid())).child("Deadlines").child("Suggested").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        deadlinesForAdapter.remove(holder.getAdapterPosition());
                        for (Integer i = 0;i<deadlinesForAdapter.size();++i){
                            String chosenDate = deadlinesForAdapter.get(i).get("DeadlineDateInfo").toString();
                            String[] date= chosenDate.split("_");
                            String Year = date[0];
                            String Month = date[1];
                            String Day = date[2];
                            String Number = date[3];
                            deadlinesForAdapter.get(i).put("DeadlineDateInfo",Year+"_"+Month+"_"+Day+"_"+i);
                            rootRef.child(auth.getUid()).child("Deadlines").child("Suggested").child(String.valueOf(i)).updateChildren(deadlinesForAdapter.get(i));
                        }
                        Intent intent=new Intent(context,MenuActivity.class);
                        context.startActivity(intent);
                    }
                });
            }
        });
        //принятие дедлайна, добавление в ветку пользователя
        holder.acceptImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                FirebaseAuth auth = FirebaseAuth.getInstance();
                HashMap<String,Object> selectedDeadline = deadlinesForAdapter.get(holder.getAdapterPosition());
                String selectedDate = selectedDeadline.get("DeadlineDateInfo").toString();
                String[] date= selectedDate.split("_");
                String selectedYear = date[0];
                String selectedMonth = date[1];
                String selectedDay = date[2];
                String selectedNumber = date[3];
                rootRef.child(Objects.requireNonNull(auth.getUid())).child("Deadlines").child("Accepted").child(selectedYear).child(selectedMonth).child(selectedDay).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        selectedDeadline.put("DeadlineDateInfo",selectedYear+"_"+selectedMonth+"_"+selectedDay+"_"+snapshot.getChildrenCount());
                        rootRef.child(auth.getUid()).child("Deadlines").child("Accepted").child(selectedYear).child(selectedMonth).child(selectedDay).child(String.valueOf(snapshot.getChildrenCount())).updateChildren(selectedDeadline).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                                rootRef.child(Objects.requireNonNull(auth.getUid())).child("Deadlines").child("Suggested").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        deadlinesForAdapter.remove(holder.getAdapterPosition());
                                        for (Integer i = 0;i<deadlinesForAdapter.size();++i){
                                            String chosenDate = deadlinesForAdapter.get(i).get("DeadlineDateInfo").toString();
                                            String[] date= chosenDate.split("_");
                                            String Year = date[0];
                                            String Month = date[1];
                                            String Day = date[2];
                                            String Number = date[3];
                                            deadlinesForAdapter.get(i).put("DeadlineDateInfo",Year+"_"+Month+"_"+Day+"_"+i);
                                            rootRef.child(auth.getUid()).child("Deadlines").child("Suggested").child(String.valueOf(i)).updateChildren(deadlinesForAdapter.get(i));
                                        }
                                        Intent intent=new Intent(context,MenuActivity.class);
                                        context.startActivity(intent);
                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return deadlinesForAdapter.size();
    }

    public static class DeadlineViewHolder extends RecyclerView.ViewHolder {
        CardView deadlineAcceptanceCardView;
        TextView deadline_acceptance_name;
        TextView deadline_acceptance_priority;
        TextView deadline_acceptance_description;
        TextView deadline_acceptance_date;
        TextView deadline_acceptance_lesson;
        ImageView acceptImageView;
        ImageView rejectImageView;

        public DeadlineViewHolder(View itemView) {
            super(itemView);
            deadlineAcceptanceCardView = itemView.findViewById(R.id.olympCardView);
            deadline_acceptance_name = itemView.findViewById(R.id.olymp_name);
            deadline_acceptance_priority = itemView.findViewById(R.id.deadline_acceptance_description);
            deadline_acceptance_description = itemView.findViewById(R.id.olymp_status);
            deadline_acceptance_date = itemView.findViewById(R.id.deadline_acceptance_date);
            deadline_acceptance_lesson = itemView.findViewById(R.id.deadline_acceptance_lesson);
            acceptImageView = itemView.findViewById(R.id.acceptImageView);
            rejectImageView = itemView.findViewById(R.id.rejectImageView);
        }
    }
}
