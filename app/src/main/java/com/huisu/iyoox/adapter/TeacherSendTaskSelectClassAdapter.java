package com.huisu.iyoox.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.entity.ClassRoomModel;

import java.util.List;

/**
 * Function:
 * Date: 2018/7/30
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class TeacherSendTaskSelectClassAdapter extends BaseAdapter {
    private Context context;
    private List<ClassRoomModel> models;

    public TeacherSendTaskSelectClassAdapter(Context context, List<ClassRoomModel> models) {
        this.context = context;
        this.models = models;
    }

    @Override
    public int getCount() {
        return models == null ? 0 : models.size();
    }

    @Override
    public ClassRoomModel getItem(int position) {
        return models.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_teacher_select_subject_version_layout, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ClassRoomModel model = getItem(position);
        holder.className.setText(model.getName());
        if (model.isSelect()) {
            holder.className.setSelected(true);
        } else {
            holder.className.setSelected(false);
        }
        return convertView;
    }

    static class ViewHolder {
        private TextView className;

        ViewHolder(View view) {
            className = view.findViewById(R.id.subject_version_tv);
        }
    }
}
