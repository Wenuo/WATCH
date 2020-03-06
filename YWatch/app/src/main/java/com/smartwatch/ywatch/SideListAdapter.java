package com.smartwatch.ywatch;
import android.widget.*;
import android.view.*;
import java.util.*;
import android.content.*;
public class SideListAdapter extends BaseAdapter
{

	private List<Tool> data;
    private Context context;
    private LayoutInflater inflater;

    public SideListAdapter(List<Tool> data, Context context) {
        this.data = data;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //将布局文件转化成View
        View view = inflater.inflate(R.layout.side_item, null);
        //找控件
        TextView ToolName = (TextView) view.findViewById(R.id.side_itemTextView);
        ImageView ToolIcon = (ImageView) view.findViewById(R.id.side_itemImageView);
        //设置数据
        Tool tool = data.get(position);
        ToolName.setText(tool.gettoolName());
	    ToolIcon.setImageResource(tool.gettoolIcon());
        return view;
    }
}
