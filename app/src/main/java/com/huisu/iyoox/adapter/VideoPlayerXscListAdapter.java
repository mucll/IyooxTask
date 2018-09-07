package com.huisu.iyoox.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huisu.iyoox.Interface.MyOnItemClickListener;
import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.student.TaskStudentHomeWorkActivity;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.entity.VideoTimuModel;
import com.huisu.iyoox.entity.VideoTitleModel;
import com.huisu.iyoox.entity.base.BaseVideoTimuModel;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.util.TabToast;
import com.huisu.iyoox.views.Loading;

import java.util.ArrayList;

/**
 * Function:
 * Date: 2018/8/2
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class VideoPlayerXscListAdapter extends RecyclerView.Adapter<VideoPlayerXscListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<VideoTitleModel> models;
    private int selectId;
    private MyOnItemClickListener onItemClickListener;
    private Loading loading;
    private User user;

    public VideoPlayerXscListAdapter(Context context, ArrayList<VideoTitleModel> models) {
        this.context = context;
        this.models = models;
        user = UserManager.getInstance().getUser();
    }

    public void setSelectId(int selectId) {
        this.selectId = selectId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_video_view_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final VideoTitleModel model = models.get(position);
        if (model.getShipin_id() == selectId) {
            holder.videoIcon.setSelected(true);
            holder.videoState.setSelected(true);
            holder.videoName.setSelected(true);
        } else {
            holder.videoIcon.setSelected(false);
            holder.videoState.setSelected(false);
            holder.videoName.setSelected(false);
        }

        holder.videoName.setText(model.getShipin_name());
        if (model.getTimu_count() > 0) {
            holder.videoStateContent.setVisibility(View.VISIBLE);
            holder.videoStateContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postExercisesData(model.getShipin_id() + "", model.getZhishidian_id() + "");
                }
            });
        } else {
            holder.videoStateContent.setVisibility(View.GONE);
        }

        if (model.getSort() > 0 && model.getSort() <= 3) {
            holder.isSort.setVisibility(View.VISIBLE);
        } else {
            holder.isSort.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return models == null ? 0 : models.size();
    }

    public void setOnItemClickListener(MyOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView videoIcon;
        private TextView videoName;
        private TextView videoState;
        private View videoStateContent;
        private View isSort;

        public ViewHolder(View itemView) {
            super(itemView);
            videoIcon = itemView.findViewById(R.id.item_book_video_icon_iv);
            videoName = itemView.findViewById(R.id.item_video_name_tv);
            videoState = itemView.findViewById(R.id.item_video_state);
            videoStateContent = itemView.findViewById(R.id.item_video_state_content);
            isSort = itemView.findViewById(R.id.is_sort);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(getLayoutPosition(), v);
            }
        }
    }

    /**
     * 获取知识点作业题目信息
     */
    private void postExercisesData(String videoId, String zhishiId) {
        if (user == null) return;
        loading = Loading.show(null, context, context.getString(R.string.loading_one_hint_text));
        RequestCenter.getVideoTimu(user.getUserId(), videoId, zhishiId, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                loading.dismiss();
                BaseVideoTimuModel baseVideoUrlModel = (BaseVideoTimuModel) responseObj;
                if (baseVideoUrlModel.code == Constant.POST_SUCCESS_CODE && baseVideoUrlModel.data != null) {
                    VideoTimuModel urlModel = baseVideoUrlModel.data;
                    //判断是否有题
                    if (urlModel.getTimu_list() != null && urlModel.getTimu_list().size() > 0) {
                        Intent intent = new Intent(context, TaskStudentHomeWorkActivity.class);
                        intent.putExtra("model", urlModel);
                        intent.putExtra("type", Constant.STUDENT_DOING);
                        context.startActivity(intent);
                    } else {
                        TabToast.showMiddleToast(context, "暂无习题");
                    }
                } else {
                    TabToast.showMiddleToast(context, "暂无习题");
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                loading.dismiss();
            }
        });
    }
}
