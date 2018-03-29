package com.multipz.dictionary.Activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.multipz.dictionary.Fragment.AddNewFormulaFragment;
import com.multipz.dictionary.Fragment.AddNewWordFragment;
import com.multipz.dictionary.Fragment.FormulaFavouriteFragment;
import com.multipz.dictionary.Fragment.WordFavouriteFragment;
import com.multipz.dictionary.R;

public class AddWordActivity extends AppCompatActivity {

    ImageView nav_back;
    public static AddWordActivity instance;
    private AddNewFormulaFragment fragmentformula;
    private AddNewWordFragment fragmentword;
    private TabLayout allTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);
        instance = this;
        getAllWidgets();
        bindWidgetsWithAnEvent();
        setupTabLayout();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        nav_back = (ImageView) findViewById(R.id.nav_back);
        nav_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });

    }

    private void getAllWidgets() {
        allTabs = (TabLayout) findViewById(R.id.tabs);
    }

    private void setupTabLayout() {
        fragmentformula = new AddNewFormulaFragment();
        fragmentword = new AddNewWordFragment();
        allTabs.addTab(allTabs.newTab().setText("ADD WORD"), true);
        allTabs.addTab(allTabs.newTab().setText("ADD FORMULA"));
    }

    private void bindWidgetsWithAnEvent() {
        allTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setCurrentTabFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void setCurrentTabFragment(int tabPosition) {
        switch (tabPosition) {
            case 0:
                replaceFragment(fragmentword);
                break;

            case 1:
                replaceFragment(fragmentformula);
                break;
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_container, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}