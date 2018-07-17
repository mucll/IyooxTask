package com.huisu.iyoox.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.entity.VideoTitleModel;
import com.huisu.iyoox.entity.VideoModel;
import com.huisu.iyoox.views.ItemVideoView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookFragmentListAdapter extends BaseAdapter {
    private Context mContext;
    private List<VideoModel> mData;
    private Map<Integer, Integer> cache = new HashMap<>();

    public BookFragmentListAdapter(Context mContext, List<VideoModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        final ViewHolder holder;
//        if (convertView == null) {
//            convertView = View.inflate(mContext, R.layout.item_fragment_book_layout, null);
//            holder = new ViewHolder(convertView);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//        final VideoModel bookBean = mData.get(position);
//        //map不包含说明没有缓存,章节名称显示出来
//        if (!cache.containsKey(bookBean.getZhangjie_id())) {
//            cache.put(bookBean.getZhangjie_id(), position);
//            holder.chapterTv.setVisibility(View.VISIBLE);
//            holder.chapterTv.setText(bookBean.getZhangjie_name());
//        } else {
//            //根据缓存的数据,来显示章节
//            int indexOf = cache.get(bookBean.getZhangjie_id());
//            if (indexOf == position) {
//                holder.chapterTv.setVisibility(View.VISIBLE);
//            } else {
//                holder.chapterTv.setVisibility(View.GONE);
//            }
//        }
//        holder.chapterTv.setText(bookBean.getZhangjie_name());
//        holder.zhishidianTv.setText(bookBean.getZhishidian_name());
//        String base = mContext.getResources().getString(R.string.video_count_text);
//        holder.videoCount.setText(String.format(base, bookBean.getShipinlist().size() + ""));
//        holder.moreUnfoldIv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (bookBean.isSelect()) {
//                    holder.videContent.setVisibility(View.GONE);
//                    bookBean.setSelect(false);
//                    holder.moreUnfoldIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.more_unfold_black));
//                } else {
//                    holder.videContent.setVisibility(View.VISIBLE);
//                    bookBean.setSelect(true);
//                    holder.moreUnfoldIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.less_black));
//                }
//            }
//        });
//        holder.videContent.removeAllViews();
//        for (VideoTitleModel titleModel : bookBean.getShipinlist()) {
//            ItemVideoView itemVideoView = new ItemVideoView(mContext);
//            itemVideoView.setData(titleModel);
//            holder.videContent.addView(itemVideoView);
//        }

        return convertView;
    }

    static class ViewHolder {
        private TextView chapterTv;
        private TextView zhishidianTv;
        private ImageView moreUnfoldIv;
        private LinearLayout videContent;
        private TextView videoCount;

        ViewHolder(View view) {
            chapterTv = view.findViewById(R.id.item_book_chapter_tv);
            zhishidianTv = view.findViewById(R.id.item_zhishidian_tv);
            moreUnfoldIv = view.findViewById(R.id.more_unfold_iv);
            videContent = view.findViewById(R.id.item_video_layout);
            videoCount = view.findViewById(R.id.item_video_count_tv);
        }
    }
}
