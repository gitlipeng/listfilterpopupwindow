package com.link.support.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.link.support.R;
import com.link.support.adapter.ChildListFilterAdapter;
import com.link.support.bean.FilterBean;
import com.link.support.impl.TowListFilterOnItemClickListener;

import java.util.List;


/**
 * ListFilterPopupWindow. <br>
 * 筛选栏控件.
 * <p>
 * Copyright: Copyright (c) 2014年9月23日 下午5:04:17
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * 
 * @author lipeng@c-platform.com
 * @version 1.0.0
 */
public class TowListFilterPopupWindow extends ListFilterPopupWindow {

	/**
	 * 当前选中的父类位置
	 */
	private int currentParentPosition;

	/**
	 * 当前临时选中的父类位置，只点击了父item，未点击子item
	 */
	private int currentTempParentPosition;

	/**
	 * 当前选中的子类位置
	 */
	private int currentChildPosition;
	
	private TowListFilterOnItemClickListener towListFilterOnItemClickListener;
	
	public TowListFilterPopupWindow(Context context) {
		super(context);
	}

	public TowListFilterPopupWindow(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TowListFilterPopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void initPopupWindow(Context context,int type) {
		super.initPopupWindow(context,2);
		// 显示一级列表
		mParentListView.setVisibility(View.VISIBLE);

		mParentListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				currentTempParentPosition = position;
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						FilterBean bean = list.get(i);
						if (i == position) {
							bean.setSelect(true);
							setChildAdapter(bean.getChildList());
						} else {
							bean.setSelect(false);
						}
					}
					parentFilterAdapter.notifyDataSetChanged();
				}
			}

		});
		mChildListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// 点了子item，确定父item的实际点击位置
				currentParentPosition = currentTempParentPosition;
				currentChildPosition = position;

				setSelectItem(currentParentPosition, position);
				if (pupupWindow.isShowing()) {
					// 消失
					pupupWindow.dismiss();
				}
				List<FilterBean> childList = list.get(currentTempParentPosition).getChildList();
				if (childList != null && towListFilterOnItemClickListener != null) {
					towListFilterOnItemClickListener.onItemClick(list.get(currentTempParentPosition),childList.get(position));
				}
			}
		});
	}

	/**
	 * 初始化时必须设置数据源，默认将数据源的第一个数据名称显示在筛选条上
	 * 
	 * @param parmelist
	 */
	public void setData(List<FilterBean> parmelist) {
		if(parmelist != null){
			list.clear();
			list.addAll(parmelist);
			setSelectItem(0, 0);
		}
	}

	/**
	 * 设置选中的item
	 */
	public void setSelectItem(int parentPosition, int childPosition) {
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				FilterBean bean = list.get(i);
				if (i == parentPosition) {
					bean.setSelect(true);
					setSelectChildItem(parentPosition,bean.getChildList(), childPosition);
				} else {
					bean.setSelect(false);
					setUnSelectChildList(bean.getChildList());
				}
			}
			parentFilterAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 根据具体分类的ID获得该ID对应的父子位置，方便设置默认显示值
	 * @param childId
	 * @return
     */
	public int[] getPositionByChildId(String childId){
		int[] positionArr = new int[2];
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				FilterBean pBean = list.get(i);
				for(int j = 0; j < pBean.getChildList().size(); j++){
					FilterBean cBean = pBean.getChildList().get(j);
					if(cBean.getId().equals(childId)){
						positionArr[0] = i;
						positionArr[1] = j;
						return positionArr;
					}
				}
			}
		}
		return positionArr;
	}

	/**
	 * 设置子列表item被选中
	 * 
	 * @param childList
	 * @param childPosition
	 */
	private void setSelectChildItem(int parentPosition,List<FilterBean> childList, int childPosition) {
		if (childList != null && childList.size() > 0) {
			for (int i = 0; i < childList.size(); i++) {
				FilterBean bean = childList.get(i);
				if (i == childPosition) {
					bean.setSelect(true);
					currentTempParentPosition = parentPosition;
					currentParentPosition = parentPosition;
					currentChildPosition = childPosition;
					if(childPosition == 0){
						//全部区域则显示父列表名称
						mScreeningText.setText(list.get(parentPosition).getName());
					}else{
						mScreeningText.setText(bean.getName());
					}
				} else {
					bean.setSelect(false);
				}
			}
			setChildAdapter(childList);
		}
	}

	/**
	 * 将指定的子列表数据源设为未选中状态
	 * 
	 * @param childList
	 */
	private void setUnSelectChildList(List<FilterBean> childList) {
		if (childList != null && childList.size() > 0) {
			for (int i = 0; i < childList.size(); i++) {
				FilterBean bean = childList.get(i);
				bean.setSelect(false);
			}
		}
	}

	/**
	 * 设置子listview的数据源
	 * 
	 * @param childList
	 */
	private void setChildAdapter(List<FilterBean> childList) {
		childFilterAdapter = new ChildListFilterAdapter(childList, context);
		mChildListView.setAdapter(childFilterAdapter);
		childFilterAdapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		int i = v.getId();
		if (i == R.id.tv_screening) {// 点击筛选条弹出活隐藏popup
			if (pupupWindow != null) {
				if (pupupWindow.isShowing()) {
					// 消失
					pupupWindow.dismiss();
				} else {
					// 展示
					pupupWindow.showAsDropDown(v);
					// 将上次选中的设为选中状态
					setSelectItem(currentParentPosition, currentChildPosition);
					setSelectTitle();
				}
			}

		}
	}

	public TowListFilterOnItemClickListener getTowListFilterOnItemClickListener() {
		return towListFilterOnItemClickListener;
	}

	public void setTowListFilterOnItemClickListener(
			TowListFilterOnItemClickListener towListFilterOnItemClickListener) {
		this.towListFilterOnItemClickListener = towListFilterOnItemClickListener;
	}

}
