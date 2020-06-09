package com.thangitus.caculator;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.thangitus.caculator.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements Contract.View {
   ActivityMainBinding binding;
   Contract.Presenter presenter;
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      binding = ActivityMainBinding.inflate(getLayoutInflater());
      setContentView(binding.getRoot());
      presenter = new Presenter(this);
   }

   @Override
   public void showResult(String res) {
      binding.textViewResult.setText(res);
   }
   @Override
   public void showCal(String cal) {
      binding.textViewCaculator.setText(cal);
   }

   public void editClick(View view) {
      presenter.editClick((String) view.getTag());
   }
   public void operatorClick(View view) {
      presenter.operatorClick((String) view.getTag());
   }
   public void numberClick(View view) {
      presenter.numberClick(Integer.parseInt((String) view.getTag()));
   }
}