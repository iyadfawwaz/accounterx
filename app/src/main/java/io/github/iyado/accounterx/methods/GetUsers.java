package io.github.iyado.accounterx.methods;


import androidx.annotation.NonNull;
import java.util.ArrayList;


public interface GetUsers {
    void onGetUsers(@NonNull ArrayList<String> users);
    void onGetUsersError(@NonNull String error);
}
