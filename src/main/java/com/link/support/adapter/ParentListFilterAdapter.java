package com.link.support.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.link.support.R;
import com.link.support.util.Util;
import com.link.support.bean.FilterBean;

import java.util.List;


/**
 * @author lipeng
 * @version 1.0.0
 */
public class ParentListFilterAdapter extends BaseAdapter {

	/**
	 * 条目
	 */
	public List<FilterBean> list;

	private Context context;

	/**
	 * 构造方法
	 * 
	 * @param items
	 *            条目
	 */
	public ParentListFilterAdapter(List<FilterBean> items, Context context) {
		this.list = items;
		this.context = context;
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		return list != null && list.size() > position ? list.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	final class ViewHolder {

		LinearLayout layout;

		ImageView line;

		TextView name;

		ImageView selectArrow,select;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			// 如果为空，则需要创建
			convertView = LayoutInflater.from(context).inflate(R.layout.listfilter_item, null);
			holder.layout = (LinearLayout) convertView.findViewById(R.id.layout);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.line = (ImageView) convertView.findViewById(R.id.line);
			holder.selectArrow = (ImageView) convertView.findViewById(R.id.selectArrow);
			holder.select = (ImageView) convertView.findViewById(R.id.main_select);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
//		holder.layout.setBackground(null);
		holder.layout.setBackgroundDrawable(null);
		FilterBean bean = list.get(position);
		holder.name.setText(bean.getName());
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.name.getLayoutParams();

		holder.selectArrow.setVisibility(View.GONE);
		holder.line.setVisibility(View.GONE);

		if (bean.isSelect()) {
			holder.name.setTextColor(context.getResources().getColor(R.color.filter_title_selected));
//			holder.layout.setBackgroundColor(context.getResources().getColor(R.color.white));
			holder.select.setVisibility(View.VISIBLE);
			if(Util.isNotEmpty(bean.getPressedImage()) && Util.getImgIdByImgName(context, bean.getPressedImage()) != 0){
				holder.name.setCompoundDrawablesWithIntrinsicBounds(Util.getImgIdByImgName(context, bean.getPressedImage()),0,0,0);
				holder.name.setCompoundDrawablePadding(10);
				layoutParams.addRule(RelativeLayout.ALIGN_LEFT);
			}else{
				// 文字居中
				layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
				holder.name.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
			}
		} else {
			holder.select.setVisibility(View.GONE);
			holder.name.setTextColor(context.getResources().getColor(R.color.gray));
			holder.layout.setBackgroundColor(context.getResources().getColor(R.color.lightgray));
			if(Util.isNotEmpty(bean.getNormalImage()) && Util.getImgIdByImgName(context, bean.getNormalImage()) != 0){
				holder.name.setCompoundDrawablesWithIntrinsicBounds(Util.getImgIdByImgName(context, bean.getNormalImage()),0,0,0);
				holder.name.setCompoundDrawablePadding(10);
				layoutParams.addRule(RelativeLayout.ALIGN_LEFT);
			}else{
				// 文字居中
				layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
				holder.name.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
			}
		}
		holder.name.setLayoutParams(layoutParams);
		return convertView;
	}

}
