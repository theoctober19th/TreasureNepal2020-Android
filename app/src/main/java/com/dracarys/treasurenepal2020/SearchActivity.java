package com.dracarys.treasurenepal2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

}
