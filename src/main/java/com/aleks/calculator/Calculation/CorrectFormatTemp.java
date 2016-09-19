package com.aleks.calculator.Calculation;

/**
 * Created by Aleksandr on 19/09/2016.
 */
public class CorrectFormatTemp {
    //check if data is in correct format:
    public static boolean check(String data, char current, boolean showLogs) {

        char arr[] = data.toCharArray();
        char operators[] = {'+', '-', '/', '*', '.'};
        int last = arr.length - 1;
        boolean correctFormat = true;
        String message = "";

        boolean firstCharWrong = (arr[0] == '.' || arr[0] == '*' || arr[0] == ')' || arr[0] == '/' || arr[0] == '+' || arr[0] == '-');
        boolean currentOperator = (current == '.' || current == '*' || current == ')' || current == '/' || current == '+');

        //Error first char is *)/+.
        if (firstCharWrong) {
            message += "\nFIRST CHAR ERROR: < " + arr[0] + " >";
            correctFormat = false;
        }

        if (arr.length>1) {

            //double operator:
            if ((!Character.isDigit(arr[last]) && (arr[last] != '(') && (arr[last] != ')'))) {
                System.out.println(arr[last] + " " + (arr[last]!='('));
                //cycle through operators:
                for (char operator : operators) {
                    if (current == operator) {
                        message += "\nDOUBLE OPERATOR ERROR: < " + arr[last] + " " + current + " >";
                        correctFormat = false;
                    }
                }
            }

            // ). )4
            if (arr[last] == ')' && (Character.isDigit(current) || current == '.')) {
                message += "\nNUMBER OR DOT AFTER CLOSED BRACKET: < " + arr[last] + " " + current + " >";
                correctFormat = false;
            }

            // (+*/.
            if (arr[last] == '(' && currentOperator) {
                message += "\nDOT OR OPERATOR AFTER OPENED BRACKET: < " + arr[last] + " " + current + " >";
                correctFormat = false;
            }
        }

        if (showLogs) {
            System.out.println(message);
        }

        return correctFormat;
    }
}
