package io.github.iyado.accounterx.holders;


import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import io.github.iyado.accounterx.R;


public class UsersViewHolder extends RecyclerView.ViewHolder {

        public final TextView userid;
        public final TextView username;
        public final TextView fullname;
        public final TextView useraccount;
        public final ShapeableImageView shapeableImageView;
        public final MaterialCardView recCard;
        public final LinearLayout linearLayout;
        public final ImageView wa;
        public final Button translate;
        public final RecyclerView recyclerView;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);

            recCard = itemView.findViewById(R.id.recCard);
            username = itemView.findViewById(R.id.useridd);
            useraccount = itemView.findViewById(R.id.usernamed);
            userid = itemView.findViewById(R.id.useraccountd);
            shapeableImageView = itemView.findViewById(R.id.recImage);


            wa = itemView.findViewById(R.id.wa);
            fullname = itemView.findViewById(R.id.fulla);
            translate = itemView.findViewById(R.id.trhil);
            linearLayout = itemView.findViewById(R.id.linearLayout2);
            recyclerView = itemView.findViewById(R.id.listViewx);
        }
}
