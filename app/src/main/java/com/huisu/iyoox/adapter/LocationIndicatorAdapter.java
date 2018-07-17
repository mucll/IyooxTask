package com.huisu.iyoox.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huisu.iyoox.Interface.MyOnItemClickListener;
import com.huisu.iyoox.R;
import com.huisu.iyoox.entity.BookBean;
import com.huisu.iyoox.entity.GradeListModel;
import com.huisu.iyoox.entity.SubjectModel;
import com.huisu.iyoox.util.ScrollSpeedLinearLayoutManger;
import com.huisu.iyoox.util.StringUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Function:
 * Date: 2017/8/30
 *
 * @author zhaojin
 * @since JDK 1.7
 */
public class LocationIndicatorAdapter extends RecyclerView.Adapter<LocationIndicatorAdapter.CustomViewHolder> {
    MyOnItemClickListener onItemClickListener;
    Context context;
    List<SubjectModel> mlist;
    private ScrollSpeedLinearLayoutManger mLayoutManager;

    public LocationIndicatorAdapter(Context context, List<SubjectModel> list) {
        this.context = context;
        this.mlist = list;
        getSelectPostion();
    }

    public int getSelectPostion() {
        for (int i = 0; i < StringUtils.getListSize(mlist); i++) {
            if (mlist.get(i).isSelect()) {
                return i;
            }
        }
        if (StringUtils.isEmpty(mlist)) {
            return -1;
        }
        mlist.get(0).setSelect(true);
        return 0;
    }

    public void setSelectPostion(int selectPostion) {
        for (int i = 0; i < StringUtils.getListSize(mlist); i++) {
            if (i == selectPostion) {
                mlist.get(i).setSelect(true);
            } else {
                mlist.get(i).setSelect(false);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_editlocation_tab_recyclerview, null);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        holder.text.setText(mlist.get(position).getName());
        if (mlist.get(position).isSelect()) {
            holder.text.setSelected(true);
        } else {
            holder.text.setSelected(false);
        }
    }

    @Override
    public int getItemCount() {
        return StringUtils.getListSize(mlist);
    }

    /**
     * 可以返回不同类型加载不同布局
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void setOnItemClickListener(MyOnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.text)
        TextView text;

        public CustomViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(getLayoutPosition(), v);
            }
        }
    }
}
