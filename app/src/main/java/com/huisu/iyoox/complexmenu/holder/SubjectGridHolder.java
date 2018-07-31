package com.huisu.iyoox.complexmenu.holder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.entity.BookDetailsModel;
import com.huisu.iyoox.util.ImageLoader;

import java.util.List;


/**
 * 科目
 * Created by vonchenchen on 2016/4/5 0005.
 */
public class SubjectGridHolder extends BaseWidgetHolder<List<BookDetailsModel>> {

    private List<BookDetailsModel> mDataList;

    private GridView mRightListView;

    private BookGridViewAdapter mRightAdapter;

    private int mRightSelectedIndex = 0;

    /**
     * 记录右侧条目对勾位置
     */
    private ImageView mRightRecordImageView = null;

    private OnRightListViewItemSelectedListener mOnRightListViewItemSelectedListener;

    public SubjectGridHolder(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.layout_holder_subject_grid, null);
        mRightListView = view.findViewById(R.id.book_grid_View);

        mRightListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mRightSelectedIndex = position;
                if (mOnRightListViewItemSelectedListener != null) {
                    List<BookDetailsModel> dataList = mDataList;
                    BookDetailsModel text = dataList.get(mRightSelectedIndex);
                    mOnRightListViewItemSelectedListener.OnRightListViewItemSelected(mRightSelectedIndex, text);
                }
            }
        });
        return view;
    }

    @Override
    public void refreshView(List<BookDetailsModel> data) {

    }

    public void refreshData(List<BookDetailsModel> data, int rightSelectedIndex) {

        this.mDataList = data;

        mRightSelectedIndex = rightSelectedIndex;

        mRightAdapter = new BookGridViewAdapter(data);

        mRightListView.setAdapter(mRightAdapter);
    }

    public void refreshData() {
        mRightAdapter = new BookGridViewAdapter(mDataList);
        mRightListView.setAdapter(mRightAdapter);
    }

    public void clearSelectedInfo() {

    }

    public class BookGridViewAdapter extends BaseAdapter {
        List<BookDetailsModel> mlist;

        public BookGridViewAdapter(List<BookDetailsModel> list) {
            this.mlist = list;
        }

        @Override
        public int getCount() {
            return mlist.size();
        }

        @Override
        public Object getItem(int i) {
            return mlist.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CustomViewHolder holder;
            if (convertView == null) {
                holder = new CustomViewHolder();
                convertView = View.inflate(mContext, R.layout.item_book_gridview, null);
                holder.nameTv = convertView.findViewById(R.id.name_tv);
                holder.image = convertView.findViewById(R.id.image);
                convertView.setTag(holder);
            } else {
                holder = (CustomViewHolder) convertView.getTag();
            }
            BookDetailsModel bean = mlist.get(position);
            holder.nameTv.setText(bean.getName());
            ImageLoader.load(mContext, holder.image,
                    TextUtils.isEmpty(bean.getCover_url()) ? "" : bean.getCover_url()
                    , R.drawable.icon_no_img);
            return convertView;
        }


    }

    static class CustomViewHolder {
        ImageView image;
        TextView nameTv;
    }

    public void setOnRightListViewItemSelectedListener(OnRightListViewItemSelectedListener onRightListViewItemSelectedListener) {
        this.mOnRightListViewItemSelectedListener = onRightListViewItemSelectedListener;
    }

    public interface OnRightListViewItemSelectedListener {
        void OnRightListViewItemSelected(int rightIndex, BookDetailsModel model);
    }
}
