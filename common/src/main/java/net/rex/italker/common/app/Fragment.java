package net.rex.italker.common.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class Fragment extends android.support.v4.app.Fragment {
    protected View mRoot;
    protected Unbinder mRootUnbinder;

    //When a fragment is added to the activity, onAttach will be invoked

    @Override
    public void onAttach(Context context) {  //actually, Activity inherits from the context.
        super.onAttach(context);
        //init the basic Arguments
        initArgs(getArguments());
    }


    /***
     * Judge whether the basic data is initialized successfully or not
     * @param bundle basic data
     */
    protected void initArgs(Bundle bundle){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mRoot == null){
            int layId = getContentLayoutId();
            // inflater is used to convert the xml file to View
            // attachToRoot = false, so the new view won't be added to the container
            // fragment will add it to container automatically
            View root = inflater.inflate(layId, container,false);
            initWidget(root);
            mRoot = root;
        } else {
            // if the fragment has been destroyed. myRoot may not be destroyed
            if(mRoot.getParent() != null){
                //remove myRoot from its root
                ((ViewGroup)mRoot.getParent()).removeView(mRoot);
            }
        }


        return mRoot;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //when the view created, then initialized the data
        initData();
    }

    /***
     * get the resource Id
     * @return id
     */
    protected abstract int getContentLayoutId();


    /***
     * initialize the widget
     * @param root
     */
    protected void initWidget(View root){
        mRootUnbinder = ButterKnife.bind(this, root);
    }


    /***
     * initialize data
     */
    protected void initData(){

    }

    /***
     *  Invoked when the back button is pressed
     * @return true means that the fragment has processed and activity do not need to finish,
     *          false means the fragment doesn't process, done by Activity
     */
    public boolean onBackPressed(){
        return false;
    }
}
