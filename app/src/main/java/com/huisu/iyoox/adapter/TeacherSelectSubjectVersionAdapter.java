package com.huisu.iyoox.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huisu.iyoox.Interface.MyOnItemClickListener;
import com.huisu.iyoox.R;
import com.huisu.iyoox.entity.BookEditionModel;

import java.util.List;

/**
 * Function:
 * Date: 2018/7/25
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class TeacherSelectSubjectVersionAdapter extends RecyclerView.Adapter<TeacherSelectSubjectVersionAdapter.ViewHolder> {
    private Context context;
    private List<BookEditionModel> models;
    private int selectVersionId = 0;
    private int versionDetailId = 0;

    public TeacherSelectSubjectVersionAdapter(Context context, List<BookEditionModel> models) {
        this.context = context;
        this.models = models;
    }

    public void setSelectVersionId(int selectVersionId, int versionDetailId) {
        this.selectVersionId = selectVersionId;
        this.versionDetailId = versionDetailId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_teacher_select_subject_version_layout, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BookEditionModel model = models.get(position);
        holder.versionTv.setText(model.getVersionName());
        if (model.getJiaocai_id() == selectVersionId && model.getGrade_detail_id() == versionDetailId) {
            holder.versionTv.setSelected(true);
        } else {
            holder.versionTv.setSelected(false);
        }
    }

    @Override
    public int getItemCount() {
        return models == null ? 0 : models.size();
    }

    private MyOnItemClickListener onItemClickListener;

    public void setOnItemClickListener(MyOnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView versionTv;

        public ViewHolder(View itemView) {
            super(itemView);
            versionTv = itemView.findViewById(R.id.subject_version_tv);
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
