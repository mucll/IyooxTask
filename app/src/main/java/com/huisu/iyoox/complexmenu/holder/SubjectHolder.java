package com.huisu.iyoox.complexmenu.holder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.entity.BookEditionModel;

import java.util.List;

/**
 * 科目
 * Created by vonchenchen on 2016/4/5 0005.
 */
public class SubjectHolder extends BaseWidgetHolder<List<String>> {

    private List<String> mDataList;

    private ListView mRightListView;

    private RightAdapter mRightAdapter;

    private int mRightSelectedIndex = 0;

    /**
     * 记录右侧条目对勾位置
     */
    private ImageView mRightRecordImageView = null;

    private OnRightListViewItemSelectedListener mOnRightListViewItemSelectedListener;

    public SubjectHolder(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.layout_holder_subject, null);
        mRightListView = (ListView) view.findViewById(R.id.listView2);

        mRightListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mRightSelectedIndex = position;
                if (mOnRightListViewItemSelectedListener != null) {
                    List<String> dataList = mDataList;
                    String text = dataList.get(mRightSelectedIndex);
                    mOnRightListViewItemSelectedListener.OnRightListViewItemSelected(mRightSelectedIndex, text);
                }
            }
        });
        return view;
    }

    @Override
    public void refreshView(List<String> data) {

    }

    public void refreshData(List<String> data, int rightSelectedIndex) {

        this.mDataList = data;

        mRightSelectedIndex = rightSelectedIndex;

        mRightAdapter = new RightAdapter(data, mRightSelectedIndex);

        mRightListView.setAdapter(mRightAdapter);
    }

    public void refreshData() {
        mRightAdapter = new RightAdapter(mDataList, mRightSelectedIndex);
        mRightListView.setAdapter(mRightAdapter);
    }

    public void clearSelectedInfo() {

    }

    private class RightAdapter extends BaseAdapter {

        private List<String> mRightDataList;

        public RightAdapter(List<String> list, int rightSelectedIndex) {
            this.mRightDataList = list;
            mRightSelectedIndex = rightSelectedIndex;
        }

        @Override
        public int getCount() {
            return mRightDataList.size();
        }

        @Override
        public Object getItem(int position) {
            return mRightDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            RightViewHolder holder;
            if (convertView == null) {
                holder = new RightViewHolder();
                convertView = View.inflate(mContext, R.layout.layout_child_menu_item, null);
                holder.rightText = (TextView) convertView.findViewById(R.id.child_textView);
                holder.selectedImage = (ImageView) convertView.findViewById(R.id.item_select_iv);
                convertView.setTag(holder);
            } else {
                holder = (RightViewHolder) convertView.getTag();
            }
            holder.rightText.setText(mRightDataList.get(position));
            if (mRightSelectedIndex == position) {
                holder.selectedImage.setBackgroundResource(R.drawable.btn_selected);
            } else {
                holder.selectedImage.setBackgroundResource(R.drawable.btn_regular);
            }
            return convertView;
        }
    }

    private static class RightViewHolder {
        TextView rightText;
        ImageView selectedImage;
    }

    public void setOnRightListViewItemSelectedListener(OnRightListViewItemSelectedListener onRightListViewItemSelectedListener) {
        this.mOnRightListViewItemSelectedListener = onRightListViewItemSelectedListener;
    }

    public interface OnRightListViewItemSelectedListener {
        void OnRightListViewItemSelected(int rightIndex, String model);
    }
}
