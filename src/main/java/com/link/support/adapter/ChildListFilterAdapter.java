package com.link.support.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.link.support.R;
import com.link.support.bean.FilterBean;

import java.util.List;


/**
 * @author lipeng
 * @version 1.0.0
 */
public class ChildListFilterAdapter extends BaseAdapter{

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
	public ChildListFilterAdapter(List<FilterBean> items, Context context) {
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

		ImageView line;

		TextView name;
		
		ImageView selectArrow;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			// 如果为空，则需要创建
			convertView = LayoutInflater.from(context).inflate(R.layout.listfilter_item, null);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.line = (ImageView) convertView.findViewById(R.id.line);
			holder.selectArrow = (ImageView) convertView.findViewById(R.id.selectArrow);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		FilterBean bean = list.get(position);
		holder.name.setText(bean.getName());
		//当二层的数据大于1时，把最后一条线去掉
		if (list.size() > 1) {
			if (position == list.size()-1) {
				holder.line.setVisibility(View.GONE);
			}else {
				holder.line.setVisibility(View.VISIBLE);
			}
		}
		if(bean.isSelect()){
			holder.line.setBackgroundColor(context.getResources().getColor(R.color.filter_title_selected));
			holder.name.setTextColor(context.getResources().getColor(R.color.filter_title_selected));
			holder.selectArrow.setVisibility(View.VISIBLE);
		}else{
			holder.line.setBackgroundColor(context.getResources().getColor(R.color.gray));
			holder.name.setTextColor(context.getResources().getColor(R.color.gray));
			holder.selectArrow.setVisibility(View.GONE);
		}
		return convertView;
	}

}
