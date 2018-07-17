package com.huisu.iyoox.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;

import com.huisu.iyoox.Interface.MyOnItemClickListener;
import com.huisu.iyoox.adapter.LocationIndicatorAdapter;
import com.huisu.iyoox.entity.BookBean;
import com.huisu.iyoox.entity.GradeListModel;
import com.huisu.iyoox.entity.SubjectModel;
import com.huisu.iyoox.util.ScrollSpeedLinearLayoutManger;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class LocationIndicatorView extends RecyclerView {
    List<SubjectModel> mlist = new ArrayList<>();
    Context context;
    public LocationIndicatorAdapter adapter;
    private ScrollSpeedLinearLayoutManger mLayoutManager;
    private AdapterView.OnItemClickListener listener;

    public LocationIndicatorView(Context context) {
        this(context, null);
    }

    public LocationIndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mLayoutManager = new ScrollSpeedLinearLayoutManger(context, LinearLayoutManager.HORIZONTAL,
                false);
        mLayoutManager.setSpeedSlow();
        setLayoutManager(mLayoutManager);
        adapter = new LocationIndicatorAdapter(context, mlist);
        setAdapter(adapter);
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * 刷新
     */
    public void notifyView() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public void init(List<SubjectModel> list) {
        mlist = list;
        setHasFixedSize(true);
        adapter = new LocationIndicatorAdapter(context, mlist);
        setAdapter(adapter);
        adapter.setOnItemClickListener(new MyOnItemClickListener() {
            @Override
            public void onItemClick(int positions, View view) {
                //如果点击的是当前选中的position
                if (positions == adapter.getSelectPostion()) {
                    return;
                }
                adapter.setSelectPostion(positions);
                adapter.notifyDataSetChanged();
                if (listener != null) {
                    listener.onItemClick(null, view, positions, view.getId());
                }
            }
        });
    }

    /**
     * 获取当前选择的项
     *
     * @return
     */
    public SubjectModel getCurrentLocation() {
        return mlist.get(adapter.getSelectPostion() == -1 ? null : adapter.getSelectPostion());
    }

    /**
     * 获取选择的项
     *
     * @return
     */
    public int getSelection() {
        if (adapter == null) {
            return -1;
        }
        return adapter.getSelectPostion();
    }

    public void setSelection(int position) {
        if (adapter != null) {
            adapter.setSelectPostion(position);
        }
    }
}
