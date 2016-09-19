package com.aleks.calculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.aleks.calculator.Calculation.Calculation;
import com.aleks.calculator.Calculation.CorrectFormat;

public class MainActivity extends AppCompatActivity {

    TextView input;
    String data = "";
    int bracketsDepth = 0;

    //TODO swipe textView to previous or next operation with answer
    //TODO swipe to right to get extra operators:

    //add number or operator:
    public void add(View view) {
        String temp = view.getTag().toString();

        if (data.equals("")||data.equals("0.0")) {
            if (CorrectFormat.check(temp, temp.charAt(0), true)) {
                data = temp;
            }
        } else {
            if (CorrectFormat.check(data, temp.charAt(0), true)) {
                data += temp;
            }
        }
        input.setText(data);
        seize();
    }

    //decrease text size:
    public void seize() {
        if (data.length() > 20) {
            input.setTextSize(25);
        } else if (data.length() > 15) {
            input.setTextSize(30);
        }
    }

    //increase text size:
    public void unseize() {
        if (data.length() < 16) {
            input.setTextSize(40);
        } else if (data.length() < 21) {
            input.setTextSize(30);
        }
    }

    //delete symbol:
    public void delete(View view) {
        if (data.length() > 0) {
            if (data.charAt(data.length() - 1) == ')') {
                bracketsDepth++;
            }
            if (data.charAt(data.length() - 1) == '(') {
                bracketsDepth--;
            }

            data = data.substring(0, data.length() - 1);
            input.setText(data);
            unseize();
        }
    }

    //clear everything:
    public void clear(View view) {
        data = "0.0";
        input.setText(data);
        unseize();
        bracketsDepth = 0;
    }

    //clever brackets:
    public void addBrackets(View view) {

        char arr[] = data.toCharArray();
        int last = arr.length - 1;

        boolean wrongPos = false;

        //cycle through if first and last char are correct:
        for (int i = 0; i < arr.length; i++) {
            char c = arr[i];

            //brackets not closed:
            if ((i == last) && (bracketsDepth > 0)) {
                if ((Character.isDigit(arr[i])) || (arr[i] == ')')) {
                    data += ')';
                    input.setText(data);
                    bracketsDepth--;
                    wrongPos = true;
                }

            }
            if (i == last && (Character.isDigit(arr[last]) || arr[last] == '.') || arr[last] == ')') {
                wrongPos = true;
            }
        }

        if (!wrongPos) {
            data += '(';
            input.setText(data);
            bracketsDepth++;
        }
    }

    //calculate data:
    public void calculate(View view) {
        Calculation calculation = new Calculation();
        System.out.println(bracketsDepth);
        if (Character.isDigit(data.charAt(data.length() - 1))||data.charAt(data.length()-1)==')') {
            while ((bracketsDepth--) > 0) {
                data += ')';
            }
            String temp =data;
            double result = Double.valueOf(calculation.perform(data, true));
            if (result > 0) {
                data = String.valueOf(result);
                bracketsDepth = 0;
            } else {
                data = "(" + String.valueOf(result);
                bracketsDepth = 1;
            }
            temp += "\n= " + result;
            input.setText(temp);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        input = (TextView) findViewById(R.id.textView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
