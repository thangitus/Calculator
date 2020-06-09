package com.thangitus.caculator;

interface Contract {

   interface View {
      void showResult(String res);

      void showCal(String cal);

   }

   interface Presenter {
      void numberClick(int num);


      void operatorClick(String operator);

      void editClick(String tag);

   }
}
