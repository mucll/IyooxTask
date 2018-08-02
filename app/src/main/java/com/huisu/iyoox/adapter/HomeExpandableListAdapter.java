package com.huisu.iyoox.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.entity.VideoGroupModel;
import com.huisu.iyoox.entity.VideoModel;
import com.huisu.iyoox.entity.VideoTitleModel;
import com.huisu.iyoox.views.ItemVideoView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author:dl
 * @function:
 * @date: 2018/7/12
 */
public class HomeExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<VideoGroupModel> data;

    public HomeExpandableListAdapter(Context context, List<VideoGroupModel> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getGroupCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return data.get(groupPosition).getZhishidian().size();
    }

    @Override
    public VideoGroupModel getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public VideoModel getChild(int groupPosition, int childPosition) {
        return data.get(groupPosition).getZhishidian().get(childPosition);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        Groupitem groupitem;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_home_list_group_layout, parent, false);
            groupitem = new Groupitem();
            groupitem.textView = convertView.findViewById(R.id.item_book_chapter_tv);
            convertView.setTag(groupitem);
        } else {
            groupitem = (Groupitem) convertView.getTag();
        }
        VideoGroupModel groupModel = data.get(groupPosition);
        groupitem.textView.setText(groupModel.getZhangjie_name());
        return convertView;
    }

    class Groupitem {
        TextView textView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_home_list_child_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final VideoModel videoModel = data.get(groupPosition).getZhishidian().get(childPosition);
        holder.zhishidianTv.setText(videoModel.getZhishidian_name());
        String base = context.getResources().getString(R.string.video_count_text);
        holder.videoCount.setText(String.format(base, videoModel.getZsd_count() + ""));
        holder.videoPgaeCount.setText(videoModel.getPage_count());
        holder.videContent.setData(videoModel.getShipinlist(), videoModel.getZhishidian_name());
        if (selectGroup == groupPosition && selectChild == childPosition) {
            holder.videContent.setVisibility(View.VISIBLE);
            holder.moreUnfoldIv.setImageDrawable(context.getResources().getDrawable(R.drawable.less_black));
        } else {
            holder.videContent.setVisibility(View.GONE);
            holder.moreUnfoldIv.setImageDrawable(context.getResources().getDrawable(R.drawable.more_unfold_black));
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectGroup == groupPosition && selectChild == childPosition) {
                    selectGroup = -1;
                    selectChild = -1;
                    holder.videContent.setVisibility(View.GONE);
                    holder.moreUnfoldIv.setImageDrawable(context.getResources().getDrawable(R.drawable.more_unfold_black));
                } else {
                    selectGroup = groupPosition;
                    selectChild = childPosition;
                    holder.videContent.setVisibility(View.VISIBLE);
                    holder.moreUnfoldIv.setImageDrawable(context.getResources().getDrawable(R.drawable.less_black));
                }

            }
        });
        return convertView;
    }

    private int selectGroup = -1;
    private int selectChild = -1;

    static class ViewHolder {
        private TextView zhishidianTv;
        private ImageView moreUnfoldIv;
        private ItemVideoView videContent;
        private TextView videoCount;
        private TextView videoPgaeCount;

        ViewHolder(View view) {
            zhishidianTv = view.findViewById(R.id.item_zhishidian_tv);
            moreUnfoldIv = view.findViewById(R.id.more_unfold_iv);
            videContent = view.findViewById(R.id.item_video_layout);
            videoCount = view.findViewById(R.id.item_video_count_tv);
            videoPgaeCount = view.findViewById(R.id.item_page_count_tv);
        }
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return getCombinedChildId(groupPosition, childPosition);
    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return super.getCombinedChildId(groupId, childId);
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    public void refreshSelectCode() {
        if (selectChild != -1) {
            selectGroup = -1;
            selectChild = -1;
            notifyDataSetChanged();
        }
    }

}
