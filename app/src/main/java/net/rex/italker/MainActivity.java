package net.rex.italker;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.widget.FloatActionButton;
import net.rex.italker.activities.AccountActivity;
import net.rex.italker.common.app.Activity;
import net.rex.italker.common.widget.PortraitView;
import net.rex.italker.fragments.assist.PermissionFragment;
import net.rex.italker.fragments.main.ActiveFragment;
import net.rex.italker.fragments.main.ContactFragment;
import net.rex.italker.fragments.main.GroupFragment;
import net.rex.italker.helper.NavHelper;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends Activity
        implements BottomNavigationView.OnNavigationItemSelectedListener,
        NavHelper.onTabChangedListener<Integer> {

    @BindView(R.id.appbar)
    View mLayAppbar;

    @BindView(R.id.im_portrait)
    PortraitView mPortrait;

    @BindView(R.id.txt_title)
    TextView mTitle;

    @BindView(R.id.lay_container)
    FrameLayout mContainer;

    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;

    @BindView(R.id.btn_action)
    FloatActionButton mAction;

    NavHelper<Integer> mNavHelper;


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }


    /**
     * show MainActivity
     * @param context context
     */
    public static void show(Context context){
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        //set the onClick Listener of the navigation menut item
        mNavigation.setOnNavigationItemSelectedListener(this);

        mNavHelper = new NavHelper<Integer>(this,
                R.id.lay_container,
                getSupportFragmentManager(),
                this);

        mNavHelper.add(R.id.action_home,new NavHelper.Tab<>(ActiveFragment.class, R.string.title_home))
                .add(R.id.action_group,new NavHelper.Tab<>(GroupFragment.class, R.string.title_group))
                .add(R.id.action_contact,new NavHelper.Tab<>(ContactFragment.class, R.string.title_contact));


        Glide.with(this)
                .load(R.drawable.bg_src_morning)
                .centerCrop()
                .into(new ViewTarget<View,GlideDrawable>(mLayAppbar) {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                this.view.setBackground(resource.getCurrent());
            }
        });


    }

    @Override
    protected void initData() {
        super.initData();
        Menu menu = mNavigation.getMenu();
        menu.performIdentifierAction(R.id.action_home,0);
    }

    @OnClick(R.id.im_search)
    void onSearchMenuClick(){

    }
    @OnClick(R.id.btn_action)
    void onActionClick(){
        AccountActivity.show(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return mNavHelper.performClickMenu(item.getItemId());
    }


    @Override
    public void OnTabChanged(NavHelper.Tab<Integer> oldTab, NavHelper.Tab<Integer> newTab) {
        mTitle.setText(newTab.getOthers());

        // hiding and showing animation of the floating button
        float transY = 0;
        float rotation = 0;

        if(Objects.equals(newTab.getOthers(),R.string.title_home)){
            transY = Ui.dipToPx(getResources(),76);
        } else {
            if(Objects.equals(newTab.getOthers(),R.string.title_group)){
                mAction.setImageResource(R.drawable.ic_group_add);
                rotation = -360;
            } else {
                mAction.setImageResource(R.drawable.ic_contact_add);
                rotation = 360;
            }

        }

        // animation: rotation, y axis move, bound effect, duration
        mAction.animate()
                .rotation(rotation)
                .translationY(transY)
                .setInterpolator(new AnticipateOvershootInterpolator(1))
                .setDuration(480)
                .start();

    }
}
