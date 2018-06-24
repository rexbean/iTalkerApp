package net.rex.italker.common.widget.recycler;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.rex.italker.common.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class RecyclerAdapter<Data> extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder<Data>>
implements View.OnClickListener,View.OnLongClickListener, AdapterCallback<Data>{
    private final List<Data> mDataList;
    private AdapterListener<Data> mListener;

    /***
     * constructor
     * @param dataList data list
     * @param listener cell listener
     */
    public RecyclerAdapter(List<Data> dataList, AdapterListener<Data> listener){
        this.mDataList = dataList;
        this.mListener = listener;
    }

    public RecyclerAdapter(){
        this(null);
    }
    public RecyclerAdapter(AdapterListener<Data> listener){
        this(new ArrayList<Data>(), listener);

    }


    /***
     * create a viewHolder
     * @param viewGroup RecyclerView
     * @param i using the xml id to be the view type
     * @return view holder
     */
    @NonNull
    @Override
    public ViewHolder<Data> onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // XML to view
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View root = inflater.inflate(i, viewGroup,false);
        ViewHolder<Data> holder = onCreatedViewHolder(root, i);

        //set the view tag to be viewHolder
        root.setTag(R.id.tag_recycler_holder, holder);

        // the onClick Event is set for the root not for the viewHolder
        root.setOnClickListener(this);
        root.setOnLongClickListener(this);



        // bind the view with annotation
        holder.unbinder = ButterKnife.bind(holder, root);
        // bind the callback
        holder.callback = this;
        return holder;
    }

    /***
     * override the method of returning the default viewType
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position, mDataList.get(position)) ;
    }

    /***
     * get the viewType
     * @param position position
     * @param data data
     * @return XML id to create view Holder
     */
    @LayoutRes
    protected abstract int getItemViewType(int position, Data data);

    /***
     * When get a new viewHolder
     * @param root root layout
     * @param viewType the type of the layout, which is the id of the layout
     * @return viewHolder
     */
    protected abstract ViewHolder<Data> onCreatedViewHolder(View root, int viewType);

    /***
     * bind the data to a view Holder
     * @param dataViewHolder holder
     * @param i the position of the data
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder<Data> dataViewHolder, int i) {
        // get the data need to bind
        Data data = mDataList.get(i);
        dataViewHolder.bind(data);

    }

    /***
     * get the number of Item
     * @return
     */
    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    /***
     * insert a data and notify
     * @param data data inserted
     */
    public void add(Data data){
        mDataList.add(data);
        notifyItemInserted(mDataList.size() - 1);
    }

    /***
     * insert a set and notify
     * @param dataList vary length arguments
     */
    public void add(Data... dataList){
        if(dataList != null && dataList.length > 0){
            int startPos = mDataList.size();
            Collections.addAll(mDataList,dataList);
            notifyItemRangeInserted(startPos, dataList.length);
        }
    }

    public void add(Collection<Data> dataList){
        if(dataList != null && dataList.size() > 0){
            int startPos = mDataList.size();
            mDataList.addAll(dataList);
            notifyItemRangeInserted(startPos, dataList.size());
        }
    }

    /***
     * clear all
     */
    public void clear(){
        mDataList.clear();
        notifyDataSetChanged();
    }

    /***
     * replace with a new set, including clear
     * @param dataList a new set
     */
    public void replace(Collection<Data> dataList){
        mDataList.clear();
        if(dataList == null || dataList.size() <= 0){
            return;
        }
        mDataList.addAll(dataList);
        notifyDataSetChanged();

    }


    @Override
    public void update(Data data, ViewHolder<Data> holder){
        // get the position of the current adapter
        int pos = holder.getAdapterPosition();
        // do the remove and update data
        if(pos >= 0){
            mDataList.remove(pos);
            mDataList.add(pos, data);
            notifyItemChanged(pos); // ->onBindViewHolder->holder.bind->holder.onBind
        }
    }


    @Override
    public void onClick(View view) {
        ViewHolder viewHolder = (ViewHolder) view.getTag(R.id.tag_recycler_holder);
        if(this.mListener != null){
            int pos = viewHolder.getAdapterPosition();
            // callback
            this.mListener.onItemClick(viewHolder, mDataList.get(pos));

        }
    }

    @Override
    public boolean onLongClick(View view) {
        ViewHolder viewHolder = (ViewHolder) view.getTag(R.id.tag_recycler_holder);
        if(this.mListener != null){
            int pos = viewHolder.getAdapterPosition();
            // callback
            this.mListener.onItemLongClick(viewHolder, mDataList.get(pos));
            return true;
        }
        return false;
    }

    public void setListener(AdapterListener<Data> adapterListener){
        this.mListener = adapterListener;
    }

    /***
     * customer monitor
     * @param <Data>
     */
    public interface AdapterListener<Data>{
        // trigger when the cell is clicked
        void onItemClick(RecyclerAdapter.ViewHolder holder, Data data);
        // trigger when the cell is long clicked
        void onItemLongClick(RecyclerView.ViewHolder holder, Data data);
    }

    /**
     * implement the interface, outside class can only implement some of the methods
     * @param <Data>
     */
    public static abstract class AdapterListenerImpl<Data> implements AdapterListener<Data>{
        @Override
        public void onItemClick(ViewHolder holder, Data data) {

        }

        @Override
        public void onItemLongClick(RecyclerView.ViewHolder holder, Data data) {

        }
    }

    public static abstract class ViewHolder<Data> extends RecyclerView.ViewHolder{
        // the data need to bind
        protected Data mData;
        private Unbinder unbinder;
        private AdapterCallback<Data> callback;


        public ViewHolder(View itemView){
            super(itemView);
        }

        /***
         * It is used to trigger binding data
         * @param data the data need to bind
         */
        void bind(Data data){
            this.mData = data;
            onBind(data);
        }

        /***
         * A callback method which will be triggered at binding data, it must be override
         * @param data the data need to bind
         */
        protected abstract void onBind(Data data);

        /***
         * update data by itself
         * @param data
         */
        public void updateData(Data data){
            if(this.callback != null){
                this.callback.update(data, this);
            }

        }
    }
}
