package io.github.iyado.accounterx;


import static io.github.iyado.accounterx.AccounterApplication.noti;
import static io.github.iyado.accounterx.utils.AllInOne.loadCurrencies;
import static io.github.iyado.accounterx.MainActivity.roundD;
import static io.github.iyado.accounterx.MainActivity.usersReference;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import io.github.iyado.accounterx.adapters.CurrencyNameAdapter;
import io.github.iyado.accounterx.adapters.CurrenciesInPrinterAdapter;
import io.github.iyado.accounterx.adapters.PrintInlineAdapter;
import io.github.iyado.accounterx.adapters.UserAccountsAdapter;
import io.github.iyado.accounterx.adapters.CurrenciesAndCountsAdapter;
import io.github.iyado.accounterx.utils.CurrencyDetails;
import io.github.iyado.accounterx.utils.DateSelector;
import io.github.iyado.accounterx.utils.Prog;
import io.github.iyado.accounterx.utils.ScreenShot;


public class PrintUserDetailsActivity extends AppCompatActivity {


    @NonNull
    public static CurrencyNameAdapter initCnameAdapter(@NonNull AppCompatActivity context) {

        ArrayList<String> xCnames = new ArrayList<>();
        CurrencyNameAdapter xCnameAdapter = new CurrencyNameAdapter(context, xCnames);

        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    xCnames.add(dataSnapshot.getKey());
                }

