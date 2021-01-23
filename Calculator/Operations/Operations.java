package com.unipi.stavrosvl7.calculator.Operations;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class Operations extends AppCompatActivity {

    private int openParenthesis = 0;

    private boolean dotUsed = false;

    private ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("rhino");;

    private boolean equalClicked = false;
    private String lastExpression = "";

    private final static int IS_NUMBER = 0;
    private final static int IS_OPERAND = 1;
    private final static int IS_OPEN_PARENTHESIS = 2;
    private final static int IS_CLOSE_PARENTHESIS = 3;
    private final static int IS_DOT = 4;

    public boolean addDot(TextView textViewInputNumbers)
    {
        boolean done = false;

        if (textViewInputNumbers.getText().length() == 0)
        {
            textViewInputNumbers.setText("0.");
            dotUsed = true;
            done = true;
        } else if (dotUsed == true)
        {
        } else if (defineLastCharacter(textViewInputNumbers.getText().charAt(textViewInputNumbers.getText().length() - 1) + "") == IS_OPERAND)
        {
            textViewInputNumbers.setText(textViewInputNumbers.getText() + "0.");
            done = true;
            dotUsed = true;
        } else if (defineLastCharacter(textViewInputNumbers.getText().charAt(textViewInputNumbers.getText().length() - 1) + "") == IS_NUMBER)
        {
            textViewInputNumbers.setText(textViewInputNumbers.getText() + ".");
            done = true;
            dotUsed = true;
        }
        return done;
    }

    public boolean addParenthesis(TextView textViewInputNumbers)
    {
        boolean done = false;
        int operationLength = textViewInputNumbers.getText().length();

        if (operationLength == 0)
        {
            textViewInputNumbers.setText(textViewInputNumbers.getText() + "(");
            dotUsed = false;
            openParenthesis++;
            done = true;
        } else if (openParenthesis > 0 && operationLength > 0)
        {
            String lastInput = textViewInputNumbers.getText().charAt(operationLength - 1) + "";
            switch (defineLastCharacter(lastInput))
            {
                case IS_NUMBER:
                case IS_CLOSE_PARENTHESIS:
                    textViewInputNumbers.setText(textViewInputNumbers.getText() + ")");
                    done = true;
                    openParenthesis--;
                    dotUsed = false;
                    break;
                case IS_OPERAND:
                case IS_OPEN_PARENTHESIS:
                    textViewInputNumbers.setText(textViewInputNumbers.getText() + "(");
                    done = true;
                    openParenthesis++;
                    dotUsed = false;
                    break;
            }
        } else if (openParenthesis == 0 && operationLength > 0)
        {
            String lastInput = textViewInputNumbers.getText().charAt(operationLength - 1) + "";
            if (defineLastCharacter(lastInput) == IS_OPERAND)
            {
                textViewInputNumbers.setText(textViewInputNumbers.getText() + "(");
                done = true;
                dotUsed = false;
                openParenthesis++;
            } else
            {
                textViewInputNumbers.setText(textViewInputNumbers.getText() + "x(");
                done = true;
                dotUsed = false;
                openParenthesis++;
            }
        }
        return done;
    }

    public boolean addOperand(TextView textViewInputNumbers, String operand,Context context)
    {
        boolean done = false;
        int operationLength = textViewInputNumbers.getText().length();
        if (operationLength > 0)
        {
            String lastInput = textViewInputNumbers.getText().charAt(operationLength - 1) + "";

            if ((lastInput.equals("+") || lastInput.equals("-") || lastInput.equals("*") || lastInput.equals("\u00F7") || lastInput.equals("%")))
            {
                Toast.makeText(context, "Wrong format", Toast.LENGTH_LONG).show();
            } else if (operand.equals("%") && defineLastCharacter(lastInput) == IS_NUMBER)
            {
                textViewInputNumbers.setText(textViewInputNumbers.getText() + operand);
                dotUsed = false;
                equalClicked = false;
                lastExpression = "";
                done = true;
            } else if (!operand.equals("%"))
            {
                textViewInputNumbers.setText(textViewInputNumbers.getText() + operand);
                dotUsed = false;
                equalClicked = false;
                lastExpression = "";
                done = true;
            }
        } else
        {
            Toast.makeText(context, "Wrong Format. Operand Without any numbers?", Toast.LENGTH_LONG).show();
        }
        return done;
    }

    @SuppressLint("SetTextI18n")
    public boolean addNumber(TextView textViewInputNumbers, String number)
    {
        boolean done = false;
        int operationLength = textViewInputNumbers.getText().length();
        if (operationLength > 0)
        {
            String lastCharacter = textViewInputNumbers.getText().charAt(operationLength - 1) + "";
            int lastCharacterState = defineLastCharacter(lastCharacter);

            if (operationLength == 1 && lastCharacterState == IS_NUMBER && lastCharacter.equals("0"))
            {
                textViewInputNumbers.setText(number);
                done = true;
            } else if (lastCharacterState == IS_OPEN_PARENTHESIS)
            {
                textViewInputNumbers.setText(textViewInputNumbers.getText() + number);
                done = true;
            } else if (lastCharacterState == IS_CLOSE_PARENTHESIS || lastCharacter.equals("%"))
            {
                textViewInputNumbers.setText(textViewInputNumbers.getText() + "x" + number);
                done = true;
            } else if (lastCharacterState == IS_NUMBER || lastCharacterState == IS_OPERAND || lastCharacterState == IS_DOT)
            {
                textViewInputNumbers.setText(textViewInputNumbers.getText() + number);
                done = true;
            }
        } else
        {
            textViewInputNumbers.setText(textViewInputNumbers.getText() + number);
            done = true;
        }
        return done;
    }


    public void saveLastExpression(String input)
    {
        String lastOfExpression = input.charAt(input.length() - 1) + "";
        if (input.length() > 1)
        {
            if (lastOfExpression.equals(")"))
            {
                lastExpression = ")";
                int numberOfCloseParenthesis = 1;

                for (int i = input.length() - 2; i >= 0; i--)
                {
                    if (numberOfCloseParenthesis > 0)
                    {
                        String last = input.charAt(i) + "";
                        if (last.equals(")"))
                        {
                            numberOfCloseParenthesis++;
                        } else if (last.equals("("))
                        {
                            numberOfCloseParenthesis--;
                        }
                        lastExpression = last + lastExpression;
                    } else if (defineLastCharacter(input.charAt(i) + "") == IS_OPERAND)
                    {
                        lastExpression = input.charAt(i) + lastExpression;
                        break;
                    } else
                        lastExpression = "";
                }
            } else if (defineLastCharacter(lastOfExpression + "") == IS_NUMBER)
            {
                lastExpression = lastOfExpression;
                for (int i = input.length() - 2; i >= 0; i--)
                {
                    String last = input.charAt(i) + "";
                    if (defineLastCharacter(last) == IS_NUMBER || defineLastCharacter(last) == IS_DOT)
                    {
                        lastExpression = last + lastExpression;
                    } else if (defineLastCharacter(last) == IS_OPERAND)
                    {
                        lastExpression = last + lastExpression;
                        break;
                    }
                    if (i == 0)
                        lastExpression = "";
                }
            }
        }
    }

    public int defineLastCharacter(String lastCharacter)
    {
        try
        {
            Integer.parseInt(lastCharacter);
            return IS_NUMBER;
        } catch (NumberFormatException e)
        {
        }

        if ((lastCharacter.equals("+") || lastCharacter.equals("-") || lastCharacter.equals("x") || lastCharacter.equals("\u00F7") || lastCharacter.equals("%")))
            return IS_OPERAND;

        if (lastCharacter.equals("("))
            return IS_OPEN_PARENTHESIS;

        if (lastCharacter.equals(")"))
            return IS_CLOSE_PARENTHESIS;

        if (lastCharacter.equals("."))
            return IS_DOT;

        return -1;
    }

    public void buttonClear(TextView textViewInputNumbers){
        textViewInputNumbers.setText("");
        openParenthesis = 0;
        dotUsed = false;
        equalClicked = false;
    }
}
