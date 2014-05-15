package com.jigbox.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;
import com.example.Carousel.R;

import java.util.ArrayList;

public class Carousel extends LinearLayout {

  private float touchDownX;
  private float touchDownY;
  private ViewFlipper viewFlipper;
  private CarouselDots carouselDots;


  private static final int DEFAULT_FLIP_INTERVAL = 4000;
  private static final int Y_SWIPE_THRESHOLD = 80;
  private static final int X_SWIPE_THRESHOLD = 50;

  public Carousel(Context context, AttributeSet attrs) {
    super(context, attrs);
  }


  public void setupUI(ArrayList<View> views) {
    viewFlipper = viewFlipper();
    viewFlipper.removeAllViews();
    setupCarouselView();

    for(View view : views){
      viewFlipper.addView(view);
    }

    carouselDots.setupCarouselDots(viewFlipper.getChildCount());

    if (hasOneViewIn()) {
      carouselDots.setVisibility(View.GONE);
      viewFlipper.setAutoStart(false);
      viewFlipper.stopFlipping();
    }
    if (hasNoViewsIn()) setVisibility(View.GONE);
  }

  private void setupCarouselView() {
    viewFlipper.setAutoStart(true);
    viewFlipper.setInAnimation(getContext(), R.anim.in_from_right);
    viewFlipper.setOutAnimation(getContext(), R.anim.out_to_left);
    viewFlipper.setFlipInterval(DEFAULT_FLIP_INTERVAL);

    carouselDots = dots();

    viewFlipper.getInAnimation().setAnimationListener(new Animation.AnimationListener() {
      @Override
      public void onAnimationStart(Animation animation) {
      }

      @Override
      public void onAnimationEnd(Animation animation) {
        carouselDots.setChecked(viewFlipper.getDisplayedChild());
      }

      @Override
      public void onAnimationRepeat(Animation animation) {
      }
    });
  }

  private CarouselDots dots() {
    return (CarouselDots) findViewById(R.id.carousel_pageview_control);
  }

  private ViewFlipper viewFlipper() {
    return (ViewFlipper) findViewById(R.id.carousel_view_flipper);
  }

  private boolean hasNoViewsIn() {
    return viewFlipper.getChildCount() == 0;
  }

  private boolean hasOneViewIn() {
    return viewFlipper.getChildCount() == 1;
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        touchDownX = event.getX();
        break;
      case MotionEvent.ACTION_UP:
        float currentX = event.getX();
        if (Math.abs(touchDownX - currentX) > X_SWIPE_THRESHOLD) {
          int displayedChild = viewFlipper.getDisplayedChild();
          if (leftToRightSwipe(currentX)) {
            if (firstChild(displayedChild)) break;
            manualSwipeFromLeftToRight(displayedChild);
          } else {
            if (lastChild(displayedChild)) break;
            manualSwipeFromRightToLeft(displayedChild);
          }
        }
    }
    return true;
  }

  private boolean lastChild(int displayedChild) {
    return displayedChild == viewFlipper.getChildCount() - 1;
  }

  private boolean firstChild(int displayedChild) {
    return displayedChild == 0;
  }

  private boolean leftToRightSwipe(float currentX) {
    return touchDownX < currentX;
  }

  private void manualSwipeFromLeftToRight(int displayedChild) {
    viewFlipper.stopFlipping();
    viewFlipper.setInAnimation(getContext(), R.anim.in_from_left);
    viewFlipper.setOutAnimation(getContext(), R.anim.out_to_right);
    viewFlipper.showPrevious();
    carouselDots.setChecked(displayedChild - 1);
  }

  private void manualSwipeFromRightToLeft(int displayedChild) {
    viewFlipper.stopFlipping();
    viewFlipper.setInAnimation(getContext(), R.anim.in_from_right);
    viewFlipper.setOutAnimation(getContext(), R.anim.out_to_left);
    viewFlipper.showNext();
    carouselDots.setChecked(displayedChild + 1);
  }


  @Override
  public boolean onInterceptTouchEvent(MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        touchDownX = event.getX();
        touchDownY = event.getY();
        return false;
      case MotionEvent.ACTION_MOVE:
        if (Math.abs(touchDownX - event.getX()) > X_SWIPE_THRESHOLD && Math.abs(touchDownY - event.getY()) < Y_SWIPE_THRESHOLD) {
          getParent().requestDisallowInterceptTouchEvent(true);
          return true;
        } else return false;
    }
    return false;
  }


  public void refresh() {
    viewFlipper = viewFlipper();
    if (hasMoreThanOneChild()) {
      viewFlipper.setVisibility(VISIBLE);
      carouselDots.setVisibility(VISIBLE);
      carouselDots.clearCheck();
      setupCarouselView();
      carouselDots.setupCarouselDots(viewFlipper.getChildCount());
      carouselDots.setChecked(viewFlipper.getDisplayedChild());
      viewFlipper.startFlipping();
    }
  }

  private boolean hasMoreThanOneChild() {
    return viewFlipper.getChildCount() > 1;
  }
}
