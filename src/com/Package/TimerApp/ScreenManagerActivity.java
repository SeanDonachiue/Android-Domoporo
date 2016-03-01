package com.Package.TimerApp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;
/**
 * Created by Sean on 2014-12-22.
 */
public class ScreenManagerActivity
        extends FragmentActivity
        implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener, SettingsFragment.settingsChangedListener {


    /**
     * The <code>TabsViewPagerFragmentActivity</code> class implements the Fragment activity that maintains a TabHost using a ViewPager.
     * @author mwho (majority)
     * @author Sean Donachiue
     */

        private TabHost mTabHost;
        private ViewPager mViewPager;
        private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, ScreenManagerActivity.TabInfo>();
        private ScreenPageAdapter mPagerAdapter;

        private TimerFragment timerFrag;
        private SettingsFragment setFrag;
        private ProgressionFragment progFrag;

    /**
         *
         * @author mwho
         * Maintains extrinsic info of a tab's construct
         * Fragment is currently useless here.
         */
        private class TabInfo {
            private String tag;
            private Class<?> clss;
            private Bundle args;
            private Fragment fragment;
            TabInfo(String tag, Class<?> clazz, Bundle args) {
                this.tag = tag;
                this.clss = clazz;
                this.args = args;
            }
        }
        /**
         * A simple factory that returns dummy views to the Tabhost
         * @author mwho
         */
        class TabFactory implements TabContentFactory {

            private final Context mContext;

            /**
             * @param context
             */
            public TabFactory(Context context) {
                mContext = context;
            }

            /** (non-Javadoc)
             * @see android.widget.TabHost.TabContentFactory#createTabContent(java.lang.String)
             */
            public View createTabContent(String tag) {
                View v = new View(mContext);
                v.setMinimumWidth(0);
                v.setMinimumHeight(0);
                return v;
            }

        }
        /** (non-Javadoc)
         * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
         */
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Inflate the layout
            setContentView(R.layout.main);
            // Initialise the TabHost
            this.initialiseTabHost(savedInstanceState);
            if (savedInstanceState != null) {
                mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); //set the tab as per the saved state
            }
            // Initialize Fragments
            timerFrag = new TimerFragment();
            setFrag = new SettingsFragment();
            setFrag.setter = this;             // Initialize the setter interface?
            progFrag = new ProgressionFragment();
            // Initialize ViewPager
            this.initializeViewPager();


        }

        /** (non-Javadoc)
         * @see android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os.Bundle)
         */
        protected void onSaveInstanceState(Bundle outState) {
            outState.putString("tab", mTabHost.getCurrentTabTag()); //save the tab selected
            super.onSaveInstanceState(outState);
        }

        /**
         * Initialise ViewPager
         */
        private void initializeViewPager() {

            List<Fragment> fragments = new Vector<Fragment>();
            fragments.add( timerFrag );
            fragments.add( setFrag );
            fragments.add( progFrag );
            this.mPagerAdapter  = new ScreenPageAdapter(super.getSupportFragmentManager(), fragments);

            this.mViewPager = (ViewPager) super.findViewById(R.id.viewpager);
            this.mViewPager.setAdapter(this.mPagerAdapter);
            this.mViewPager.setOnPageChangeListener(this);
        }

        /**
         * Initialise the Tab Host
         */
        private void initialiseTabHost(Bundle args) {
            mTabHost = (TabHost)findViewById(android.R.id.tabhost);
            mTabHost.setup();
            TabInfo tabInfo = null;
            ScreenManagerActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Timer").setIndicator("Timer"),
                                        ( tabInfo = new TabInfo("Timer", TimerFragment.class, args)));
            this.mapTabInfo.put(tabInfo.tag, tabInfo);
            ScreenManagerActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Settings").setIndicator("Settings"),
                                        ( tabInfo = new TabInfo("Settings", SettingsFragment.class, args)));
            this.mapTabInfo.put(tabInfo.tag, tabInfo);
            ScreenManagerActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Progress").setIndicator("Progress"),
                                        ( tabInfo = new TabInfo("Progress", ProgressionFragment.class, args)));
            this.mapTabInfo.put(tabInfo.tag, tabInfo);
            // Default to first tab
            //this.onTabChanged("Tab1");
            //
            mTabHost.setOnTabChangedListener(this);
        }

        /**
         * Add Tab content to the Tabhost
         * @param activity
         * @param tabHost
         * @param tabSpec
         */
        private static void AddTab(ScreenManagerActivity activity, TabHost tabHost, TabHost.TabSpec tabSpec, TabInfo tabInfo) {
            // Attach a Tab view factory to the spec
            tabSpec.setContent(activity.new TabFactory(activity));
            tabHost.addTab(tabSpec);
        }

        /** (non-Javadoc)
         * @see android.widget.TabHost.OnTabChangeListener#onTabChanged(java.lang.String)
         */
        public void onTabChanged(String tag) {
            //TabInfo newTab = this.mapTabInfo.get(tag);
            int pos = this.mTabHost.getCurrentTab();
            this.mViewPager.setCurrentItem(pos);
        }

        /* (non-Javadoc)
         * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrolled(int, float, int)
         */

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
            // TODO Auto-generated method stub

        }

        /* (non-Javadoc)
         * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageSelected(int)
         *
         * THIS IS THE ONE THAT DOES THE STUFF!
         */
        @Override
        public void onPageSelected(int position) {
            // TODO Auto-generated method stub
            this.mTabHost.setCurrentTab(position);
        }

        /* (non-Javadoc)
         * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrollStateChanged(int)
         */
        @Override
        public void onPageScrollStateChanged(int state) {
            // TODO Auto-generated method stub

        }
        /*
            SETTINGS CHANGED METHODS
         */
        @Override
        public void onSetWork(int work) {
            timerFrag.setWork(work);
        }

        @Override
        public void onSetBreak(int breakI) {
            timerFrag.setBreak(breakI);
        }

        @Override
        public void onSetLoop(int loop) {
            timerFrag.setNumLoops(loop);
        }

}
