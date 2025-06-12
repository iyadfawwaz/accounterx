package io.github.iyado.accounterx.adapters;


import static io.github.iyado.accounterx.MainActivity.usersReference;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.Objects;

import io.github.iyado.accounterx.methods.GetUserCurrenciesCounts;
import io.github.iyado.accounterx.R;
import io.github.iyado.accounterx.utils.SingleCurrency;
import io.github.iyado.accounterx.utils.CurrencyDetails;


public class CurrenciesInPrinterAdapter extends RecyclerView.Adapter<CurrenciesInPrinterAdapter.CursViewHolder> {

    public final HashMap<Integer, CurrencyDetails> currencyDetailsList;
    private String user;
    private final double[] counts;

    public CurrenciesInPrinterAdapter(String user, @NonNull HashMap<Integer, CurrencyDetails> currencyDetailsList) {
        this.user = user;
        this.currencyDetailsList = currencyDetailsList;
        this.counts = new double[currencyDetailsList.size()];

    }


    public void addUserCurrenciesCounts(@NonNull GetUserCurrenciesCounts getUserCurrenciesCounts) {
        getUserCurrenciesCounts.getCount(counts);
    }

    public void setUser(String user) {
        this.user = user;
    }

    /** @noinspection ClassEscapesDefinedScope*/
    @NonNull
    @Override
    public CursViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.print_inline, parent, false);
        return new CursViewHolder(view);
    }

    /** @noinspection ClassEscapesDefinedScope*/
    @SuppressLint("SuspiciousIndentation")
    @Override
    public void onBindViewHolder(@NonNull CursViewHolder holderx, @SuppressLint("RecyclerView") int position) {


        String currency = Objects.requireNonNull(currencyDetailsList.get(Objects.requireNonNull(currencyDetailsList.get(position)).getId())).getName();
        holderx.curx.setText(currency);
        if (Objects.requireNonNull(currencyDetailsList.get(Objects.requireNonNull(currencyDetailsList.get(position)).getId())).getColor() != null) {

            holderx.curx.setTextColor(Color.parseColor(Objects.requireNonNull(currencyDetailsList.get(Objects.requireNonNull(currencyDetailsList.get(position)).getId())).getColor()));
        }
        //noinspection SuspiciousIndentAfterControlStatement
        if (user != null && currency != null)
        usersReference.child(user).child("account")
                        .child(currency)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @SuppressLint("NotifyDataSetChanged")
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        if (!snapshot.exists()) {
                                            counts[position] = 0.0d;
                                            holderx.countx.setText(String.valueOf(counts[position]));
                                            return;
                                        }
                                        SingleCurrency singleCurrency = snapshot.getValue(SingleCurrency.class);
                                        assert singleCurrency != null;
                                        singleCurrency.setCurrencyName(currency);
                                        counts[position] = singleCurrency.getCount();
                                        holderx.countx.setText(String.valueOf(counts[position]));
                                        if (singleCurrency.getCount() < 0) {
                                            holderx.countx.setTextColor(holderx.countx.getContext().getColor(R.color.red));
                                        }
                                        if (singleCurrency.getCount() == 0) {
                                            holderx.countx.setTextColor(holderx.countx.getContext().getColor(R.color.orange));
                                        }
                                        if (singleCurrency.getCount() > 0) {
                                            holderx.countx.setTextColor(holderx.countx.getContext().getColor(R.color.x400));
                                        }
                                    }

                                    @SuppressLint("NotifyDataSetChanged")
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        holderx.countx.setText(error.getMessage());
                                    }
                                });

    }

    @Override
    public int getItemCount() {
        return currencyDetailsList.size();
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setWhere() {
    }

    static class CursViewHolder extends RecyclerView.ViewHolder {


        final TextView countx;
        final TextView curx;
        final LinearLayout x;

        public CursViewHolder(@NonNull View itemView) {
            super(itemView);
            countx = itemView.findViewById(R.id.sumdx);
            curx   = itemView.findViewById(R.id.areax);
            x      = itemView.findViewById(R.id.x);
        }
    }



}
