# Common iOS Views ported to Android

## Requirements
Android Studio (gradle) 
Min sdk 15

## Demo

![Demo](http://i.imgur.com/YCOgHzr.gif)

## Usage

Add this to to your build.gradle dependencies
`compile 'com.github.shubhank101.sgiosviews:sgiosviews-lib:1.0.0'`

## SGStepper

A port of iOS UIStepper, can be used a simple number picker

### Usage

Add the following code to the root element of layout
```
xmlns:stepper="http://schemas.android.com/apk/res-auto"
```
Adding the element to the xml 

```
<com.sgiosviews.SGStepper    
    android:id="@+id/stepper"
    android:layout_width="wrap_content"
    android:layout_height="20dp"
    stepper:roundedradius="0dp"
    stepper:bordercolor="#fff"
    stepper:highlightcolor="#ccc"
/>
```

Getting Values in Java
```
SGStepper stepper = (SGStepper)findViewById(R.id.stepper);
stepper.setValueChangedlistener(new SGStepper.SGStepperListener() {

    @Override
    public void valuesChanged(SGStepper stepper, int newValue) {
        stepper1Tv.setText(String.valueOf(newValue));
    }

    @Override
    public void afterValueChanged(SGStepper stepper, int finalValue) {
    }
});
```

## SGPickerView

Info
A very basic port of iOS UIPickerView.




## Developed By

Shubhank Gupta, shubhankscores@gmail.com

## License

Copyright (c) 2016 Shubhank Gupta, Licensed under the MIT license.
