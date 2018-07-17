package com.huisu.iyoox.complexmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.complexmenu.holder.SubjectGridHolder;
import com.huisu.iyoox.complexmenu.holder.SubjectHolder;
import com.huisu.iyoox.entity.BookChapterModel;
import com.huisu.iyoox.entity.BookDetailsModel;
import com.huisu.iyoox.entity.BookEditionModel;
import com.huisu.iyoox.util.StringUtils;
import com.huisu.iyoox.util.TabToast;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索菜单栏
 * Created by vonchenchen on 2016/4/5 0005.
 */
public class SelectMenuView extends LinearLayout {

    private static final int TAB_EDITION = 1;
    private static final int TAB_CHAPTER = 2;
    private static final int TAB_KNOWLEDGE = 3;

    private Context mContext;

    private View mEditonView;
    private View mChapterView;
    private View mKnowledgeView;

    private View mPopupWindowView;

    private FrameLayout mMainContentLayout;

    /**
     * 教材版本
     */
    private SubjectGridHolder mEditonHolder;
    private boolean isEditionSelect;
    /**
     * 章节
     */
    private SubjectHolder mChapterHolder;
    private boolean isChapterSelect;
    /**
     * 知识点
     */
    private SubjectHolder mKnowledgeHolder;
    private boolean isKnowledgeSelect;

    private OnMenuSelectDataChangedListener mOnMenuSelectDataChangedListener;

    private RelativeLayout mContentLayout;

    private TextView mEditonText;
    private ImageView mEditonImage;
    private TextView mChapterText;
    private ImageView mChapterImage;
    private TextView mKnowledgeText;
    private ImageView mKnowledgeImage;

    private List<BookDetailsModel> mEditonList = new ArrayList<>();
    private List<BookChapterModel> mChapterList = new ArrayList<>();
    private List<String> mKnowledgeList = new ArrayList<>();

    private int mTabRecorder = -1;
    private BookChapterModel chapterModel;
    private BookDetailsModel editionModel;
    private String KnowledgeModel;

    public SelectMenuView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public SelectMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init() {

        //教材版本
        mEditonHolder = new SubjectGridHolder(mContext);
        mEditonHolder.setOnRightListViewItemSelectedListener(new SubjectGridHolder.OnRightListViewItemSelectedListener() {
            @Override
            public void OnRightListViewItemSelected(int rightIndex, BookDetailsModel model) {
                editionModel = mEditonList.get(rightIndex);
                //点击回调 重新请求章节跟知识点
                if (mOnMenuSelectDataChangedListener != null) {
                    mOnMenuSelectDataChangedListener.onEditonChanged(editionModel);
                }
                setAllUnSelect();
                dismissPopupWindow();
                mEditonText.setText(model.getName());
                setRefresh(editionModel);
            }
        });

        //章节
        mChapterHolder = new SubjectHolder(mContext);
        mChapterHolder.setOnRightListViewItemSelectedListener(new SubjectHolder.OnRightListViewItemSelectedListener() {
            @Override
            public void OnRightListViewItemSelected(int rightIndex, String model) {
                chapterModel = mChapterList.get(rightIndex);
                if (mOnMenuSelectDataChangedListener != null) {
                    mOnMenuSelectDataChangedListener.onChapterChanged(chapterModel);
                }
                setAllUnSelect();
                dismissPopupWindow();
                mChapterText.setText(model);
                //章节有变化知识点跟着变化
                mKnowledgeList.clear();
                mKnowledgeList.addAll(mChapterList.get(rightIndex).getZhishidianArr());
                mKnowledgeHolder.refreshData(mKnowledgeList, 0);
                mKnowledgeText.setText(mKnowledgeList.get(0));
            }
        });

        //知识点
        mKnowledgeHolder = new SubjectHolder(mContext);
        mKnowledgeHolder.setOnRightListViewItemSelectedListener(new SubjectHolder.OnRightListViewItemSelectedListener() {
            @Override
            public void OnRightListViewItemSelected(int rightIndex, String model) {
                KnowledgeModel = chapterModel.getZhishidianArr().get(rightIndex);
                if (mOnMenuSelectDataChangedListener != null) {
                    mOnMenuSelectDataChangedListener.onKnowledgeChanged(KnowledgeModel);
                }
                setAllUnSelect();
                dismissPopupWindow();
                mKnowledgeText.setText(model);
            }
        });
    }

