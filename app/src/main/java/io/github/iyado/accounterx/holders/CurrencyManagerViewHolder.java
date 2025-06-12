package io.github.iyado.accounterx.holders;


import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import io.github.iyado.accounterx.R;


public class CurrencyManagerViewHolder extends RecyclerView.ViewHolder{

    public final TextView saar;
    public final TextView moaaml;
    public final TextView rmz;
    public final TextView asm;
    public final TextView rqm;
    public final View view;
    public final LinearLayout layout;
    public final ConstraintLayout constraintLayout1;

    public CurrencyManagerViewHolder(@NonNull View itemView) {
        super(itemView);

        saar = itemView.findViewById(R.id.saar);
        moaaml = itemView.findViewById(R.id.moaaml);
        rmz = itemView.findViewById(R.id.rmz);
        asm = itemView.findViewById(R.id.asm);
        rqm = itemView.findViewById(R.id.rqm);
        layout = itemView.findViewById(R.id.linearLayout10);
        constraintLayout1 = itemView.findViewById(R.id.constraintLayout1);
        view = itemView;

    }
}
