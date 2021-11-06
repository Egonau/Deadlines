package com.example.deadlines;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.DeadlineViewHolder> {
    private LayoutInflater inflater;
    private ArrayList<HashMap<String,Object>> deadlinesForAdapter;
    private Context context;

    public RecyclerAdapter(Context context,ArrayList<HashMap<String,Object>> arr){
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.deadlinesForAdapter = arr;
    }
    @NonNull
    @Override
    public DeadlineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item_view, parent, false);
        return new DeadlineViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DeadlineViewHolder holder, int position) {
        //наполнение каждого элемента списка
        holder.deadline_name.setText(deadlinesForAdapter.get(position).get("DeadlineName").toString());
        holder.deadline_priority.setText(deadlinesForAdapter.get(position).get("DeadlinePriority").toString());
        holder.deadline_description.setText(deadlinesForAdapter.get(position).get("DeadlineDescription").toString());
        holder.editImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = deadlinesForAdapter.get(holder.getAdapterPosition()).get("DeadlineDateInfo").toString();
                Single.getInstance().dateInfo = date.split("_");
                String year = Single.getInstance().dateInfo[0];
                String month = Single.getInstance().dateInfo[1];
                String day = Single.getInstance().dateInfo[2];
                String number = Single.getInstance().dateInfo[3];
                Single.getInstance().editedDeadline = deadlinesForAdapter.get(holder.getAdapterPosition());
                Intent intent=new Intent(context,EditingActivity.class);
                context.startActivity(intent);
            }
        });
        holder.shareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Single.getInstance().sharingDeadline = deadlinesForAdapter.get(holder.getAdapterPosition());
                Intent intent=new Intent(context,SharingActivity.class);
                context.startActivity(intent);
            }
        });
        holder.deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = deadlinesForAdapter.get(holder.getAdapterPosition()).get("DeadlineDateInfo").toString();
                Single.getInstance().dateInfo = date.split("_");
                String year = Single.getInstance().dateInfo[0];
                String month = Single.getInstance().dateInfo[1];
                String day = Single.getInstance().dateInfo[2];
                String number = Single.getInstance().dateInfo[3];
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                FirebaseAuth auth = FirebaseAuth.getInstance();
                Single.getInstance().deletingDeadlines.clear();
                rootRef.child(auth.getUid()).child("Deadlines").child("Accepted").child(year).child(month).child(day).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds:snapshot.getChildren()){
                            GenericTypeIndicator<HashMap<String,Object>> GTI = new GenericTypeIndicator<HashMap<String, Object>>() {};
                            Single.getInstance().deletingDeadlines.add(ds.getValue(GTI));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                rootRef.child(auth.getUid()).child("Deadlines").child("Accepted").child(year).child(month).child(day).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Single.getInstance().deletingDeadlines.remove(Integer.parseInt(number));
                        for (Integer i = 0;i<Single.getInstance().deletingDeadlines.size();++i){
                            Single.getInstance().deletingDeadlines.get(i).put("DeadlineDateInfo",year+"_"+month+"_"+day+"_"+i);
                            rootRef.child(auth.getUid()).child("Deadlines").child("Accepted").child(year).child(month).child(day).child(String.valueOf(i)).updateChildren(Single.getInstance().deletingDeadlines.get(i));
                        }
                        Single.getInstance().deletingDeadlines.clear();
                        Intent intent=new Intent(context,MenuActivity.class);
                        context.startActivity(intent);
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
        CardView deadlineCardView;
        TextView deadline_name;
        TextView deadline_priority;
        TextView deadline_description;
        ImageView shareImageView;
        ImageView deleteImageView;
        ImageView editImageView;
        public DeadlineViewHolder(View itemView) {
            super(itemView);
            deadlineCardView = itemView.findViewById(R.id.olympCardView);
            deadline_name = itemView.findViewById(R.id.olymp_name);
            deadline_description = itemView.findViewById(R.id.deadline_acceptance_description);
            deadline_priority = itemView.findViewById(R.id.olymp_status);
            shareImageView = itemView.findViewById(R.id.shareImageView);
            deleteImageView = itemView.findViewById(R.id.deleteImageView);
            editImageView = itemView.findViewById(R.id.editImageView);
        }
    }
}
