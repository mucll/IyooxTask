package com.huisu.iyoox.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.entity.BookChapterModel;
import com.huisu.iyoox.entity.VideoGroupModel;
import com.huisu.iyoox.util.PopWinDownUtil;

import java.util.List;

/**
 * Function:
 * Date: 2018/7/27
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class DropdownButton extends RelativeLayout implements Checkable, View.OnClickListener, PopWinDownUtil.OnDismissLisener, AdapterView.OnItemClickListener {
    /**
     * 菜单按钮文字内容
     */
    private TextView text;
    /**
     * 菜单按钮底部的提示条
     */
    private boolean isCheced;
    private PopWinDownUtil popWinDownUtil;
    private Context mContext;
    private DropDownAdapter adapter;
    /**
     * 传入的数据
     */
    private List<VideoGroupModel> drops;
    /**
     * 当前被选择的item位置
     */
    private int selectPosition;

    public DropdownButton(Context context) {
        this(context, null);
    }

    public DropdownButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DropdownButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        //菜单按钮的布局
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dropdown_tab_button, this, true);
        text = (TextView) view.findViewById(R.id.dropdown_tab_name_tv);
        //点击事件，点击外部区域隐藏popupWindow
        setOnClickListener(this);
    }

    /**
     * 添加数据，默认选择第一项
     *
     * @param dropBeans
     */
    public void setData(List<VideoGroupModel> dropBeans) {
        if (dropBeans == null || dropBeans.isEmpty()) {
            return;
        }
        drops = dropBeans;
        drops.get(0).setChoiced(true);
        text.setText(drops.get(0).getZhangjie_name());
        selectPosition = 0;
        View view = LayoutInflater.from(mContext).inflate(R.layout.dropdown_content, null);
        view.findViewById(R.id.dropdown_tab_content).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popWinDownUtil.hide();
            }
        });
        ListView listView = view.findViewById(R.id.dropdown_tab_list_view);
        listView.setOnItemClickListener(this);

        adapter = new DropDownAdapter(drops, mContext);
        listView.setAdapter(adapter);

        popWinDownUtil = new PopWinDownUtil(mContext, view, this);
        popWinDownUtil.setOnDismissListener(this);
    }

    public void setText(CharSequence content) {
        text.setText(content);
    }

    /**
     * 根据传过来的参数改变状态
     *
     * @param checked
     */
    @Override
    public void setChecked(boolean checked) {
        isCheced = checked;
        if (checked) {
            popWinDownUtil.show();
        } else {
            popWinDownUtil.hide();
        }
    }

    @Override
    public boolean isChecked() {
        return isCheced;
    }

    @Override
    public void toggle() {
        setChecked(!isCheced);
    }

    @Override
    public void onClick(View v) {
        if (drops != null && drops.size() > 0) {
            setChecked(!isCheced);
        }
    }

    @Override
    public void onDismiss() {
        setChecked(false);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (selectPosition == position) {
            popWinDownUtil.hide();
            return;
        }
        drops.get(selectPosition).setChoiced(false);
        drops.get(position).setChoiced(true);
        text.setText(drops.get(position).getZhangjie_name());
        adapter.notifyDataSetChanged();
        selectPosition = position;
        popWinDownUtil.hide();
        if (onDropItemSelectListener != null) {
            onDropItemSelectListener.onDropItemSelect(position);
        }
    }

    private OnDropItemSelectListener onDropItemSelectListener;

    public void setOnDropItemSelectListener(OnDropItemSelectListener onDropItemSelectListener) {
        this.onDropItemSelectListener = onDropItemSelectListener;
    }

    public interface OnDropItemSelectListener {
        void onDropItemSelect(int Postion);
    }


    class DropDownAdapter extends BaseAdapter {
        private List<VideoGroupModel> drops;
        private Context context;

        public DropDownAdapter(List<VideoGroupModel> drops, Context context) {
            this.drops = drops;
            this.context = context;
        }

        @Override
        public int getCount() {
            return drops == null ? 0 : drops.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_dropdown, parent, false);
                holder.tv = convertView.findViewById(R.id.subject_chapter_tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv.setText(drops.get(position).getZhangjie_name());
            if (drops.get(position).isChoiced()) {
                holder.tv.setSelected(true);
            } else {
                holder.tv.setSelected(false);
            }
            return convertView;
        }

        private class ViewHolder {
            TextView tv;
        }
    }
}

