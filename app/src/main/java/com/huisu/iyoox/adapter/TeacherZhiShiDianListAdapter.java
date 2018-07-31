package com.huisu.iyoox.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.entity.VideoGroupModel;
import com.huisu.iyoox.entity.VideoModel;
import com.huisu.iyoox.entity.VideoTitleModel;
import com.huisu.iyoox.views.ItemVideoView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:dl
 * @function:
 * @date: 2018/7/12
 */
public class TeacherZhiShiDianListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<VideoModel> data;

    public TeacherZhiShiDianListAdapter(Context context, ArrayList<VideoModel> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getGroupCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return data.get(groupPosition).getShipinlist().size();
    }

    @Override
    public VideoModel getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public VideoTitleModel getChild(int groupPosition, int childPosition) {
        return data.get(groupPosition).getShipinlist().get(childPosition);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        Groupitem groupitem;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_task_zhishidian_list_layout, parent, false);
            groupitem = new Groupitem();
            groupitem.textView = convertView.findViewById(R.id.item_book_chapter_tv);
            convertView.setTag(groupitem);
        } else {
            groupitem = (Groupitem) convertView.getTag();
        }
        VideoModel groupModel = data.get(groupPosition);
        groupitem.textView.setText(groupPosition + 1 + "." + groupModel.getZhishidian_name());
        return convertView;
    }

    class Groupitem {
        TextView textView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_task_zhishidian_list_child_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final VideoTitleModel videoModel = data.get(groupPosition).getShipinlist().get(childPosition);
        holder.zhishidianTv.setText(groupPosition + 1 + "." + (childPosition + 1) + "." + videoModel.getShipin_name());
        return convertView;
    }


    static class ViewHolder {
        private TextView zhishidianTv;

        ViewHolder(View view) {
            zhishidianTv = view.findViewById(R.id.item_zhishidian_tv);
        }
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
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

}
