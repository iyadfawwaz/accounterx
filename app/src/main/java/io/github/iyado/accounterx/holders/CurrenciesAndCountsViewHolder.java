package io.github.iyado.accounterx.holders;


import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import io.github.iyado.accounterx.R;


public class CurrenciesAndCountsViewHolder extends RecyclerView.ViewHolder{

        public final CardView cardView;
        public final TextView textView;
        public CurrenciesAndCountsViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.c1);
            textView = itemView.findViewById(R.id.TxtVw_DisplayName);

        }
    
}
