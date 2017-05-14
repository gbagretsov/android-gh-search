package gbagretsov.ghsearch.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;
import com.paginate.Paginate;
import gbagretsov.ghsearch.app.GitHubModel.GitHubUser;
import gbagretsov.ghsearch.app.GitHubModel.GitHubUsersResponse;
import gbagretsov.ghsearch.app.UsersDisplay.UsersAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

// TODO: Скрывать actionbar при скролле вверх
// TODO: Поворот экрана

public class SearchResultsActivity extends AppCompatActivity {

    public static final String QUERY = "query"; // Имя параметра, переданного из другой Activity

    private RecyclerView recyclerView;
    private List<GitHubUser> users;

    // Параметры пагинации
    private int currentPage = 1;
    private static final int PER_PAGE = 30;

    // Параметры для бесконечной загрузки
    private boolean loadingInProgress = true;
    private boolean hasLoadedAllItems = false;
    private Paginate.Callbacks callbacks;
    Paginate paginate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        // Устанавливаем тулбар
        Toolbar toolbar = (Toolbar) findViewById(R.id.search_results_toolbar);
        setSupportActionBar(toolbar);

        // Показываем кнопку "Назад"
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Получаем переданный параметр
        Intent intent = getIntent();
        final String query = intent.getStringExtra(QUERY);

        // Устанавливаем отображение списка найденных пользователей
        users = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.search_results_users_recycle_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        UsersAdapter adapter = new UsersAdapter(users, this);
        recyclerView.setAdapter(adapter);

        // Бесконечный скролл
        callbacks = new Paginate.Callbacks() {
            @Override
            public void onLoadMore() {
                if (hasLoadedAllItems) {
                    return;
                }
                sendRequest(query, recyclerView.getContext());
            }

            @Override
            public boolean isLoading() {
                return loadingInProgress;
            }

            @Override
            public boolean hasLoadedAllItems() {
                return hasLoadedAllItems;
            }
        };

        paginate = Paginate.with(recyclerView, callbacks)
                .setLoadingTriggerThreshold(2)
                .build();

        // Выполняем запрос
        sendRequest(query, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_results, menu);
        return true;
    }

    // Выполняем запрос
    private void sendRequest(String query, final Context context){

        loadingInProgress = true;
        Log.d("API", "Loading");

        // Запрос выполняется асинхронно, также присутствует обработка ошибок.
        // Загружается конкретная страница результатов запроса
        App.getApi().getData(query, currentPage, PER_PAGE).enqueue(new Callback<GitHubUsersResponse>() {
            @Override
            public void onResponse(Call<GitHubUsersResponse> call, Response<GitHubUsersResponse> response) {
                loadingInProgress = false;

                // Данные успешно пришли, но надо проверить response.body() на null
                if (response.body() != null &&
                        response.body().getGitHubUsers() != null &&
                       !response.body().getGitHubUsers().isEmpty()) {

                    // Сохраняем в память и отображаем результат
                    users.addAll(response.body().getGitHubUsers());
                    recyclerView.getAdapter().notifyDataSetChanged();

                    // Если загрузили меньше максимально возможного на странице, это значит,
                    // что страница последняя
                    if (response.body().getGitHubUsers().size() < PER_PAGE) {
                        endLoadHandling();
                        return;
                    }

                    // Увеличиваем номер страницы, чтобы при скролле к концу списка загрузить очередную порцию
                    currentPage++;
                } else if (currentPage == 1) { // Если пустой ответ на первой странице...
                    // ... значит, ничего не найдено
                    Toast.makeText(context, getText(R.string.users_not_found), Toast.LENGTH_LONG).show();
                    ((Activity) context).finish();
                } else { // Если пустой ответ не на первой странице, значит всё загрузили
                    endLoadHandling();
                }
            }

            @Override
            public void onFailure(Call<GitHubUsersResponse> call, Throwable t) {
                // Произошла ошибка
                Toast.makeText(context, getText(R.string.error_occured), Toast.LENGTH_SHORT).show();
                endLoadHandling();
                // TODO: попробовать снова
            }

            // Убираем индикатор загрузки и отписываемся от скролла
            private void endLoadHandling() {
                Log.d("API", "Done loading!");
                loadingInProgress = false;
                hasLoadedAllItems = true;
                paginate.unbind();
            }
        });
    }
}
