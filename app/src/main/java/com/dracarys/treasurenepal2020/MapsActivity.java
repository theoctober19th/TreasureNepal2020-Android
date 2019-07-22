package com.dracarys.treasurenepal2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dracarys.treasurenepal2020.api.ApiService;
import com.dracarys.treasurenepal2020.api.RetrofitClient;
import com.dracarys.treasurenepal2020.entities.Treasure;
import com.dracarys.treasurenepal2020.utils.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.itemanimators.AlphaCrossFadeAnimator;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.ExpandableBadgeDrawerItem;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private static final int PROFILE_SETTING = 100000;

    //save our header or result
    private AccountHeader headerResult = null;
    private Drawer result = null;

    private GoogleMap mMap;

    //Views
    FloatingActionButton searchFab;
    FloatingActionButton takeMeHomeFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //initialize Views
        initializeViews();

        createDrawer(savedInstanceState);
    }

    private void initializeViews() {
        searchFab = findViewById(R.id.find_treasures_fab);
        takeMeHomeFab = findViewById(R.id.take_me_home_fab);

        searchFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mMap.clear();
                //addRandomMarkers();
                showNearbyTreasures();
            }
        });

        takeMeHomeFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeMeWhereIAm();
            }
        });
    }

    private void showNearbyTreasures() {
        /*Create handle for the RetrofitInstance interface*/
        ApiService service = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        Call<List<Treasure>> call = service.getTreasures("json");
        call.enqueue(new Callback<List<Treasure>>() {

            @Override
            public void onResponse(Call<List<Treasure>> call, Response<List<Treasure>> response) {
                createNearbyTreasureMarkers(response.body());
            }

            @Override
            public void onFailure(Call<List<Treasure>> call, Throwable t) {
                Toast.makeText(MapsActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createNearbyTreasureMarkers(List<Treasure> body) {
        for(Treasure t: body){
            addLocationMarker(t);
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(MapsActivity.this);

        takeMeWhereIAm();
    }

    public void takeMeWhereIAm(){
        // Add a marker in Sydney and move the camera
        LatLng latlng = new LatLng(27.7236931,85.382269);
        mMap.addMarker(new MarkerOptions().position(latlng).title("You are here"));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(10.0f));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));

        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }
//    public void createDrawer(Bundle savedInstanceState){
//        // Handle Toolbar
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        // Create a few sample profile
//        // NOTE you have to define the loader logic too. See the CustomApplication for more details
//        final IProfile profile = new ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com").withIcon(R.drawable.profile).withIdentifier(100);
//
//        // Create the AccountHeader
//        headerResult = new AccountHeaderBuilder()
//                .withActivity(this)
//                .withTranslucentStatusBar(true)
//                .addProfiles(
//                        profile
//
//                        //don't ask but google uses 14dp for the add account icon in gmail but 20dp for the normal icons (like manage account)
//                )
//                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
//                    @Override
//                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
//                        //sample usage of the onProfileChanged listener
//                        //if the clicked item has the identifier 1 add a new profile ;)
//                        if (profile instanceof IDrawerItem && profile.getIdentifier() == PROFILE_SETTING) {
//                            int count = 100 + headerResult.getProfiles().size() + 1;
//                            IProfile newProfile = new ProfileDrawerItem().withNameShown(true).withName("Batman" + count).withEmail("batman" + count + "@gmail.com").withIcon(R.drawable.profile).withIdentifier(count);
//                            if (headerResult.getProfiles() != null) {
//                                //we know that there are 2 setting elements. set the new profile above them ;)
//                                headerResult.addProfile(newProfile, headerResult.getProfiles().size() - 2);
//                            } else {
//                                headerResult.addProfiles(newProfile);
//                            }
//                        }
//
//                        //false if you have not consumed the event and it should close the drawer
//                        return false;
//                    }
//                })
//                .withSavedInstance(savedInstanceState)
//                .build();
//
//        //Create the drawer
//        result = new DrawerBuilder()
//                .withActivity(this)
//                .withToolbar(toolbar)
//                .withHasStableIds(true)
//                .withItemAnimator(new AlphaCrossFadeAnimator())
//                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
//                .addDrawerItems(
//                        new PrimaryDrawerItem().withName(R.string.drawer_item_home).withDescription(R.string.drawer_item_home_desc).withIcon(GoogleMaterial.Icon.gmd_brightness_5).withIdentifier(DRAWER_ITEM_HOME_CODE).withSelectable(false),
//                        new PrimaryDrawerItem().withName(R.string.drawer_item_hunt_treasure).withDescription(R.string.drawer_item_hunt_treasure_desc).withIcon(FontAwesome.Icon.faw_home).withIdentifier(DRAWER_ITEM_HUNT_TREASURE_CODE).withSelectable(false),
////                        new PrimaryDrawerItem().withName(R.string.drawer_item_multi_drawer).withDescription(R.string.drawer_item_multi_drawer_desc).withIcon(FontAwesome.Icon.faw_gamepad).withIdentifier(3).withSelectable(false),
////                        new PrimaryDrawerItem().withName(R.string.drawer_item_non_translucent_status_drawer).withDescription(R.string.drawer_item_non_translucent_status_drawer_desc).withIcon(FontAwesome.Icon.faw_eye).withIdentifier(4).withSelectable(false).withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700)),
////                        new PrimaryDrawerItem().withName(R.string.drawer_item_advanced_drawer).withDescription(R.string.drawer_item_advanced_drawer_desc).withIcon(GoogleMaterial.Icon.gmd_adb).withIdentifier(5).withSelectable(false),
////                        new PrimaryDrawerItem().withName(R.string.drawer_item_embedded_drawer).withDescription(R.string.drawer_item_embedded_drawer_desc).withIcon(GoogleMaterial.Icon.gmd_battery_full).withIdentifier(7).withSelectable(false),
////                        new PrimaryDrawerItem().withName(R.string.drawer_item_fullscreen_drawer).withDescription(R.string.drawer_item_fullscreen_drawer_desc).withIcon(GoogleMaterial.Icon.gmd_label).withIdentifier(8).withSelectable(false),
////                        new PrimaryDrawerItem().withName(R.string.drawer_item_custom_container_drawer).withDescription(R.string.drawer_item_custom_container_drawer_desc).withIcon(GoogleMaterial.Icon.gmd_my_location).withIdentifier(9).withSelectable(false),
////                        new PrimaryDrawerItem().withName(R.string.drawer_item_menu_drawer).withDescription(R.string.drawer_item_menu_drawer_desc).withIcon(GoogleMaterial.Icon.gmd_filter_list).withIdentifier(10).withSelectable(false),
////                        new PrimaryDrawerItem().withName(R.string.drawer_item_mini_drawer).withDescription(R.string.drawer_item_mini_drawer_desc).withIcon(GoogleMaterial.Icon.gmd_battery_charging_full).withIdentifier(11).withSelectable(false),
////                        new PrimaryDrawerItem().withName(R.string.drawer_item_fragment_drawer).withDescription(R.string.drawer_item_fragment_drawer_desc).withIcon(GoogleMaterial.Icon.gmd_disc_full).withIdentifier(12).withSelectable(false),
////                        new PrimaryDrawerItem().withName(R.string.drawer_item_collapsing_toolbar_drawer).withDescription(R.string.drawer_item_collapsing_toolbar_drawer_desc).withIcon(GoogleMaterial.Icon.gmd_camera_rear).withIdentifier(13).withSelectable(false),
////                        new PrimaryDrawerItem().withName(R.string.drawer_item_persistent_compact_header).withDescription(R.string.drawer_item_persistent_compact_header_desc).withIcon(GoogleMaterial.Icon.gmd_brightness_5).withIdentifier(14).withSelectable(false),
////                        new PrimaryDrawerItem().withName(R.string.drawer_item_crossfade_drawer_layout_drawer).withDescription(R.string.drawer_item_crossfade_drawer_layout_drawer_desc).withIcon(GoogleMaterial.Icon.gmd_format_bold).withIdentifier(15).withSelectable(false),
//
//                        new ExpandableBadgeDrawerItem().withName("Collapsable Badge").withIcon(GoogleMaterial.Icon.gmd_format_bold).withIdentifier(18).withSelectable(false).withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700)).withBadge("100").withSubItems(
//                                new SecondaryDrawerItem().withName("CollapsableItem").withLevel(2).withIcon(GoogleMaterial.Icon.gmd_format_bold).withIdentifier(2000),
//                                new SecondaryDrawerItem().withName("CollapsableItem 2").withLevel(2).withIcon(GoogleMaterial.Icon.gmd_format_bold).withIdentifier(2001)
//                        ),
//                        new ExpandableDrawerItem().withName("Collapsable").withIcon(GoogleMaterial.Icon.gmd_filter_list).withIdentifier(19).withSelectable(false).withSubItems(
//                                new SecondaryDrawerItem().withName("CollapsableItem").withLevel(2).withIcon(GoogleMaterial.Icon.gmd_filter_list).withIdentifier(2002),
//                                new SecondaryDrawerItem().withName("CollapsableItem 2").withLevel(2).withIcon(GoogleMaterial.Icon.gmd_filter_list).withIdentifier(2003)
//                        )
////                        new SectionDrawerItem().withName(R.string.drawer_item_section_header),
////                        new SecondaryDrawerItem().withName(R.string.drawer_item_open_source).withIcon(FontAwesome.Icon.faw_github).withIdentifier(20).withSelectable(false),
////                        new SecondaryDrawerItem().withName(R.string.drawer_item_contact).withIcon(GoogleMaterial.Icon.gmd_format_color_fill).withIdentifier(21).withTag("Bullhorn")
//
//                ) // add the items we want to use with our Drawer
//                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
//                    @Override
//                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
//                        //check if the drawerItem is set.
//                        //there are different reasons for the drawerItem to be null
//                        //--> click on the header
//                        //--> click on the footer
//                        //those items don't contain a drawerItem
//
//                        if (drawerItem != null) {
//                            Intent intent = null;
//                            if (drawerItem.getIdentifier() == DRAWER_ITEM_HOME_CODE) {
//                                intent = new Intent(MapsActivity.this, DrawerActivity.class);
//                            }
//                            if (drawerItem.getIdentifier() == DRAWER_ITEM_HUNT_TREASURE_CODE) {
//                                //intent = new Intent(MapsActivity.this, MapsActivity.class);
//                            }/*else if (drawerItem.getIdentifier() == 2) {
//                                intent = new Intent(DrawerActivity.this, ActionBarActivity.class);
//                            } else if (drawerItem.getIdentifier() == 3) {
//                                intent = new Intent(DrawerActivity.this, MultiDrawerActivity.class);
//                            } else if (drawerItem.getIdentifier() == 4) {
//                                intent = new Intent(DrawerActivity.this, NonTranslucentDrawerActivity.class);
//                            } else if (drawerItem.getIdentifier() == 5) {
//                                intent = new Intent(DrawerActivity.this, AdvancedActivity.class);
//                            } else if (drawerItem.getIdentifier() == 7) {
//                                intent = new Intent(DrawerActivity.this, EmbeddedDrawerActivity.class);
//                            } else if (drawerItem.getIdentifier() == 8) {
//                                intent = new Intent(DrawerActivity.this, FullscreenDrawerActivity.class);
//                            } else if (drawerItem.getIdentifier() == 9) {
//                                intent = new Intent(DrawerActivity.this, CustomContainerActivity.class);
//                            } else if (drawerItem.getIdentifier() == 10) {
//                                intent = new Intent(DrawerActivity.this, MenuDrawerActivity.class);
//                            } else if (drawerItem.getIdentifier() == 11) {
//                                intent = new Intent(DrawerActivity.this, MiniDrawerActivity.class);
//                            } else if (drawerItem.getIdentifier() == 12) {
//                                intent = new Intent(DrawerActivity.this, FragmentActivity.class);
//                            } else if (drawerItem.getIdentifier() == 13) {
//                                intent = new Intent(DrawerActivity.this, CollapsingToolbarActivity.class);
//                            } else if (drawerItem.getIdentifier() == 14) {
//                                intent = new Intent(DrawerActivity.this, PersistentDrawerActivity.class);
//                            } else if (drawerItem.getIdentifier() == 15) {
//                                intent = new Intent(DrawerActivity.this, CrossfadeDrawerLayoutActvitiy.class);
//                            } */else if (drawerItem.getIdentifier() == 20) {
//                                intent = new LibsBuilder()
//                                        .withFields(R.string.class.getFields())
//                                        .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
//                                        .intent(MapsActivity.this);
//                            }
//                            if (intent != null) {
//                                MapsActivity.this.startActivity(intent);
//                            }
//                        }
//
//                        return false;
//                    }
//                })
//                .withSavedInstance(savedInstanceState)
//                .withShowDrawerOnFirstLaunch(true)
////              .withShowDrawerUntilDraggedOpened(true)
//                .build();
//
//        //only set the active selection or active profile if we do not recreate the activity
//        if (savedInstanceState == null) {
//            // set the selection to the item with the identifier 11
//            result.setSelection(21, false);
//
//            //set the active profile
//            headerResult.setActiveProfile(profile);
//        }
//
//        result.updateBadge(4, new StringHolder(10 + ""));
//    }

    private static final int DRAWER_ITEM_HOME_CODE = 1;
    private static final int DRAWER_ITEM_HUNT_TREASURE_CODE = 2;
    private static final int DRAWER_ITEM_FIND_TREASURE_CODE = 3;
    private static final int DRAWER_ITEM_SEARCH_CODE = 4;
    private static final int DRAWER_ITEM_SCAN_CODE = 5;
    private static final int DRAWER_ITEM_LEADERBOARD_CODE = 6;

    public void createDrawer(Bundle savedInstanceState){
        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create a few sample profile
        // NOTE you have to define the loader logic too. See the CustomApplication for more details
        final IProfile profile = new ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com").withIcon(R.drawable.profile).withIdentifier(100);

        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .addProfiles(
                        profile

                        //don't ask but google uses 14dp for the add account icon in gmail but 20dp for the normal icons (like manage account)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        //sample usage of the onProfileChanged listener
                        //if the clicked item has the identifier 1 add a new profile ;)
                        if (profile instanceof IDrawerItem && profile.getIdentifier() == PROFILE_SETTING) {
                            int count = 100 + headerResult.getProfiles().size() + 1;
                            IProfile newProfile = new ProfileDrawerItem().withNameShown(true).withName("Batman" + count).withEmail("batman" + count + "@gmail.com").withIcon(R.drawable.profile).withIdentifier(count);
                            if (headerResult.getProfiles() != null) {
                                //we know that there are 2 setting elements. set the new profile above them ;)
                                headerResult.addProfile(newProfile, headerResult.getProfiles().size() - 2);
                            } else {
                                headerResult.addProfiles(newProfile);
                            }
                        }

                        //false if you have not consumed the event and it should close the drawer
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

        //Create the drawer
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                .withItemAnimator(new AlphaCrossFadeAnimator())
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        new SectionDrawerItem().withName("Navigate"),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_home).withDescription(R.string.drawer_item_home_desc).withIcon(FontAwesome.Icon.faw_home).withIdentifier(DRAWER_ITEM_HOME_CODE).withSelectable(false),
                        new SectionDrawerItem().withName("Treasure Hunt"),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_hunt_treasure).withDescription(R.string.drawer_item_hunt_treasure_desc).withIcon(FontAwesome.Icon.faw_map).withIdentifier(DRAWER_ITEM_HUNT_TREASURE_CODE).withSelectable(false),
                        // new PrimaryDrawerItem().withName(R.string.drawer_item_find_treasure).withDescription(R.string.drawer_item_find_treasure_desc).withIcon(FontAwesome.Icon.faw_list).withIdentifier(DRAWER_ITEM_FIND_TREASURE_CODE).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_search).withDescription(R.string.drawer_item_search_desc).withIcon(FontAwesome.Icon.faw_search).withIdentifier(DRAWER_ITEM_SEARCH_CODE).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_scan).withDescription(R.string.drawer_item_scan_desc).withIcon(FontAwesome.Icon.faw_qrcode).withIdentifier(DRAWER_ITEM_SCAN_CODE).withSelectable(false),
                        new SectionDrawerItem().withName("Community"),
                        new PrimaryDrawerItem().withName("LeaderBoard").withDescription("See where you are, in your community.").withIcon(FontAwesome.Icon.faw_object_group).withIdentifier(DRAWER_ITEM_LEADERBOARD_CODE).withSelectable(false)

//                        new PrimaryDrawerItem().withName(R.string.drawer_item_multi_drawer).withDescription(R.string.drawer_item_multi_drawer_desc).withIcon(FontAwesome.Icon.faw_gamepad).withIdentifier(3).withSelectable(false),
//                        new PrimaryDrawerItem().withName(R.string.drawer_item_non_translucent_status_drawer).withDescription(R.string.drawer_item_non_translucent_status_drawer_desc).withIcon(FontAwesome.Icon.faw_eye).withIdentifier(4).withSelectable(false).withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700)),
//                        new PrimaryDrawerItem().withName(R.string.drawer_item_advanced_drawer).withDescription(R.string.drawer_item_advanced_drawer_desc).withIcon(GoogleMaterial.Icon.gmd_adb).withIdentifier(5).withSelectable(false),
//                        new PrimaryDrawerItem().withName(R.string.drawer_item_embedded_drawer).withDescription(R.string.drawer_item_embedded_drawer_desc).withIcon(GoogleMaterial.Icon.gmd_battery_full).withIdentifier(7).withSelectable(false),
//                        new PrimaryDrawerItem().withName(R.string.drawer_item_fullscreen_drawer).withDescription(R.string.drawer_item_fullscreen_drawer_desc).withIcon(GoogleMaterial.Icon.gmd_label).withIdentifier(8).withSelectable(false),
//                        new PrimaryDrawerItem().withName(R.string.drawer_item_custom_container_drawer).withDescription(R.string.drawer_item_custom_container_drawer_desc).withIcon(GoogleMaterial.Icon.gmd_my_location).withIdentifier(9).withSelectable(false),
//                        new PrimaryDrawerItem().withName(R.string.drawer_item_menu_drawer).withDescription(R.string.drawer_item_menu_drawer_desc).withIcon(GoogleMaterial.Icon.gmd_filter_list).withIdentifier(10).withSelectable(false),
//                        new PrimaryDrawerItem().withName(R.string.drawer_item_mini_drawer).withDescription(R.string.drawer_item_mini_drawer_desc).withIcon(GoogleMaterial.Icon.gmd_battery_charging_full).withIdentifier(11).withSelectable(false),
//                        new PrimaryDrawerItem().withName(R.string.drawer_item_fragment_drawer).withDescription(R.string.drawer_item_fragment_drawer_desc).withIcon(GoogleMaterial.Icon.gmd_disc_full).withIdentifier(12).withSelectable(false),
//                        new PrimaryDrawerItem().withName(R.string.drawer_item_collapsing_toolbar_drawer).withDescription(R.string.drawer_item_collapsing_toolbar_drawer_desc).withIcon(GoogleMaterial.Icon.gmd_camera_rear).withIdentifier(13).withSelectable(false),
//                        new PrimaryDrawerItem().withName(R.string.drawer_item_persistent_compact_header).withDescription(R.string.drawer_item_persistent_compact_header_desc).withIcon(GoogleMaterial.Icon.gmd_brightness_5).withIdentifier(14).withSelectable(false),
//                        new PrimaryDrawerItem().withName(R.string.drawer_item_crossfade_drawer_layout_drawer).withDescription(R.string.drawer_item_crossfade_drawer_layout_drawer_desc).withIcon(GoogleMaterial.Icon.gmd_format_bold).withIdentifier(15).withSelectable(false),

//                        new ExpandableBadgeDrawerItem().withName("Collapsable Badge").withIcon(GoogleMaterial.Icon.gmd_format_bold).withIdentifier(18).withSelectable(false).withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700)).withBadge("100").withSubItems(
//                                new SecondaryDrawerItem().withName("CollapsableItem").withLevel(2).withIcon(GoogleMaterial.Icon.gmd_format_bold).withIdentifier(2000),
//                                new SecondaryDrawerItem().withName("CollapsableItem 2").withLevel(2).withIcon(GoogleMaterial.Icon.gmd_format_bold).withIdentifier(2001)
//                        ),
//                        new ExpandableDrawerItem().withName("Collapsable").withIcon(GoogleMaterial.Icon.gmd_filter_list).withIdentifier(19).withSelectable(false).withSubItems(
//                                new SecondaryDrawerItem().withName("CollapsableItem").withLevel(2).withIcon(GoogleMaterial.Icon.gmd_filter_list).withIdentifier(2002),
//                                new SecondaryDrawerItem().withName("CollapsableItem 2").withLevel(2).withIcon(GoogleMaterial.Icon.gmd_filter_list).withIdentifier(2003)
//                        )
//                        new SectionDrawerItem().withName(R.string.drawer_item_section_header),
//                        new SecondaryDrawerItem().withName(R.string.drawer_item_open_source).withIcon(FontAwesome.Icon.faw_github).withIdentifier(20).withSelectable(false),
//                        new SecondaryDrawerItem().withName(R.string.drawer_item_contact).withIcon(GoogleMaterial.Icon.gmd_format_color_fill).withIdentifier(21).withTag("Bullhorn")

                ) // add the items we want to use with our Drawer
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        //check if the drawerItem is set.
                        //there are different reasons for the drawerItem to be null
                        //--> click on the header
                        //--> click on the footer
                        //those items don't contain a drawerItem

                        if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == DRAWER_ITEM_HOME_CODE) {
                                intent = new Intent(MapsActivity.this, DrawerActivity.class);
                            } else if (drawerItem.getIdentifier() == DRAWER_ITEM_HUNT_TREASURE_CODE) {
                                //intent = new Intent(MapsActivity.this, MapsActivity.class);

                            } else if (drawerItem.getIdentifier() == DRAWER_ITEM_FIND_TREASURE_CODE) {
                                intent = new Intent(MapsActivity.this, ListActivity.class);

                            } else if (drawerItem.getIdentifier() == DRAWER_ITEM_SEARCH_CODE) {
                                intent = new Intent(MapsActivity.this, SearchActivity.class);

                            } else if (drawerItem.getIdentifier() == DRAWER_ITEM_SCAN_CODE) {
                                intent = new Intent(MapsActivity.this, QRCodeActivity.class);
                            }  else if (drawerItem.getIdentifier() == DRAWER_ITEM_LEADERBOARD_CODE) {
                                intent = new Intent(MapsActivity.this, LeaderboardActivity.class);
                            }
                            /*else if (drawerItem.getIdentifier() == 2) {
                                intent = new Intent(DrawerActivity.this, ActionBarActivity.class);
                            } else if (drawerItem.getIdentifier() == 3) {
                                intent = new Intent(DrawerActivity.this, MultiDrawerActivity.class);
                            } else if (drawerItem.getIdentifier() == 4) {
                                intent = new Intent(DrawerActivity.this, NonTranslucentDrawerActivity.class);
                            } else if (drawerItem.getIdentifier() == 5) {
                                intent = new Intent(DrawerActivity.this, AdvancedActivity.class);
                            } else if (drawerItem.getIdentifier() == 7) {
                                intent = new Intent(DrawerActivity.this, EmbeddedDrawerActivity.class);
                            } else if (drawerItem.getIdentifier() == 8) {
                                intent = new Intent(DrawerActivity.this, FullscreenDrawerActivity.class);
                            } else if (drawerItem.getIdentifier() == 9) {
                                intent = new Intent(DrawerActivity.this, CustomContainerActivity.class);
                            } else if (drawerItem.getIdentifier() == 10) {
                                intent = new Intent(DrawerActivity.this, MenuDrawerActivity.class);
                            } else if (drawerItem.getIdentifier() == 11) {
                                intent = new Intent(DrawerActivity.this, MiniDrawerActivity.class);
                            } else if (drawerItem.getIdentifier() == 12) {
                                intent = new Intent(DrawerActivity.this, FragmentActivity.class);
                            } else if (drawerItem.getIdentifier() == 13) {
                                intent = new Intent(DrawerActivity.this, CollapsingToolbarActivity.class);
                            } else if (drawerItem.getIdentifier() == 14) {
                                intent = new Intent(DrawerActivity.this, PersistentDrawerActivity.class);
                            } else if (drawerItem.getIdentifier() == 15) {
                                intent = new Intent(DrawerActivity.this, CrossfadeDrawerLayoutActvitiy.class);
                            } */else if (drawerItem.getIdentifier() == 20) {
                                intent = new LibsBuilder()
                                        .withFields(R.string.class.getFields())
                                        .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                                        .intent(MapsActivity.this);
                            }
                            if (intent != null) {
                                MapsActivity.this.startActivity(intent);
                            }
                        }

                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
