package net.rex.italker.fragments.assist;


import android.Manifest;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.rex.italker.R;
import net.rex.italker.common.app.Application;
import net.rex.italker.fragments.media.GalleryFragment;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * A simple {@link Fragment} subclass.
 */
public class PermissionFragment extends BottomSheetDialogFragment
        implements EasyPermissions.PermissionCallbacks{

    private static final int RC = 0x0100;
    public PermissionFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new GalleryFragment.TransStatusBottomSheetDialog(getContext());
    }

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_permission, container, false);
        refreshState(root);

        root.findViewById(R.id.btn_submit)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestPermission();
                    }
                });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        // refresh when show the view
        refreshState(getView());
    }

    /**
     * refresh the state in the pic
     * @param root root view
     */
    private void refreshState(View root){
        if(root == null){
            return;
        }
        root.findViewById(R.id.im_state_permission_network)
                .setVisibility(haveNetworkPermit(getContext())?View.VISIBLE:View.GONE);
        root.findViewById(R.id.im_state_permission_read)
                .setVisibility(haveReadPermit(getContext())?View.VISIBLE:View.GONE);
        root.findViewById(R.id.im_state_permission_write)
                .setVisibility(haveWritePermit(getContext())?View.VISIBLE:View.GONE);
        root.findViewById(R.id.im_state_permission_record)
                .setVisibility(haveRecordPermit(getContext())?View.VISIBLE:View.GONE);

    }

    /**
     * Check whether it has the permisiion of internet
     * @param context context
     * @return has or doesn't have
     */
    private static boolean haveNetworkPermit(Context context){
        // prepare for the permission related to the network
        String[] perms = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE
        };
        return EasyPermissions.hasPermissions(context, perms);
    }

    /**
     * Check whether it has the permisiion of read
     * @param context context
     * @return has or doesn't have
     */
    private static boolean haveReadPermit(Context context){
        // prepare for the permission related to the read storage
        String[] perms = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
        };
        return EasyPermissions.hasPermissions(context, perms);
    }
    /**
     * Check whether it has the permisiion of write
     * @param context context
     * @return has or doesn't have
     */
    private static boolean haveWritePermit(Context context){
        // prepare for the permission related to the write storage
        String[] perms = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        return EasyPermissions.hasPermissions(context, perms);
    }
    /**
     * Check whether it has the permission of record
     * @param context context
     * @return has or doesn't have
     */
    private static boolean haveRecordPermit(Context context){
        // prepare for the permission related to the record audio
        String[] perms = new String[]{
                Manifest.permission.RECORD_AUDIO
        };
        return EasyPermissions.hasPermissions(context, perms);
    }

    /**
     * private show method
     * @param manager fragment manager
     */
    private static void show(FragmentManager manager){
        // invoke buttonSheetDialog prepared show method
        new PermissionFragment().show(manager, PermissionFragment.class.getName());
    }

    /**
     * check whether we have all permissions
     * @param context context
     * @param manager Fragment Manager
     * @return have or not
     */
    public static boolean haveAll(Context context, FragmentManager manager){
        boolean haveAll = haveNetworkPermit(context) &&
                haveReadPermit(context) &&
                haveWritePermit(context) &&
                haveRecordPermit(context);

        if(!haveAll){
            show(manager);
        }
        return haveAll;
    }

    /**
     * method for request permission
     */
    @AfterPermissionGranted(RC)
    private void requestPermission(){
        String[] perms = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        };

        if(EasyPermissions.hasPermissions(getContext(), perms)){
            Application.showToast(R.string.label_permission_ok);
            refreshState(getView());
        } else {
            EasyPermissions.requestPermissions(this,
                    getString(R.string.title_assist_permissions),
                    RC,
                    perms);
        }
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        // if there is any permission that does not have been denied, than toast
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)){
            new AppSettingsDialog.Builder(this)
                    .build()
                    .show();
        }
    }

    /**
     * the callback method, put the state of the applying permission to the EasyPermission
     * @param requestCode request code
     * @param permissions permissions
     * @param grantResults grant result
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // give corresponding arguments,and tell that the receiver of the process permission is itself
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }
}
