# TouchSwitchButton
A widget for touch swipe to select actions.

![TouchSwitchButton](https://github.com/BennyWang/TouchSwitchButton/blob/master/art/screen_shot.png)

## Usage

### TouchSwitchButton

Declare a TouchSwitchButton inside your XML layout file with a content like an RelativeLayout or whatever.

```xml

<com.benny.library.tsbutton.TouchSwitchButton
        android:id="@+id/two_way"
        android:layout_width="match_parent"
        android:layout_height="50dp"
        android:padding="5dp"
        app:rsb_radius="10000dp"
        app:rsb_direction="TwoWay"
        app:rsb_thumb="@drawable/bg_circle_red"
        app:tsb_startColor="@android:color/holo_purple"
        app:rsb_toLeftEndColor="@android:color/holo_green_light"
        app:rsb_toRightEndColor="@android:color/holo_orange_light"
        />

```

Set Listener for ToLeft and ToRight actions

```java

public interface OnActionSelectedListener {
    // return value represents delay for thumb to go back it's initial position
    int onSelected();
}

TouchSwitchButton twoWay = (TouchSwitchButton) findViewById(R.id.two_way);
twoWay.setOnToLeftSelectedListener(onToLeft);
twoWay.setOnToRightSelectedListener(onToRight);

```

## Customization

You can change several attributes in the XML file

TouchSwitchButton default background is a round rect with color you specified in tsb_startColor if you are not set background for TouchSwitchButton

* app:rsb_radius [dimension def:10000] --> radius for default round rect background.
* app:tsb_startColor [color def:@android:color/white] --> default color for default round rect background, if you set background for TouchSwitchButton, this attr will be ignored.
* app:rsb_toLeftEndColor [color def:@android:color/white] --> default background's color when you touch move to left, if you set background for TouchSwitchButton, this attr will be ignored.
* app:rsb_toRightEndColor [color def:@android:color/white] --> default background's color when you touch move to right, if you set background for TouchSwitchButton, this attr will be ignored.
* app:rsb_direction  --> swipe direction for TouchSwitchButton.
* app:rsb_thumb [drawable def:400] --> thumb drawable for touch, must be set.

### Using with Gradle

```gradle
dependencies {
    compile 'com.benny.library:tsbutton:0.1.0'
}
```

### Discussion

QQ Group: 516157585
