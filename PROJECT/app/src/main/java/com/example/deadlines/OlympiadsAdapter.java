package com.example.deadlines;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

public class OlympiadsAdapter extends RecyclerView.Adapter<OlympiadsAdapter.DeadlineViewHolder> {
    private LayoutInflater inflater;
    private ArrayList<HashMap<String,Object>> olympsForAdapter;
    private Context context;

    public OlympiadsAdapter(Context context, ArrayList<HashMap<String,Object>> arr){
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.olympsForAdapter = arr;
    }
    @NonNull
    @Override
    public DeadlineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item_olymps_view, parent, false);
        return new DeadlineViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DeadlineViewHolder holder, int position) {
        //наполнение элемента списка олимпиад
        holder.olymp_name.setText(olympsForAdapter.get(position).get("Name").toString());
        holder.olymp_status.setText(olympsForAdapter.get(position).get("Status").toString());
        String url = olympsForAdapter.get(position).get("Link").toString();
        holder.olympImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(browserIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return olympsForAdapter.size();
    }

    public static class DeadlineViewHolder extends RecyclerView.ViewHolder {
        CardView olympCardView;
        TextView olymp_name;
        TextView olymp_status;
        ImageView olympImageView;

        public DeadlineViewHolder(View itemView) {
            super(itemView);
            olympCardView = itemView.findViewById(R.id.olympCardView);
            olymp_name = itemView.findViewById(R.id.olymp_name);
            olymp_status = itemView.findViewById(R.id.olymp_status);
            olympImageView = itemView.findViewById(R.id.olympImageView);
        }
    }
}
