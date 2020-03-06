package com.smartwatch.ywatch;
import android.widget.*;
import android.content.*;
import java.util.*;
import android.view.*;
public class TraceListAdapter extends BaseAdapter {
    private Context context;
    private List<Trace> traceList = new ArrayList<>(1);
    private static final int TYPE_TOP = 0x0000;
    private static final int TYPE_NORMAL= 0x0001;

    public TraceListAdapter(Context context, List<Trace> traceList) {
        this.context = context;
        this.traceList = traceList;
    }

    @Override
    public int getCount() {
        return traceList.size();
    }

    @Override
    public Trace getItem(int position) {
        return traceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final Trace trace = getItem(position);
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_trace, parent, false);
            holder.tvAcceptTime = (TextView) convertView.findViewById(R.id.tvAcceptTime);
            holder.tvAcceptStation = (TextView) convertView.findViewById(R.id.tvAcceptStation);
            holder.tvTopLine = (TextView) convertView.findViewById(R.id.tvTopLine);
            holder.tvDot = (TextView) convertView.findViewById(R.id.tvDot);
			holder.tv_bpm=(TextView)convertView.findViewById(R.id.trace_bpm);
			holder.tv_image=(ImageView)convertView.findViewById(R.id.trace_iamge);
            convertView.setTag(holder);
        }

        if (getItemViewType(position) == TYPE_TOP) {
            // 第一行头的竖线不显示
            holder.tvTopLine.setVisibility(View.INVISIBLE);
            // 字体颜色加深
            holder.tvAcceptTime.setTextColor(0xff555555);
            holder.tv_bpm.setTextColor(0xff555555);
			holder.tv_image.setColorFilter(0xff555555);
            holder.tvDot.setBackgroundResource(R.drawable.timeline_dot_first);
        } else if (getItemViewType(position) == TYPE_NORMAL) {
            holder.tvTopLine.setVisibility(View.VISIBLE);
            holder.tvAcceptTime.setTextColor(0xff999999);
			holder.tv_bpm.setTextColor(0xff999999);
			holder.tv_image.setColorFilter(0xff999999);
            holder.tvDot.setBackgroundResource(R.drawable.timelline_dot_normal);
        }

        holder.tvAcceptTime.setText(trace.getAcceptTime());
        holder.tvAcceptStation.setText(trace.getAcceptStation());
        return convertView;
    }

    @Override
    public int getItemViewType(int id) {
        if (id == 0) {
            return TYPE_TOP;
        }
        return TYPE_NORMAL;
    }

    static class ViewHolder {
        public TextView tvAcceptTime, tvAcceptStation,tv_bpm;
        public TextView tvTopLine, tvDot;
		public ImageView tv_image;
    }
}
