package ru.savchenko.andrey.ectrolux.network;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.savchenko.andrey.ectrolux.entities.Current;

/**
 * Created by Andrey on 14.11.2017.
 */

public interface RetrofitService {
    @GET("/latest")
    Single<Current>getCurrent(@Query("base")String base);
}