    private int getSubjectId(int index) {
        return index;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(mContext, R.layout.layout_search_menu, this);

        mEditonText = (TextView) findViewById(R.id.subject);
        mEditonImage = (ImageView) findViewById(R.id.img_sub);

        mChapterText = (TextView) findViewById(R.id.comprehensive_sorting);
        mChapterImage = (ImageView) findViewById(R.id.img_cs);

        mKnowledgeText = (TextView) findViewById(R.id.tv_select);
        mKnowledgeImage = (ImageView) findViewById(R.id.img_sc);

        mContentLayout = (RelativeLayout) findViewById(R.id.rl_content);
        mPopupWindowView = View.inflate(mContext, R.layout.layout_search_menu_content, null);
        mMainContentLayout = (FrameLayout) mPopupWindowView.findViewById(R.id.rl_main);
        mEditonView = findViewById(R.id.ll_subject);
        mChapterView = findViewById(R.id.ll_sort);
        mKnowledgeView = findViewById(R.id.ll_select);

        //点击 科目 弹出菜单
        mEditonView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditonList.size() == 0) {
                    return;
                }
                if (!isEditionSelect) {
                    handleClickSubjectView();
                    isEditionSelect = true;
                    isChapterSelect = false;
                    isKnowledgeSelect = false;
                } else {
                    dismissPopupWindow();
                    isEditionSelect = false;
                }

            }
        });
        //点击 综合排序 弹出菜单
        mChapterView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mChapterList.size() == 0) {
                    return;
                }
                if (!isChapterSelect) {
                    handleClickSortView();
                    isChapterSelect = true;
                    isEditionSelect = false;
                    isKnowledgeSelect = false;
                } else {
                    dismissPopupWindow();
                    isChapterSelect = false;
                }
            }
        });
        //点击 筛选 弹出菜单
        mKnowledgeView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mKnowledgeList.size() == 0) {
                    return;
                }
                if (!isKnowledgeSelect) {
                    handleClickSelectView();
                    isKnowledgeSelect = true;
                    isEditionSelect = false;
                    isChapterSelect = false;
                } else {
                    dismissPopupWindow();
                    isKnowledgeSelect = false;
                }

            }
        });

        mContentLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissPopupWindow();
            }
        });
    }

    private void setAllUnSelect() {
        isEditionSelect = false;
        isChapterSelect = false;
        isKnowledgeSelect = false;
    }

    private void handleClickSubjectView() {

        mMainContentLayout.removeAllViews();
        mMainContentLayout.addView(mEditonHolder.getRootView());
        mEditonHolder.refreshData();
        popUpWindow(TAB_EDITION);
    }

    private void handleClickSortView() {

        mMainContentLayout.removeAllViews();
        mMainContentLayout.addView(mChapterHolder.getRootView());
        mChapterHolder.refreshData();
        popUpWindow(TAB_CHAPTER);
    }

    private void handleClickSelectView() {

        mMainContentLayout.removeAllViews();
        mMainContentLayout.addView(mKnowledgeHolder.getRootView());
        mKnowledgeHolder.refreshData();
        popUpWindow(TAB_KNOWLEDGE);
    }

    private void popUpWindow(int tab) {
        if (mTabRecorder != -1) {
            resetTabExtend(mTabRecorder);
        }
        extendsContent(tab);
        setTabExtend(tab);
        mTabRecorder = tab;
    }

    private void extendsContent(int tab) {
        mContentLayout.removeAllViews();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mContentLayout.addView(mPopupWindowView, params);
        if (tab == TAB_EDITION) {
            LinearLayout linearLayout = mPopupWindowView.findViewById(R.id.ll_content_layout);
            LinearLayout.LayoutParams params1 = (LayoutParams) linearLayout.getLayoutParams();
            params1.width = LayoutParams.MATCH_PARENT;
            params1.height = LayoutParams.MATCH_PARENT;
            linearLayout.setLayoutParams(params1);
        } else {
            int dip = StringUtils.dp2px(mContext, 192);
            LinearLayout linearLayout = mPopupWindowView.findViewById(R.id.ll_content_layout);
            LinearLayout.LayoutParams params1 = (LayoutParams) linearLayout.getLayoutParams();
            params1.width = LayoutParams.MATCH_PARENT;
            params1.height = dip;
            linearLayout.setLayoutParams(params1);
        }

    }

    private void dismissPopupWindow() {
        mContentLayout.removeAllViews();
        setTabClose();
    }

    public void dismissAllPopupWindow() {
        dismissPopupWindow();
        setAllUnSelect();
    }

    public void setOnMenuSelectDataChangedListener(OnMenuSelectDataChangedListener onMenuSelectDataChangedListener) {
        this.mOnMenuSelectDataChangedListener = onMenuSelectDataChangedListener;
    }

    public interface OnMenuSelectDataChangedListener {

        void onEditonChanged(BookDetailsModel editionModel);

        void onChapterChanged(BookChapterModel chapterModel);

        void onKnowledgeChanged(String KnowledgeModel);

    }

    public BookDetailsModel getSelectJiaoCaiData() {
        return editionModel;
    }

    public BookChapterModel getSelectZhangJieData() {
        return chapterModel;
    }

    public String getSelectZhiShiDianData() {
        return KnowledgeModel;
    }

    private void setTabExtend(int tab) {
        if (tab == TAB_EDITION) {
            mEditonText.setTextColor(getResources().getColor(R.color.maincolor));
            mEditonImage.setImageResource(R.drawable.less_black);
        } else if (tab == TAB_CHAPTER) {
            mChapterText.setTextColor(getResources().getColor(R.color.maincolor));
            mChapterImage.setImageResource(R.drawable.less_black);
        } else if (tab == TAB_KNOWLEDGE) {
            mKnowledgeText.setTextColor(getResources().getColor(R.color.maincolor));
            mKnowledgeImage.setImageResource(R.drawable.less_black);
        }
    }

    private void resetTabExtend(int tab) {
        if (tab == TAB_EDITION) {
            mEditonText.setTextColor(getResources().getColor(R.color.text_color_gey));
            mEditonImage.setImageResource(R.drawable.more_unfold_black);
        } else if (tab == TAB_CHAPTER) {
            mChapterText.setTextColor(getResources().getColor(R.color.text_color_gey));
            mChapterImage.setImageResource(R.drawable.more_unfold_black);
        } else if (tab == TAB_KNOWLEDGE) {
            mKnowledgeText.setTextColor(getResources().getColor(R.color.text_color_gey));
            mKnowledgeImage.setImageResource(R.drawable.more_unfold_black);
        }
    }

    private void setTabClose() {

        mEditonText.setTextColor(getResources().getColor(R.color.text_color_gey));
        mEditonImage.setImageResource(R.drawable.more_unfold_black);

        mChapterText.setTextColor(getResources().getColor(R.color.text_color_gey));
        mChapterImage.setImageResource(R.drawable.more_unfold_black);

        mKnowledgeText.setTextColor(getResources().getColor(R.color.text_color_gey));
        mKnowledgeImage.setImageResource(R.drawable.more_unfold_black);
    }

    public void setEditionData(List<BookDetailsModel> editonList, int default_code) {
        this.mEditonList.clear();
        this.mEditonList.addAll(editonList);
        //初始化教材版本
        editionModel = editonList.get(default_code);
        mEditonText.setText(editionModel.getName());
        mEditonHolder.refreshData(mEditonList, default_code);
        //初始化章节
        this.mChapterList.clear();
        this.mChapterList.addAll(editionModel.getZhangjielist());
        chapterModel = mChapterList.get(default_code);
        mChapterText.setText(chapterModel.getName());
        List<String> chapter = new ArrayList<>();
        for (BookChapterModel chapterModel : mChapterList) {
            chapter.add(chapterModel.getName());
        }
        mChapterHolder.refreshData(chapter, default_code);
        //初始化知识点
        mKnowledgeList.clear();
        mKnowledgeList.addAll(chapterModel.getZhishidianArr());
        mKnowledgeText.setText(mKnowledgeList.get(default_code));
        mKnowledgeHolder.refreshData(mKnowledgeList, default_code);
    }

    private void setRefresh(BookDetailsModel editionModel) {
        //初始化章节
        this.mChapterList.clear();
        this.mChapterList.addAll(editionModel.getZhangjielist());
        chapterModel = mChapterList.get(0);
        mChapterText.setText(chapterModel.getName());
        List<String> chapter = new ArrayList<>();
        for (BookChapterModel chapterModel : mChapterList) {
            chapter.add(chapterModel.getName());
        }
        mChapterHolder.refreshData(chapter, 0);
        //初始化知识点
        mKnowledgeList.clear();
        mKnowledgeList.addAll(chapterModel.getZhishidianArr());
        mKnowledgeText.setText(mKnowledgeList.get(0));
        mKnowledgeHolder.refreshData(mKnowledgeList, 0);
    }
}