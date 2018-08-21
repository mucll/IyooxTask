package com.huisu.iyoox.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.entity.ArtBookZSDModel;

import java.util.List;

/**
 * Function:
 * Date: 2018/8/15
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class ArtBookListAdapter extends RecyclerView.Adapter<ArtBookListAdapter.ViewHolder> {
    private Context mContext;
    private List<ArtBookZSDModel> models;

    public ArtBookListAdapter(Context mContext, List<ArtBookZSDModel> models) {
        this.mContext = mContext;
        this.models = models;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_other_book_list_child_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ArtBookZSDModel model = models.get(position);
        holder.name.setText(model.getName());
    }

    @Override
    public int getItemCount() {
        return models == null ? 0 : models.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_zhishidian_tv);
        }
    }
}
