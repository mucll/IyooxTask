package com.huisu.iyoox.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.VipBuyDetailActivity;
import com.huisu.iyoox.entity.VipCardModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Function:
 * Date: 2018/8/10
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class VipBuyListAdapter extends RecyclerView.Adapter<VipBuyListAdapter.ViewHodler> {
    private Context mContext;
    private ArrayList<VipCardModel> models;

    public VipBuyListAdapter(Context mContext, ArrayList<VipCardModel> models) {
        this.mContext = mContext;
        this.models = models;
    }

    @Override
    public ViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_vip_buy_layout, parent, false);
        ViewHodler viewHodler = new ViewHodler(view);
        return viewHodler;
    }

    @Override
    public void onBindViewHolder(ViewHodler holder, int position) {
        final VipCardModel model = models.get(position);
        holder.goodsNameTv.setText(model.getType_name());
        holder.goodsPriceTv.setText("￥" + model.getPrice());
        holder.submitTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VipBuyDetailActivity.start(mContext, model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return models == null ? 0 : models.size();
    }

    class ViewHodler extends RecyclerView.ViewHolder {

        private final TextView goodsNameTv;
        private final TextView goodsPriceTv;
        private final TextView submitTv;

        public ViewHodler(View itemView) {
            super(itemView);
            goodsNameTv = itemView.findViewById(R.id.item_goods_name_tv);
            goodsPriceTv = itemView.findViewById(R.id.item_goods_price_tv);
            submitTv = itemView.findViewById(R.id.item_goods_submit_tv);
        }
    }

}
