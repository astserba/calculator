package com.aleks.calculator.Calculation;

import java.util.ArrayList;

/**
 * Created by Aleksandr on 15/09/2016.
 */

public class Calculation {

    private ArrayList<Entity> data = new ArrayList<>();
    private int priority = 0;
    private boolean showLogs = false;
    private int operation = 1;

    //perform operation:
    public String perform(String value, boolean showLogs) {
        this.showLogs = showLogs;

        printLogs("\nDATA: " + value, showLogs);
        String result = "WRONG DATA";

        //fill data each entity with priority:
        fillData(value);

        //cycle mechanism till only answer left:
        while (data.size() > 1) {
            calculatePriority();
        }

        result = String.valueOf(data.get(0).getNumber());
        printLogs("ANSWER: " + result, showLogs);

        return result;
    }

    //fill data with numbers or operators:
    private void fillData(String str) {

        char arr[] = str.toCharArray();
        String number = "";

        for (int i = 0; i < arr.length; i++) {
            char temp = arr[i];

            //collect number:
            if (isNumber(temp)) {
                number += temp;

                //last number
                if (i == arr.length - 1) {
                    data.add(new Entity(number, priority));
                }
            }

            //add operator and (number):
            else if (isOperator(temp)) {

                //check if brackets:
                if (temp == '(' || temp == ')') {

                    //open bracket:
                    if (temp == '(') {
                        priority += 2;
                    }

                    //add number:
                    if (!number.equals("")) {
                        data.add(new Entity(number, priority));
                        number = "";
                    }

                    //close bracket:
                    if (temp == ')') {
                        calculatePriority();
                        priority -= 2;
                    }
                }

                //simple operator: (+-/*)
                else {

                    //add: (number)
                    if (!number.equals("")) {
                        data.add(new Entity(number, priority));
                        number = "";
                    }

                    //add operator: (+-/*)
                    data.add(new Entity(temp, priority));
                }
            }
        }

    }

    //calculate everything on 1 priority level:
    private void calculatePriority() {

        int maxPriority = 0;

        //find max priority:
        for (int i = 0; i < data.size(); i++) {
            if (maxPriority < data.get(i).getPriority()) {
                maxPriority = data.get(i).getPriority();
            }
        }

        //find operations indexes with max priority:
        for (int i = 0; i < data.size(); i++) {
            if (maxPriority == data.get(i).getPriority()) {
                calculate(i--);
            }
        }
    }

    //calculate one operation:
    private void calculate(int MaxPriorityPos) {

        double tempAnswer = 0.0;
        int currentPriority = 0;
        int newPriority = 0;
        double num1 = 0.0;
        double num2 = 0.0;
        boolean nextSamePriority = false;
        boolean nextOperator = false;
        Operators operator = null;

        //last number:
        if (MaxPriorityPos < data.size() - 1) {
            nextSamePriority = (data.get(MaxPriorityPos).getPriority() == data.get(MaxPriorityPos + 1).getPriority());
            nextOperator = !(data.get(MaxPriorityPos + 1).isNumber());
        }

        //normal operation (4+-5) --> 9.0  (same priority)
        if (data.get(MaxPriorityPos).isNumber() && nextSamePriority && nextOperator) {
            currentPriority = data.get(MaxPriorityPos + 1).getPriority();
            newPriority = currentPriority;
            num1 = data.get(MaxPriorityPos).getNumber();
            num2 = data.get(MaxPriorityPos + 2).getNumber();
            operator = data.get(MaxPriorityPos + 1).getOperator();
            data.remove(MaxPriorityPos + 1);
            data.remove(MaxPriorityPos + 1);
        }

        //normal operation (3*/2) --> 6.0  (decrease priority by 1)
        else if (!(data.get(MaxPriorityPos).isNumber()) && (!nextSamePriority)) {
            currentPriority = data.get(MaxPriorityPos).getPriority();
            newPriority = currentPriority;
            num1 = data.get(MaxPriorityPos - 1).getNumber();
            num2 = data.get(MaxPriorityPos + 1).getNumber();
            operator = data.get(MaxPriorityPos).getOperator();
            data.remove(MaxPriorityPos);
            data.remove(MaxPriorityPos);
            MaxPriorityPos--;
        }

        //negative number:  (- 5) --> -5.0   (same priority)
        else if (!(data.get(MaxPriorityPos).isNumber()) && data.get(MaxPriorityPos + 1).isNumber()) {
            currentPriority = data.get(MaxPriorityPos).getPriority();
            newPriority = currentPriority;
            num1 = 0;
            num2 = data.get(MaxPriorityPos + 1).getNumber();
            operator = Operators.MINUS;
            data.remove(MaxPriorityPos + 1);
        }

        //single number with sign: ((-)5.0) -> (-)5.0  (decrease priority by 2)
        else if (data.get(MaxPriorityPos).isNumber() && !(nextSamePriority)) {
            currentPriority = data.get(MaxPriorityPos).getPriority();
            newPriority = currentPriority - 2;
            num1 = 0;
            num2 = data.get(MaxPriorityPos).getNumber();
            operator = Operators.PLUS;
        }

        //basic calculation:
        switch (operator) {
            case PLUS:
                tempAnswer = num1 + num2;
                break;
            case MINUS:
                tempAnswer = num1 - num2;
                break;
            case DIVIDE:
                tempAnswer = num1 / num2;
                newPriority -= 1;
                break;
            case MULTIPLY:
                tempAnswer = num1 * num2;
                newPriority -= 1;
                break;
        }

        //set answer on previous position:
        data.set(MaxPriorityPos, new Entity(tempAnswer, newPriority));
        printOperation(operator, num1, num2, currentPriority, newPriority, tempAnswer);

    }

    //check if char is number
    private boolean isNumber(char temp) {
        return (Character.isDigit(temp) || temp == '.');
    }

    //check if char is operator
    private boolean isOperator(char temp) {
        return (temp == '-' || temp == '+' || temp == '/' || temp == '*' || temp == '(' || temp == ')');
    }

    //print one operation message
    private void printOperation(Operators operator, double num1, double num2, int currentPriority, int newPriority, double result) {
        String message = "";
        message += "(" + (operation++) + ") ";
        message += "[ " + num1 + " ";
        message += operator + " ";
        message += num2 + " = ";
        message += result;
        message += " ] (" + currentPriority + ") --> (" + newPriority + ") \n";
        printLogs(message, showLogs);
    }

    //print message in console
    private void printLogs(String message, boolean visible) {
        if (visible) {
            System.out.println(message);
        }
    }
}