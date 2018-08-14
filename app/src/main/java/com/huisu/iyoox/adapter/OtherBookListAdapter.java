package com.huisu.iyoox.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.entity.OtherBookZSDModel;
import com.huisu.iyoox.entity.OtherBookZhangJieModel;
import com.huisu.iyoox.entity.VideoGroupModel;
import com.huisu.iyoox.entity.VideoModel;
import com.huisu.iyoox.views.ItemVideoView;

import java.util.List;

/**
 * @author:dl
 * @function:
 * @date: 2018/7/12
 */
public class OtherBookListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<OtherBookZhangJieModel> data;

    public OtherBookListAdapter(Context context, List<OtherBookZhangJieModel> data) {
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
    public OtherBookZhangJieModel getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public OtherBookZSDModel getChild(int groupPosition, int childPosition) {
        return data.get(groupPosition).getZhishidian().get(childPosition);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        Groupitem groupitem;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_home_list_group_layout, parent, false);
            groupitem = new Groupitem();
            groupitem.textView = convertView.findViewById(R.id.item_book_chapter_tv);
            groupitem.moduleRemarkTv = convertView.findViewById(R.id.group_module_remark_tv);
            convertView.setTag(groupitem);
        } else {
            groupitem = (Groupitem) convertView.getTag();
        }
        OtherBookZhangJieModel groupModel = data.get(groupPosition);
        if (!TextUtils.isEmpty(groupModel.getZhangjie_name())) {
            groupitem.textView.setText(groupModel.getZhangjie_name());
            groupitem.textView.setVisibility(View.VISIBLE);
        } else {
            groupitem.textView.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    class Groupitem {
        TextView textView;
        TextView moduleRemarkTv;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_other_book_list_child_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final OtherBookZSDModel videoModel = data.get(groupPosition).getZhishidian().get(childPosition);
        holder.zhishidianTv.setText(videoModel.getName());
        return convertView;
    }

    private int selectGroup = -1;
    private int selectChild = -1;

    static class ViewHolder {
        private TextView zhishidianTv;
        private TextView videoPgaeCount;

        ViewHolder(View view) {
            zhishidianTv = view.findViewById(R.id.item_zhishidian_tv);
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
