package io.github.iyado.accounterx.adapters;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import io.github.iyado.accounterx.R;
import io.github.iyado.accounterx.holders.CurrenciesAndCountsViewHolder;


public class LocalSpinnerAdapter extends RecyclerView.Adapter<CurrenciesAndCountsViewHolder> {

    private ArrayList<String> mStrings;
    private int key;
    private Dialog dialog;

    public LocalSpinnerAdapter( ArrayList<String> strings) {
        mStrings = strings;
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    public Dialog getDialog() {
        return dialog;
    }

    @NonNull
    @Override
    public CurrenciesAndCountsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_item,parent,false);
        return new CurrenciesAndCountsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrenciesAndCountsViewHolder holder, int position) {
        String s = mStrings.get(position);
        holder.textView.setText(s);
        holder.textView.setOnClickListener(view ->
        {

            holder.textView.setText(mStrings.get(position));
            getDialog().dismiss();
            //reset(mStrings);
        });

    }
    public void reset(ArrayList<String> stringArrayList){
        mStrings = stringArrayList;
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

    public void setKey(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }
}
