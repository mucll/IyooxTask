package com.huisu.iyoox.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.adapter.SerchListAdapter;
import com.huisu.iyoox.entity.SerchBean;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    @Bind(R.id.back)
    ImageButton back;
    @Bind(R.id.serch_edit)
    EditText serchEdit;
    @Bind(R.id.serch)
    TextView serch;
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.clear)
    TextView clear;
    @Bind(R.id.activity_search)
    LinearLayout activitySearch;
    @Bind(R.id.searchListView)
    ListView searchListView;
    @Bind(R.id.search_layout)
    LinearLayout searchLayout;
    @Bind(R.id.delete_btn)
    ImageButton deleteBtn;

    List<SerchBean> mlist = new ArrayList<>();
    SerchListAdapter adapter;

    private void initList() {
        adapter = new SerchListAdapter(this, mlist);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        searchListView.setOnItemClickListener(this);
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        serchEdit.clearFocus();
        serchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(serchEdit.getText())) {
                    deleteBtn.setVisibility(View.GONE);
                } else {
                    deleteBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        serchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                /*判断是否是“GO”键*/
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (serchEdit.getText().length() != 0) {
                        ContentValues cv = new ContentValues();
                        cv.put("upDateTime", System.currentTimeMillis());
                        int i = LitePal.updateAll(SerchBean.class, cv, "title=?", serchEdit.getText().toString());
                        if (i == 0) {
                            SerchBean bean = new SerchBean();
                            bean.setTitle(serchEdit.getText().toString());
                            bean.setUpDateTime(System.currentTimeMillis());
                            bean.save();
                        }
                        searchObject(serchEdit.getText().toString());
                    }
                    return false;
                }
                return false;
            }
        });
        initList();
    }

    @Override
    protected void initData() {
        mlist.clear();
        mlist.addAll(LitePal.limit(10).order("upDateTime desc").find(SerchBean.class));
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void setEvent() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_search;
    }

    @OnClick({R.id.back, R.id.serch, R.id.clear, R.id.delete_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.serch:
                if (TextUtils.isEmpty(serchEdit.getText())) {
                    return;
                }
                ContentValues cv = new ContentValues();
                cv.put("upDateTime", System.currentTimeMillis());
                int i = LitePal.updateAll(SerchBean.class, cv, "title=?", serchEdit.getText().toString());
                if (i == 0) {
                    SerchBean bean = new SerchBean();
                    bean.setTitle(serchEdit.getText().toString());
                    bean.setUpDateTime(System.currentTimeMillis());
                    bean.save();
                }
                searchObject(serchEdit.getText().toString());
                break;
            case R.id.clear:
                LitePal.deleteAll(SerchBean.class);
                initData();
                break;
            case R.id.delete_btn:
                serchEdit.setText("");
                break;
        }
    }

    private void searchObject(String key) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.listView:
                break;
            case R.id.searchListView:
                break;
        }
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }
}
