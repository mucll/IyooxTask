package com.huisu.iyoox.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huisu.iyoox.Interface.MyOnItemClickListener;
import com.huisu.iyoox.R;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.ExercisesChooseModel;
import com.huisu.iyoox.entity.ExercisesModel;
import com.huisu.iyoox.entity.NotificationMsgModel;
import com.huisu.iyoox.util.StringUtils;
import com.huisu.iyoox.views.BaseExercisesView;
import com.huisu.iyoox.views.HtmlTextView;

import java.util.ArrayList;

/**
 * Function:
 * Date: 2018/7/21
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {
    private Context context;
    private ArrayList<NotificationMsgModel> models;
    private final int new_msg_code = 0;

    public MsgAdapter(Context context, ArrayList<NotificationMsgModel> models) {
        this.context = context;
        this.models = models;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_msg_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        NotificationMsgModel model = models.get(position);
        String time = TextUtils.isEmpty(model.getCreatedate()) ? "" : model.getCreatedate();
        switch (model.getMessage_action()) {
            case Constant.NOTIFICATION_SEND_TASK:
                holder.iconIv.setImageResource(R.drawable.message_homework);
                holder.msgNameTv.setText("作业通知");
                break;
            case Constant.NOTIFICATION_DP:
                holder.iconIv.setImageResource(R.drawable.message_dp);
                holder.msgNameTv.setText("点评");
                break;
            case Constant.NOTIFICATION_REMIND:
                holder.iconIv.setImageResource(R.drawable.message_homework);
                holder.msgNameTv.setText("作业提醒");
                break;
            default:
                break;
        }
        holder.msgContentTv.setText(model.getBody());
        if (!TextUtils.isEmpty(time)) {
            holder.timeTv.setText(StringUtils.getTimeStringMD(time));
        }
        if (model.getStatus() == new_msg_code) {
            holder.newMsg.setVisibility(View.VISIBLE);
        } else {
            holder.newMsg.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return models == null ? 0 : models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iconIv;
        private TextView timeTv;
        private TextView msgNameTv;
        private TextView msgContentTv;
        private View newMsg;

        public ViewHolder(View itemView) {
            super(itemView);
            iconIv = itemView.findViewById(R.id.item_msg_icon_iv);
            msgNameTv = itemView.findViewById(R.id.item_msg_name_tv);
            msgContentTv = itemView.findViewById(R.id.item_msg_content_tv);
            timeTv = itemView.findViewById(R.id.item_msg_time_tv);
            newMsg = itemView.findViewById(R.id.item_new_msg_view);
        }
    }
}
