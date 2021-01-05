package com.maxi.demo.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.maxi.demo.R;
import com.maxi.demo.adapters.DemoPopupBottomAdapter;
import com.maxi.demo.utils.ScreenUtil;
import com.maxi.sblibrary.BasePopupBottomView;
import com.maxi.sblibrary.PopupBottomRecyclerView;
import com.maxi.sblibrary.PopupParentView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maxi on 2021/01/05.
 */
public class DemoPopupBottomView extends BasePopupBottomView {
    private View baseView;
    private PopupBottomRecyclerView recyclerView;
    private List<String> datas = new ArrayList<String>();

    public DemoPopupBottomView(Context context) {
        this(context, null);
    }

    public DemoPopupBottomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DemoPopupBottomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDatas();
        initWidget();
    }

    @Override
    public void initViews(Context context) {
        baseView = LayoutInflater.from(context).inflate(R.layout.view_popup_recyclerview, this);
        recyclerView = (PopupBottomRecyclerView) baseView.findViewById(R.id.rv_popup_bottom);
    }

    @Override
    public PopupParentView setPopupParentView() {
        return baseView.findViewById(R.id.ppv_content);
    }

    @Override
    public ViewGroup setSlideView() {
        return baseView.findViewById(R.id.ll_popup_bottom_view);
    }

    @Override
    public ImageView setClose() {
        return baseView.findViewById(R.id.iv_popup_bottom_close);
    }

    @Override
    public float popupHeight() {
        return ScreenUtil.getScreenHeight(getContext()) * 2 / 3.0f;
    }

    private void initDatas() {
        for (int index = 0; index <= 10; index++) {
            datas.add("item ===> " + index);
        }
    }

    private void initWidget() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new DemoPopupBottomAdapter(getContext(), datas));
    }
}
