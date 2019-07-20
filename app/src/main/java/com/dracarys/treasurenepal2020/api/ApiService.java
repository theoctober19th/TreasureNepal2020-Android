package com.dracarys.treasurenepal2020.api;
import com.dracarys.treasurenepal2020.entities.Treasure;

import java.util.List;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("challenges/")
    Call<List<Treasure>> getTreasures(@Query("format")String format);

    //@GET("challenges/{id}/")
    @GET("1atj9p")
    Call<Treasure> getTreasureById(@Query("format") String format,
                                   @Path("id") String id);
}