                if (xCnames.isEmpty()){
                    xCnames.add(
                            "no users found....."
                    );
                }
                xCnameAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                noti(context,error.getMessage());
            }
        });


        return xCnameAdapter;
    }



    public static final String TAGx = "*السلام عليكم ورحمة الله وبركاته*\n" +
            "✨✨✨✨✨✨✨✨✨\n" +
            "*مطابقة الأرصدة-  إدارة أموالي* \n" +
            "\n" +
            "*الحساب:";


    @SuppressWarnings("unused")
    public static final String TAGy =
            "\n" +
            "*تاريخ المطابقة\uD83D\uDD50*\n";
    @SuppressWarnings("unused")
    public static final String TAGz =
            "\n" +
            "✨✨✨✨✨✨✨✨✨\n" +
            "\n" +
            " *\uD83C\uDDF9\uD83C\uDDF7";
    @SuppressWarnings("unused")

    public static final String l1 = "*\n" +
            " \n" +
            "\n" +
            "  *\uD83C\uDDFA\uD83C\uDDF8";
    @SuppressWarnings("unused")
    public static final String d1 = "*\n" +
            " \n" +
            "\n" +
            "  *⚖️⚖️ الرصيد المقوم ⚖️⚖️* \n" +
            "\n" +
            "  *《 ";
    public static final String dl = "》\uD83D\uDCB2* \n" +
            "✨✨✨✨✨✨✨✨✨   \n" +
            "             \n" +
            "*يرجى الرد على المطابقة وتأكيدها*\n" +
            "*مع فائق الاحترام والتقدير\uD83C\uDF39*\n";


    public BottomNavigationView bottomNavigationView;
    RecyclerView recyclerView;
    CurrencyNameAdapter cnameAdapter;
    TextView
            sum;
    public TextView accountux;
    ArrayList<Prog> progs;
    UserAccountsAdapter printerViewAdapter;
    ArrayList<Prog> progME;
    TextView textView11;
    String s;
    private Spinner spinnercur;
    private MaterialTextView textView;
    private RecyclerView inlineRecyclerView;
    CurrenciesInPrinterAdapter cursNameAdapter;

    public LinearLayout screenShot;
    public CurrenciesAndCountsAdapter adapterF;
    public ArrayList<String> cnames;
    public TextInputLayout fx,tx;
   public CoordinatorLayout coordinatorLayout;
    public AppBarLayout appBarLayout;
    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd,yyyy");
    HashMap<Integer, CurrencyDetails> hashMapx;


    @SuppressLint({"NonConstantResourceId", "NotifyDataSetChanged"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_x);

        recyclerView = findViewById(R.id.rec);
        sum = findViewById(R.id.sum);

        fx = findViewById(R.id.datef);
        tx = findViewById(R.id.datet);

        spinnercur = findViewById(R.id.spinnercur);
        textView = findViewById(R.id.spinnerusers);

        textView11 = findViewById(R.id.textView11);
        SearchView searchView = findViewById(R.id.searchprint);

        coordinatorLayout = findViewById(R.id.coordinator);
        appBarLayout = findViewById(R.id.appBarLayout);
        accountux = findViewById(R.id.accountux);
        screenShot = findViewById(R.id.screenShot);


        bottomNavigationView = findViewById(R.id.bottomnavp);
        inlineRecyclerView = findViewById(R.id.inlinerecd);

        hashMapx = new HashMap<>();


        if (getIntent() != null) {
            s = getIntent().getStringExtra("user");
        }

        initView();


        if (s != null) {
            textView.setText((s));
            cnameAdapter.notifyDataSetChanged();
            loadUserInfo(s, spinnercur.getSelectedItem().toString());
            loadAll(s);
            cursNameAdapter.setUser(s);
            cursNameAdapter.notifyDataSetChanged();

        }



        bottomNavigationView.setOnItemSelectedListener(item ->{
            int id = item.getItemId();
                    if (id == R.id.printx) {
                        copyingAccount();
                    }
                    if (id == R.id.takeScreen) {
                        new ScreenShot(this,this).takeScreenShot(screenShot);
                    }
                    if (id == R.id.kshf) {
                        loadUserInfo(textView.getText().toString(),spinnercur.getSelectedItem().toString());
                        loadAll(textView.getText().toString());
                        cursNameAdapter.setUser(textView.getText().toString());
                        cursNameAdapter.notifyDataSetChanged();
                    }

            return true;
                });



        accountux.setOnClickListener(v -> copyingAccount());


        sum.setOnClickListener(v -> {setCustomerTheme(0);recreate();});

        sum.setOnLongClickListener(v -> {
             setCustomerTheme(1);
             recreate();
             return true;
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });

        textView.setOnClickListener(v -> searcho(textView, cnames));

        fx.setOnClickListener(v -> new DateSelector(fx).show(getSupportFragmentManager(),"from"));
        tx.setOnClickListener(v -> new DateSelector(tx).show(getSupportFragmentManager(),"to"));

        Objects.requireNonNull(fx.getEditText()).setOnClickListener(v -> new DateSelector(fx).show(getSupportFragmentManager(),"from"));
        Objects.requireNonNull(tx.getEditText()).setOnClickListener(v -> new DateSelector(tx).show(getSupportFragmentManager(),"to"));

    }

    private void loadAll(String user){

        usersReference.child(user).child("all")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue() == null) return;
                        //noinspection DataFlowIssue
                        double sums = snapshot.getValue(Double.class);
                        sum.setText(String.valueOf(sums));
                        if (sums > 0){
                            textView11.setText(" مدين لنا");
                            textView11.setTextColor(Color.GREEN);
                            sum.setTextColor(Color.GREEN);
                        }else if (sums < 0){
                            textView11.setText(" دائن علينا");
                            textView11.setTextColor(Color.RED);
                            sum.setTextColor(Color.RED);
                        }else {
                            textView11.setText(" الحساب 0 ");
                            textView11.setTextColor(getColor(R.color.orange));
                            sum.setTextColor(getColor(R.color.orange));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }



    @SuppressLint("SimpleDateFormat")
    private void copyingAccount() {

        StringBuilder hellox = new StringBuilder();
        cursNameAdapter.addUserCurrenciesCounts(counts -> {

           for (int i = 0; i < counts.length;i++) {
               hellox.append(roundD(counts[i])).append(" ").append(Objects.requireNonNull(cursNameAdapter.currencyDetailsList.get(Objects.requireNonNull(cursNameAdapter.currencyDetailsList.get(i)).getId())).getName()).append("\n");
           }
       });

        new SimpleDateFormat("hh:mm aa \n MM/dd,yyyy").format(new Date(System.currentTimeMillis()));

        String forus = "مدين لنا";
        String onus = "دائن علينا";

        String all;
        if (!sum.getText().toString().contains("-")){
            all = sum.getText().toString() +" "+ forus;
        }else {
            all = sum.getText().toString() +" "+ onus;
        }

        String hello = TAGx + textView.getText().toString() + "*";
        String hex =   all + dl + "\n";

        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(ClipData.newPlainText("account",hello+"\n"+ hellox+"\n" +hex));
        noti(this,"تم النسخ");

    }

    private void setCustomerTheme(int i) {
        saveTheme(this,i == 0);
    }
    public static void saveTheme(@NonNull Context context, boolean isThemed){

        SharedPreferences sharedPreferences = context.getSharedPreferences("theme",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isThemed",isThemed);
        editor.apply();
    }


    @SuppressLint("NotifyDataSetChanged")
    private void searchList(String newText) {
        ArrayList<Prog> searchList = new ArrayList<>();
        for (Prog prog: progME){
            if (prog.getCustomer().toLowerCase().contains(newText.toLowerCase()) ||
                    prog.getSender().toLowerCase().contains(newText.toLowerCase()) ||
                    prog.getReciever().toLowerCase().contains(newText.toLowerCase()) ||
                    String.valueOf(prog.getSumAll()).contains(newText.toLowerCase()) ||
                    prog.getEx().toLowerCase().contains(newText.toLowerCase())){
                searchList.add(prog);
            }
        }
        printerViewAdapter.setProgs(searchList);
        printerViewAdapter.notifyDataSetChanged();
    }




    @SuppressLint("NotifyDataSetChanged")
    private void initView(){

        Objects.requireNonNull(fx.getEditText()).setText(simpleDateFormat.format(new Date(System.currentTimeMillis())));
        Objects.requireNonNull(tx.getEditText()).setText(simpleDateFormat.format(new Date(System.currentTimeMillis())));
        cnameAdapter = initCnameAdapter(this);
        cnames = cnameAdapter.getCnames();
        new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cnameAdapter.getCnames());
        CurrencyNameAdapter exAdapter = new CurrencyNameAdapter(this,null);
        ArrayList<String> ex = new ArrayList<>();
        for (CurrencyDetails currencyDetails : loadCurrencies().values()){
            ex.add(currencyDetails.getName());
        }
        exAdapter.setBackupStrings(ex);

        spinnercur.setAdapter(exAdapter);

        progs = new ArrayList<>();
        progME = new ArrayList<>();
        printerViewAdapter = new UserAccountsAdapter(this,this,"",progs);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(printerViewAdapter);
        if (textView.getText()!= null) {
            loadUserInfo(textView.getText().toString(),spinnercur.getSelectedItem().toString());

        }

        PrintInlineAdapter printInlineAdapter = new PrintInlineAdapter(s, loadCurrencies());
        cursNameAdapter = new CurrenciesInPrinterAdapter(s, loadCurrencies());
        cursNameAdapter.setUser(s);
        inlineRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        inlineRecyclerView.setAdapter(cursNameAdapter);
        printInlineAdapter.notifyDataSetChanged();

    }

    public void searcho(TextView search,ArrayList<String> arrayList){
        adapterF = new CurrenciesAndCountsAdapter(search,arrayList);
        Dialog alertDialog = new Dialog(this);
        alertDialog.setContentView(R.layout.dialog_x);
        alertDialog.setCancelable(true);
        Window window = alertDialog.getWindow();
        if (window != null) {
            window.setLayout(getWindow().getAttributes().width, getWindow().getAttributes().height);

            window.setNavigationBarColor(Color.RED);
        }
        alertDialog.show();
        adapterF.setDialog(alertDialog);
        SearchView searchView = alertDialog.findViewById(R.id.serx);
        RecyclerView listView = alertDialog.findViewById(R.id.listax);

        listView.setLayoutManager(new LinearLayoutManager(alertDialog.getContext()));
        listView.setAdapter(adapterF);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                ArrayList<String> searchString = new ArrayList<>();
                for (String string : arrayList){
                    if (string.toLowerCase().contains(newText.toLowerCase())){
                        searchString.add(string);
                    }
                }
                adapterF.setBackupStrings(searchString);

                return true;
            }
        });


    }


    @SuppressLint("NotifyDataSetChanged")
    private void loadUserInfo(String user, String cur) {

        printerViewAdapter.setUser(user);
        usersReference
                .child(user)
                .child("accounts")
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        progs.clear();
                        progME.clear();
                        // progME = new ArrayList<>();
                        String s ;
                        String t;
                        for (DataSnapshot dates : snapshot.getChildren()) {

                            for (DataSnapshot keys : dates.getChildren()) {
                                progME.add(keys.getValue(Prog.class));
                                Prog prog = keys.getValue(Prog.class);

                                assert prog != null;
                                s = prog.getEx();
                                t = prog.getDate();

                                Date date;
                                try {
                                    date = simpleDateFormat.parse(t);
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }


                                if (s.equals(cur)) {
                                    try {
                                        assert date != null;
                                        if (date.getTime() >= Objects.requireNonNull(simpleDateFormat.parse(Objects.requireNonNull(fx.getEditText()).getText().toString())).getTime() && date.getTime() <= Objects.requireNonNull(simpleDateFormat.parse(Objects.requireNonNull(tx.getEditText()).getText().toString())).getTime()) {
                                            progs.add(prog);
                                        }
                                    } catch (ParseException e) {
                                        throw new RuntimeException(e);
                                    }
                                }

                            }
                        }
                        printerViewAdapter.notifyDataSetChanged();


                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        printerViewAdapter.notifyDataSetChanged();
                    }
                });


        setTitle(getString(R.string.balance_matching_for_mr) + " " +user + "\n");



    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }

}
