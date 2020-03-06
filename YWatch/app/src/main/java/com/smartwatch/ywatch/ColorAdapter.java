package com.smartwatch.ywatch;
import android.widget.*;
import android.content.*;
import android.view.*;
import java.util.*;
import android.support.v7.widget.*;


public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorViewHolder>
{
	
	private List<Colors> mcolors;
	//private LayoutInflater layoutInflater;
	static class ColorViewHolder extends RecyclerView.ViewHolder{
	   ImageView image;
		public ColorViewHolder (View itemView){
			super(itemView);
			image=(ImageView) itemView.findViewById(R.id.item_color_iamge);
		}
	}
	
	
	public ColorAdapter(List<Colors> mcolors){
		this.mcolors=mcolors;
	}

	@Override
	public ColorViewHolder onCreateViewHolder(ViewGroup p1, int p2)
	{
		// TODO: Implement this method
		View view = LayoutInflater.from(p1.getContext()).inflate(R.layout.color_item,p1,false);
        ColorViewHolder holder = new ColorViewHolder(view);
        return holder;
	}

	public void onBindViewHolder(final ColorViewHolder holder, final int position)
	{
		Colors colors=mcolors.get(position);
	    holder.image.setImageResource(colors.getColorIcon());
	
        holder.itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//触发自定义监听的单击事件
					onItemClickListener.onItemClick(holder.itemView,position);
				}
			});
        //长按
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					//触发自定义监听的长按事件
					onItemClickListener.onItemLongClick(holder.itemView,position);

					return  true;//表示此事件已经消费，不会触发单击事件
				}
			});
    }

	public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    private OnItemClickListener onItemClickListener;

    /**
     * 自定义监听回调，RecyclerView 的 单击和长按事件
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
	
	@Override
	public int getItemCount()
	{
		// TODO: Implement this method
		return mcolors.size();
	}
	
	
	}
 

