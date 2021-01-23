package com.unipi.stavrosvl7.calculator.Currency;


import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by tomm on 4/4/16 AD.
 */
public interface CurrencyExchangeService {
    @GET("http://data.fixer.io/api/latest?access_key=838295fd8df398aba5936427222c8e9d")
    Call<CurrencyExchange> loadCurrencyExchange();
}
