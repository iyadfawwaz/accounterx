package io.github.iyado.accounterx.utils;

public class AccounterException extends Exception{

    private String message;
    private Throwable throwable;
    public AccounterException(String message){
        super(message);
        this.message = message;
    }

    public AccounterException(String message, Throwable cause){
        super(message, cause);
        this.message = message;
        this.throwable = cause;
    }

    public String getMessage(){
        return getLocalizedMessage();
    }

    public Throwable getThrowable(){
        return throwable;
    }

    public String getLocalMessage(){
      return getLocalizedMessage();
    }


    public void setMessage(String message) {
        this.message = message;
    }
}
