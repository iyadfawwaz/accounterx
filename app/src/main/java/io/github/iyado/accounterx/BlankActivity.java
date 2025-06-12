package io.github.iyado.accounterx;


import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;


public class BlankActivity extends AppCompatActivity {
    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser()!=null){
            Intent m = new Intent(this,MainActivity.class);
            m.putExtra("cname",FirebaseAuth.getInstance().getCurrentUser().getUid());
            m.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(m);
        }else {
         Intent l = new Intent(this,LoginActivity.class);
         l.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
         startActivity(l);
        }
    }
}
