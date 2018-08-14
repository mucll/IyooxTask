package com.huisu.iyoox.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.student.TaskStudentHomeWorkActivity;
import com.huisu.iyoox.activity.videoplayer.VideoPlayerActivity;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.VideoTitleModel;
import com.huisu.iyoox.util.TabToast;

import java.util.List;

/**
 * @author:dl
 * @function:
 * @date: 2018/7/13
 */
public class ItemVideoListAdapter extends BaseAdapter {
    private Context context;
    private List<VideoTitleModel> data;

    public ItemVideoListAdapter(Context context, List<VideoTitleModel> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public VideoTitleModel getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_video_view_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final VideoTitleModel titleModel = getItem(position);
        holder.videoIcon.setSelected(true);
        holder.videoState.setSelected(true);
        holder.videoName.setText(titleModel.getShipin_name());
        if (titleModel.getTimu_count() > 0) {
            holder.videoStateContent.setVisibility(View.VISIBLE);
            holder.videoStateContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, TaskStudentHomeWorkActivity.class);
                    intent.putExtra("videoId", titleModel.getShipin_id() + "");
                    intent.putExtra("zhishiId", titleModel.getZhishidian_id() + "");
                    intent.putExtra("type", Constant.STUDENT_DOING);
                    context.startActivity(intent);
                }
            });
        } else {
            holder.videoStateContent.setVisibility(View.GONE);
        }
        if (titleModel.getSort() == 1) {
            holder.isSort.setVisibility(View.VISIBLE);
        } else {
            holder.isSort.setVisibility(View.GONE);
        }
        return convertView;
    }

    static class ViewHolder {
        private ImageView videoIcon;
        private TextView videoName;
        private TextView videoState;
        private View videoStateContent;
        private View isSort;

        public ViewHolder(View view) {
            videoIcon = view.findViewById(R.id.item_book_video_icon_iv);
            videoName = view.findViewById(R.id.item_video_name_tv);
            videoState = view.findViewById(R.id.item_video_state);
            videoStateContent = view.findViewById(R.id.item_video_state_content);
            isSort = view.findViewById(R.id.is_sort);
        }
    }
}
