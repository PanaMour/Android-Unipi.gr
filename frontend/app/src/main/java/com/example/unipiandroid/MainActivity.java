package com.example.unipiandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.unipiandroid.ui.communication.AdminInboxFragment;
import com.example.unipiandroid.ui.communication.CommunicationFragment;
import com.example.unipiandroid.ui.departments.DepartmentsFragment;
import com.example.unipiandroid.ui.gallery.EventsFragment;
import com.example.unipiandroid.ui.gallery.GalleryFragment;
import com.example.unipiandroid.ui.gallery.LocationFragment;
import com.example.unipiandroid.ui.gallery.PolitikiPoiotitas;
import com.example.unipiandroid.ui.gallery.ProstasiaDedomenon;
import com.example.unipiandroid.ui.gallery.StrategyFragment;
import com.example.unipiandroid.ui.home.HomeFragment;
import com.example.unipiandroid.ui.research.DoctorateFragment;
import com.example.unipiandroid.ui.research.LabFragment;
import com.example.unipiandroid.ui.research.PostDoctorateFragment;
import com.example.unipiandroid.ui.research.ResearchFragment;
import com.example.unipiandroid.ui.slideshow.DioikitikesYpuresies;
import com.example.unipiandroid.ui.slideshow.ModipFragment;
import com.example.unipiandroid.ui.slideshow.PrytSymboulio;
import com.example.unipiandroid.ui.slideshow.PrytanikiArxi;
import com.example.unipiandroid.ui.slideshow.SlideshowFragment;
import com.example.unipiandroid.ui.slideshow.SygklitosFragment;
import com.example.unipiandroid.ui.support.GraduateFragment;
import com.example.unipiandroid.ui.support.OnlineSupportFragment;
import com.example.unipiandroid.ui.support.StudentsLifeFragment;
import com.example.unipiandroid.ui.support.SupportFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.unipiandroid.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    ExpandableListAdapter expandableListAdapter;
    ExpandableListView expandableListView;
    List<MenuModel> headerList = new ArrayList<>();
    HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    boolean adminFlag = false;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Starting Fragment
        Fragment fragment= new HomeFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        expandableListView = findViewById(R.id.expandableListView);
        prepareMenuData();
        populateExpandableList();

        //show fab if already logged in

        sharedPreferences = getSharedPreferences("MyUserPrefs",Context.MODE_PRIVATE);

        String username = sharedPreferences.getString("Username","");
        String fullname = sharedPreferences.getString("Name","");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);

        if(!username.isEmpty() && !fullname.isEmpty()) {    //if admin is logged in make it visible
            fab.setVisibility(View.VISIBLE);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), AddAnnouncement.class));
                }
            });
        }

        //

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public static Drawable getDrawable(String name) {
        Context context = MyApp.getContext();
        int resourceId = context.getResources().getIdentifier(name, "drawable", MyApp.getContext().getPackageName());
        return context.getResources().getDrawable(resourceId);
    }

    private void prepareMenuData() {
        MenuModel menuModel = new MenuModel(" Αρχική Σελίδα", true, false, new HomeFragment(), getDrawable("home")); //Menu with no sub menus
        headerList.add(menuModel);
        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }

        menuModel = new MenuModel(" Πανεπιστήμιο", true, true, new GalleryFragment(),getDrawable("graduationcap"));
        headerList.add(menuModel);
        List<MenuModel> childModelsList = new ArrayList<>();
        MenuModel childModel = new MenuModel("Ιστορία", false, false, new GalleryFragment(),null);
        childModelsList.add(childModel);

        childModel = new MenuModel("Περιήγηση", false, false, new SlideshowFragment(),null);
        childModelsList.add(childModel);

        childModel = new MenuModel("Τοποθεσία & Πρόσβαση", false, false, new LocationFragment(),null);
        childModelsList.add(childModel);

        childModel = new MenuModel("Προστασία Δεδομένων Προσωπικού Χαρακτήρα", false, false, new ProstasiaDedomenon(),null);
        childModelsList.add(childModel);

        childModel = new MenuModel("Πολιτική Ποιότητας", false, false, new PolitikiPoiotitas(),null);
        childModelsList.add(childModel);

        childModel = new MenuModel("Στρατιγική Πανεπιστημίου", false, false, new StrategyFragment(),null);
        childModelsList.add(childModel);

        childModel = new MenuModel("Events", false, false, new EventsFragment(),null);
        childModelsList.add(childModel);

        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }

        childModelsList = new ArrayList<>();
        menuModel = new MenuModel(" Διοίκηση", true, true, new HomeFragment(),getDrawable("graph"));
        headerList.add(menuModel);
        childModel = new MenuModel("Πρυτανική Αρχή", false, false, new PrytanikiArxi(),null);
        childModelsList.add(childModel);

        childModel = new MenuModel("Σύγκλητος", false, false, new SygklitosFragment(),null);
        childModelsList.add(childModel);

        childModel = new MenuModel("Πρυτανικό Συμβούλιο", false, false, new PrytSymboulio(),null);
        childModelsList.add(childModel);

        childModel = new MenuModel("ΜΟΔΙΠ", false, false, new ModipFragment(),null);
        childModelsList.add(childModel);

        childModel = new MenuModel("Διοικητικές Υπηρεσίες", false, false, new DioikitikesYpuresies(),null);
        childModelsList.add(childModel);

        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }

        menuModel = new MenuModel(" Σχολές", true, false, new DepartmentsFragment(),getDrawable("department"));
        headerList.add(menuModel);

        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }

        childModelsList = new ArrayList<>();
        menuModel = new MenuModel(" Έρευνα", true, true, new HomeFragment(),getDrawable("research"));
        headerList.add(menuModel);
        childModel = new MenuModel("ΕΛΚΕ-ΚΕΠΠ", false, false, new ResearchFragment(),null);
        childModelsList.add(childModel);
        childModel = new MenuModel("Εργαστήρια", false, false, new LabFragment(),null);
        childModelsList.add(childModel);
        childModel = new MenuModel("Διδακτορικές Σπουδές", false, false, new DoctorateFragment(),null);
        childModelsList.add(childModel);
        childModel = new MenuModel("Μεταδιδακτορική Έρευνα", false, false, new PostDoctorateFragment(),null);
        childModelsList.add(childModel);

        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }

        childModelsList = new ArrayList<>();
        menuModel = new MenuModel(" Υπηρεσίες-Παροχές", true, true, new HomeFragment(),getDrawable("support"));
        headerList.add(menuModel);
        childModel = new MenuModel("Παροχές Προς Φοιτητές", false, false, new SupportFragment(),null);
        childModelsList.add(childModel);
        childModel = new MenuModel("Ηλεκτρονικές Υπηρεσίες", false, false, new OnlineSupportFragment(),null);
        childModelsList.add(childModel);
        childModel = new MenuModel("Φοιτιτική και Κοινωνική Ζωή", false, false, new StudentsLifeFragment(),null);
        childModelsList.add(childModel);
        childModel = new MenuModel("Απόφοιτοι Πανεπιστημίου", false, false, new GraduateFragment(),null);
        childModelsList.add(childModel);

        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }

        sharedPreferences = getSharedPreferences("MyUserPrefs",Context.MODE_PRIVATE);

        String username = sharedPreferences.getString("Username","");
        String fullname = sharedPreferences.getString("Name","");

        if(!username.isEmpty() && !fullname.isEmpty()){
            menuModel = new MenuModel(" Inbox Διαχειριστή", true, false, new AdminInboxFragment(), getDrawable("inbox"));

        }else {
            menuModel = new MenuModel(" Επικοινωνία", true, false, new CommunicationFragment(), getDrawable("phone"));
        }
        headerList.add(menuModel);
        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }
    }

    private void populateExpandableList() {

        expandableListAdapter = new ExpandableListAdapter(this, headerList, childList);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                if (headerList.get(groupPosition).isGroup) {
                    if (!headerList.get(groupPosition).hasChildren) {
                        Fragment fragment= headerList.get(groupPosition).fragmentClass;
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.container, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();

                        onBackPressed();
                    }
                }

                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                if (childList.get(headerList.get(groupPosition)) != null) {
                    MenuModel model = childList.get(headerList.get(groupPosition)).get(childPosition);
                    Fragment fragment= model.fragmentClass;
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();

                    onBackPressed();
                }

                return false;
            }
        });
    }
}