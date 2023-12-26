package io.github.iyado.accounterx;


import static io.github.iyado.accounterx.JobActivity.COMPANY_NAME;
import static io.github.iyado.accounterx.JobActivity.initHasMap;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PrinterViewAdapter extends RecyclerView.Adapter<CViewHolder> {

    private ArrayList<Prog> progs;
    private Spinner spinner;
    private String user;
    private DatabaseReference databaseReference1,databaseReference2;

    public PrinterViewAdapter(@NonNull String user,@NonNull ArrayList<Prog> progs){
        this.progs = progs;
        this.user = user;
    }

    public ArrayList<Prog> getProgs() {
        return progs;
    }

    public void setProgs(ArrayList<Prog> progs) {
        this.progs = progs;
    }

    @NonNull
    @Override
    public CViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new CViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.printer_info,parent,false));

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CViewHolder cViewHolder, @SuppressLint("RecyclerView") int position) {

        Prog prog = progs.get(position);

        databaseReference1 = FirebaseDatabase.getInstance().getReference()
                        .child(COMPANY_NAME).child("users")
                        .child(prog.getSender());

        databaseReference2 = FirebaseDatabase.getInstance().getReference()
                        .child(COMPANY_NAME).child("users")
                        .child(prog.getReciever());


        cViewHolder.count.setText(prog.getEx());

       // Date date = new Date(prog.getKey())
        cViewHolder.ex.setText(prog.getSumAll());

        String hx;
        if (prog.getReciever().equals(user)) {
            hx = prog.getSender();
        }
        else {
            hx = prog.getReciever();
        }
            cViewHolder.notice.setText(prog.getCustomer() + "\n" + hx
                    + "\n" +
                    new SimpleDateFormat("dd MMM yyyy HH:mm").format(new Date(Long.parseLong(prog.getKey()))));


        cViewHolder.imageView.setOnClickListener(v ->{

                    AlertDialog alertDialog = new AlertDialog.Builder(v.getContext())
                            .setMessage(R.string.sure)
                            .setNeutralButton("نعم",(dialog, which) ->
                            {
                                databaseReference1.child("accounts")
                                        .addListenerForSingleValueEvent(
                                                new ValueEventListener() {

                                                    @SuppressLint("NotifyDataSetChanged")
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        databaseReference1
                                                                .child("accounts")
                                                                .child(prog.getDate())
                                                                .child(prog.getKey()).removeValue()
                                                                .addOnSuccessListener(unused -> {
                                                                    databaseReference2
                                                                            .child("accounts")
                                                                            .child(prog.getDate())
                                                                            .child(prog.getKey())
                                                                            .removeValue()
                                                                            .addOnSuccessListener(unused1 -> {

                                                                                notifyDataSetChanged();

                                                                            }).addOnFailureListener(e -> {
                                                                                Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                                                                            });
                                                                })
                                                                .addOnFailureListener(e -> {
                                                                    Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                });
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                }
                                        );
                            })
                            .setNegativeButton("الغاء",(dialog, which) ->
                                    dialog.dismiss())
                            .setCancelable(true)
                            .setTitle("حذف القيد المحدد")
                            .setIcon(android.R.drawable.ic_menu_delete)
                            .show();
                }
        );

    }

    @Override
    public int getItemCount() {
        return progs.size();
    }
}
class CViewHolder extends RecyclerView.ViewHolder {

    TextView notice,count, ex;
    CardView recCard;
    ImageView imageView;

    public CViewHolder(@NonNull View itemView) {
        super(itemView);

        recCard = itemView.findViewById(R.id.cCard);
        notice = itemView.findViewById(R.id.noticeu);
        ex = itemView.findViewById(R.id.exu);
        count = itemView.findViewById(R.id.accountu);
        imageView = itemView.findViewById(R.id.imageView2);
    }

}
