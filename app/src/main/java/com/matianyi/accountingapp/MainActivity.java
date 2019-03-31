package com.matianyi.accountingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

public class MainActivity extends AppCompatActivity {

    private TickerView tickerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecordBean bean = new RecordBean();


    }
}
