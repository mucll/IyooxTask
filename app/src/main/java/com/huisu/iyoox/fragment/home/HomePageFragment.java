package com.huisu.iyoox.fragment.home;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.huisu.iyoox.R;
import com.huisu.iyoox.fragment.AboutUsFragment;
import com.huisu.iyoox.fragment.base.BaseFragment;

import java.util.ArrayList;

/**
 * @author: dl
 * @function: 主页fragment
 * @date: 18/6/28
 */
public class HomePageFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener {

    private View view;
    private RadioGroup radioGroup;
    private ArrayList<BaseFragment> fragments = new ArrayList<>();
    private FragmentManager fm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_page, container, false);
        initView();
        setEvent();
        initFragment();
        return view;
    }

    private void initFragment() {
        fragments.clear();
        fragments.add(new AboutUsFragment());
        ((RadioButton) radioGroup.getChildAt(0)).setChecked(true);
    }

    private void setEvent() {
        radioGroup.setOnCheckedChangeListener(this);
    }

    private void initView() {
        radioGroup = view.findViewById(R.id.home_page_radio_group);
        fm = getActivity().getSupportFragmentManager();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int indexof = group.indexOfChild(group.findViewById(checkedId));
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.home_page_content_ll, fragments.get(0));
        ft.commit();
    }
}
