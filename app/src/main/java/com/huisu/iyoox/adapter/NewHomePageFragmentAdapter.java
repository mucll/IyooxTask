package com.huisu.iyoox.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.huisu.iyoox.Interface.MyOnItemClickListener;
import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.VipBuyActivity;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.List;

/**
 * Function:
 * Date: 2018/8/8
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class NewHomePageFragmentAdapter extends RecyclerView.Adapter {

    private final static int HEAD_COUNT = 1;

    private final static int TYPE_HEAD = 0;
    private final static int TYPE_CONTENT = 1;

    private Context context;
    private List<Integer> list;

    public NewHomePageFragmentAdapter(Context context, List<Integer> list) {
        this.context = context;
        this.list = list;
    }

    public int getContentSize() {
        return list.size();
    }

    public boolean isHead(int position) {
        return HEAD_COUNT != 0 && position == 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (HEAD_COUNT != 0 && position == 0) { // 头部
            return TYPE_HEAD;
        } else {
            return TYPE_CONTENT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEAD) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.head_for_recyclerview_home_page, parent, false);
            return new NewHomePageFragmentAdapter.HeadHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_for_recycler_view_home_page, parent, false);
            return new NewHomePageFragmentAdapter.ContentHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NewHomePageFragmentAdapter.HeadHolder) { // 头部
            NewHomePageFragmentAdapter.HeadHolder headHolder = (NewHomePageFragmentAdapter.HeadHolder) holder;
            setBanner(headHolder.mBanner);
            headHolder.vipIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VipBuyActivity.start(context);
                }
            });
            //1
            headHolder.xsc_math_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectPosition(0);
                }
            });
            //2
            headHolder.xsc_math_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectPosition(1);
                }
            });
            //3
            headHolder.xsc_yuwen_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectPosition(2);
                }
            });
            //4
            headHolder.xsc_english_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectPosition(3);
                }
            });

        } else if (holder instanceof NewHomePageFragmentAdapter.ContentHolder) { // 内容
            NewHomePageFragmentAdapter.ContentHolder myHolder = (NewHomePageFragmentAdapter.ContentHolder) holder;
            myHolder.imageView.setImageResource(getResId(position - 1));
//            myHolder.imageView.setBackgroundResource(getResIds(position - 1));
        }
    }

    public void getRadioGroup(RadioGroup radioGroup) {

    }

    public void setBanner(Banner banner) {

    }

    public void selectPosition(int position) {

    }

    private int getResId(int type) {
        switch (type) {
            case 0:
                return R.drawable.home_page_1;
            case 1:
                return R.drawable.home_page_2;
            case 2:
                return R.drawable.home_page_3;
            case 3:
                return R.drawable.home_page_4;
            case 4:
                return R.drawable.home_page_5;
            case 5:
                return R.drawable.home_page_6;
            case 6:
                return R.drawable.home_page_7;
            case 7:
                return R.drawable.home_page_8;
            default:
                return R.drawable.home_page_1;
        }
    }

    private int getResIds(int type) {
        switch (type) {
            case 0:
                return R.color.goods_color_1;
            case 1:
                return R.color.goods_color_2;
            case 2:
                return R.color.goods_color_3;
            case 3:
                return R.color.goods_color_4;
            case 4:
                return R.color.goods_color_5;
            case 5:
                return R.color.goods_color_6;
            case 6:
                return R.color.goods_color_7;
            case 7:
                return R.color.goods_color_8;
            default:
                return R.color.goods_color_1;
        }
    }

    @Override
    public int getItemCount() {
        return list.size() + HEAD_COUNT;
    }

    // 头部
    private class HeadHolder extends RecyclerView.ViewHolder {
        //        RadioGroup radioGroup;
        Banner mBanner;
        ImageView vipIv;
        ImageView xsc_math_1;
        ImageView xsc_math_2;
        ImageView xsc_yuwen_1;
        ImageView xsc_english_1;

        public HeadHolder(View itemView) {
            super(itemView);
            mBanner = itemView.findViewById(R.id.home_page_banner);
            vipIv = itemView.findViewById(R.id.home_page_vip_iv);
            xsc_math_1 = itemView.findViewById(R.id.xsc_math_1);
            xsc_math_2 = itemView.findViewById(R.id.xsc_math_2);
            xsc_yuwen_1 = itemView.findViewById(R.id.xsc_yuwen_1);
            xsc_english_1 = itemView.findViewById(R.id.xsc_english_1);
//            radioGroup = itemView.findViewById(R.id.home_page_radio_group);
        }
    }

    private MyOnItemClickListener onItemClickListener;

    public void setOnItemClickListener(MyOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    // 内容
    private class ContentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;

        public ContentHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.new_page_iv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(getLayoutPosition(), v);
            }
        }
    }

}
