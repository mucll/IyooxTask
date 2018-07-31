package com.huisu.iyoox.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.huisu.iyoox.Interface.MyOnItemClickListener;
import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.teacher.TeacherClassRoomConfigActivity;
import com.huisu.iyoox.activity.teacher.TeacherClassRoomDetailsActivity;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.ClassRoomModel;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.util.DialogUtil;
import com.huisu.iyoox.util.TabToast;
import com.huisu.iyoox.views.Loading;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Function:
 * Date: 2018/7/25
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class TeacherClassFragmentListAdapter extends RecyclerView.Adapter<TeacherClassFragmentListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ClassRoomModel> roomModels;
    MyOnItemClickListener onItemClickListener;

    public TeacherClassFragmentListAdapter(Context context, ArrayList<ClassRoomModel> roomModels) {
        this.context = context;
        this.roomModels = roomModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_teacher_class_fragment_list_layout, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ClassRoomModel model = getItem(position);
        holder.className.setText(model.getName());
        holder.classId.setText("班级ID:" + model.getClassroom_no());
        holder.classNum.setText("班级人数:" + model.getStudent_num() + "人");
        holder.classCreateTime.setText("创建时间:" + model.getCreate_date());
        if (model.getIsadmin() == 1) {
            holder.configTv.setVisibility(View.VISIBLE);
            holder.configTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TeacherClassRoomConfigActivity.start(context, model);
                }
            });
        } else {
            holder.configTv.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return roomModels == null ? 0 : roomModels.size();
    }

    private ClassRoomModel getItem(int position) {
        return roomModels.get(position);
    }

    public void setOnItemClickListener(MyOnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView className;
        private TextView classId;
        private TextView classNum;
        private TextView classCreateTime;
        private TextView configTv;

        public ViewHolder(View itemView) {
            super(itemView);
            className = itemView.findViewById(R.id.class_room_name_tv);
            classId = itemView.findViewById(R.id.class_room_id_tv);
            classNum = itemView.findViewById(R.id.class_room_student_num_tv);
            classCreateTime = itemView.findViewById(R.id.class_room_create_time_tv);
            configTv = itemView.findViewById(R.id.teacher_class_config_tv);
            this.itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(getLayoutPosition(), v);
            }
        }
    }
}
