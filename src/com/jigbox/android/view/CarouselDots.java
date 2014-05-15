package com.jigbox.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import com.example.Carousel.R;

public class CarouselDots extends LinearLayout {
  private int childCount;

  public CarouselDots(Context context) {
    super(context);
  }

  public CarouselDots(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public void setupCarouselDots(int childCount) {
    setUpDots(childCount);
  }

  public void clearCheck() {
    for(int i=0; i<childCount; i++) {
      getChildAt(i).setBackgroundResource(R.drawable.carousel_indicator_off);
    }
  }

  private void check(int index) {
    clearCheck();
    getChildAt(index-1).setBackgroundResource(R.drawable.carousel_indicator_on);
  }

  private void setUpDots(int childCount) {
    this.childCount = childCount;
    int radioButtonIndex = 1;
    removeAllViews();
    LayoutInflater inflater = LayoutInflater.from(getContext());
    do {
      View dotView = inflater.inflate(R.layout.dots_layout, null);
      dotView.setId(radioButtonIndex++);
      dotView.setClickable(false);
      addView(dotView);
    } while (--childCount > 0);
    check(1);
  }

  public void setChecked(int childAtIndex) {
      check(childAtIndex+1);
  }
}