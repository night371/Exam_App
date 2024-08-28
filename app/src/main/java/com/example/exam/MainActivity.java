package com.example.exam;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.exam.Fragments.AccountFragment;
import com.example.exam.Fragments.Category_Fragment;
import com.example.exam.Fragments.leaderFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompat implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerL;
    NavigationView navigationView;
    Toolbar toolbar;
    FrameLayout frameLayout;
    TextView drawerNameTxt, drawerImage_txt;
    BottomNavigationView bottomNavigationView;
    languageManager manager;
    long firstTimeClick = 0;


    BottomNavigationView.OnNavigationItemSelectedListener selectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {

            if (item.getItemId() == R.id.nav_bottom_home) {
                setFragment(new Category_Fragment());
                return true;
            } else if (item.getItemId() == R.id.nav_bottom_leader) {
                setFragment(new leaderFragment());
                return true;
            } else if (item.getItemId() == R.id.nav_bottom_account) {
                setFragment(new AccountFragment());
                return true;
            }
            return false;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        manager = new languageManager(this);

        toolbarSetUp();

        setupUI();


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerL, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerL.addDrawerListener(toggle);
        toggle.syncState();


        bottomNavigationView.setOnNavigationItemSelectedListener(selectedListener);

        navigationView.setNavigationItemSelectedListener(this);

        drawerNameTxt = navigationView.getHeaderView(0).findViewById(R.id.drawer_name);
        drawerImage_txt = navigationView.getHeaderView(0).findViewById(R.id.nav_drawer_text_img);


        String name = DbQuery.myProfile.getName();
        drawerNameTxt.setText(name);
        drawerImage_txt.setText(name.toUpperCase().substring(0, 1));

        setFragment(new Category_Fragment());
    }

    private void setupUI() {
        navigationView = findViewById(R.id.nav_view);
        drawerL = findViewById(R.id.drawer_layout1);
        frameLayout = findViewById(R.id.main_frame);
        bottomNavigationView = findViewById(R.id.bottom_navbar);
    }

    public void toolbarSetUp() {
        toolbar = findViewById(R.id.toolbar1);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setTitle(R.string.app_name);
    }

    @Override
    public void onBackPressed() {
        if (drawerL.isDrawerOpen(GravityCompat.START)) {
            drawerL.closeDrawer(GravityCompat.START);
        } else if (firstTimeClick + 1000 > System.currentTimeMillis()) {
            super.onBackPressed();

        } else {
            Toast.makeText(this, "Double Click to close", Toast.LENGTH_SHORT).show();

        }
        firstTimeClick = System.currentTimeMillis();

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.nav_menu_home) {

            setFragment(new Category_Fragment());

        } else if (item.getItemId() == R.id.nav_menu_leaderboard) {
            setFragment(new leaderFragment());


        } else if (item.getItemId() == R.id.nav_menu_account) {
            setFragment(new AccountFragment());


        } else if (item.getItemId() == R.id.nav_menu_savedQues) {
            startActivity(new Intent(MainActivity.this, Bookmark_Activity.class));
        } else if (item.getItemId() == R.id.settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));

        } else if (item.getItemId() == R.id.language) {
            showChangeDialog();
        } else if (item.getItemId() == R.id.share) {
            shareOurApp();
        } else if (item.getItemId() == R.id.aboutUs) {
            startActivity(new Intent(MainActivity.this, About_Us.class));
        }


        DrawerLayout layout = findViewById(R.id.drawer_layout1);
        layout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void shareOurApp() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String Body = "Download our App.";
        String Sub = "https://play.google.com";
        intent.putExtra(Intent.EXTRA_TEXT, Body);
        intent.putExtra(Intent.EXTRA_TEXT, Sub);
        startActivity(Intent.createChooser(intent, "Share using"));


    }

    private void showChangeDialog() {
        String[] listItem = {"English", "دری"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.choose_your_language))
                .setSingleChoiceItems(listItem, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            manager.updateResources("en");
                            recreate();
                        } else if (which == 1) {
                            manager.updateResources("fa");
                            recreate();
                        }
                        dialog.dismiss();
                    }

                });
        AlertDialog mbuilder = builder.create();
        builder.show();
    }

    public void setFragment(Fragment Fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(frameLayout.getId(), Fragment);
        transaction.commit();
    }


}