package io.github.iyado.accounterx.adapters;


import static io.github.iyado.accounterx.AccounterApplication.noti;
import static io.github.iyado.accounterx.Inputactivities.AddRestrictionActivity.doublito;
import static io.github.iyado.accounterx.MainActivity.detectCurAllReturnDolar;
import static io.github.iyado.accounterx.utils.AllInOne.loadCurrencies;
import static io.github.iyado.accounterx.utils.CutAndDiscount.exchanges;
import static io.github.iyado.accounterx.MainActivity.roundD;
import static io.github.iyado.accounterx.MainActivity.usersReference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DatabaseReference;
import org.jetbrains.annotations.Contract;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import io.github.iyado.accounterx.Inputactivities.CurrencyConversationActivity;
import io.github.iyado.accounterx.PrintUserDetailsActivity;
import io.github.iyado.accounterx.holders.UsersViewHolder;
import io.github.iyado.accounterx.utils.CurrencyDetails;
import io.github.iyado.accounterx.utils.Prog;
import io.github.iyado.accounterx.R;
import io.github.iyado.accounterx.utils.Informations;


public class UsersAdapter extends RecyclerView.Adapter<UsersViewHolder> {

    public final Context context;
    private final AppCompatActivity activity;
    public List<Informations> dataList;


    @SuppressLint("NotifyDataSetChanged")
    public UsersAdapter(AppCompatActivity activity, @NonNull Context context, @NonNull List<Informations> dataList) {
        this.context = context;
        this.activity = activity;
        this.dataList = dataList;
        setHasStableIds(true);
    }


    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_x, parent, false);

        return new UsersViewHolder(view);

    }


    public List<Informations> getDataList() {
        return dataList;
    }

    @SuppressLint({"NonConstantResourceId", "ClickableViewAccessibility", "NewApi", "SetTextI18n", "NotifyDataSetChanged"})
    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Informations informations = dataList.get(holder.getAdapterPosition());
        double s = informations.getAll();

        if (informations.getType()!=null && informations.getType().equals("prim")) {

            holder.wa.setVisibility(View.GONE);
            holder.translate.setVisibility(View.VISIBLE);
            holder.translate.setOnClickListener(v -> {

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                builder.setTitle(context.getString(R.string.profanddeprof));
                builder.setMessage(context.getString(R.string.sure));
                builder.setPositiveButton(context.getString(R.string.save), (dialog, which) -> translateProfits(dataList.get(position)));
                builder.setNegativeButton(context.getString(R.string.cancel), (dialog, which) -> dialog.dismiss());
                builder.show();


            });
        }

        holder.useraccount.setText(roundD(s) + "");
        holder.userid.setText(informations.getUid());
        holder.username.setText(informations.getUsername());

        if (informations.getFullname() != null && !informations.getFullname().trim().isEmpty()) {
            holder.fullname.setText(informations.getFullname());
        }

        if (s < 0) {
            holder.useraccount.setTextColor(Color.RED);
        } else if (s > 0) {
            holder.useraccount.setTextColor(context.getColor(R.color.x500));
        } else {
            holder.useraccount.setTextColor(context.getColor(R.color.orange));

        }

        CurrenciesInPrinterAdapter cursNameAdapter = new CurrenciesInPrinterAdapter(informations.getUsername(), loadCurrencies());
        cursNameAdapter.setWhere();
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(cursNameAdapter);
        cursNameAdapter.notifyDataSetChanged();


        holder.recCard.setOnClickListener(view -> {
            Intent intent = new Intent(context, PrintUserDetailsActivity.class);
            intent.putExtra("user", dataList.get(position).getUsername());
            intent.putExtra("sum", roundD(0));
            context.startActivity(intent);

        });

        holder.shapeableImageView.setOnClickListener(v -> showMenu(holder, position).show());

        holder.wa.setOnClickListener(v ->
                new AlertDialog.Builder(context)
                        .setMessage(dataList.get(position).getUid() + " هل انت متأكد من حذف " + "\n" + dataList.get(position).getUsername() + " " + exchanges[0])
                        .setNeutralButton(context.getString(R.string.save), (dialog, which) ->
                                removeItem(dataList.get(position).getUsername())
                        )
                        .setTitle(dataList.get(position).getUsername() + " حذف ")
                        .setNegativeButton(context.getString(R.string.cancel), (dialog, which) ->
                                dialog.dismiss())
                        .setIcon(R.drawable.dolar)
                        .show()
        );


    }

    @NonNull
    private PopupMenu showMenu(@NonNull UsersViewHolder holder, int position) {
        PopupMenu popupMenu = new PopupMenu(context, holder.shapeableImageView);
        popupMenu.getMenuInflater().inflate(R.menu.options, popupMenu.getMenu());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popupMenu.setForceShowIcon(true);
        }
        popupMenu.setGravity(Gravity.BOTTOM);

        popupMenu.setOnMenuItemClickListener(item -> {

            if (item.getItemId() == R.id.edito) {
                showEditingDialog(position);
            }
            if (item.getItemId() == R.id.printo) {
                holder.recCard.callOnClick();
            }
            if (item.getItemId() == R.id.cuto) {
                Intent intent = new Intent(context, CurrencyConversationActivity.class);
                intent.putExtra("user", dataList.get(position).getUsername());
                context.startActivity(intent);
            }
            if (item.getItemId() == R.id.rename) {
                renamez(position);
            }
            if (item.getItemId() == R.id.retype) {
                retype(position);
            }
            return true;
        });
        return popupMenu;
    }

    private void retype(int pos) {

        EditText editText = new EditText(context);
        Spinner spinner = new Spinner(context);
        BaseAdapter adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return 2;
            }

            @NonNull
            @Contract(pure = true)
            @Override
            public Object getItem(int position) {
                String x;
                if (position == 0) {
                    x = "prim";

                } else {
                    x = "seco";
                }
                return x;
            }

            @Override
            public long getItemId(int position) {
                return getItem(position).hashCode();
            }

            @NonNull
            @Contract(pure = true)
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {

                @SuppressLint("ViewHolder") View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_list_item, parent, false);
                TextView txtVw_DisplayName = view.findViewById(R.id.TxtVw_DisplayName);
                txtVw_DisplayName.setText(getItem(position).toString());

                return view;
            }
        };
        spinner.setAdapter(adapter);
        editText.setHint(dataList.get(pos).getType());

        spinner.setSelection(dataList.get(pos).getType().equals("prim") ? 0 : 1);
        // spinner.setOnItemClickListener((parent, view, position, id) -> {
        //     editText.setText(parent.getAdapter().getItem(position).toString());

        //  });
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setView(spinner)
                .setCancelable(false)
                .setIcon(R.drawable.baseline_auto_mode_24)
                .setNeutralButton("موافق", (dialog, which) ->
                        usersReference.child(dataList.get(pos).getUsername())
                                .child("type")
                                .setValue(spinner.getSelectedItem().toString()))
                .setNegativeButton("الغاء", (dialog, which) -> dialog.dismiss())
                .create();
        alertDialog.show();

    }

    private void renamez(int pos) {
        EditText editText = new EditText(context);
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setView(editText)
                .setCancelable(false)
                .setIcon(R.drawable.baseline_auto_mode_24)
                .setNeutralButton("موافق", (dialog, which) -> usersReference.child(dataList.get(pos).getUsername())
                        .child("fullname")
                        .setValue(editText.getText().toString()))
                .setNegativeButton("الغاء", (dialog, which) -> dialog.dismiss())
                .create();
        alertDialog.show();

    }

    /** @noinspection DataFlowIssue*/
    private void showEditingDialog(int pos) {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setIcon(R.drawable.dolar)
                .setMessage(context.getString(R.string.add_plus_count))
                .setCancelable(false)
                .setView(R.layout.editorx)
                .setNegativeButton(context.getString(R.string.cancel), (dialog, which) -> dialog.dismiss())
                .show();
        EditText newUser = alertDialog.findViewById(R.id.newUser);
        Spinner spinner = alertDialog.findViewById(R.id.spinnereto);
        assert spinner != null;
        CurrencyNameAdapter exAdapter = new CurrencyNameAdapter(context, null);
        spinner.setAdapter(exAdapter);

        ArrayList<String> cursName = new ArrayList<>(loadCurrencies().size());

        for (CurrencyDetails currencyDetails : loadCurrencies().values()) {
            cursName.add(currencyDetails.getName());
        }
        exAdapter.setBackupStrings(cursName);
        exAdapter.notifyDataSetChanged();


        Button okk = alertDialog.findViewById(R.id.okk);
        assert newUser != null;
        newUser.setHint(dataList.get(pos).getUid() + " " + dataList.get(pos).getUsername());
        assert okk != null;
        okk.setOnClickListener(v -> {
            String newAccount = newUser.getText().toString();
            double xx = doublito(newAccount);


            long yourmilliseconds = System.currentTimeMillis();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy");
            Date resultdate = new Date(yourmilliseconds);
            String time = String.valueOf(yourmilliseconds);
            String date = sdf.format(resultdate);

            Prog progF = new Prog(dataList.get(pos).getUsername(), "رأس المال", Math.abs(xx),
                    spinner.getSelectedItem().toString(),
                    0, 0, "رصيد معزز", "",
                    xx,
                    time, date);
            Prog progT = new Prog("رأس المال", getDataList().get(pos).getUsername(), Math.abs(xx),
                    spinner.getSelectedItem().toString(),
                    0, 0, "رصيد معزز", "",
                    -xx,
                    time, date);

            usersReference.child(getDataList().get(pos).getUsername())
                    .child("accounts")
                    .child(date)
                    .child(time)
                    .setValue(progF)
                    .addOnCompleteListener(runnable -> {
                        if (runnable.isSuccessful()) {

                            usersReference.child(getDataList().get(pos).getUsername())
                                    .child("account")
                                    .child(spinner.getSelectedItem().toString())
                                    .child("count").get().addOnSuccessListener(dataSnapshot1 -> {

                                        double count1 = 0.0d;
                                        if (dataSnapshot1.exists()) {
                                            count1 = dataSnapshot1.getValue(Double.class);
                                        }
                                        count1 += xx;
                                        usersReference.child(getDataList().get(pos).getUsername())
                                                .child("account")
                                                .child(spinner.getSelectedItem().toString())
                                                .child("count").setValue(count1);

                                        usersReference.child(getDataList().get(pos).getUsername())
                                                .child("all").get().addOnSuccessListener(command -> {

                                                    double count1x = 0.0d;
                                                    if (command.exists()) {
                                                        count1x = command.getValue(Double.class);
                                                    }
                                                    count1x += detectCurAllReturnDolar(spinner.getSelectedItem().toString(), xx);
                                                    usersReference.child(getDataList().get(pos).getUsername())
                                                            .child("all").setValue(count1x);
                                                });

                                    }).addOnFailureListener(e -> noti(activity, e.getLocalizedMessage()));

                            usersReference.child("رأس المال")
                                    .child("accounts")
                                    .child(date)
                                    .child(time)
                                    .setValue(progT).addOnSuccessListener(unused -> {

                                        usersReference.child("رأس المال")
                                                .child("account")
                                                .child(spinner.getSelectedItem().toString())
                                                .child("count").get().addOnSuccessListener(dataSnapshot1 -> {
                                                    double count1 = 0.0d;
                                                    if (dataSnapshot1.exists()) {
                                                        count1 = dataSnapshot1.getValue(Double.class);
                                                    }
                                                    count1 -= xx;
                                                    usersReference.child("رأس المال")
                                                            .child("account")
                                                            .child(spinner.getSelectedItem().toString())
                                                            .child("count").setValue(count1);
                                                });
                                        usersReference.child("رأس المال")
                                                .child("all").get().addOnSuccessListener(command -> {
                                                    double count1x = 0.0d;
                                                    if (command.exists()) {
                                                        count1x = command.getValue(Double.class);
                                                    }
                                                    count1x += detectCurAllReturnDolar(spinner.getSelectedItem().toString(), -xx);
                                                    usersReference.child("رأس المال")
                                                            .child("all").setValue(count1x);

                                                }).addOnFailureListener(e -> noti(activity, e.getLocalizedMessage()));


                                        noti(activity, " \n تمت اضافة" + "القيد" + " بنجاح");
                                        alertDialog.dismiss();
                                    });
                        } else {
                            if (runnable.getException() != null) {
                                noti(activity, runnable.getException().getMessage());
                                alertDialog.dismiss();
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        noti(activity, e.getMessage());
                        alertDialog.dismiss();
                    });
        });
    }

    private void removeItem(String username) {
        usersReference
                .child(username).removeValue((error, ref) ->
                {
                    if (error != null) {
                        noti(activity, error.getMessage());
                    } else {
                        noti(activity, "تم الحذف" + username + " بنجاح");
                    }
                });
    }

    @Override
    public int getItemCount() {


        return dataList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void searchDataList(ArrayList<Informations> searchList) {
        dataList = searchList;
        notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private void translateProfits(@NonNull Informations informations) {


        double all = informations.getAll();
        hta(usersReference, informations, -all, exchanges[0]);

    }

    /** @noinspection DataFlowIssue*/
    private void hta(@NonNull DatabaseReference reference, @NonNull Informations informations, double count, String cur) {

        long yourmilliseconds = System.currentTimeMillis();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy");
        Date resultdate = new Date(yourmilliseconds);

        String time = String.valueOf(yourmilliseconds);
        String date = sdf.format(resultdate);


        Prog progF = new Prog(informations.getUsername(), context.getString(R.string.profanddeprof),
                count,
                cur,
                0, 0, context.getString(R.string.profanddeprof),
                context.getString(R.string.profanddeprof),
                count,
                time, date);

        Prog progT = new Prog(informations.getUsername(), context.getString(R.string.profanddeprof),
                count,
                cur,
                0, 0, context.getString(R.string.profanddeprof),
                context.getString(R.string.profanddeprof),
                -count,
                time, date);


        reference.child(informations.getUsername())
                .child("accounts")
                .child(date)
                .child(time)
                .setValue(progF);

        reference.child(informations.getUsername())
                .child("account")
                .child(cur)
                .child("count").get().addOnSuccessListener(dataSnapshot1 -> {
                    double count1 = 0.0d;
                    if (dataSnapshot1.exists()) {
                        count1 = dataSnapshot1.getValue(Double.class);
                    }
                    count1 += count;
                    reference.child(informations.getUsername())
                            .child("account")
                            .child(cur).child("count").setValue(count1);

                }).addOnFailureListener(e -> noti(activity, e.getLocalizedMessage()));


        reference.child(informations.getUsername())
                .child("all").get().addOnSuccessListener(command -> {

                    double count1x = 0.0d;
                    if (command.exists()) {
                        count1x = command.getValue(Double.class);
                    }
                    count1x += count;
                    reference.child(informations.getUsername())
                            .child("all").setValue(count1x);
                }).addOnFailureListener(e -> noti(activity, e.getLocalizedMessage()));


        reference.child(context.getString(R.string.profanddeprof))
                .child("accounts")
                .child(date)
                .child(time)
                .setValue(progT);

        reference.child(context.getString(R.string.profanddeprof))
                .child("account")
                .child(cur)
                .child("count").get().addOnSuccessListener(dataSnapshot1 -> {
                    double count1 = 0.0d;
                    if (dataSnapshot1.exists()) {
                        count1 = dataSnapshot1.getValue(Double.class);
                    }
                    count1 -= count;
                    reference.child(context.getString(R.string.profanddeprof))
                            .child("account")
                            .child(cur).child("count").setValue(count1);
                }).addOnFailureListener(e -> noti(activity, e.getLocalizedMessage()));


        reference.child(context.getString(R.string.profanddeprof))
                .child("all").get().addOnSuccessListener(command -> {
                    double count1x = 0.0d;
                    if (command.exists()) {
                        count1x = command.getValue(Double.class);

                    }
                    count1x -= count;
                    reference.child(context.getString(R.string.profanddeprof))
                            .child("all").setValue(count1x);

                }).addOnFailureListener(e -> noti(activity, e.getLocalizedMessage()));


        noti(activity, context.getString(R.string.profanddeprof));


    }
}

