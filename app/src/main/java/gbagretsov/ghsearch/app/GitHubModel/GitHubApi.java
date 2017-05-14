package gbagretsov.ghsearch.app.GitHubModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Интерфейс для работы с API GitHub
 */
public interface GitHubApi {
    @GET("/search/users")
    Call<GitHubUsersResponse> getData
            (@Query("q") String query, @Query("page") int currentPage, @Query("per_page") int perPage);

    @GET("/users/{user}")
    Call<GitHubUser> getUser
            (@Path("user") String login);
}
