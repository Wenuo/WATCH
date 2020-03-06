package com.smartwatch.ywatch;
import android.*;
import android.bluetooth.*;
import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;


public class MyListAdapter extends BaseAdapter {
	public List<BluetoothDevice> mdevice;
    private LayoutInflater mInflater;

    public MyListAdapter(Context context , List<BluetoothDevice> list){
        mdevice = list;
        mInflater = LayoutInflater.from(context);
    }

    //获取传入的数组大小
    @Override
    public int getCount() {
        return mdevice.size();
    }

    //获取第N条数据
    @Override
    public Object getItem(int i) {
        return mdevice.get(i);
    }

    //获取item id
    @Override
    public long getItemId(int i) {
        return i;
    }

    //主要方法
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = new ViewHolder();
        if(view == null){
            //首先为view绑定布局
            view = mInflater.inflate(R.layout.list_item , null);
            viewHolder.name = (TextView) view.findViewById(R.id.device_name);
            viewHolder.address=(TextView) view.findViewById(R.id.device_address);
           viewHolder.icon=(ImageView)view.findViewById(R.id.device_type);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
      //  BluetoothDevice bd = mlist.get(i);
        viewHolder.name.setText(mdevice.get(i).getName());
        viewHolder.address.setText(mdevice.get(i).getAddress());
       if(("honor band A2-3CE").equals(mdevice.get(i).getName())||("YD20067").equals(mdevice.get(i).getName())||("YWATCH").equals(mdevice.get(i).getName())){
			viewHolder.icon.setImageResource(R.drawable.icons_smartwatch);
		}
		else{
			viewHolder.icon.setImageResource(R.drawable.icons_bluetooth);
		}
        //viewHolder.status.setText(R.string.noconnect);
        return view;
    }

    class ViewHolder{
        private TextView name , address;
		private ImageView icon;
    }

	
}
