package com.skula.lybreria;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.skula.lybreria.models.ExplorerItem;

public class ItemAdapter extends ArrayAdapter<ExplorerItem> {

	Context context;
	int layoutResourceId;
	ExplorerItem data[] = null;

	public ItemAdapter(Context context, int layoutResourceId, ExplorerItem[] data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ExplorerItem item = data[position];
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.itemlayout, parent, false);

		TextView name = (TextView) rowView.findViewById(R.id.name);
		name.setText(item.getName());
		
		ImageView picture = (ImageView) rowView.findViewById(R.id.picture);
		if(item.isDirectory()){
			picture.setImageResource(R.drawable.directory);
		}else{
			if(isVideoFile(item.getName())){
				if(item.isSubtitled()){
					picture.setImageResource(R.drawable.movie_srt);
				}else{
					picture.setImageResource(R.drawable.movie);
				}
			}else{
				picture.setImageResource(R.drawable.music);
			}
		}
		return rowView;
	}

	public boolean isVideoFile(String name){
		return name.endsWith(".mp4") || name.endsWith(".avi") || name.endsWith(".mkv");
	}
}
