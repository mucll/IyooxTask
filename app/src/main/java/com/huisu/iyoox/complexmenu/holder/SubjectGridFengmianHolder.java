package com.huisu.iyoox.complexmenu.holder;

import android.content.Context;
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
import com.huisu.iyoox.entity.ArtBookZhangJieModel;
import com.huisu.iyoox.entity.BookDetailsModel;
import com.huisu.iyoox.util.ImageLoader;

import java.util.List;


/**
 * 封面图片View
 */
public class SubjectGridFengmianHolder extends BaseWidgetHolder<List<ArtBookZhangJieModel>> {

    private List<ArtBookZhangJieModel> mDataList;

    private RecyclerView mRightListView;

    private BookGridViewAdapter mRightAdapter;

    private int mRightSelectedIndex = 0;

    public void setmRightSelectedIndex(int mRightSelectedIndex) {
        this.mRightSelectedIndex = mRightSelectedIndex;
        mRightAdapter.notifyDataSetChanged();
    }

    /**
     * 记录右侧条目对勾位置
     */
    private ImageView mRightRecordImageView = null;

    private OnRightListViewItemSelectedListener mOnRightListViewItemSelectedListener;

    public SubjectGridFengmianHolder(Context context) {
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
    public void refreshView(List<ArtBookZhangJieModel> data) {

    }

    public void refreshData(List<ArtBookZhangJieModel> data, int rightSelectedIndex) {

        this.mDataList = data;

        mRightSelectedIndex = rightSelectedIndex;

        mRightAdapter = new BookGridViewAdapter(data);

        mRightListView.setAdapter(mRightAdapter);
    }

    public void refreshData(List<ArtBookZhangJieModel> data) {

        this.mDataList = data;

        mRightSelectedIndex = -1;

        mRightAdapter = new BookGridViewAdapter(data);

        mRightListView.setAdapter(mRightAdapter);
    }

    public void refreshData() {
        mRightAdapter = new BookGridViewAdapter(mDataList);
        mRightListView.setAdapter(mRightAdapter);
    }

    public class BookGridViewAdapter extends RecyclerView.Adapter<BookGridViewAdapter.ViewHolder> {
        List<ArtBookZhangJieModel> mlist;

        public BookGridViewAdapter(List<ArtBookZhangJieModel> list) {
            this.mlist = list;
        }


        @Override
        public BookGridViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_book_gridview, parent, false);
            BookGridViewAdapter.ViewHolder viewHolder = new BookGridViewAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(BookGridViewAdapter.ViewHolder holder, int position) {
            ArtBookZhangJieModel bean = mlist.get(position);
            holder.nameTv.setText(bean.getZhangjie_name());
            if (mRightSelectedIndex == position) {
                holder.nameTv.setSelected(true);
                holder.coverBg.setSelected(true);
            } else {
                holder.nameTv.setSelected(false);
                holder.coverBg.setSelected(false);
            }
            ImageLoader.load(mContext, holder.image,
                    TextUtils.isEmpty(bean.getCover_map()) ? "" : bean.getCover_map()
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
                    ArtBookZhangJieModel text = mDataList.get(mRightSelectedIndex);
                    mOnRightListViewItemSelectedListener.OnRightListViewItemSelected(mRightSelectedIndex, text);
                }
            }
        }
    }

    public void setOnRightListViewItemSelectedListener(OnRightListViewItemSelectedListener onRightListViewItemSelectedListener) {
        this.mOnRightListViewItemSelectedListener = onRightListViewItemSelectedListener;
    }

    public interface OnRightListViewItemSelectedListener {
        void OnRightListViewItemSelected(int rightIndex, ArtBookZhangJieModel model);
    }
}
