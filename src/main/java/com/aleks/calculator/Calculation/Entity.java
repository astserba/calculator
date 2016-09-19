package com.aleks.calculator.Calculation;

/**
 * Created by Aleksandr on 14/09/2016.
 */
public class Entity {

    private int priority = 0;
    private Operators operator;
    private double number = 0;
    private boolean isNum = false;

    //constructor for number:
    public Entity(String number, int priority) {
        this.number = Double.valueOf(number);
        this.priority = priority;
        isNum = true;
    }

    //constructor for number:
    public Entity(double number, int priority) {
        this.number = number;
        this.priority = priority;
        isNum = true;
    }

    //constructor for operator:
    public Entity(char c, int priority) {
        switch (c) {
            case '+':
                this.operator = Operators.PLUS;
                this.priority = priority;
                break;
            case '-':
                this.operator = Operators.MINUS;
                this.priority = priority;
                break;
            case '/':
                this.operator = Operators.DIVIDE;
                this.priority = priority + 1;
                break;
            case '*':
                this.operator = Operators.MULTIPLY;
                this.priority = priority + 1;
                break;
        }
    }

    //get priority of operation:
    public int getPriority() {
        return priority;
    }

    //check if number:
    public boolean isNumber() {
        return isNum;
    }

    //get number:
    public double getNumber() {
        return number;
    }

    //get operator:
    public Operators getOperator() {
        return operator;
    }
}