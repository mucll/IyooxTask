package com.huisu.iyoox.fragment.home;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huisu.iyoox.R;
import com.huisu.iyoox.fragment.base.BaseFragment;

/**
 * @author: dl
 * @function: 主页fragment
 * @date: 18/6/28
 */
public class HomePageFragment extends BaseFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_page, container, false);
    }

}
