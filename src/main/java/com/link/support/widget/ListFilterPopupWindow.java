package com.link.support.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.link.support.R;
import com.link.support.adapter.ChildListFilterAdapter;
import com.link.support.adapter.ParentListFilterAdapter;
import com.link.support.bean.FilterBean;
import com.link.support.impl.ListFilterOnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * ListFilterPopupWindow. <br>
 * 筛选栏控件.
 * <p/>
 * Copyright: Copyright (c) 2014年9月23日 下午5:04:17
 * <p/>
 * Company: 北京宽连十方数字技术有限公司
 * <p/>
 *
 * @author lipeng@c-platform.com
 * @version 1.0.0
 */
public class ListFilterPopupWindow extends LinearLayout implements OnClickListener, OnItemClickListener {

    private LayoutInflater inflater;

    /**
     * 筛选条名称
     */
    protected TextView mScreeningText;

    /**
     * 筛选条右下角的三角图片
     */
    private ImageView mScreeingImage;

    /**
     * 一级列表
     */
    protected ListView mChildListView;

    /**
     * 二级列表，还未做，后续
     */
    protected ListView mParentListView;

    /**
     * 适配器
     */
    protected ParentListFilterAdapter parentFilterAdapter;

    /**
     * 适配器
     */
    protected ChildListFilterAdapter childFilterAdapter;

    /**
     * pupup
     */
    protected PopupWindow pupupWindow;

    /**
     * listview的数据源
     */
    protected List<FilterBean> list;

    /**
     * 点击某一个item后触发的事件
     */
    protected ListFilterOnItemClickListener listFilterOnItemClickListener;

    protected Context context;

    public static final int LEFT_ANIMATION = R.style.popupwindow_anim_left_style;

    public static final int CENTER_ANIMATION = R.style.popupwindow_anim_middle_style;

    public static final int RIGHT_ANIMATION = R.style.popupwindow_anim_right_style;

    public ListFilterPopupWindow(Context context) {
        super(context);
        this.context = context;
        initView(context);
    }

    public ListFilterPopupWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView(context);
    }

    public ListFilterPopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView(context);
    }

    /**
     * 初始化布局
     *
     * @param context
     */
    private void initView(Context context) {
        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.listfilter, this);
        mScreeningText = (TextView) view.findViewById(R.id.tv_screening);
        mScreeingImage = (ImageView) view.findViewById(R.id.img_right);
        mScreeningText.setOnClickListener(this);

        list = new ArrayList<FilterBean>();
        initPopupWindow(context, 1);
    }

    /**
     * 初始化所有的弹出框
     */
    @SuppressWarnings("deprecation")
    protected void initPopupWindow(Context context, int type) {
        View filter = inflater.inflate(R.layout.pop_filter, null);

        // 适配器
        childFilterAdapter = new ChildListFilterAdapter(list, context);
        parentFilterAdapter = new ParentListFilterAdapter(list, context);


        filter.findViewById(R.id.pop_filter_ll).setOnClickListener(this);
        // 父级列表
        mParentListView = (ListView) filter.findViewById(R.id.list1);
        mParentListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mParentListView.setVisibility(View.GONE);
        mParentListView.setAdapter(parentFilterAdapter);

        // 最底层显示级列表
        mChildListView = (ListView) filter.findViewById(R.id.list2);
        mChildListView.setBackgroundColor(getResources().getColor(R.color.white));
        mChildListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mChildListView.setAdapter(childFilterAdapter);
        mChildListView.setOnItemClickListener(this);

        // 声明弹出框
        if (type == 1) {
            pupupWindow = new PopupWindow(filter, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
        } else {
            pupupWindow = new PopupWindow(filter, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
        }
        // 设置背景
        pupupWindow.setBackgroundDrawable(new BitmapDrawable());
        // 外面点击可消失
        pupupWindow.setOutsideTouchable(false);

        pupupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            public void onDismiss() {
                setUnSelectTitle();
            }
        });
        pupupWindow.setAnimationStyle(CENTER_ANIMATION);
    }

    public void setAnimationStyle(int id) {
        pupupWindow.setAnimationStyle(id);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.pop_filter_ll) {//点击阴影部分，筛选栏消失
            if (pupupWindow.isShowing()) {
                // 消失
                pupupWindow.dismiss();
            }

        } else if (i == R.id.tv_screening) {// 点击筛选条弹出活隐藏popup
            if (pupupWindow != null) {
                if (pupupWindow.isShowing()) {
                    // 消失
                    pupupWindow.dismiss();
                } else {
                    // 展示
                    pupupWindow.showAsDropDown(v);
                    setSelectTitle();
                }

            }

        }
    }

    /**
     * 初始化时必须设置数据源，默认将数据源的第一个数据名称显示在筛选条上
     *
     * @param paremlist
     */
    public void setData(List<FilterBean> paremlist) {
        if (paremlist != null) {
            list.clear();
            list.addAll(paremlist);
            setSelectItem(0);
        }
    }

    /**
     * 初始化时必须设置数据源，默认将数据源的第一个数据名称显示在筛选条上
     *
     * @param paremlist
     */
    public void setDataWithNoSelected(List<FilterBean> paremlist) {
        list.clear();
        list.addAll(paremlist);
    }

    /**
     * 取消当前选中项，当选中其他筛选条时，将当前筛选条变为灰色，并将数据源设为全部未点击状态
     */
    public void setUnSelectTitle() {
//		setSelectTitle();
//		 mScreeingImage.setImageResource(R.drawable.right_bottom_unselect);
        mScreeningText.setTextColor(getResources().getColor(R.color.filter_title_unselect));
    }

    /**
     * 将当前筛选条设为选中状态，颜色变为黄色
     */
    public void setSelectTitle() {
//		mScreeingImage.setImageResource(R.drawable.right_bottom_select);
        mScreeningText.setTextColor(getResources().getColor(R.color.filter_title_selected));
    }

    /**
     * 设置选中的item
     */
    public void setSelectItem(int position) {
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                FilterBean bean = list.get(i);
                if (i == position) {
                    bean.setSelect(true);
                    mScreeningText.setText(bean.getName());
                } else {
                    bean.setSelect(false);
                }
            }
        }
        childFilterAdapter.notifyDataSetChanged();
    }

    /**
     * 设置选中的item
     */
    public void setSelectItem(String id) {
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                FilterBean bean = list.get(i);
                if (id.equals(bean.getId())) {
                    bean.setSelect(true);
                    mScreeningText.setText(bean.getName());
                } else {
                    bean.setSelect(false);
                }
            }
        }
        childFilterAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FilterBean selectBean = list.get(position);
        setSelectItem(position);
        setSelectTitle();
        if (pupupWindow.isShowing()) {
            // 消失
            pupupWindow.dismiss();
        }
        if (listFilterOnItemClickListener != null) {
            listFilterOnItemClickListener.onItemClick(selectBean);
        }
    }

    public void setonItemClickListener(ListFilterOnItemClickListener listener) {
        this.listFilterOnItemClickListener = listener;
    }


    public PopupWindow getPopupWindow() {
        return pupupWindow;
    }

    public void setDefaultText(String name) {
        mScreeningText.setText(name);
    }


    public List<FilterBean> getList() {
        return list;
    }

    public void setList(List<FilterBean> list) {
        this.list = list;
    }
}
