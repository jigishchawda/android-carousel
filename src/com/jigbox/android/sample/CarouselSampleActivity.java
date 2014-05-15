package com.jigbox.android.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import com.example.Carousel.R;
import com.jigbox.android.view.Carousel;

import java.util.ArrayList;

public class CarouselSampleActivity extends Activity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.carousel_sample_layout);
    Carousel carousel = (Carousel) findViewById(R.id.carousel_container);
    final View view1 = LayoutInflater.from(this).inflate(R.layout.carousel_view_layout, null, false);
    final View view2 = LayoutInflater.from(this).inflate(R.layout.carousel_view_layout, null, false);
    carousel.setupUI(new ArrayList<View>(){{
      add(view1);
      add(view2);
    }});
  }
}
