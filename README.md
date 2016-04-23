# Common iOS Views ported to Android

## Requirements
Android Studio (gradle) 
Min sdk 15

## Demo

![Demo](http://i.imgur.com/YCOgHzr.gif)

## Usage

Add this to your build.gradle dependencies

```java
compile 'com.github.shubhank101.sgiosviews:sgiosviews-lib:1.0.0'
```

## SGStepper

A port of iOS UIStepper, can be used a simple number picker

### Usage

Add the following code to the root element of layout
```xml
xmlns:stepper="http://schemas.android.com/apk/res-auto"
```
Adding the element to the xml 

```xml
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
```java
SGStepper stepper = (SGStepper)findViewById(R.id.stepper);
stepper.setValueChangedlistener(new SGStepper.SGStepperListener() {

    @Override
    public void valuesChanged(SGStepper stepper, int newValue) {
    }

    @Override
    public void afterValueChanged(SGStepper stepper, int finalValue) {
    }
});
```

## SGPickerView

A very basic port of iOS UIPickerView.

### Usage

Add the following code to the root element of layout

```xml
xmlns:picker="http://schemas.android.com/apk/res-auto"
```
Adding the element to the xml 

```xml
<com.sgiosviews.SGPickerView
    android:id="@+id/pickerView"
    android:layout_width="match_parent"
    android:layout_marginRight="20dp"
    android:layout_marginLeft="20dp"
    android:layout_marginBottom="40dp"
    android:layout_height="wrap_content"
    picker:defaultTextColor="#ccc"
    picker:selectedTextColor="#666"/>
```

Setting Picker Values in Java
```java
pickerView = (SGPickerView)findViewById(R.id.pickerView);

ArrayList<String> items = new ArrayList<String>();
items.add("Funny");
items.add("Awesomeeeee");
pickerView.setItems(items);
```

Listening to any new selection of element
```java
pickerView.setPickerListener(new SGPickerView.SGPickerViewListener() {
    @Override
    public void itemSelected(String item, int index) {
        Toast.makeText(MainActivity.this, " Index = " + String.valueOf(index) + " Item name " + item, Toast.LENGTH_SHORT).show();
    }
});
```

Public methods to access selected index or item at any time
```java
pickerView.getCurrentSelectedItemIndex();
pickerView.getCurrentSelectedItem();
```

## Developed By

Shubhank Gupta, shubhankscores@gmail.com

## License

Copyright (c) 2016 Shubhank Gupta, Licensed under the MIT license.
