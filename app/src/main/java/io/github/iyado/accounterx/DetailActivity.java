package io.github.iyado.accounterx;


import static io.github.iyado.accounterx.AccounterApplication.noti;
import static io.github.iyado.accounterx.MainActivity.roundD;
import static io.github.iyado.accounterx.MainActivity.usersReference;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

import io.github.iyado.accounterx.utils.Informations;


public class DetailActivity extends AppCompatActivity {

    TextView detailDesc, detailTitle, detailLang,textView17;
    ImageView detailImage;
    FloatingActionButton deleteButton;
    private List<Informations> users;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_FULLSCREEN);

        WindowInsetsControllerCompat windowInsetsControllerCompat = WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());

        windowInsetsControllerCompat.hide(WindowInsetsCompat.Type.systemBars());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detials);

        detailDesc = findViewById(R.id.detailDesc);
        detailImage = findViewById(R.id.detailImage);
        detailTitle = findViewById(R.id.detailTitle);
        deleteButton = findViewById(R.id.deleteButton);
        detailLang = findViewById(R.id.detailLang);
        textView17 = findViewById(R.id.textView17);

        users = new ArrayList<>();

        deleteButton.setOnClickListener(view -> new Profitx());
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        textView17.setText(currentUser.getUid()+"\n"+"\n"+currentUser.getEmail());

    }

    private class Profitx {

        public Profitx() {
            loadProfit();
        }

        @SuppressLint("SetTextI18n")
        private void loadProfit() {

            usersReference.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            users.clear();
                            for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                                Informations informations = itemSnapshot.getValue(Informations.class);

                                if (informations != null) {
                                    informations.setUsername(itemSnapshot.getKey());
                                }
                                users.add(informations);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            noti(DetailActivity.this, error.getMessage());
                        }
                    });

            double m0 = 0.0d;
            double mx0 = 0.0d;

            for (Informations informations : users) {
                if (informations.getType() != null && !informations.getType().trim().isEmpty()) {
                    if (informations.getType().equals("prim")) {
                        m0 += informations.getAll();
                    }
                    if (informations.getType().equals("sec")) {
                        mx0 += informations.getAll();
                    }
                }
            }
            detailLang.setText(roundD(m0+mx0 )+ "");
            if (roundD(m0+mx0 ) >= 0) {
                detailLang.setTextColor(Color.GREEN);
            } else {
                detailLang.setTextColor(Color.RED);
            }
        }
    }
}
