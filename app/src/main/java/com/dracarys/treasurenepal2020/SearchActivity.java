package com.dracarys.treasurenepal2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dracarys.treasurenepal2020.adapter.TreasureAdapter;
import com.dracarys.treasurenepal2020.api.ApiService;
import com.dracarys.treasurenepal2020.api.RetrofitClient;
import com.dracarys.treasurenepal2020.entities.Treasure;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.itemanimators.AlphaCrossFadeAnimator;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements MaterialSearchBar.OnSearchActionListener {

    private MaterialSearchBar searchBar;
    private RecyclerView searchResultRV;
    private ProgressBar searchingProgressBar;
    private TreasureAdapter treasureAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        
        initializeViews();

        searchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
        searchBar.setHint("Custom hint");
        searchBar.setSpeechMode(true);
        //enable searchbar callbacks
        searchBar.setOnSearchActionListener(this);
    }

    private void initializeViews() {
        searchResultRV = findViewById(R.id.search_result_recyclerview);
        searchingProgressBar = findViewById(R.id.search_progress_bar);
        searchingProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {

    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        searchQuery(text.toString());
    }

    @Override
    public void onButtonClicked(int buttonCode) {

    }


    public void searchQuery(String query){
        /*Create handle for the RetrofitInstance interface*/
        ApiService service = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        Call<List<Treasure>> call = service.getTreasures("json");
        call.enqueue(new Callback<List<Treasure>>() {

            @Override
            public void onResponse(Call<List<Treasure>> call, Response<List<Treasure>> response) {
                searchingProgressBar.setVisibility(View.GONE);
                generateDataList(response.body());
            }

            @Override
            public void onFailure(Call<List<Treasure>> call, Throwable t) {
                searchingProgressBar.setVisibility(View.GONE);
                Toast.makeText(SearchActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    /*Method to generate List of data using RecyclerView with custom adapter*/
    private void generateDataList(List<Treasure> treasureList) {
        treasureAdapter = new TreasureAdapter(this,treasureList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SearchActivity.this);
        searchResultRV.setLayoutManager(layoutManager);
        searchResultRV.setAdapter(treasureAdapter);
    }

    private static final int DRAWER_ITEM_HOME_CODE = 1;
    private static final int DRAWER_ITEM_HUNT_TREASURE_CODE = 2;
    private static final int DRAWER_ITEM_FIND_TREASURE_CODE = 3;
    private static final int DRAWER_ITEM_SEARCH_CODE = 4;
    private static final int DRAWER_ITEM_SCAN_CODE = 5;
    private static final int DRAWER_ITEM_LEADERBOARD_CODE = 6;
    private AccountHeader headerResult = null;
    private Drawer result = null;
    private static final int PROFILE_SETTING = 100000;


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
                                intent = new Intent(SearchActivity.this, DrawerActivity.class);
                            } else if (drawerItem.getIdentifier() == DRAWER_ITEM_HUNT_TREASURE_CODE) {
                                intent = new Intent(SearchActivity.this, MapsActivity.class);

                            } else if (drawerItem.getIdentifier() == DRAWER_ITEM_FIND_TREASURE_CODE) {
                                intent = new Intent(SearchActivity.this, ListActivity.class);

                            } else if (drawerItem.getIdentifier() == DRAWER_ITEM_SEARCH_CODE) {
                                //intent = new Intent(MapsActivity.this, SearchActivity.class);

                            } else if (drawerItem.getIdentifier() == DRAWER_ITEM_SCAN_CODE) {
                                intent = new Intent(SearchActivity.this, QRCodeActivity.class);
                            }  else if (drawerItem.getIdentifier() == DRAWER_ITEM_LEADERBOARD_CODE) {
                                intent = new Intent(SearchActivity.this, LeaderboardActivity.class);
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
                                        .intent(SearchActivity.this);
                            }
                            if (intent != null) {
                                SearchActivity.this.startActivity(intent);
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


}
