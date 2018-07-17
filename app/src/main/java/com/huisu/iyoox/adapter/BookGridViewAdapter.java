package com.huisu.iyoox.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.entity.BookDetailsModel;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Function:课本教材版本封面
 * Date: 2018/7/17
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class BookGridViewAdapter extends BaseAdapter {
    Context context;
    List<BookDetailsModel> mlist;

    public BookGridViewAdapter(Context context, List<BookDetailsModel> list) {
        this.context = context;
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        CustomViewHolder holder;
        if (view == null) {
            view = View.inflate(context, R.layout.item_book_gridview, null);
            holder = new CustomViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (CustomViewHolder) view.getTag();
        }
        BookDetailsModel bean = mlist.get(i);
        holder.nameTv.setText(bean.getName());
        return view;
    }

    static class CustomViewHolder {
        @Bind(R.id.image)
        ImageView image;
        @Bind(R.id.name_tv)
        TextView nameTv;

        public CustomViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
