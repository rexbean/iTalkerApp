package net.rex.italker.helper;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;

/**
 * solve the dispatch and reuse problem of the fragment
 */

public class NavHelper<T> {
    // the container of the Tabs
    private SparseArray<Tab<T>> tabs = new SparseArray<>();

    // initialization variables;
    private Context context;
    private int mContainerId;
    private FragmentManager mFragmentManager;
    private onTabChangedListener<T> mChangedListener;

    public NavHelper(Context context, int containerId, FragmentManager mFragmentManager, onTabChangedListener<T> mChangedListener) {
        this.context = context;
        this.mContainerId = containerId;
        this.mFragmentManager = mFragmentManager;
        this.mChangedListener = mChangedListener;
    }

    // the current tab
    private Tab<T> currentTab;


    /***
     * what is stream operation?
     * @param tabId the id of the tab
     * @param tab the tab
     * @return the nav helper
     */
    public NavHelper<T> add(int tabId, Tab<T> tab){
        tabs.put(tabId, tab);
        return this;
    }

    /***
     * get the current Tab
     * @return the current Tab
     */
    public Tab<T> getCurrentTab(){
        return this.currentTab;
    }


    public boolean performClickMenu(int tabId){
        Tab<T> tab = tabs.get(tabId);
        if(tab != null){
            doSelect(tab);
            return true;
        }
        return false;
    }

    /**
     * do the select operation
     * @param tab
     */
    private void doSelect(Tab<T> tab){
        Tab<T> oldTab = null;
        //two situation: 1. click the same tab
        //               2. click a new one
        if(currentTab != null){
            oldTab = currentTab;
            if(oldTab == tab){
                // when they are same
                // do the second time select operation
                notifyTabReselected();
                return;
            }
        }
        currentTab = tab;
        doTabChange(oldTab, currentTab);

    }

    private void doTabChange(Tab<T> oldTab, Tab<T> newTab){
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if(oldTab != null){
            if(oldTab.fragment != null){
                ft.detach(oldTab.fragment);
            }
        }
        if(newTab != null){
            if(newTab.fragment == null){
                Fragment fragment = Fragment.instantiate(context, newTab.mClass.getName(), null);
                newTab.fragment = fragment;
                ft.add(mContainerId,fragment,newTab.mClass.getName());
            } else {
                ft.attach(newTab.fragment);
            }
        }
        ft.commit();
        notifyTabChanged(oldTab, newTab);

    }

    /**
     * notify that the tab has changed
     */
    private void notifyTabChanged(Tab<T> oldTab, Tab<T> newTab){
        mChangedListener.OnTabChanged(oldTab, newTab);
    }
    /**
     * second time select operation
     */
    private void notifyTabReselected(){
        // TODO  second time select operation
    }

    /**
     * static here is to avoid circular invoking with NavHelper
     */
    public static class Tab<T> {
        private Class<?> mClass;
        private T others;
        // using in package;
        private Fragment fragment;

        public Tab(Class<?> mClass, T others) {
            this.mClass = mClass;
            this.others = others;
        }

        public T getOthers() {
            return others;
        }
    }


    /**
     *  an event of tab having been changed
     */
    public interface onTabChangedListener<T>{
        void OnTabChanged(Tab<T> oldTab, Tab<T> newTab);
    }
}

