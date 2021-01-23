package com.unipi.stavrosvl7.calculator.Operations;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Spinner;
import android.widget.Toast;

import com.unipi.stavrosvl7.calculator.Currency.CurrencyExchange;

import java.math.BigDecimal;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class Calculate extends AsyncTask<Void, Void, String> {
    private String input;
    private String name;
    private String result = "";
    private double x;
    private double lastX;
    private boolean thrown = false;
    private Context context;
    private Spinner spinner;
    private CurrencyExchange currencyExchange;
    private boolean equalClicked = false;
    private Operations operations = new Operations();
    private ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("rhino");

    public Calculate(){

    }
    public Calculate(String input, Context context){
        this.input = input;
        this.context = context;
    }

    public Calculate(String input, Context context,Spinner spinner,CurrencyExchange currencyExchange,double x,double lastX){
        this.input = input;
        this.context = context;
        this.spinner = spinner;
        this.currencyExchange = currencyExchange;
        this.x=x;
        this.lastX = lastX;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Spinner getSpinner() {
        return spinner;
    }

    public void setSpinner(Spinner spinner) {
        this.spinner = spinner;
    }

    public CurrencyExchange getCurrencyExchange() {
        return currencyExchange;
    }

    public void setCurrencyExchange(CurrencyExchange currencyExchange) {
        this.currencyExchange = currencyExchange;
    }

    public String calculate(String input, Context context)
    {
        try
        {
            String temp = input;
            if (equalClicked)
            {
                String lastExpression = "";
                temp = input + lastExpression;
            } else
            {
                operations.saveLastExpression(input);
            }
            result = scriptEngine.eval(temp.replaceAll("%", "/100").replaceAll("x", "*").replaceAll("[^\\x00-\\x7F]", "/")).toString();
            BigDecimal decimal = new BigDecimal(result);
            result = decimal.setScale(8, BigDecimal.ROUND_HALF_EVEN).toPlainString();
            equalClicked = true;
        } catch (Exception e) {
            thrown = true;
        }

        if (result.equals("Infinity")) {
            return "";
        } else if (result.contains("."))
            result = result.replaceAll("\\.?0*$", "");
        if(thrown){
            return result;
        }
        if(spinner!=null){
            double temp = Double.parseDouble(result)/ lastX;
            temp = temp*x;
            return String.valueOf(temp);

        }
        return result;
    }



    @Override
    protected String doInBackground(Void... voids) {
        return calculate(this.input,this.context);
    }

    @Override
    protected void onPostExecute(String result) {
        if(thrown){
            Toast.makeText(context, "Wrong Format", Toast.LENGTH_SHORT).show();
        }
    }
}
