# Vertical Slider for Android
 [ ![Download](https://api.bintray.com/packages/2hamed/maven/VerticalSlider/images/download.svg) ](https://bintray.com/2hamed/maven/VerticalSlider/_latestVersion)
 [![Android Arsenal]( https://img.shields.io/badge/Android%20Arsenal-VerticalSlider-green.svg?style=flat )]( https://android-arsenal.com/details/1/7564 )
 
Have you ever seen those sleek volume and light sliders for iOS? Well here they are for Android as an extremely lightweight library.

![Showcase](showcase.gif)

It allows to set 3 different icons for different states of the slide: low, medium and high.

## Usage

As always it is available through jCenter:

```groovy
implementation 'com.hmomeni.verticalslider:verticalslider:0.2.0'
```

There are a number of properties which can be set either in xml or code:

### Icons

You can set icons directly by setting Bitmap values to them or you can use any of the helper methods to pass in a Resource Id:

```kotlin
iconHigh: Bitmap
iconMedium: Bitmap
iconLow: Bitmap

verticalSlider.setIconHighResourse(R.drawable.ic_volume_high)
verticalSlider.setIconMediumResourse(R.drawable.ic_volume_medium)
verticalSlider.setIconLowResourse(R.drawable.ic_volume_low)
```

### Max & Progress

`max` can be any integer larger than 0 and `progress` should be: 0 <= `progress` <= max

```
max: Int // the maximum amount that the slider will allow
progress: Int // the current progress of slider
```

### Other properties

```kotlin
verticalSlider.cornerRadius = dpToPx(10).toFloat()

// this gets called in every update of the progress
verticalSlider.onProgressChangeListener = object : VerticalSlider.OnSliderProgressChangeListener {
            override fun onChanged(progress: Int, max: Int) {
                // use progress and max to calculate percentage
            }
        }
```

```xml
<com.hmomeni.verticalslider.VerticalSlider
        android:id="@+id/verticalSlider"
        android:layout_width="120dp"
        android:layout_height="250dp"
        app:vs_cornerRadius="10dp"
        app:vs_iconHigh="@drawable/volume_high"
        app:vs_iconLow="@drawable/volume_low"
        app:vs_iconMedium="@drawable/volume_medium"
        app:vs_max="100"
        app:vs_progress="10" />
```

Feel free to post any issues or feature requests.