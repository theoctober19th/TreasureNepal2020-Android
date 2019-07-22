package com.dracarys.treasurenepal2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dracarys.treasurenepal2020.adapter.LeaderboardAdapter;
import com.dracarys.treasurenepal2020.adapter.TreasureAdapter;
import com.dracarys.treasurenepal2020.api.ApiService;
import com.dracarys.treasurenepal2020.api.RetrofitClient;
import com.dracarys.treasurenepal2020.entities.LeaderBoard;
import com.dracarys.treasurenepal2020.entities.Treasure;
import com.dracarys.treasurenepal2020.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaderboardActivity extends AppCompatActivity {

    RecyclerView leaderboardRV;
    ProgressBar leaderBoardProgressBar;
    LeaderboardAdapter leaderboardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        initializeViews();

        /*Create handle for the RetrofitInstance interface*/
//        ApiService service = RetrofitClient.getRetrofitInstance().create(ApiService.class);
//
//        Call<List<LeaderBoard>> call = service.getLeaderBoards("json");
//        call.enqueue(new Callback<List<LeaderBoard>>() {
//
//            @Override
//            public void onResponse(Call<List<LeaderBoard>> call, Response<List<LeaderBoard>> response) {
//
//                leaderBoardProgressBar.setVisibility(View.GONE);
//                Log.d("BIKALPA", response.body().toString());
//                generateDataList(response.body());
//
//            }
//
//            @Override
//            public void onFailure(Call<List<LeaderBoard>> call, Throwable t) {
//
//                leaderBoardProgressBar.setVisibility(View.GONE);
//                Toast.makeText(LeaderboardActivity.this, t.toString(), Toast.LENGTH_LONG).show();
//                //Toast.makeText(LeaderboardActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
//
//            }
//        });

        generateDataList(Utils.getLeaderBoard());
    }


    @Override
    protected void onStart() {
        super.onStart();

        leaderboardAdapter.notifyDataSetChanged();
    }

    private void initializeViews() {
        leaderboardRV = findViewById(R.id.leaderboard_recyclerview);
        leaderBoardProgressBar = findViewById(R.id.leaderboard_progress_bar);
        leaderBoardProgressBar.setVisibility(View.VISIBLE);
    }

    /*Method to generate List of data using RecyclerView with custom adapter*/
    private void generateDataList(List<LeaderBoard> leaderBoardList) {
        leaderboardAdapter = new LeaderboardAdapter(this,leaderBoardList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(LeaderboardActivity.this);
        leaderboardRV.setLayoutManager(layoutManager);
        leaderboardRV.setAdapter(leaderboardAdapter);
    }
}
