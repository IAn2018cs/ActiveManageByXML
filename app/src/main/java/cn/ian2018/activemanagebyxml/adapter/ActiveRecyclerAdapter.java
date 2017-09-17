package cn.ian2018.activemanagebyxml.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.ian2018.activemanagebyxml.MyApplication;
import cn.ian2018.activemanagebyxml.R;
import cn.ian2018.activemanagebyxml.model.Active;

/**
 * Created by Administrator on 2017/1/23/023.
 */

public class ActiveRecyclerAdapter extends RecyclerView.Adapter<ActiveRecyclerAdapter.ViewHolder> {

    private List<Active> mActiveList;
    private OnRecyclerViewOnClickListener mListener;
    private OnDeleteClickListener mDeleteListener;

    static class ViewHolder extends RecyclerView.ViewHolder {

        View activeView;
        ImageView activeImage;
        ImageView deleteImage;
        TextView activeNameText;
        TextView activeTimeText;
        TextView activeLocText;

        public ViewHolder(View itemView) {
            super(itemView);
            activeView = itemView;
            activeImage = (ImageView) itemView.findViewById(R.id.active_image);
            deleteImage = (ImageView) itemView.findViewById(R.id.iv_delete);
            activeNameText = (TextView) itemView.findViewById(R.id.active_name);
            activeTimeText = (TextView) itemView.findViewById(R.id.active_time);
            activeLocText = (TextView) itemView.findViewById(R.id.active_location);
        }
    }

    /**
     * 定义一个item点击事件接口
     */
    public interface OnRecyclerViewOnClickListener {
        void onItemClick(View view, int position);
    }

    public void setItemClickListener(OnRecyclerViewOnClickListener listener){
        this.mListener = listener;
    }

    /**
     * 定义删除点击事件接口
     */
    public interface OnDeleteClickListener {
        void onDeleteClick(View view, int position);
    }

    public void setDeleteClickListener(OnDeleteClickListener listener){
        this.mDeleteListener = listener;
    }

    public ActiveRecyclerAdapter(List<Active> activeList) {
        mActiveList = activeList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_active_layout, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        // 设置点击事件
        holder.activeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                // 设置接口回调
                mListener.onItemClick(view, position);
            }
        });

        // 删除
        holder.deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                // 设置接口回调
                mDeleteListener.onDeleteClick(view, position);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Active active = mActiveList.get(position);
        Glide.with(MyApplication.getContext()).load(active.getImage()).placeholder(R.drawable.ic_placeholder).into(holder.activeImage);
        holder.activeNameText.setText(active.getName());
        holder.activeTimeText.setText(active.getTime());
        holder.activeLocText.setText(active.getLocation());
    }

    @Override
    public int getItemCount() {
        return mActiveList.size();
    }
}
