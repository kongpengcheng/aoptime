/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 * @author Fernando Cejas (the android10 coder)
 */
package com.example.myaspectjapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.myaspectjapplication.R;

import org.android10.gintonic.annotation.DebugTrace;


/**
 *
 */
public class RelativeLayoutTestActivity extends Activity {
  Button btn_test,btn_test2;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_relative_layout_test);
    btn_test=(Button)findViewById(R.id.btn_test);
    btn_test.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        testAOP();
      }
    });
    btn_test2=(Button)findViewById(R.id.btn_test2);
    btn_test2.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

      }
    });

  }
  @DebugTrace
  private  void testAOP(){

    int cunt=0;
    for ( int i=0;i<1000;i++){
      cunt++;

    }
    //Log.d("ydc","cunt:"+cunt+"");
  }
}
