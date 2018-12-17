package com.ctd.choose.view.adapters;

import android.content.Context;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.recyclerview.widget.RecyclerView;
import com.ctd.choose.R;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018\1\19 0019.
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int ADD = -100;
    public static final int UPDATE = -101;

    protected Context mContext;
    protected List<T> mData;

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    protected OnItemClick mOnItemClick;
    protected OnLongItemClick mOnLongItemClick;


    protected boolean mEditMode;
    protected boolean[] mChecked;
    private int lastPosition = -1;
    private boolean mEnableAnim;

    public BaseAdapter(Context context, List<T> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        mOnItemClick = new OnItemClick(this, holder, position);
        mOnLongItemClick = new OnLongItemClick(this, holder, position);

        if (mEnableAnim && position > lastPosition && position > 0) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_recyclerview_enter_bottom);
            holder.itemView.startAnimation(animation);
        }
        lastPosition = position;
    }

    public void enableAnim(boolean enableAnim) {
        mEnableAnim = enableAnim;
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    public void setDatas(List<T> datas, int updateType) {
        if (updateType == ADD) {
            mData.addAll(datas);
            if (isEditMode()) {
                boolean[] temp = new boolean[mChecked.length];
                System.arraycopy(mChecked, 0, temp, 0, temp.length);
                mChecked = new boolean[mData.size()];
                System.arraycopy(temp, 0, mChecked, 0, temp.length);
            }
//            notifyDataSetChanged();
            notifyItemInserted(mData.size());
        } else {
            mData.clear();
            mData.addAll(datas);
            if (isEditMode()) {
                mChecked = new boolean[mData.size()];
            }
            notifyDataSetChanged();
        }
    }

    public void clear() {
        if (mData != null) {
            mData.clear();
        }
        notifyDataSetChanged();
    }

    public List<T> getDatas() {
        return mData;
    }

    public int getCount() {
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }

    public T getDataByPosition(int position) {
        if (position>=mData.size()){
            return null;
        }
        return mData.get(position);
    }


    public boolean isEditMode() {
        return mEditMode;
    }

    public void setEditMode(boolean editMode) {
        mEditMode = editMode;
        if(mEditMode) {
            mChecked = new boolean[mData.size()];
        }
    }

    public boolean isChecked(int position) {
        if(mChecked == null || mChecked.length <= 0) {
            return false;
        }
        return mChecked[position];
    }

    public void checked(int position, boolean checked) {
        mChecked[position] = checked;
        notifyItemChanged(position);
    }

    public void checkedAll() {
        if(mChecked != null && mChecked.length > 0) {
            for(int i = 0; i < mChecked.length; i++) {
                mChecked[i] = true;
            }
        }
        notifyDataSetChanged();
    }

    public boolean isAllChecked() {
        if(mChecked != null && mChecked.length > 0) {
            for(int i = 0; i < mChecked.length; i++) {
                if(!mChecked[i]) {
                    return false;
                }
            }
        }
        return true;
    }

    public void uncheckedAll() {
        if(mChecked != null && mChecked.length > 0) {
            for(int i = 0; i < mChecked.length; i++) {
                mChecked[i] = false;
            }
        }
        notifyDataSetChanged();
    }

    public int getCheckedNum() {
        int num = 0;
        if(mChecked != null && mChecked.length > 0) {
            for(int i = 0; i < mChecked.length; i++) {
                if(mChecked[i]) {
                    num++;
                }
            }
        }
        return num;
    }

    public List<T> getCheckedDatas() {
        if(mChecked == null || mChecked.length <= 0) {
            return null;
        }
        List<T> checkedDatas = new ArrayList<>();
        for(int i = 0; i < mChecked.length; i++) {
            if(mChecked[i]) {
                checkedDatas.add(mData.get(i));
            }
        }
        return checkedDatas;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mOnItemLongClickListener = listener;
    }


    public interface OnItemClickListener {

        void onItemClick(RecyclerView.ViewHolder holder, View view, int position);
    }

    public interface OnItemLongClickListener {

        boolean onItemLongClick(RecyclerView.ViewHolder holder, View view, int position);
    }

    private static class OnItemClick implements View.OnClickListener {

        WeakReference<BaseAdapter> wr;
        RecyclerView.ViewHolder holder;
        int position;

        public OnItemClick(BaseAdapter adapter, RecyclerView.ViewHolder holder, int position) {
            wr = new WeakReference<>(adapter);
            this.holder = holder;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if(wr == null) {
                return;
            }
            BaseAdapter adapter = wr.get();
            if(adapter == null) {
                return;
            }
            if(adapter.mOnItemClickListener != null) {
                adapter.mOnItemClickListener.onItemClick(holder, v, position);
            }
        }
    }

    private static class OnLongItemClick implements View.OnLongClickListener {

        WeakReference<BaseAdapter> wr;
        RecyclerView.ViewHolder holder;
        int position;

        public OnLongItemClick(BaseAdapter adapter, RecyclerView.ViewHolder holder, int position) {
            wr = new WeakReference<>(adapter);
            this.holder = holder;
            this.position = position;
        }

        @Override
        public boolean onLongClick(View v) {
            if(wr == null) {
                return false;
            }
            BaseAdapter adapter = wr.get();
            if(adapter == null) {
                return false;
            }
            if(adapter.mOnItemLongClickListener == null) {
                return false;
            }
            return adapter.mOnItemLongClickListener.onItemLongClick(holder, v, position);
        }
    }
}
