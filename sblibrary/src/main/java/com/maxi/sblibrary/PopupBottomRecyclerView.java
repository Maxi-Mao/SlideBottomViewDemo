package com.maxi.sblibrary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 用于判断rv的手势监听
 * 滑动到最顶层后继续下滑手势交给父类消费
 * Created by maxi on 2018/10/16.
 */

public class PopupBottomRecyclerView extends RecyclerView {
    boolean stopSlide = true;
    boolean allowDrag = true;
    private int startY = 0;
    private int slideY = 50;

    public PopupBottomRecyclerView(Context context) {
        this(context, null);
    }

    public PopupBottomRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PopupBottomRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            startY = (int) event.getY();
            stopSlide = true;
            allowDrag = !canScrollVertically(-1);
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (!stopSlide) {
                getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            } else if (allowDrag) {
                int endY = (int) event.getY();
                if (endY - startY > slideY) {
                    stopSlide = false;
                    getParent().requestDisallowInterceptTouchEvent(false);
                    return false;
                }
            }
        }
        getParent().requestDisallowInterceptTouchEvent(stopSlide);
        return super.dispatchTouchEvent(event);
    }
}
