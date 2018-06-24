package net.rex.italker.fragments.main;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.rex.italker.R;
import net.rex.italker.common.widget.GalleryView;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActiveFragment extends net.rex.italker.common.app.Fragment {
    @BindView(R.id.galleryView)
    GalleryView mGallery;

    public ActiveFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_active;
    }

    @Override
    protected void initData() {
        super.initData();

    }
}
