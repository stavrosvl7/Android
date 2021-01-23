package com.unipi.stavrosvl7.calculator.Activites;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.unipi.stavrosvl7.calculator.Currency.CurrencyExchange;
import com.unipi.stavrosvl7.calculator.Currency.CurrencyExchangeService;
import com.unipi.stavrosvl7.calculator.Data.MyData;
import com.unipi.stavrosvl7.calculator.Operations.Calculate;
import com.unipi.stavrosvl7.calculator.Operations.Operations;
import com.unipi.stavrosvl7.calculator.R;

import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, Callback<CurrencyExchange>
{
    double x = 1;
    double lastX = x;
    Context context;
    Button buttonNumber0;
    Button buttonNumber1;
    Button buttonNumber2;
    Button buttonNumber3;
    Button buttonNumber4;
    Button buttonNumber5;
    Button buttonNumber6;
    Button buttonNumber7;
    Button buttonNumber8;
    Button buttonNumber9;

    Button buttonClear;
    Button buttonParentheses;
    Button buttonPercent;
    Button buttonDivision;
    Button buttonMultiplication;
    Button buttonSubtraction;
    Button buttonAddition;
    Button buttonEqual;
    Button buttonDot;
    Spinner spinner;
    CurrencyExchange currencyExchange;

    TextView textViewInputNumbers;

    Operations operations;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();
        operations = new Operations();

        initializeViewVariables();
        setOnClickListeners();
        setOnTouchListener();
        loadCurrencyExchangeData();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner.setSelection(position);
                if (!textViewInputNumbers.getText().toString().equals("")) {
                    try {
                        MyData data = getXs();
                        assert data != null;
                        textViewInputNumbers.setText(new Calculate(textViewInputNumbers.getText().toString(),context,spinner,currencyExchange,data.getX(),data.getLastX()).execute().get());
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private MyData getXs() {
        String name = spinner.getSelectedItem().toString();
        lastX = x;
        for(int i=0;i<currencyExchange.getCurrencyList().size();i++){
            if(currencyExchange.getCurrencyList().get(i).getName().equals(name)) {
                x = currencyExchange.getCurrencyList().get(i).getRate();
                return new MyData(x, lastX);
            }
        }
        return null;
    }

    private void initializeViewVariables()
    {
        buttonNumber0 = findViewById(R.id.button_zero);
        buttonNumber1 = findViewById(R.id.button_one);
        buttonNumber2 = findViewById(R.id.button_two);
        buttonNumber3 = findViewById(R.id.button_three);
        buttonNumber4 = findViewById(R.id.button_four);
        buttonNumber5 = findViewById(R.id.button_five);
        buttonNumber6 = findViewById(R.id.button_six);
        buttonNumber7 = findViewById(R.id.button_seven);
        buttonNumber8 = findViewById(R.id.button_eight);
        buttonNumber9 = findViewById(R.id.button_nine);

        buttonClear = findViewById(R.id.button_clear);
        buttonParentheses = findViewById(R.id.button_parentheses);
        buttonPercent = findViewById(R.id.button_percent);
        buttonDivision = findViewById(R.id.button_division);
        buttonMultiplication = findViewById(R.id.button_multiplication);
        buttonSubtraction = findViewById(R.id.button_subtraction);
        buttonAddition = findViewById(R.id.button_addition);
        buttonEqual = findViewById(R.id.button_equal);
        buttonDot = findViewById(R.id.button_dot);
        textViewInputNumbers = findViewById(R.id.textView_input_numbers);
        spinner = findViewById(R.id.spinner);

    }

    private void setOnClickListeners()
    {
        buttonNumber0.setOnClickListener(this);
        buttonNumber1.setOnClickListener(this);
        buttonNumber2.setOnClickListener(this);
        buttonNumber3.setOnClickListener(this);
        buttonNumber4.setOnClickListener(this);
        buttonNumber5.setOnClickListener(this);
        buttonNumber6.setOnClickListener(this);
        buttonNumber7.setOnClickListener(this);
        buttonNumber8.setOnClickListener(this);
        buttonNumber9.setOnClickListener(this);

        buttonClear.setOnClickListener(this);
        buttonParentheses.setOnClickListener(this);
        buttonPercent.setOnClickListener(this);
        buttonDivision.setOnClickListener(this);
        buttonMultiplication.setOnClickListener(this);
        buttonSubtraction.setOnClickListener(this);
        buttonAddition.setOnClickListener(this);
        buttonEqual.setOnClickListener(this);
        buttonDot.setOnClickListener(this);

    }

    @SuppressLint("ClickableViewAccessibility")
    private void setOnTouchListener()
    {
        buttonNumber0.setOnTouchListener(this);
        buttonNumber1.setOnTouchListener(this);
        buttonNumber2.setOnTouchListener(this);
        buttonNumber3.setOnTouchListener(this);
        buttonNumber4.setOnTouchListener(this);
        buttonNumber5.setOnTouchListener(this);
        buttonNumber6.setOnTouchListener(this);
        buttonNumber7.setOnTouchListener(this);
        buttonNumber8.setOnTouchListener(this);
        buttonNumber9.setOnTouchListener(this);

        buttonClear.setOnTouchListener(this);
        buttonParentheses.setOnTouchListener(this);
        buttonPercent.setOnTouchListener(this);
        buttonDivision.setOnTouchListener(this);
        buttonMultiplication.setOnTouchListener(this);
        buttonSubtraction.setOnTouchListener(this);
        buttonAddition.setOnTouchListener(this);
        buttonDot.setOnTouchListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.button_zero:
                operations.addNumber(textViewInputNumbers, "0");
                break;
            case R.id.button_one:
                operations.addNumber(textViewInputNumbers, "1");
                break;
            case R.id.button_two:
                operations.addNumber(textViewInputNumbers, "2");
                break;
            case R.id.button_three:
                operations.addNumber(textViewInputNumbers, "3");
                break;
            case R.id.button_four:
                operations.addNumber(textViewInputNumbers, "4");
                break;
            case R.id.button_five:
                operations.addNumber(textViewInputNumbers, "5");
                break;
            case R.id.button_six:
                operations.addNumber(textViewInputNumbers, "6");
                break;
            case R.id.button_seven:
                operations.addNumber(textViewInputNumbers, "7");
                break;
            case R.id.button_eight:
                operations.addNumber(textViewInputNumbers, "8");
                break;
            case R.id.button_nine:
                operations.addNumber(textViewInputNumbers, "9");
                break;
            case R.id.button_addition:
                operations.addOperand(textViewInputNumbers, "+", context);
                break;
            case R.id.button_subtraction:
                operations.addOperand(textViewInputNumbers, "-", context);
                break;
            case R.id.button_multiplication:
                operations.addOperand(textViewInputNumbers, "x", context);
                break;
            case R.id.button_division:
                operations.addOperand(textViewInputNumbers, "\u00F7", context);
                break;
            case R.id.button_percent:
                operations.addOperand(textViewInputNumbers, "%", context);
                break;
            case R.id.button_dot:
                operations.addDot(textViewInputNumbers);
                break;
            case R.id.button_parentheses:
                operations.addParenthesis(textViewInputNumbers);
                break;
            case R.id.button_clear:
                    operations.buttonClear(textViewInputNumbers);
                break;
            case R.id.button_equal:
                if (!textViewInputNumbers.getText().toString().equals("")) {
                    try {
                        textViewInputNumbers.setText(new Calculate(textViewInputNumbers.getText().toString(),context).execute().get());
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                break;
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent)
    {
        switch (motionEvent.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            {
                view.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                view.invalidate();
                break;
            }
            case MotionEvent.ACTION_UP:
            {
                view.getBackground().clearColorFilter();
                view.invalidate();
                break;
            }
        }
        return false;
    }

    private void loadCurrencyExchangeData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.fixer.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CurrencyExchangeService service = retrofit.create(CurrencyExchangeService.class);
        Call<CurrencyExchange> call = service.loadCurrencyExchange();
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<CurrencyExchange> call, Response<CurrencyExchange> response) {
        currencyExchange = response.body();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, currencyExchange.getCurrencyListNames(currencyExchange.getCurrencyList()));
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

    @Override
    public void onFailure(Call<CurrencyExchange> call, Throwable t) {
        Toast.makeText(this, t.getMessage(), Toast.LENGTH_LONG).show();
    }

}