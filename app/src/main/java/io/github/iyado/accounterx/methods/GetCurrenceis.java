package io.github.iyado.accounterx.methods;

import java.util.HashMap;

import io.github.iyado.accounterx.utils.CurrencyDetails;

public interface GetCurrenceis{
    void getCurrency(HashMap<Integer, CurrencyDetails> currencyDetails);
    void getError(Exception error);
    
    
}
