package com.dracarys.treasurenepal2020;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.mancj.materialsearchbar.MaterialSearchBar;

public class SearchActivity extends AppCompatActivity implements MaterialSearchBar.OnSearchActionListener {

    private MaterialSearchBar searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
        searchBar.setHint("Custom hint");
        searchBar.setSpeechMode(true);
        //enable searchbar callbacks
        searchBar.setOnSearchActionListener(this);
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

    }
}
