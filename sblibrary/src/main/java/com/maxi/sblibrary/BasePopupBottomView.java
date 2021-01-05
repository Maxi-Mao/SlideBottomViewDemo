package com.maxi.sblibrary;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 底部弹出控件基类
 * setPopupParentView 设置可滑动弹框父布局
 * setSlideView 设置可滑动区域及动画区域
 * setClose 设置关闭按钮
 * popupHeight 弹出高度
 * <p>
 * Created by maxi on 2019-10-22.
 */
public abstract class BasePopupBottomView extends RelativeLayout {

    public static final int DURATION = 300;
    private boolean isShowAnimation = false;//正在执行显示动画
    private boolean isCloseAnimation = false;//正在执行关闭动画
    private PopupParentView ppvContent;
    private ViewGroup llBottomView;
    private ImageView ivClose;

    public BasePopupBottomView(Context context) {
        this(context, null);
    }

    public BasePopupBottomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BasePopupBottomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
        ppvContent = setPopupParentView();
        llBottomView = setSlideView();
        ivClose = setClose();

        if (ivClose != null) {
            ivClose.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    close(0);
                }
            });
        }
        if (llBottomView != null) {
            llBottomView.setOnKeyListener(new OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_UP) && (keyCode == KeyEvent.KEYCODE_BACK)) {
                        close(0);
                        return true;
                    }
                    return false;
                }
            });
        }
        if (ppvContent == null) {
            return;
        }
        dontSlideViews(null);
        ppvContent.setCallBack(new PopupParentView.CallBack() {
            @Override
            public void onFinish(int height) {
                // TODO: 12/23/20 涉及Android 11 接口黑名单
                close(height);
            }
        });
    }

    public abstract void initViews(Context context);

    public abstract PopupParentView setPopupParentView();

    public abstract ViewGroup setSlideView();

    public abstract ImageView setClose();

    public abstract float popupHeight();

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initHeight();
    }

    private void initHeight(){
        if (llBottomView != null) {
            ViewGroup.LayoutParams lp = llBottomView.getLayoutParams();
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            lp.height = (int)popupHeight();
            llBottomView.setLayoutParams(lp);
        }
    }

    /**
     * 设置不消费滑动事件的view 设置进去后，手势在该view上下滑不触发下滑动画
     * 适用于一些view需要自己消费手势
     * @param dontSlideView
     */
    public void dontSlideViews(List<View> dontSlideView) {
        if (dontSlideView == null) {
            dontSlideView = new ArrayList<>();
        }
        dontSlideView.add(ivClose);
        ppvContent.setDontSlideView(dontSlideView);
    }

    /**
     * 展示
     */
    public void show() {
        if (isCloseAnimation || isShowAnimation || llBottomView == null) {
            return;
        }
        setVisibility(VISIBLE);
        llBottomView.setFocusable(true);
        llBottomView.setFocusableInTouchMode(true);
        llBottomView.requestFocus();
        llBottomView.setTranslationY(popupHeight());
        llBottomView.animate().translationY(0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isShowAnimation = false;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isShowAnimation = true;
            }
        }).setDuration(DURATION);
    }

    /**
     * 关闭
     * @param translationY
     */
    public void close(int translationY) {
        if (isCloseAnimation || isShowAnimation || llBottomView == null) {
            return;
        }
        llBottomView.setTranslationY(translationY);
        llBottomView.animate().translationY(popupHeight()).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isCloseAnimation = false;
                setVisibility(GONE);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isCloseAnimation = true;
            }
        }).setDuration(DURATION);
    }

}
