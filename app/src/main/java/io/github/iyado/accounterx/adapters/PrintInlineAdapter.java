package io.github.iyado.accounterx.adapters;


import static io.github.iyado.accounterx.MainActivity.usersReference;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import io.github.iyado.accounterx.R;
import io.github.iyado.accounterx.utils.CurrencyDetails;


public class PrintInlineAdapter extends RecyclerView.Adapter<PrintInlineAdapter.ViewHolder> {
    public final ArrayList<String> list;
    public final HashMap<Integer, CurrencyDetails> hashMap;
    public final double[] counts;
    private String user;

    public PrintInlineAdapter(String user, @NonNull HashMap<Integer, CurrencyDetails> hashMap){

        this.user = user;
        this.hashMap = hashMap;
       list = new ArrayList<>(hashMap.size());
       counts = new double[hashMap.size()];
       for (int i = 0; i < hashMap.size(); i++) {
           counts[i] = 0;
           list.add(Objects.requireNonNull(hashMap.get(i)).getName());
       }


    }

// --Commented out by Inspection START (12/06/2025 14:15):
//    @SuppressLint("NotifyDataSetChanged")
//    public void setUser(String user) {
//        this.user = user;
//
//        notifyDataSetChanged();
//    }
// --Commented out by Inspection STOP (12/06/2025 14:15)

// --Commented out by Inspection START (12/06/2025 14:15):
//    public String getUser() {
//        return user;
//    }
// --Commented out by Inspection STOP (12/06/2025 14:15)

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.print_inline, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

      String cur = list.get(position);
      double count = counts[position];
      holder.cur.setText(cur);


        usersReference.child(user).child("account")
                .child(cur)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.getValue() == null){
                            counts[position] = 0;
                            return;
                        }
                        if (snapshot.getValue(Double.class) == null){
                            counts[position] = 0;
                            return;
                        }
                        System.out.println(snapshot.getValue());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




        holder.count.setText(String.valueOf(counts[position]));
        if (count < 0) {
            holder.count.setTextColor(holder.count.getContext().getColor(R.color.red));
        }
        if (count == 0) {
            holder.count.setTextColor(holder.count.getContext().getColor(R.color.orange));
        }
        if (count > 0) {
            holder.count.setTextColor(holder.count.getContext().getColor(R.color.x500));
        }
    }

    @Override
    public int getItemCount() {
        return hashMap.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        final TextView count;
        final TextView cur;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            count = itemView.findViewById(R.id.sumdx);
            cur = itemView.findViewById(R.id.areax);
        }
    }
}
