package io.github.iyado.accounterx.adapters;


import static io.github.iyado.accounterx.AccounterApplication.noti;
import static io.github.iyado.accounterx.MainActivity.localCur;
import static io.github.iyado.accounterx.MainActivity.localCurByName;
import static io.github.iyado.accounterx.MainActivity.roundD;
import static io.github.iyado.accounterx.MainActivity.stringo;
import static io.github.iyado.accounterx.MainActivity.sumallitemsindouble;
import static io.github.iyado.accounterx.MainActivity.usersReference;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import io.github.iyado.accounterx.holders.PrintAdapterViewHolder;
import io.github.iyado.accounterx.utils.Prog;
import io.github.iyado.accounterx.R;



public class UserAccountsAdapter extends RecyclerView.Adapter<PrintAdapterViewHolder> {

    @NonNull
    private ArrayList<Prog> progs;
    @NonNull
    private final Context context;
    final AppCompatActivity activity;
    @NonNull
    private String user;
    private int TYPE=0;

    public UserAccountsAdapter(@NonNull AppCompatActivity activity, @NonNull Context context, @NonNull String user, @NonNull ArrayList<Prog> progs){
        this.progs = progs;
        this.user = user;
        this.activity = activity;
        this.context = context;
    }

    public void setTYPE(int TYPE) {
        this.TYPE = TYPE;
    }

    public void setProgs(@NonNull ArrayList<Prog> progs) {
        this.progs = progs;
    }

    @NonNull
    @Override
    public PrintAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new PrintAdapterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.printer_info,parent,false));

    }

    @Override
    public int getItemViewType(int position) {
        return TYPE;
    }

    @SuppressLint({"SetTextI18n", "SimpleDateFormat", "NotifyDataSetChanged", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NonNull PrintAdapterViewHolder cViewHolder, @SuppressLint("RecyclerView") int position) {

        Prog prog = progs.get(position);


        if (TYPE==1){
            cViewHolder.checkBox.setEnabled(false);
        }

        cViewHolder.checkBox.setChecked(prog.isChecked());


        cViewHolder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
           editcheck(prog, !prog.isChecked());
           prog.setChecked(isChecked);


       });

        double m = 0;
        for (int j=0; j<=position; j++){
            m += progs.get(j).getSumAll();
        }
        cViewHolder.total.setText(" "+roundD(m));
        cViewHolder.total.setTextColor(context.getColor(R.color.m));
        if (prog.getSumAll() > 0 ){
            cViewHolder.ex.setTextColor(context.getColor(R.color.x500));
        }else if (prog.getSumAll() < 0){
            cViewHolder.ex.setTextColor(context.getColor(R.color.red));
        }else {
            cViewHolder.ex.setTextColor(context.getColor(R.color.orange));
        }


        usersReference
                .child(prog.getSender());

        usersReference
                .child(prog.getReciever());

        cViewHolder.count.setText(prog.getEx());
        cViewHolder.count.setTextColor(Color.parseColor(Objects.requireNonNull(localCurByName.get(prog.getEx())).getColor()));
        cViewHolder.ex.setText(stringo(prog.getSumAll()));

        String hx;
        if (prog.getReciever().equals(user)) {
            hx = prog.getSender();
        }
        else {
            hx = prog.getReciever();
        }
        cViewHolder.dates.setText( new SimpleDateFormat("dd MMM yyyy HH:mm").format(new Date(Long.parseLong(prog.getKey()))));
            cViewHolder.notice.setText(prog.getCustomer());
            cViewHolder.client.setText(hx);
            cViewHolder.client.setTextColor(context.getColor(R.color.orange));
            cViewHolder.client.setBackground(context.getDrawable(R.drawable.radiusxl));


        cViewHolder.imageView.setOnClickListener(v -> new AlertDialog.Builder(v.getContext())
                        .setMessage(R.string.sure)
                        .setNeutralButton("نعم",(dialog, which) -> removeRestrictions(prog.getKey()))

                        .setNegativeButton("الغاء",(dialog, which) ->
                                dialog.dismiss())
                        .setCancelable(true)
                        .setTitle("حذف القيد المحدد")
                        .setIcon(android.R.drawable.ic_menu_delete)
                        .show()
        );

    }

    private void removeRestrictions(String key) {
        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            /** @noinspection DataFlowIssue*/
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot user: snapshot.getChildren()){

                    DatabaseReference userRef = user.getRef();
                        for (DataSnapshot account : user.child("accounts").getChildren()){
                            for (DataSnapshot prog: account.getChildren()){
                                Prog removedProg = prog.getValue(Prog.class);
                                assert removedProg != null;
                                String ke = removedProg.getKey();
                                String cur2 = removedProg.getEx();
                                double pr2 = removedProg.getSumAll();

                                String newKey = ke.subSequence(0,9).toString();
                                if (newKey.contentEquals(key.subSequence(0,9))){

                                    userRef.child("account")
                                            .child(cur2)
                                            .child("count").get()
                                            .addOnSuccessListener(dataSnapshot1x -> {

                                                double count1 = 0.0d;
                                                if (dataSnapshot1x.exists()) {
                                                    count1 = dataSnapshot1x.getValue(Double.class);
                                                }
                                                count1 -= pr2;
                                                userRef.child("account")
                                                        .child(cur2)
                                                        .child("count").setValue(count1);
                                                userRef.child("all").get().addOnSuccessListener(command -> updateAll(userRef));

                                                prog.getRef().removeValue();

                                            }).addOnFailureListener(e -> noti(activity, e.getLocalizedMessage()));
                                }
                            }
                        }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        noti(activity,activity.getString(R.string.deleted));

    }

    private void updateAll(@NonNull DatabaseReference userRef) {
        userRef.child("account").addListenerForSingleValueEvent(new ValueEventListener() {
            /** @noinspection DataFlowIssue*/
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double[] allCount = new double[localCur.size()];
                for (DataSnapshot curs : snapshot.getChildren()){
                    for (DataSnapshot counts : curs.getChildren() ){

                        allCount[Objects.requireNonNull(localCurByName.get(curs.getKey())).getId()] += counts.getValue(Double.class);

                    }

                }
                userRef.child("all").setValue(roundD(sumallitemsindouble(allCount)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void editcheck(@NonNull Prog prog, boolean b) {
        usersReference.child(user).child("accounts")
                .child(prog.getDate())
                .child(prog.getKey())
                .get().addOnSuccessListener(command -> {
                    if (command.exists()){
                        command.child("checked").getRef()
                                .setValue(b);
                    }
                });

    }

    public void setUser(@NonNull String user) {
        this.user = user;
    }

// --Commented out by Inspection START (12/06/2025 14:15):
//    /**
//     * @return {@code }
//     */
//    @NonNull
//    public String getUser() {
//        return user;
//    }
// --Commented out by Inspection STOP (12/06/2025 14:15)

    @Override
    public int getItemCount() {
        return progs.size();
    }
}
