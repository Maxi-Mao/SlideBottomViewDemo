package com.maxi.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.maxi.demo.widgets.DemoPopupBottomView;
import com.maxi.sblibrary.BasePopupBottomView;
import com.maxi.sblibrary.PopupBottomRecyclerView;
import com.maxi.sblibrary.PopupParentView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button ivShow;
    private DemoPopupBottomView dpbvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findview();
        initListener();
    }

    private void findview() {
        ivShow = findViewById(R.id.btn_show);
        dpbvContent = findViewById(R.id.dpbv_content);
    }

    private void initListener() {
        ivShow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_show:
                if (dpbvContent != null) {
                    dpbvContent.show();
                }
                break;
            default:
                break;
        }
    }
}