//              .withShowDrawerUntilDraggedOpened(true)
                .build();

        //only set the active selection or active profile if we do not recreate the activity
        if (savedInstanceState == null) {
            // set the selection to the item with the identifier 11
            result.setSelection(21, false);

            //set the active profile
            headerResult.setActiveProfile(profile);
        }

        result.updateBadge(4, new StringHolder(10 + ""));
    }



    public void showTreasureInfoDialog(final Treasure treasure){
        DialogPlus dialog = DialogPlus.newDialog(this)
                .setContentHolder(new ViewHolder(R.layout.treasure_info))
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                        //Toast.makeText(MapsActivity.this, "Clicked!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
                .create();
        dialog.show();

        final View rootView = dialog.getHolderView();

        TextView treasureName = rootView.findViewById(R.id.treasure_name);
        treasureName.setText(treasure.getName());

        TextView treasureAddress = rootView.findViewById(R.id.treasure_address);
        treasureAddress.setText(treasure.getAddress());
        TextView treasurePoints = rootView.findViewById(R.id.treasure_points);
        treasurePoints.setText(treasure.getPoints() + " Points");
        Button seeMoreBtn = rootView.findViewById(R.id.read_more_btn);
        seeMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapsActivity.this, TreasureInfoActivity.class);
                intent.putExtra("treasure", treasure);
                startActivity(intent);
            }
        });
       // landmarkEditNameView = (EditText) textEntryView.findViewById(R.id.landmark_name_dialog_edit);

    }

//    public void addRandomMarkers(){
//        addLocationMarker(new Treasure(27.6867787,85.3294549, "ABC", "1"));
//    }

    public void addLocationMarker(/*Treasure treasure*/ Treasure treasure){
        // Creating a marker
        MarkerOptions markerOptions = new MarkerOptions().icon(Utils.bitmapDescriptorFromVector(this, R.drawable.ic_monetization_on_red_24dp)).title(treasure.getName());

        LatLng latLng = new LatLng(treasure.getLatitude(), treasure.getLongitude());
        // Setting the position for the marker
        markerOptions.position(latLng);

        // Animating to the touched position
        // mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

        // Placing a marker on the touched position
        mMap.addMarker(markerOptions).setTag(treasure);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        //marker.getId();
        if(!marker.getTitle().equals("You are here")){
            showTreasureInfoDialog((Treasure)marker.getTag());
        }
        return false;
    }

}
