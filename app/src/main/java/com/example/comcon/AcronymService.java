package com.example.comcon;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AcronymService {
    @GET("dictionary.py")
    Call<List<AcronymDef>> getDefinitionsOf(@Query("sf") String sf);
}
