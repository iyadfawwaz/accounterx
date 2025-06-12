package io.github.iyado.accounterx.adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.github.dhaval2404.colorpicker.ColorPickerDialog;
import com.github.dhaval2404.colorpicker.model.ColorShape;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;
import java.util.Objects;
import io.github.iyado.accounterx.holders.CurrencyManagerViewHolder;
import io.github.iyado.accounterx.R;
import io.github.iyado.accounterx.utils.CurrencyDetails;


public class CurrencyManagerAdapter extends RecyclerView.Adapter<CurrencyManagerViewHolder>{

    EditText nameEditText,codeEditText,evaluationPriceEditText;
    Spinner operatorSpinner;
    Button saveButton,cancelButton;
    Button colorPickerButton;

    private final Context mContext;
    public final List<CurrencyDetails> mCurrencyDetails;
    public final DatabaseReference cursReference;

    public CurrencyManagerAdapter(Context mContext, List<CurrencyDetails> mCurrencyManager) {
        this.mContext = mContext;
        this.mCurrencyDetails = mCurrencyManager;
        cursReference = FirebaseDatabase.getInstance().getReference()
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("currencies");
    }

    @NonNull
    @Override
    public CurrencyManagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.currency_manager,parent,false);
        return new CurrencyManagerViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull CurrencyManagerViewHolder holder, int position) {


        CurrencyDetails currencyDetails = mCurrencyDetails.get(position);
        holder.rqm.setText(String.valueOf(currencyDetails.getId()));
        holder.asm.setText(currencyDetails.getName());
        holder.rmz.setText(currencyDetails.getCode());
        holder.moaaml.setText(currencyDetails.getSymbol());
        holder.saar.setText(String.valueOf(currencyDetails.getRate()));
        holder.asm.setTextColor(Color.parseColor(currencyDetails.getColor()));
        holder.rmz.setTextColor(Color.parseColor(currencyDetails.getColor()));
        holder.moaaml.setTextColor(Color.parseColor(currencyDetails.getColor()));
        holder.saar.setTextColor(Color.parseColor(currencyDetails.getColor()));
        holder.rqm.setTextColor(Color.parseColor(currencyDetails.getColor()));
        holder.view.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(currencyDetails.getColor())));
        holder.constraintLayout1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(currencyDetails.getColor())));
        holder.constraintLayout1.setBackgroundColor(Color.parseColor(currencyDetails.getColor()));

        holder.view.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
            .setTitle(holder.itemView.getContext().getString(R.string.edit_currency))
            .setMessage(holder.itemView.getContext().getString(R.string.sure))
            .setCancelable(false)
            .setNeutralButton(holder.itemView.getContext().getString(R.string.delete), (dialog, which) -> cursReference.child(currencyDetails.getName()).removeValue())
            .setView(R.layout.add_currency)
            .setPositiveButton(holder.itemView.getContext().getString(R.string.save), (dialog, which) -> {
               DatabaseReference cur =  cursReference.child(currencyDetails.getName());
                        cur.child("name").setValue(nameEditText.getText().toString());
                        cur.child("code").setValue(codeEditText.getText().toString());
                        cur.child("rate").setValue(Double.parseDouble(evaluationPriceEditText.getText().toString()));
                        cur.child("symbol").setValue(operatorSpinner.getSelectedItem().toString());
                        cur.child("color").setValue(colorPickerButton.getText().toString());
                        dialog.dismiss();

            });
            builder.setNegativeButton(holder.itemView.getContext().getString(R.string.cancel), (dialog, which) -> {
                dialog.dismiss();
                notifyDataSetChanged();
                });

            AlertDialog dialog = builder.create();

            dialog.show();
            saveButton = dialog.findViewById(R.id.saveButton);
            cancelButton = dialog.findViewById(R.id.cancelButton);
            saveButton.setVisibility(View.GONE);
            cancelButton.setVisibility(View.GONE);
            colorPickerButton = dialog.findViewById(R.id.colorPickerButton);
            assert colorPickerButton != null;
            colorPickerButton.setTextColor(Color.parseColor(currencyDetails.getColor()));
            colorPickerButton.setText(currencyDetails.getColor());
            colorPickerButton.setOnClickListener(vx -> new ColorPickerDialog.Builder(mContext)
                    .setTitle(holder.itemView.getContext().getString(R.string.choose_color))
                    .setColorShape(ColorShape.CIRCLE)
                    .setColorListener((color, hex) -> {
                        colorPickerButton.setBackgroundColor(Color.parseColor(hex));
                        colorPickerButton.setText(hex);
                        currencyDetails.setColor(hex);
                    }).setPositiveButton(holder.itemView.getContext().getString(R.string.save)).setNegativeButton(holder.itemView.getContext().getString(R.string.cancel)).setDefaultColor(R.color.primaryTextColor).show());


            operatorSpinner = dialog.findViewById(R.id.operatorSpinner);
            assert operatorSpinner != null;
            operatorSpinner.setPrompt( holder.itemView.getContext().getString(R.string.operator));
            String[] operators = {"/", "*"};
            operatorSpinner.setAdapter(new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, operators));

            if(currencyDetails.getSymbol().equals("/")){
                operatorSpinner.setSelection(0);
            }else{
                operatorSpinner.setSelection(1);
            }

            nameEditText = dialog.findViewById(R.id.nameEditText);
            codeEditText = dialog.findViewById(R.id.codeEditText);
            evaluationPriceEditText = dialog.findViewById(R.id.evaluationPriceEditText);

            nameEditText.setText(currencyDetails.getName());
            codeEditText.setText(String.valueOf(currencyDetails.getCode()));
            evaluationPriceEditText.setText(String.valueOf(currencyDetails.getRate()));

        });


    }

    @Override
    public int getItemCount() {
        return mCurrencyDetails.size();
    }
}
