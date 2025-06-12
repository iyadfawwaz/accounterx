package io.github.iyado.accounterx;


import static io.github.iyado.accounterx.MainActivity.usersReference;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Objects;
import io.github.iyado.accounterx.adapters.UserAccountsAdapter;
import io.github.iyado.accounterx.utils.Prog;


public class RestrictionPrintActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    UserAccountsAdapter printerViewAdapter;
    ArrayList<Prog> arrayList;
    TextView numberOfRess;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_restrictions);
        recyclerView = findViewById(R.id.recress);
        numberOfRess = findViewById(R.id.textView16);
       initView();
        loadAll();
        numberOfRess.setText(printerViewAdapter.getItemCount()+"");


    }

    private void initView() {
     arrayList = new ArrayList<>();
     printerViewAdapter = new UserAccountsAdapter(this,this,"",arrayList);
     printerViewAdapter.setTYPE(1);
     LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
     linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
     linearLayoutManager.setReverseLayout(true);
     linearLayoutManager.setStackFromEnd(true);
     recyclerView.setLayoutManager(linearLayoutManager);

     recyclerView.setAdapter(printerViewAdapter);
    }

    private void loadAll() {
        usersReference.orderByKey().addValueEventListener(new ValueEventListener() {
            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot user: snapshot.getChildren()){

                    for (DataSnapshot account : user.child("accounts").getChildren()){

                        for (DataSnapshot prog: account.getChildren()){
                            arrayList.add(prog.getValue(Prog.class));
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                arrayList.sort((o1, o2) -> {
                                    if (o1.getKey().equals(o2.getKey())) {
                                        return 0;
                                    }
                                    return o1.getKey().compareTo(o2.getKey());
                                });
                            }

                            printerViewAdapter.setUser(Objects.requireNonNull(user.getKey()));
                        }

                    }
                }

                numberOfRess.setText(printerViewAdapter.getItemCount()+"");
                    printerViewAdapter.notifyDataSetChanged();
                }

                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                   printerViewAdapter.notifyDataSetChanged();

            }
        });

    }
}
