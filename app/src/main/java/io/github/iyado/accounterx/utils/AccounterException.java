package io.github.iyado.accounterx.utils;

public class AccounterException extends Exception{

    public AccounterException(String message, Throwable cause){
        super(message, cause);
    }

    public String getMessage(){
        return getLocalizedMessage();
    }


}
