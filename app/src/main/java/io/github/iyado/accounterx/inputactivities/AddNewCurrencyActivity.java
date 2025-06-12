package io.github.iyado.accounterx.inputactivities;


import static io.github.iyado.accounterx.AccounterApplication.noti;
import static io.github.iyado.accounterx.MainActivity.curReference;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.github.dhaval2404.colorpicker.ColorPickerDialog;
import com.github.dhaval2404.colorpicker.model.ColorShape;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

import io.github.iyado.accounterx.databinding.AddCurrencyBinding;
import io.github.iyado.accounterx.utils.CurrencyDetails;
import io.github.iyado.accounterx.R;


public class AddNewCurrencyActivity extends AppCompatActivity {
    EditText nameEditText,codeEditText,evaluationPriceEditText;
    Spinner operatorSpinner;
    Button saveButton,cancelButton;
    Button colorPickerButton;
    ArrayList<CurrencyDetails> currencyDetailslist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_currency);

        nameEditText = findViewById(R.id.nameEditText);
        codeEditText = findViewById(R.id.codeEditText);
        operatorSpinner = findViewById(R.id.operatorSpinner);
        evaluationPriceEditText = findViewById(R.id.evaluationPriceEditText);

        saveButton = findViewById(R.id.saveButton);
        cancelButton = findViewById(R.id.cancelButton);
        colorPickerButton = findViewById(R.id.colorPickerButton);

        currencyDetailslist = new ArrayList<>();
        initializeSpinner();

        colorPickerButton.setOnClickListener(v -> showColorPicker());
        saveButton.setOnClickListener(v -> saveCurrency());
        cancelButton.setOnClickListener(v -> finish());
    }

    private void saveCurrency() {
        String name = nameEditText.getText().toString();
        String code = codeEditText.getText().toString();
        String operator = operatorSpinner.getSelectedItem().toString();
        String evaluationPrice = evaluationPriceEditText.getText().toString();
        String color = colorPickerButton.getText().toString();
        curReference
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            System.out.println(dataSnapshot.getValue(CurrencyDetails.class));
                            CurrencyDetails currencyDetails = dataSnapshot.getValue(CurrencyDetails.class);
                            assert currencyDetails != null;
                            System.out.println(currencyDetails.getName());
                            currencyDetailslist.add(currencyDetails);

                        }

                        CurrencyDetails currencyDetails = new CurrencyDetails(currencyDetailslist.size(),name,code,operator,Double.parseDouble(evaluationPrice),"R.drawable.flag_egypt",color);
                        curReference.child(name).setValue(currencyDetails);
                        finish();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        noti(AddNewCurrencyActivity.this,error.getMessage());

                    }
                });



    }

    private void initializeSpinner() {
        String[] operators = {"/", "*"};
        operatorSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, operators));
        operatorSpinner.setSelection(0);
        operatorSpinner.setPrompt(getString(R.string.operator));
    }

    private void showColorPicker() {
        new ColorPickerDialog.Builder(this)
                .setTitle(getString(R.string.choose_color))
                .setColorShape(ColorShape.CIRCLE)
                .setColorListener((color, hex) -> {
                   colorPickerButton.setBackgroundColor(Color.parseColor(hex));
                   colorPickerButton.setText(hex);

                })
                .setPositiveButton(R.string.save)
                .setNegativeButton(R.string.cancel)
                .setDefaultColor(R.color.primaryTextColor)
                .show();

    }


}
