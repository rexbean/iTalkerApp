package net.rex.italker.common.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import butterknife.ButterKnife;

public abstract class Activity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize window before initialize the view
        initWindows();
        // if the basic data is not initialized successfully,
        // then the other initializations won't run
        if (initArgs(getIntent().getExtras())){
            int layoutId = getContentLayoutId();
            setContentView(layoutId);

            initWidget();
            initData();
        } else {
            finish();
        }


    }

    /**
     * Initialize the window
     */
    protected void initWindows(){

    }

    /***
     * Judge whether the basic data is initialized successfully or not
     * @param bundle basic data
     * @return initialized successfully or not
     */
    protected boolean initArgs(Bundle bundle){
        return true; //default is true, this value can be override by the child activity
    }


    /***
     *  Get the resource files of the current activity
     */
    protected abstract int getContentLayoutId();

    /***
     * Initialize the widget
     */
    protected void initWidget(){
        ButterKnife.bind(this);
    }

    /***
     * Initialize data
     */
    protected void initData(){

    }

    /***
     * action of pressing the back button on the navigation bar
     * @return
     */
    @Override
    public boolean onSupportNavigateUp() {
        // when press the back button on the navigation bar, finish the current activity
        finish();
        return super.onSupportNavigateUp();
    }

    /***
     * action of pressing the back button
     */
    @Override
    public void onBackPressed() {
        // When an activity has several fragment,
        // pressing the back button will be back to the last fragment
        // get all fragments
        List<android.support.v4.app.Fragment> fragments = getSupportFragmentManager().getFragments();
        // judge it is null or not
        if(fragments != null && fragments.size()>0){
            for (Fragment fragment : fragments) {
                // judge whether we are able to process the Fragment
                if(fragment instanceof net.rex.italker.common.app.Fragment){
                    // judge that whether we interrupt the back button
                    if(((net.rex.italker.common.app.Fragment)fragment).onBackPressed()){
                        // return when we don't need do anything
                        return;
                    }
                }
            }

        }
        super.onBackPressed();
        finish();
    }
}
