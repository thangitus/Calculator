package com.thangitus.caculator;

import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

class Presenter implements Contract.Presenter {
   private String sMath;
   private String res;
   private Contract.View view;
   private String current;
   private boolean hasDot = false;
   private boolean hasError = false;

   public Presenter(Contract.View view) {
      this.view = view;
      this.sMath = "";
      this.current = "";
   }

   @Override
   public void numberClick(int num) {
      this.sMath += String.valueOf(num);
      this.current = "";
      view.showMath(sMath);
      calculator();
   }
   @Override
   public void operatorClick(String operator) {
      if (sMath.equals("") || (operator.equals(".") && hasDot))
         return;

      if (operator.equals("."))
         hasDot = true;

      if (operator.equalsIgnoreCase("=")) {
         sMath = res;
         view.showMath(sMath);
         if (this.hasError) {sMath = ""; this.hasError = false;};
         current = "";
         view.showResult("0");
         return;
      }

      if (!operator.equals(current)) {
         if (!current.equals(""))
            sMath = (String) sMath.subSequence(0, sMath.length() - 1);
         sMath += operator;
      }
      current = operator;
      view.showMath(sMath);
   }

   @Override
   public void editClick(String tag) {
//      if (sMath.equals(""))
//         return;

      if (tag.equalsIgnoreCase("C")) {
         sMath = "";
         current = "";
         view.showResult("0");
      } else {
         sMath = (String) sMath.subSequence(0, sMath.length() - 1);
         if (!isOperator(sMath.charAt(sMath.length() - 1)))
            calculator();
      }
      view.showMath(sMath);
   }

   private void calculator() {
      String[] process = processString(sMath);
      List<String> post = postfix(process);
      res = valueMath(post);
      view.showResult(res);
   }
   private String[] processString(String sMath) {
      StringBuilder s1 = new StringBuilder();
      String[] elementMath;
      for (char c : sMath.toCharArray()) {
         if (!isOperator(c))
            s1.append(c);
         else
            s1.append(" ")
              .append(c)
              .append(" ");
      }
      s1 = new StringBuilder(s1.toString()
                               .trim());
      s1 = new StringBuilder(s1.toString()
                               .replaceAll("\\s+", " "));
      elementMath = s1.toString()
                      .split(" ");
      return elementMath;
   }

   private List<String> postfix(String[] elementMath) {
      StringBuilder s1 = new StringBuilder();
      List<String> res = new ArrayList<>();
      Stack<String> stack = new Stack<>();
      for (String s : elementMath) {
         char c = s.charAt(0);
         if (!isOperator(c))
            res.add(s);
         else {
            while (!stack.isEmpty() && priority(stack.peek()
                                                     .charAt(0)) >= priority(c)) {
               res.add(stack.peek());
               stack.pop();
            }
            stack.push(s);
         }
      }
      while (!stack.isEmpty()) {
         res.add(stack.peek());
         stack.pop();
      }
      return res;
   }

   public String valueMath(List<String> elementMath) {
      boolean divideByZero = false;
      Stack<String> stack = new Stack<String>();
      for (String s : elementMath) {
         char c = s.charAt(0);
         if (!isOperator(c))
            stack.push(s);
         else {
            double num = 0f;
            double num1 = Float.parseFloat(stack.pop());
            double num2 = Float.parseFloat(stack.pop());
            switch (c) {
               case '+':
                  num = num2 + num1;
                  break;
               case '-':
                  num = num2 - num1;
                  break;
               case 'x':
                  num = num2 * num1;
                  break;
               case '÷':
                  if (num1 <= 0.00000000f) {
                     divideByZero = true;
                     this.hasError = true;
                  }
                  num = num2 / num1;
                  break;
               default:
                  break;
            }
            if (divideByZero) {
               stack.clear();
               stack.push("Error: Divide by zero");
               break;
            }
            stack.push(Double.toString((double) Math.round(num * 1000) / 1000));
         }
      }
      return stack.pop();
   }

   private int priority(char c) {
      if (c == '+' || c == '-')
         return 1;

      if (c == 'x' || c == '÷')
         return 2;

      return 0;
   }

   private boolean isOperator(char c) {
      char[] operator = {'+', '-', 'x', '÷'};
      Arrays.sort(operator);
      return Arrays.binarySearch(operator, c) > -1;
   }
}
