package com.huisu.iyoox.views;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.huisu.iyoox.R;
import com.huisu.iyoox.fragment.base.BaseFragment;

import java.util.List;

/**
 * @author 赵进
 * @步骤1 在布局中添加此布局，一般这里选是垂直布局
 * @步骤2 写一个tabLayout布局用来当做tab布局，布局格式参照tablayout1.xml记住里面的子控件都用一样的ID，
 * 方便用lastTabView和currentTabView找到
 * @步骤3 (此步骤都可以不设置 ， 都有默认值)代码中找到此布局，代码中设置是否可以滑动切换fragmentLayout.setScorllToNext(false)，
 * 设置点击tab切换时是否带滑动setScorll,设置tab位于viewpager上方还是下方setWhereTab,设置导航线的颜色宽度setTabHeight();
 * @步骤4 设置切换时的监听，主要用来改变tab项的状态，比如字体颜色，背景颜色等，fragmentLayout.
 * setOnChangeFragmentListener(new ChangeFragmentListener() {
 * @步骤5 设置适配器，fragmentLayout.setAdapter(fList,R.layout.tablayout1);
 */
public class MyFragmentLayout_line extends LinearLayout implements
        OnPageChangeListener {
    public ViewPager viewPager;
    public int tabWidth = 0;
    ViewTreeObserver vto;
    private List<BaseFragment> list;
    private Fragment_viewpager_Adapter fragmentAdapter;
    private FragmentActivity context;
    private LinearLayout tabLayout;
    private int position = 0;
    private boolean isScorll = false;
    private boolean isScorllToNext = true;
    private int whereTab = 0;
    private ChangeFragmentListener changeListener;
    private int tabHeight = 6;
    private int lineWidth = 0;
    private int tabColor = Color.GREEN;
    private ImageView v;
    private boolean hasPadding = false;
    private int resId = 0;
    private int marginBottum = 0;
    private int gravity = Gravity.BOTTOM;

    public MyFragmentLayout_line(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = (FragmentActivity) context;
    }

    public MyFragmentLayout_line(Context context) {
        super(context);
        this.context = (FragmentActivity) context;
    }

    public void setOnChangeFragmentListener(ChangeFragmentListener listener) {
        this.changeListener = listener;
    }

    /**
     * @param list        fragment数据
     * @param tabLayoutId tab布局 id
     * @param id          任意int型，不能重复
     */
    public void setAdapter(List<BaseFragment> list, int tabLayoutId, int id) {
        this.setOrientation(LinearLayout.VERTICAL);
        View root = View.inflate(context, tabLayoutId,
                null);
        FrameLayout tabFrame = (FrameLayout) root.findViewById(R.id.root);
        tabLayout = (LinearLayout) (tabFrame.findViewById(R.id.tabLayout));
        tabLayout.setLayoutParams(new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        this.list = list;
        fragmentAdapter = new Fragment_viewpager_Adapter(
                context.getSupportFragmentManager());
        viewPager = new MyViewPager(context);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
        params.weight = 1;
        viewPager.setLayoutParams(params);
        viewPager.setId(id);
        if (whereTab == 0) {
            this.addView(viewPager);
            this.addView(root);
        } else {
            this.addView(root);
            this.addView(viewPager);
        }
        for (int i = 0; i < tabLayout.getChildCount(); i++) {
            View view = tabLayout.getChildAt(i);
            view.setClickable(true);
            view.setOnClickListener(new tabClickLisener(i));
        }
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setOnPageChangeListener(this);
        // 添加导航动画横线
        v = new ImageView(context);
        FrameLayout.LayoutParams vParams = new FrameLayout.LayoutParams(
                180, tabHeight, gravity);
        v.setLayoutParams(vParams);
        tabFrame.addView(v);
        setTabLine(hasPadding);
    }

    /**
     * @param hasPadding 横线是否有边距，
     */
    public void setTabLine(final boolean hasPadding) {
        vto = tabLayout.getChildAt(0).getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (!vto.isAlive()) {
                    vto = tabLayout.getChildAt(0).getViewTreeObserver();
                }
                if (vto.isAlive()) {
                    vto.removeOnPreDrawListener(this);
                }
                tabWidth = tabLayout.getChildAt(0).getWidth();
                if (hasPadding) {
                    lineWidth = ((LinearLayout) (tabLayout.getChildAt(0))).getChildAt(0).getWidth();
                } else {
                    lineWidth = tabWidth;
                }
                if (resId == 0) {
                    FrameLayout.LayoutParams vParams = new FrameLayout.LayoutParams(
                            lineWidth, tabHeight,
                            gravity);
                    vParams.setMargins((tabWidth - lineWidth) / 2, 0, (tabWidth - lineWidth) / 2,
                            0);
                    v.setLayoutParams(vParams);
                    v.setBackgroundColor(tabColor);
                } else {
                    FrameLayout.LayoutParams vParams = new FrameLayout.LayoutParams(
                            lineWidth, tabHeight,
                            gravity);
                    vParams.setMargins((tabWidth - lineWidth) / 2, 0, (tabWidth - lineWidth) / 2,
                            marginBottum);
                    v.setLayoutParams(vParams);
                    v.setImageResource(resId);
                }
                return true;
            }
        });
    }

    public void setOffscreenPageLimit(int i) {
        viewPager.setOffscreenPageLimit(i);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        if (Build.VERSION.SDK_INT < 11) {
            FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams)
                    MyFragmentLayout_line.this.v.getLayoutParams();
            params.leftMargin = (int) (arg0 * tabWidth + tabWidth * arg1 + (tabWidth - lineWidth)
                    / 2);
            MyFragmentLayout_line.this.v.setLayoutParams(params);
        } else {
            if (arg1 > 0) {
                if (hasPadding) {
                    lineWidth = (int) ((((LinearLayout) (tabLayout.getChildAt(arg0))).getChildAt(0)
                            .getWidth() * (1 - arg1) +
                            ((LinearLayout) (tabLayout.getChildAt(arg0 + 1))).getChildAt(0)
                                    .getWidth() * arg1));
                    if (resId == 0) {
                        FrameLayout.LayoutParams vParams = new FrameLayout.LayoutParams(
                                lineWidth, tabHeight,
                                Gravity.BOTTOM);
                        vParams.setMargins((tabWidth - lineWidth) / 2, 0, (tabWidth - lineWidth)
                                        / 2,
                                0);
                        v.setLayoutParams(vParams);
                        v.setBackgroundColor(tabColor);
                    }
                }
            }
            MyFragmentLayout_line.this.v.setX(arg0 * tabWidth + tabWidth * arg1 + (tabWidth -
                    lineWidth) / 2);
        }
    }

    @Override
    public void onPageSelected(int arg0) {
        if (changeListener != null) {
            changeListener.change(position, arg0,
                    tabLayout.getChildAt(position), tabLayout.getChildAt(arg0));
        }
        position = arg0;
    }

    public int getCurrentPosition() {
        return position;
    }

    public boolean isScorll() {
        return isScorll;
    }

    /**
     * @param isScorll 设置点击tab时fragment切换是否带滑动，默认不带
     */
    public void setScorll(boolean isScorll) {
        this.isScorll = isScorll;
    }

    public void setCurrenItem(int position) {
        viewPager.setCurrentItem(position, isScorll);
    }

    public boolean isScorllToNext() {
        return isScorllToNext;
    }

    /**
     * @param isScorllToNext 是否可以滑动切换，默认为true
     */
    public void setScorllToNext(boolean isScorllToNext) {
        this.isScorllToNext = isScorllToNext;
    }

    public int getWhereTab() {
        return whereTab;
    }

    /**
     * @param whereTab 设置tab位于viewpager上方还是下方，0代表下方，1代表上方
     */
    public void setWhereTab(int whereTab) {
        this.whereTab = whereTab;
    }

    public int getTabHeight() {
        return tabHeight;
    }

    /**
     * @param tabHeight  设置导航线宽度
     * @param tabColor   设置导航线颜色
     * @param hasPadding 设置是否有边距
     * @param gravity    导航线在文字上方还是下方
     */
    public void setTabHeight(int tabHeight, int tabColor, boolean hasPadding, int gravity) {
        this.tabHeight = tabHeight;
        this.tabColor = tabColor;
        this.hasPadding = hasPadding;
        this.gravity = gravity;
    }

    /**
     * @param tabHeight  设置导航线宽度
     * @param tabColor   设置导航线颜色
     * @param hasPadding 设置是否有边距
     */
    public void setTabHeight(int tabHeight, int tabColor, boolean hasPadding) {
        this.tabHeight = tabHeight;
        this.tabColor = tabColor;
        this.hasPadding = hasPadding;
    }

    /****
     * @param resId 导航线drawble资源ID
     */
    public void setTabDrawble(int resId, int tabHeight, int marginBottum) {
        this.resId = resId;
        this.tabHeight = tabHeight;
        this.marginBottum = marginBottum;
    }

    public int getTabColor() {
        return tabColor;
    }

    public LinearLayout getTabLayout() {
        return tabLayout;
    }

    public interface ChangeFragmentListener {
        /**
         * @param positon        切换到哪项
         * @param lastTabView    上一项的tab视图，用来改变没选中tab状态
         * @param currentTabView 当前想的tab视图,用来改变选中的tab样式
         */
        public void change(int lastPosition, int positon, View lastTabView,
                           View currentTabView);
    }

    private class Fragment_viewpager_Adapter extends FragmentStatePagerAdapter {
        public Fragment_viewpager_Adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Fragment getItem(int arg0) {
            return list.get(arg0);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            //			((ViewGroup) list.get(position).getView()).removeAllViews();
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }
    }

    class tabClickLisener implements OnClickListener {
        private int position;

        public tabClickLisener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (list.size() == tabLayout.getChildCount()) {
                viewPager.setCurrentItem(position, isScorll);
            } else {
                Toast.makeText(context, "page项数量不等于tab项数量", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    class MyViewPager extends ViewPager {
        public MyViewPager(Context context) {
            super(context);
        }

        @Override
        public boolean onTouchEvent(MotionEvent arg0) {
            if (isScorllToNext) {
                return super.onTouchEvent(arg0);
            }
            return false;
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            if (isScorllToNext == false) {
                return false;
            } else {
                return super.onInterceptTouchEvent(ev);
            }
        }
    }

    public ViewPager getViewPager() {
        return viewPager;
    }
}
