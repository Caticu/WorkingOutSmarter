package com.caticu.workingoutsmarter.API;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitClientInstance {

    private static Retrofit retrofit;
    private static final String BASE_URL = "https://api.api-ninjas.com/v1/";

    public static Retrofit getRetrofitInstance()
    {
        if (retrofit == null)
        {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain ->
                    {
                        Request original = chain.request();
                        Request request = original.newBuilder()
                                .header("X-Api-Key", "jEBW0AlhrhJGC7NOCYcZNA==CShfl7pH7agORoV8")
                                .method(original.method(), original.body())
                                .build();
                        return chain.proceed(request);
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
