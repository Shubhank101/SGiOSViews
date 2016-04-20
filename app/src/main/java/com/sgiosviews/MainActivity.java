package com.sgiosviews.app;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sgiosviews.SGPickerView;
import com.sgiosviews.SGStepper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    SGPickerView pickerView = null;

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
                    pickerView.setVisibility(View.GONE);

                } else {
                    stepper1.setBorderColor(Color.BLUE);
                    stepper1.setHighlightColor(Color.BLUE);
                    pickerView.setVisibility(View.VISIBLE);
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

        pickerView = (SGPickerView)findViewById(R.id.pickerView);
        ArrayList<String> items = new ArrayList<String>();

        items.add("Shubhank");
        items.add("Funny");
        items.add("Awesomeeeee");
        items.add("Androidd");
        items.add("Something long here");
        items.add("SGPickerView");

        pickerView.setItems(items);


        pickerView.setPickerListener(new SGPickerView.SGPickerViewListener() {
            @Override
            public void itemSelected(String item, int index) {
                Toast.makeText(MainActivity.this, " Index = " + String.valueOf(index) + " Item name " + item, Toast.LENGTH_SHORT).show();
            }
        });

        Button showPicker = (Button)findViewById(R.id.showPickerButton);
        showPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pickerView.getVisibility() == View.VISIBLE) {
                    pickerView.setVisibility(View.GONE);
                } else {
                    pickerView.setVisibility(View.VISIBLE);
                }
            }
        });


        Button pickerValueButton = (Button)findViewById(R.id.showValuePickerButton);
        pickerValueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this," Index = " + String.valueOf(pickerView.getCurrentSelectedItemIndex()) + " Item name " + pickerView.getCurrentSelectedItem(),Toast.LENGTH_SHORT).show();
            }
        });

        Button changeColorButton = (Button)findViewById(R.id.changeColorButton);
        changeColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (pickerView.getCurrentSelectedItemIndex() == 0) {
                        pickerView.setDefaultItemTextColor(Color.rgb(140, 0, 0));
                        pickerView.setSelectedItemTextColor(Color.rgb(255, 0, 0));
                    }
                    else if (pickerView.getCurrentSelectedItemIndex() == 1) {
                        pickerView.setDefaultItemTextColor(Color.rgb(0, 140, 0));
                        pickerView.setSelectedItemTextColor(Color.rgb(0, 255, 0));
                    }
                    else if (pickerView.getCurrentSelectedItemIndex() == 2) {
                        pickerView.setDefaultItemTextColor(Color.rgb(0, 0, 140));
                        pickerView.setSelectedItemTextColor(Color.rgb(0, 0, 255));
                    }
                    else if (pickerView.getCurrentSelectedItemIndex() == 3) {
                        pickerView.setDefaultItemTextColor(Color.rgb(70, 70, 0));
                        pickerView.setSelectedItemTextColor(Color.rgb(140, 140, 0));
                    }
                    else if (pickerView.getCurrentSelectedItemIndex() == 4) {

                    }


            }
        });

    }

}
