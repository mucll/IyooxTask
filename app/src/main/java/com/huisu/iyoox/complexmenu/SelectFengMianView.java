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
import com.huisu.iyoox.complexmenu.holder.SubjectGridFengmianHolder;
import com.huisu.iyoox.entity.ArtBookModel;
import com.huisu.iyoox.entity.ArtBookZhangJieModel;
import com.huisu.iyoox.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 3个tab封面图片view
 */
public class SelectFengMianView extends LinearLayout {

    private static final int TAB_EDITION = 1;
    private static final int TAB_CHAPTER = 2;
    private static final int TAB_KNOWLEDGE = 3;

    private Context mContext;

    private View mEditonView;
    private View mChapterView;
    private View mKnowledgeView;

    private View mPopupWindowView;

    private FrameLayout mMainContentLayout;

    private int selectIndexOf = -1;

    /**
     * 教材版本
     */
    private SubjectGridFengmianHolder oneHolder;
    private boolean isEditionSelect;
    /**
     * 章节
     */
    private SubjectGridFengmianHolder twoHolder;
    private boolean isChapterSelect;
    /**
     * 知识点
     */
    private SubjectGridFengmianHolder threeHolder;
    private boolean isKnowledgeSelect;

    private OnMenuSelectDataChangedListener mOnMenuSelectDataChangedListener;

    private RelativeLayout mContentLayout;

    private TextView oneText;
    private TextView twoText;
    private TextView threeText;
    private ImageView mEditonImage;
    private ImageView mChapterImage;
    private ImageView mKnowledgeImage;

    private List<ArtBookZhangJieModel> oneList = new ArrayList<>();
    private List<ArtBookZhangJieModel> twoList = new ArrayList<>();
    private List<ArtBookZhangJieModel> threeList = new ArrayList<>();


    private int mTabRecorder = -1;
    private View oneUnderline;
    private View twoUnderline;
    private View threeUnderline;
    private int selectTab = -1;

    public SelectFengMianView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public SelectFengMianView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init() {

        //教材版本
        oneHolder = new SubjectGridFengmianHolder(mContext);
        oneHolder.setOnRightListViewItemSelectedListener(new SubjectGridFengmianHolder.OnRightListViewItemSelectedListener() {
            @Override
            public void OnRightListViewItemSelected(int rightIndex, ArtBookZhangJieModel model) {
                //点击回调 重新请求章节跟知识点
                if (mOnMenuSelectDataChangedListener != null) {
                    mOnMenuSelectDataChangedListener.onOneChanged(model);
                }
                selectIndexOf = rightIndex;

                selectTab = 1;
                setAllUnSelect();
                dismissPopupWindow();
            }
        });

        //章节
        twoHolder = new SubjectGridFengmianHolder(mContext);
        twoHolder.setOnRightListViewItemSelectedListener(new SubjectGridFengmianHolder.OnRightListViewItemSelectedListener() {
            @Override
            public void OnRightListViewItemSelected(int rightIndex, ArtBookZhangJieModel model) {
                if (mOnMenuSelectDataChangedListener != null) {
                    mOnMenuSelectDataChangedListener.onTwoChanged(model);
                }
                selectIndexOf = rightIndex;
                selectTab = 2;
                setAllUnSelect();
                dismissPopupWindow();
                //章节有变化知识点跟着变化

            }
        });

        //知识点
        threeHolder = new SubjectGridFengmianHolder(mContext);
        threeHolder.setOnRightListViewItemSelectedListener(new SubjectGridFengmianHolder.OnRightListViewItemSelectedListener() {
            @Override
            public void OnRightListViewItemSelected(int rightIndex, ArtBookZhangJieModel model) {
                if (mOnMenuSelectDataChangedListener != null) {
                    mOnMenuSelectDataChangedListener.onThreeChanged(model);
                }
                selectIndexOf = rightIndex;

                selectTab = 3;
                setAllUnSelect();
                dismissPopupWindow();
            }
        });
    }

