package io.github.iyado.accounterx.holders;


import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import io.github.iyado.accounterx.R;


public class PrintAdapterViewHolder extends RecyclerView.ViewHolder {

    public final TextView notice;
    public final TextView count;
    public final TextView ex;
    public final TextView total;
    public final TextView dates;
    public final TextView client;
    public final CardView recCard;
    public final ImageView imageView;
    public final CheckBox checkBox;

    public PrintAdapterViewHolder(@NonNull View itemView) {
        super(itemView);

        recCard = itemView.findViewById(R.id.cCard);
        notice = itemView.findViewById(R.id.noticeu);
        ex = itemView.findViewById(R.id.exu);
        count = itemView.findViewById(R.id.accountu);
        imageView = itemView.findViewById(R.id.imageView2);
        total = itemView.findViewById(R.id.total);
        checkBox = itemView.findViewById(R.id.checkBox);
        dates = itemView.findViewById(R.id.total2);
        client = itemView.findViewById(R.id.noticeu2);
    }

}
