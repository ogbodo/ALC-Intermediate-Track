package com.gmail.softpronigeria.androidlearningcommunity;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyCustomList extends ArrayAdapter<UserProfile> {
	private final Activity context;
	private final List<UserProfile> listItemModelObj;

	public MyCustomList(Activity context, List<UserProfile> listItemModelObj) {
		super(context, R.layout.items, listItemModelObj);
		this.context = context;
		this.listItemModelObj = listItemModelObj;

	}

	@SuppressLint({ "ViewHolder", "InflateParams" })
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.items, null, true);
		TextView txtTitle = (TextView) rowView.findViewById(R.id.username);

		ImageView imageView = (ImageView) rowView.findViewById(R.id.image);

		txtTitle.setText(listItemModelObj.get(position).getUsername());

		imageView.setImageBitmap(listItemModelObj.get(position).getImgBitmap());

		return rowView;
	}
}
