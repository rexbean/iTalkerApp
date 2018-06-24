package net.rex.italker.fragments.media;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import net.qiujuer.genius.ui.Ui;
import net.rex.italker.R;
import net.rex.italker.common.tools.UiTools;
import net.rex.italker.common.widget.GalleryView;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends BottomSheetDialogFragment
        implements GalleryView.SelectedChangedListener {
    private GalleryView mGallery;
    private OnSelectedListener mListener;


    public GalleryFragment() {
        // Required empty public constructor
    }

    /**
     * setup listener and return itself
     * @param mListener listener
     * @return itself GalleryFragment
     */
    public GalleryFragment setListener(OnSelectedListener mListener) {
        this.mListener = mListener;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery,container,false);
        mGallery = (GalleryView) root.findViewById(R.id.galleryView);
        return root;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new TransStatusBottomSheetDialog(getContext());
    }

    public static class TransStatusBottomSheetDialog extends BottomSheetDialog{

        public TransStatusBottomSheetDialog(@NonNull Context context) {
            super(context);
        }

        public TransStatusBottomSheetDialog(@NonNull Context context, int theme) {
            super(context, theme);
        }

        protected TransStatusBottomSheetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
            super(context, cancelable, cancelListener);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            final Window window = getWindow();
            if(window == null){
                return;
            }

            // get the screen height;
            int screenHeight = UiTools.getScreenHeight(getOwnerActivity());
            // get the status height;
            //int statusHeight = (int) Ui.dipToPx(getContext().getResources(),25);
            int statusHeight = UiTools.getStatusBarHeight(getOwnerActivity());
            int dialogHeight = screenHeight - statusHeight;
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    dialogHeight <= 0 ? ViewGroup.LayoutParams.MATCH_PARENT:dialogHeight);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        mGallery.setup(getLoaderManager(), this);
    }

    @Override
    public void onSelectedCountChanged(int count) {
        // if on pic selected
        if(count > 0){
            dismiss();//hide
            if(mListener != null){
                String[] paths = mGallery.getSelectedPath();
                //return the first one
                mListener.onSelectedImage(paths[0]);
                // destroy the reference with the listen, speed up the garbage collection
                mListener = null;
            }
        }
    }

    /**
     * listener for having selected
     */
    public interface OnSelectedListener{
        void onSelectedImage(String path);
    }
}
