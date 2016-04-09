package com.sgiosviews.app;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.sgiosviews.SGStepper;
import com.sgiosviews.app.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        final TextView stepper1Tv = (TextView)findViewById(R.id.stepper1Tv);
        final SGStepper stepper1 = (SGStepper)findViewById(R.id.stepper1);
        stepper1.setValueChangedlistener(new SGStepper.SGStepperListener() {
            @Override
            public void valuesChanged(SGStepper stepper, int newValue) {
                stepper1Tv.setText(String.valueOf(newValue));
            }

            @Override
            public void afterValueChanged(SGStepper stepper, int finalValue) {
                stepper1Tv.setText(String.valueOf(finalValue));

                if (finalValue > 10) {
                    stepper1.setBorderColor(Color.MAGENTA);
                    stepper1.setHighlightColor(Color.MAGENTA);
                } else {
                    stepper1.setBorderColor(Color.BLUE);
                    stepper1.setHighlightColor(Color.BLUE);

                }

            }
        });

        final TextView stepper2Tv = (TextView)findViewById(R.id.stepper2Tv);
        final SGStepper stepper2 = (SGStepper)findViewById(R.id.stepper2);
        stepper2.setValueChangedlistener(new SGStepper.SGStepperListener() {
            @Override
            public void valuesChanged(SGStepper stepper, int newValue) {
                stepper2Tv.setText(String.valueOf(newValue));
            }

            @Override
            public void afterValueChanged(SGStepper stepper, int finalValue) {
                stepper2Tv.setText(String.valueOf(finalValue));

            }
        });

    }

}
