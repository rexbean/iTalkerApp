package net.rex.italker.fragments.account;



import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.app.Fragment;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.yalantis.ucrop.UCrop;

import net.rex.italker.R;
import net.rex.italker.common.app.Application;
import net.rex.italker.common.widget.PortraitView;
import net.rex.italker.factory.Factory;
import net.rex.italker.factory.net.UploadHelper;
import net.rex.italker.fragments.media.GalleryFragment;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateInfoFragment extends net.rex.italker.common.app.Fragment {
    @BindView(R.id.im_portrait)
    PortraitView mPortrait;

    public UpdateInfoFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_update;
    }

    @OnClick(R.id.im_portrait)
    void onPortraitClick(){
        new GalleryFragment().setListener(new GalleryFragment.OnSelectedListener() {
            @Override
            public void onSelectedImage(String path) {
                // set the format of the result;
                UCrop.Options options = new UCrop.Options();
                options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                // set the compressed quality
                options.setCompressionQuality(96);

                // the cache of the pic
                File dPath = Application.getPortaitTmpFile();
                UCrop.of(Uri.fromFile(new File(path)), Uri.fromFile(dPath))
                        .withAspectRatio(1,1)
                        .withMaxResultSize(520,520)
                        .withOptions(options)
                        .start(getActivity());// this will return to the AccountActivity
            }
        }).show(getChildFragmentManager(),GalleryFragment.class.getName());
        //Because of this is a fragment, so getChildFragmentManager is recommend;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP){
            final Uri resultUri = UCrop.getOutput(data);
            if(resultUri != null){
                loadPortrait(resultUri);
            }
        } else if(resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);

        }
    }

    private void loadPortrait(Uri uri){
        Glide.with(this)
                .load(uri)
                .asBitmap()
                .centerCrop()
                .into(mPortrait);

        // get the local path of the file
        final String localPath = uri.getPath();
        Log.e("TAG", "LocalPath" + localPath);

        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
               String url =  UploadHelper.uploadPortrait(localPath);
                Log.e("TAG", "url" + url);
            }
        });




    }
}
