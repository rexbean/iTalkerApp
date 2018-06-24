package net.rex.italker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.rex.italker.common.app.Activity;
import net.rex.italker.fragments.assist.PermissionFragment;

public class LaunchActivity extends Activity {

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(PermissionFragment.haveAll(this,getSupportFragmentManager())){
            MainActivity.show(this);
            finish();
        }
    }
}
