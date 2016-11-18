package com.benny.library.tsbutton;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by benny on 17/11/2016.
 */

public class TouchSwitchButton extends RelativeLayout {
    public static final int TWO_WAY = 0;
    public static final int TO_LEFT = 1;
    public static final int TO_RIGHT = 2;

    private ImageView thumbContainer;
    private OnActionSelectedListener onToLeftSelectedListener;
    private OnActionSelectedListener onToRightSelectedListener;

    private Drawable thumb;
    private int startColor;
    private int toLeftEndColor;
    private int toRightEndColor;
    private float radius;
    private int direction = TWO_WAY;

    public TouchSwitchButton(Context context) {
        super(context);
        setupThumb();
    }

    public TouchSwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        resolveAttributes(context, attrs);

        initView();
    }

    public TouchSwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        resolveAttributes(context, attrs);

        initView();
    }

    public void setOnToLeftSelectedListener(OnActionSelectedListener toLeftSelectedListener) {
        this.onToLeftSelectedListener = toLeftSelectedListener;
    }

    public void setOnToRightSelectedListener(OnActionSelectedListener toRightSelectedListener) {
        this.onToRightSelectedListener = toRightSelectedListener;
    }

    void resolveAttributes(Context context, AttributeSet attrs) {
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.TouchSwitchButton);
        thumb = t.getDrawable(R.styleable.TouchSwitchButton_tsb_thumb);
        direction = t.getInt(R.styleable.TouchSwitchButton_tsb_direction, TWO_WAY);
        startColor = t.getColor(R.styleable.TouchSwitchButton_tsb_startColor, Color.TRANSPARENT);
        toLeftEndColor = t.getColor(R.styleable.TouchSwitchButton_tsb_toLeftEndColor, Color.TRANSPARENT);
        toRightEndColor = t.getColor(R.styleable.TouchSwitchButton_tsb_toRightEndColor, Color.TRANSPARENT);
        radius = t.getDimension(R.styleable.TouchSwitchButton_tsb_radius, 10000);
        t.recycle();
    }

    private Drawable createBackground() {
        if(getBackground() == null) {
            //if not set background in layout xml, than create default round rect background
            float[] outerRadii = {radius, radius, radius, radius, radius, radius, radius, radius};
            RoundRectShape roundRectShape = new RoundRectShape(outerRadii, null, null);
            ShapeDrawable drawable = new ShapeDrawable(roundRectShape);
            drawable.getPaint().setColor(startColor);
            drawable.getPaint().setStyle(Paint.Style.FILL);
            return drawable;
        }
        return getBackground();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(createBackground());
        } else {
            setBackgroundDrawable(createBackground());
        }
    }

    private void setupThumb() {
        thumbContainer = new ImageView(getContext());
        thumbContainer.setImageDrawable(thumb);
        thumbContainer.setOnTouchListener(new OnThumbTouchListener());

        addView(thumbContainer, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    void setThumbSize(int thumbWidth, int thumbHeight) {
        ViewGroup.LayoutParams lp = thumbContainer.getLayoutParams();
        lp.width = thumbWidth;
        lp.height = thumbHeight;
        thumbContainer.setLayoutParams(lp);
    }

    private float getThumbPosition() {
        return thumbContainer.getX() - getPaddingLeft();
    }

    private float getThumbInitialPosition(float containerWidth, float thumbWidth) {
        float position = 0;
        switch (direction) {
            case TWO_WAY:
                position = (containerWidth - getPaddingLeft() - getPaddingRight() - thumbWidth) / 2;
                break;
            case TO_LEFT:
                position = getThumbMaxPosition(containerWidth, thumbWidth);
                break;
        }
        return position;
    }

    private float getThumbInitialPosition() {
        return getThumbInitialPosition(getWidth(), thumbContainer.getWidth());
    }


    private float getThumbMaxPosition(float containerWidth, float thumbWidth) {
        return containerWidth - getPaddingRight() - getPaddingLeft() - thumbWidth;
    }

    private float getThumbMaxPosition() {
        return getWidth() - getPaddingRight() - getPaddingLeft() - thumbContainer.getWidth();
    }

    float getOffsetPercentage(float delta) {
        double percent;
        switch (direction) {
            case TWO_WAY:
                percent = Math.abs(delta) / (0.5 * getThumbMaxPosition());
                break;
            default:
                percent = Math.abs(delta) / getThumbMaxPosition();
        }
        return (float)(percent > 1 ? 1f : percent);
    }

    public void changeBackground(float delta) {
        int currentColor = (int)new ArgbEvaluator().evaluate(getOffsetPercentage(delta), startColor, delta > 0 ? toRightEndColor : toLeftEndColor);
        ShapeDrawable background = (ShapeDrawable)getBackground().mutate();
        background.getPaint().setColor(currentColor);
        background.invalidateSelf();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setupThumb();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int receivedWidth = MeasureSpec.getSize(widthMeasureSpec);
        int receivedHeight = MeasureSpec.getSize(heightMeasureSpec);

        int thumbWidth = 0;

        if(receivedHeight == 0 && thumb.getIntrinsicHeight() != 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(thumb.getIntrinsicHeight() + getPaddingBottom() + getPaddingTop(), MeasureSpec.EXACTLY);
        }
        else if(receivedHeight != 0) {
            if(thumb.getIntrinsicHeight() == 0) {
                thumbWidth = receivedHeight - getPaddingBottom() - getPaddingTop();
                setThumbSize(thumbWidth, thumbWidth);
            }
            else {
                float heightWithoutPadding = receivedHeight  - getPaddingBottom() - getPaddingTop();
                double ratio = heightWithoutPadding / thumb.getIntrinsicHeight();
                thumbWidth = (int) (thumb.getIntrinsicWidth() * ratio);
                setThumbSize(thumbWidth, (int) heightWithoutPadding);
            }
        }

        thumbContainer.setTranslationX(getThumbInitialPosition(receivedWidth, thumbWidth));

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public interface OnActionSelectedListener {
        int onSelected();
    }

    private class OnThumbTouchListener implements OnTouchListener {
        private static final int THRESHOLD = 20;

        private float initialX = 0;
        private float initialTouchX = 0;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            float delta, targetX;

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    requestDisallowInterceptTouchEvent(true);
                    initialX = getThumbInitialPosition();
                    initialTouchX = event.getRawX();
                    return true;
                case MotionEvent.ACTION_UP:
                    requestDisallowInterceptTouchEvent(false);
                    delta = event.getRawX() - initialTouchX;
                    targetX = initialX + delta;

                    if(targetX < 0) targetX = 0;
                    if(targetX > getThumbMaxPosition()) targetX = getThumbMaxPosition();

                    int delay = 0;
                    if(targetX < THRESHOLD && direction != TO_RIGHT) {
                        if(onToLeftSelectedListener != null) delay = onToLeftSelectedListener.onSelected();
                    }
                    else if(Math.abs(targetX - getThumbMaxPosition()) < THRESHOLD && direction != TO_LEFT) {
                        if(onToRightSelectedListener != null) delay = onToRightSelectedListener.onSelected();
                    }
                    restoreState(delta, delay);
                    return true;
                case MotionEvent.ACTION_MOVE:
                    delta = event.getRawX() - initialTouchX;
                    targetX = initialX + delta;

                    if(targetX < 0) targetX = 0;
                    if(targetX > getThumbMaxPosition()) targetX = getThumbMaxPosition();

                    thumbContainer.setTranslationX(targetX);
                    changeBackground(delta);
                    return true;

                case MotionEvent.ACTION_CANCEL:
                    requestDisallowInterceptTouchEvent(false);
                    restoreState(event.getRawX() - initialTouchX, 0);

            }
            return false;
        }

        private void restoreState(final float delta, int delay) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    ObjectAnimator.ofFloat(thumbContainer, "translationX", getThumbPosition(), initialX).setDuration(200).start();

                    ValueAnimator animation = ValueAnimator.ofFloat(delta, 0).setDuration(200);
                    animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            changeBackground((float)animation.getAnimatedValue());
                        }
                    });
                    animation.start();
                }
            }, delay);
        }
    }
}
