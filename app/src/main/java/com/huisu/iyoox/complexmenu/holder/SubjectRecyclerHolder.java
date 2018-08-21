package com.huisu.iyoox.complexmenu.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
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
 */
public class SubjectRecyclerHolder extends BaseWidgetHolder<List<BookDetailsModel>> {

    private List<BookDetailsModel> mDataList;

    private RecyclerView mRightListView;

    private BookGridViewAdapter mRightAdapter;

    private int mRightSelectedIndex = 0;

    /**
     * 记录右侧条目对勾位置
     */
    private ImageView mRightRecordImageView = null;

    private OnRightListViewItemSelectedListener mOnRightListViewItemSelectedListener;

    public SubjectRecyclerHolder(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.layout_holder_subject_recycler, null);
        mRightListView = view.findViewById(R.id.book_recycler_View);
        GridLayoutManager manager = new GridLayoutManager(mContext, 2);
        mRightListView.setLayoutManager(manager);
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

    public class BookGridViewAdapter extends RecyclerView.Adapter<BookGridViewAdapter.ViewHolder> {
        List<BookDetailsModel> mlist;

        public BookGridViewAdapter(List<BookDetailsModel> list) {
            this.mlist = list;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_book_gridview, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            BookDetailsModel bean = mlist.get(position);
            holder.nameTv.setText(bean.getName());
            if (mRightSelectedIndex == position) {
                holder.nameTv.setSelected(true);
                holder.coverBg.setSelected(true);
            } else {
                holder.nameTv.setSelected(false);
                holder.coverBg.setSelected(false);
            }
            ImageLoader.load(mContext, holder.image,
                    TextUtils.isEmpty(bean.getCover_url()) ? "" : bean.getCover_url()
                    , R.drawable.icon_no_img);
        }


        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public int getItemCount() {
            return mlist.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            ImageView image;
            TextView nameTv;
            View coverBg;

            public ViewHolder(View itemView) {
                super(itemView);
                nameTv = itemView.findViewById(R.id.name_tv);
                image = itemView.findViewById(R.id.image);
                coverBg = itemView.findViewById(R.id.book_cover_bg);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                mRightSelectedIndex = getLayoutPosition();
                if (mOnRightListViewItemSelectedListener != null) {
                    List<BookDetailsModel> dataList = mDataList;
                    BookDetailsModel text = dataList.get(mRightSelectedIndex);
                    mOnRightListViewItemSelectedListener.OnRightListViewItemSelected(mRightSelectedIndex, text);
                }
            }
        }
    }


    public void setOnRightListViewItemSelectedListener(OnRightListViewItemSelectedListener onRightListViewItemSelectedListener) {
        this.mOnRightListViewItemSelectedListener = onRightListViewItemSelectedListener;
    }

    public interface OnRightListViewItemSelectedListener {
        void OnRightListViewItemSelected(int rightIndex, BookDetailsModel model);
    }
}
