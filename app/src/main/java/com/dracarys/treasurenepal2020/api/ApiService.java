package com.dracarys.treasurenepal2020.api;
import com.dracarys.treasurenepal2020.entities.LeaderBoard;
import com.dracarys.treasurenepal2020.entities.Treasure;

import java.util.List;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @PATCH("userprofile/1/")
    Call<LeaderBoard> validateQR(@Query("format")String format, @Query("ch_id") String chID);

    @GET("userprofile/1/")
    Call<LeaderBoard> getUserDetail(@Query("format")String format);

    @GET("challenges/")
    Call<List<Treasure>> getTreasures(@Query("format")String format);

    //@GET("challenges/{id}/")
    @GET("challenges/{id}")
    Call<Treasure> getTreasureById(@Path("id") String id,
                                   @Query("format") String format
                                   );

    @GET("leaderboards/")
    Call<List<LeaderBoard>> getLeaderBoards(@Query("format")String format);
}
