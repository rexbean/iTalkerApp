package net.rex.italker.activities;


import android.content.Context;
import android.content.Intent;

import net.rex.italker.R;
import net.rex.italker.common.app.Activity;
import net.rex.italker.common.app.Fragment;
import net.rex.italker.fragments.account.UpdateInfoFragment;

public class AccountActivity extends Activity {

    private Fragment mFragment;

    /***
     * used to show activity, the entrance
     */
    public static void show(Context context){
        context.startActivity(new Intent(context, AccountActivity.class));
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_account;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mFragment = new UpdateInfoFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lay_container,mFragment)
                .commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
      //return to updateInfoFragment
        mFragment.onActivityResult(requestCode,resultCode,data);
    }
}
