package com.huisu.iyoox.fragment.home;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huisu.iyoox.R;
import com.huisu.iyoox.fragment.base.BaseFragment;
import com.huisu.iyoox.util.StatusBarUtil;

/**
 * @author: dl
 * @function: 班级Fragment
 * @date: 18/6/28
 */
public class ClassFragment extends BaseFragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_class, container, false);
    }

}
