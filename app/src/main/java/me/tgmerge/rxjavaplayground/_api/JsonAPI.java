package me.tgmerge.rxjavaplayground._api;

import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by tgmerge on 11/17.
 * me.tgmerge.rxjavaplayground._api
 *
 * short for JsonPlaceholderService.
 *
 * see: https://jsonplaceholder.typicode.com/
 */

public class JsonAPI {

    private static JsonAPIService service;

    public static void init(Retrofit retrofit) {
        service = retrofit.create(JsonAPIService.class);
    }

    private interface JsonAPIService {
        @GET("/posts/{id}")
        Observable<String> getPost(@Path("id") int postId);

        @GET("/posts")
        Observable<String> getPosts();
    }

    public static Observable<String> getPost(int postId) {
        return service.getPost(postId);
    }

    public static Observable<String> getPosts() {
        return service.getPosts();
    }

}
