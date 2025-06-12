package io.github.iyado.accounterx.adapters;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import io.github.iyado.accounterx.R;
import io.github.iyado.accounterx.holders.CurrenciesAndCountsViewHolder;


public class CurrenciesAndCountsAdapter extends RecyclerView.Adapter<CurrenciesAndCountsViewHolder> {


    private ArrayList<String> mStrings;
    private TextView textViewx;
    private int key;
    private Dialog dialog;
    private CurrenciesInPrinterAdapter cursNameAdapter;

    public CurrenciesAndCountsAdapter(TextView textView, ArrayList<String> strings) {
        mStrings = strings;
        this.textViewx = textView;
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    public void setCursNameAdapter(CurrenciesInPrinterAdapter cursNameAdapter) {
        this.cursNameAdapter = cursNameAdapter;
    }

    public CurrenciesInPrinterAdapter getCursNameAdapter() {
        return cursNameAdapter;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTextViewx(TextView textViewx) {
        this.textViewx = textViewx;
        notifyDataSetChanged();
    }

    public Dialog getDialog() {
        return dialog;
    }

    @NonNull
    @Override
    public CurrenciesAndCountsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_item, parent, false);
        return new CurrenciesAndCountsViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull CurrenciesAndCountsViewHolder holder, int position) {
        String s = mStrings.get(position);
        holder.textView.setText(s);
        holder.textView.setOnClickListener(view ->
        {

            textViewx.setText(mStrings.get(position));
            if (getCursNameAdapter() != null) {
                getCursNameAdapter().setUser(mStrings.get(position));
                getCursNameAdapter().notifyDataSetChanged();
            }
            getDialog().dismiss();
        });

    }


    @Override
    public long getItemId(int position) {
        return mStrings.get(position).hashCode();
    }

    @Override
    public int getItemCount() {
        return mStrings.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setBackupStrings(ArrayList<String> mBackupStrings) {
        mStrings = mBackupStrings;
        notifyDataSetChanged();
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }
}
