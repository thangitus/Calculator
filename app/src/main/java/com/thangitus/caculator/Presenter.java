package com.thangitus.caculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

class Presenter implements Contract.Presenter {
   private String sMath;
   private String res;
   private Contract.View view;
   private String current;
   public Presenter(Contract.View view) {
      this.view = view;
      sMath = "";
      current = "";
   }

   @Override
   public void numberClick(int num) {
      sMath += String.valueOf(num);
      current = null;
      view.showCal(sMath);
      calculator();
   }
   @Override
   public void operatorClick(String operator) {
      if (current == null) {
         current = operator;
         sMath += current;
      } else if (!operator.equals(current)) {
         current = operator;
         sMath = (String) sMath.subSequence(0, sMath.length() - 1);
         sMath += current;
      }
      view.showCal(sMath);
   }

   @Override
   public void editClick(String tag) {
      if (tag.equalsIgnoreCase("C")) {
         sMath = "";
         view.showCal("0");
      } else {
         sMath = (String) sMath.subSequence(0, sMath.length() - 1);
         calculator();
      }
      view.showCal(sMath);
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
      return res;
   }
   public String valueMath(List<String> elementMath) {
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
               case '*':
                  num = num2 * num1;
                  break;
               case '/':
                  num = num2 / num1;
                  break;
               default:
                  break;
            }
            stack.push(Double.toString(num));
         }
      }
      return stack.pop();
   }
   private int priority(char c) {
      if (c == '+' || c == '-')
         return 1;

      if (c == '*' || c == '/')
         return 2;

      return 0;
   }
   private boolean isOperator(char c) {
      char[] operator = {'+', '-', '*', '/'};
      Arrays.sort(operator);
      return Arrays.binarySearch(operator, c) > -1;
   }
}