    private int getSubjectId(int index) {
        return index;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(mContext, R.layout.layout_fengmian_search_menu, this);

        oneText = findViewById(R.id.subject);
        mEditonImage = findViewById(R.id.img_sub);
        oneUnderline = findViewById(R.id.one_underline);

        twoText = findViewById(R.id.comprehensive_sorting);
        mChapterImage = findViewById(R.id.img_cs);
        twoUnderline = findViewById(R.id.two_underline);

        threeText = findViewById(R.id.tv_select);
        mKnowledgeImage = findViewById(R.id.img_sc);
        threeUnderline = findViewById(R.id.three_underline);

        mContentLayout = findViewById(R.id.rl_content);
        mPopupWindowView = View.inflate(mContext, R.layout.layout_search_menu_content, null);
        mMainContentLayout = mPopupWindowView.findViewById(R.id.rl_main);
        mEditonView = findViewById(R.id.ll_subject);
        mChapterView = findViewById(R.id.ll_sort);
        mKnowledgeView = findViewById(R.id.ll_select);

        //点击第一个栏目
        mEditonView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setOneViewClick();
            }
        });
        //点击第二个栏目
        mChapterView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setTwoViewClick();
            }
        });
        //点击第三个栏目
        mKnowledgeView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setThreeViewClick();
            }
        });

        mContentLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissPopupWindow();
            }
        });
    }

    /**
     * 点击第一个栏目逻辑
     */
    private void setOneViewClick() {
        if (oneList.size() == 0) {
            return;
        }
        setAllUnderlineGone();
        oneUnderline.setVisibility(View.VISIBLE);
        if (selectTab == 1) {
            oneHolder.setmRightSelectedIndex(selectIndexOf);
        } else {
            oneHolder.setmRightSelectedIndex(-1);
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

    /**
     * 点击第二个栏目逻辑
     */
    private void setTwoViewClick() {
        if (twoList.size() == 0) {
            return;
        }
        setAllUnderlineGone();
        twoUnderline.setVisibility(View.VISIBLE);
        if (selectTab == 2) {
            twoHolder.setmRightSelectedIndex(selectIndexOf);
        } else {
            twoHolder.setmRightSelectedIndex(-1);
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

    /**
     * 点击第三个栏目逻辑
     */
    private void setThreeViewClick() {
        if (threeList.size() == 0) {
            return;
        }
        setAllUnderlineGone();
        threeUnderline.setVisibility(View.VISIBLE);
        if (selectTab == 3) {
            threeHolder.setmRightSelectedIndex(selectIndexOf);
        } else {
            threeHolder.setmRightSelectedIndex(-1);
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

    private void setAllUnSelect() {
        isEditionSelect = false;
        isChapterSelect = false;
        isKnowledgeSelect = false;
    }

    private void setAllUnderlineGone() {
        oneUnderline.setVisibility(View.GONE);
        twoUnderline.setVisibility(View.GONE);
        threeUnderline.setVisibility(View.GONE);
    }

    private void handleClickSubjectView() {

        mMainContentLayout.removeAllViews();
        mMainContentLayout.addView(oneHolder.getRootView());
        oneHolder.refreshData();
        popUpWindow(TAB_EDITION);
    }

    private void handleClickSortView() {

        mMainContentLayout.removeAllViews();
        mMainContentLayout.addView(twoHolder.getRootView());
        twoHolder.refreshData();
        popUpWindow(TAB_CHAPTER);
    }

    private void handleClickSelectView() {

        mMainContentLayout.removeAllViews();
        mMainContentLayout.addView(threeHolder.getRootView());
        threeHolder.refreshData();
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
        LinearLayout linearLayout = mPopupWindowView.findViewById(R.id.ll_content_layout);
        LayoutParams params1 = (LayoutParams) linearLayout.getLayoutParams();
        params1.width = LayoutParams.MATCH_PARENT;
        params1.height = LayoutParams.MATCH_PARENT;
        linearLayout.setLayoutParams(params1);
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

        void onOneChanged(ArtBookZhangJieModel editionModel);

        void onTwoChanged(ArtBookZhangJieModel chapterModel);

        void onThreeChanged(ArtBookZhangJieModel KnowledgeModel);

    }


    private void setTabExtend(int tab) {
        if (tab == TAB_EDITION) {
            oneText.setTextColor(getResources().getColor(R.color.maincolor));
//            mEditonImage.setImageResource(R.drawable.less_black);
        } else if (tab == TAB_CHAPTER) {
            twoText.setTextColor(getResources().getColor(R.color.maincolor));
//            mChapterImage.setImageResource(R.drawable.less_black);
        } else if (tab == TAB_KNOWLEDGE) {
            threeText.setTextColor(getResources().getColor(R.color.maincolor));
//            mKnowledgeImage.setImageResource(R.drawable.less_black);
        }
    }

    private void resetTabExtend(int tab) {
        if (tab == TAB_EDITION) {
            oneText.setTextColor(getResources().getColor(R.color.text_color_gey));
//            mEditonImage.setImageResource(R.drawable.more_unfold_black);
        } else if (tab == TAB_CHAPTER) {
            twoText.setTextColor(getResources().getColor(R.color.text_color_gey));
//            mChapterImage.setImageResource(R.drawable.more_unfold_black);
        } else if (tab == TAB_KNOWLEDGE) {
            threeText.setTextColor(getResources().getColor(R.color.text_color_gey));
//            mKnowledgeImage.setImageResource(R.drawable.more_unfold_black);
        }
    }

    private void setTabClose() {

        oneText.setTextColor(getResources().getColor(R.color.text_color_gey));
//        mEditonImage.setImageResource(R.drawable.more_unfold_black);

        twoText.setTextColor(getResources().getColor(R.color.text_color_gey));
//        mChapterImage.setImageResource(R.drawable.more_unfold_black);

        threeText.setTextColor(getResources().getColor(R.color.text_color_gey));
//        mKnowledgeImage.setImageResource(R.drawable.more_unfold_black);
    }

    public void setListData(List<ArtBookModel> models) {
        if (models.size() > 0) {
            oneList.addAll(models.get(0).getZhangjie());
            oneText.setText(models.get(0).getGrade_name());
            oneHolder.refreshData(oneList);
            setAllUnderlineGone();
        }
        if (models.size() >= 1) {
            twoList.addAll(models.get(1).getZhangjie());
            twoText.setText(models.get(1).getGrade_name());
            twoHolder.refreshData(twoList);
        }
        if (models.size() >= 2) {
            threeList.addAll(models.get(2).getZhangjie());
            threeText.setText(models.get(2).getGrade_name());
            threeHolder.refreshData(threeList);
        }
    }

    public void setSelectLayout(int gradeId) {
        if (gradeId == 0) {
            oneUnderline.setVisibility(View.VISIBLE);
            setOneViewClick();
        } else if (gradeId > 0 && gradeId <= 6) {
            twoUnderline.setVisibility(View.VISIBLE);
            setTwoViewClick();
        } else if (gradeId > 6) {
            threeUnderline.setVisibility(View.VISIBLE);
            setThreeViewClick();
        }
    }

}