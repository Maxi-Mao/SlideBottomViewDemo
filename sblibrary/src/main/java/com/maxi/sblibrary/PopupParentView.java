package com.maxi.sblibrary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.customview.widget.ViewDragHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * PopupBottomView外面一层viewgroup
 * 用于拖动子View 判断拖动距离 并消失
 * Created by maxi on 2018/10/16.
 */

public class PopupParentView extends RelativeLayout {
    private CallBack mCallBack;
    private ViewDragHelper mDragHelper;
    private View child;
    private View toCaptureView;
    private List<View> dontSlideView;
    private int childTop;

    public PopupParentView(Context context) {
        this(context, null);
    }

    public PopupParentView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PopupParentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        /*
         * 创建带回调接口的ViewDragHelper
		 */
        mDragHelper = ViewDragHelper.create(this, 10.0f, new DragHelperCallback());// 参数一:该类生成的对象(当前的ViewGroup)
        dontSlideView = new ArrayList<>();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        child = getChildAt(0);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        childTop = child.getTop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isCanDragge = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                final float x = ev.getX();
                final float y = ev.getY();
                toCaptureView = findSlideChild((int) x, (int) y);
                isCanDragge = toCaptureView != null
                        && toCaptureView == child;
                break;
        }
        if (isCanDragge) {
            mDragHelper.processTouchEvent(ev);
            return super.onInterceptTouchEvent(ev);
        } else {
            return mDragHelper.shouldInterceptTouchEvent(ev);
        }
    }

    public View findSlideChild(int x, int y) {
        for (View view : dontSlideView) {
            if (x >= view.getLeft() && x < view.getRight()
                    && y >= child.getTop() + view.getTop() && y < child.getTop() + view.getBottom()) {
                return view;
            }
        }

        if (x >= child.getLeft() && x < child.getRight()
                && y >= child.getTop() && y < child.getBottom()) {
            return child;
        }

        return null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    class DragHelperCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View arg0, int arg1) {
            // TODO Auto-generated method stub
            return arg0 == child;//返回true表示可以捕捉ViewGroup中的View
        }

        // 当前被捕获的View释放之后回调
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            int top = releasedChild.getTop();
            if (mCallBack != null && top > childTop + child.getMeasuredHeight() / 3) {
                mCallBack.onFinish((int) top);
            }
            mDragHelper.settleCapturedViewAt(0, childTop);
            invalidate();
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            if (top < getMeasuredHeight() - child.getMeasuredHeight()) {
                //限制上边界
                top = getMeasuredHeight() - child.getMeasuredHeight();
            } else if (top > getMeasuredHeight()) {
                //限制下边界
                top = getMeasuredHeight();
            }
            return top;
        }

        //记录值的变化
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
        }
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    /**
     * 设置屏蔽该slide的view 适用于一些view需要自己消费手势
     *
     * @param dontSlideView
     */
    public void setDontSlideView(List<View> dontSlideView) {
        this.dontSlideView = dontSlideView;
    }

    public interface CallBack {
        void onFinish(int height);
    }

    public void setCallBack(CallBack callBack) {
        mCallBack = callBack;
    }
}
