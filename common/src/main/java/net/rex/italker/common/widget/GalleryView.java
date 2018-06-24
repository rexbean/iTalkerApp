package net.rex.italker.common.widget;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.rex.italker.common.R;
import net.rex.italker.common.widget.recycler.RecyclerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * TODO: document your custom view class.
 */
public class GalleryView extends RecyclerView {
    private static final int LOADER_ID = 0x0100;
    private static final int MAX_SELECTED_COUNT = 3;
    private static final int MIN_IMAGE_SIZE = 10 * 1024;
    private Adapter mAdapter = new Adapter();
    private LoaderCallback mLoaderCallback = new LoaderCallback();
    private List<Image> mSelectedImages = new LinkedList<>();
    private SelectedChangedListener mListener;

    public GalleryView(Context context) {
        super(context);
        init();
    }

    public GalleryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GalleryView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setLayoutManager(new GridLayoutManager(getContext(), 4));
        setAdapter(mAdapter);
        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<Image>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, Image image) {
                // if the click is allowed, then update the cell, update view
                // Or it is not allowed,(it has exceed maximum selection),do not update view
                if(onSelectedClick(image)){
                    //noinspection unchecked
                    holder.updateData(image);
                }
            }
        });
    }

    /**
     * initialize the loader
     * @param loaderManager loadManger
     * @return this ID can be used to destroyed
     */
    public int setup(LoaderManager loaderManager, SelectedChangedListener listener){
        loaderManager.initLoader(LOADER_ID, null, mLoaderCallback);
        mListener = listener;
        return LOADER_ID;
    }

    /**
     * this is loader callback used to bind data
     */
    private class LoaderCallback implements LoaderManager.LoaderCallbacks{
        private final String[] IMAGE_PROJECTION = new String[]{
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA, // path
                MediaStore.Images.Media.DATE_ADDED,
        };
        @NonNull
        @Override
        public Loader onCreateLoader(int id, @Nullable Bundle args) {
            // when loader created
            if(id == LOADER_ID){
                // when loaderId is correct then initialize
                return new CursorLoader(getContext(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_PROJECTION,
                        null,
                        null,
                        IMAGE_PROJECTION[2] +" DESC");
            }
            return null;
        }

        @Override
        public void onLoadFinished(@NonNull Loader loader, Object data) {
            // when loading finished
            List<Image> images = new ArrayList<>();
            if(data != null){
                int count = ((Cursor)data).getCount();
                if(count > 0){
                    ((Cursor) data).moveToFirst();
                    int indexId = ((Cursor) data).getColumnIndexOrThrow(IMAGE_PROJECTION[0]);
                    int indexPath = ((Cursor) data).getColumnIndexOrThrow(IMAGE_PROJECTION[1]);
                    int indexDate = ((Cursor) data).getColumnIndexOrThrow(IMAGE_PROJECTION[2]);
                    do{
                        //circularly read in until none
                        int id = ((Cursor) data).getInt(indexId);
                        String path = ((Cursor) data).getString(indexPath);
                        long date = ((Cursor) data).getLong(indexDate);

                        File file = new File(path);
                        if(!file.exists() || file.length() < MIN_IMAGE_SIZE){
                            continue;
                        }
                        Image image = new Image();
                        image.id = id;
                        image.path = path;
                        image.date = date;
                        images.add(image);


                    }while(((Cursor) data).moveToNext());
                }
            }
            updateSource(images);
        }

        @Override
        public void onLoaderReset(@NonNull Loader loader) {
            // when loader is destroyed or reset
            updateSource(null);
        }
    }

    /**
     * notify adapter to update the latest data
     * @param images all images from disk
     */
    private void updateSource(List<Image> images){
        mAdapter.replace(images);
    }

    /***
     *
     * @param image image
     * @return true, the data has modified, vice
     */
    private boolean onSelectedClick(Image image){
        boolean notifyRefresh;
        if(mSelectedImages.contains(image)){
            mSelectedImages.remove(image);
            image.isSelect = false;
            notifyRefresh = true;
        } else {
            if(mSelectedImages.size() >= MAX_SELECTED_COUNT){
                // Toast a
                String str = getResources().getString(R.string.label_gallery_select_max_size);
                String maxString =String.format(str,MAX_SELECTED_COUNT);
                Toast.makeText(getContext(),maxString,Toast.LENGTH_SHORT).show();
                notifyRefresh = false;
            } else {
                image.isSelect = true;
                mSelectedImages.add(image);
                notifyRefresh = true;
            }
        }
        // if the data has changed, we need to notify the listener outside
        if(notifyRefresh){
            notifySelectChanged();
        }
        return notifyRefresh;
    }

    /**
     * notify that select state has changed
     */
    private void notifySelectChanged(){
        SelectedChangedListener listener = mListener;
        if(listener != null){
            listener.onSelectedCountChanged(mSelectedImages.size());
        }
    }
    /**
     * get selected images' path
     * @return an array of paths
     */
    public String[] getSelectedPath(){
        String[] paths = new String[mSelectedImages.size()];
        int index = 0;
        for (Image image : mSelectedImages) {
            paths[index++] = image.path;
        }
        return paths;
    }

    /**
     * clean up all image
     */
    public void clear(){
        for (Image image : mSelectedImages) {
            image.isSelect = false;
        }
        mSelectedImages.clear();
        mAdapter.notifyDataSetChanged();

    }



    /**
     *  data structure of the data
     */
    private static class Image{
        int id;
        String path;
        long date;
        boolean isSelect;

        //override equal and hashcode

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Image image = (Image) o;
            return Objects.equals(path, image.path);
        }

        @Override
        public int hashCode() {
            return path != null ? path.hashCode() : 0;
        }
    }

    private class Adapter extends RecyclerAdapter<Image>{

        @Override
        protected ViewHolder<Image> onCreatedViewHolder(View root, int viewType) {
            return new GalleryView.ViewHolder(root);
        }

        @Override
        protected int getItemViewType(int position, Image image) {
            return R.layout.cell_gallery;
        }
    }

    private class ViewHolder extends RecyclerAdapter.ViewHolder<Image>{
        private ImageView mPic;
        private View mShade;
        private CheckBox mSelected;
        public ViewHolder(View itemView) {
            super(itemView);

            mPic = (ImageView)itemView.findViewById(R.id.im_image);
            mShade = (View)itemView.findViewById(R.id.view_shade);
            mSelected = (CheckBox) itemView.findViewById(R.id.cb_select);
        }

        @Override
        protected void onBind(Image image) {
            Glide.with(getContext())
                    .load(image.path)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .centerCrop()
                    .placeholder(R.color.grey_200)
                    .into(mPic);

            mShade.setVisibility(image.isSelect?VISIBLE:INVISIBLE);
            mSelected.setChecked(image.isSelect);
            mSelected.setVisibility(VISIBLE);
        }
    }

    public interface SelectedChangedListener{
        void onSelectedCountChanged(int count);
    }


}
