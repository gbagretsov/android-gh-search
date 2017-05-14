package gbagretsov.ghsearch.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Toast;
import gbagretsov.ghsearch.app.GitHubModel.GitHubUser;
import gbagretsov.ghsearch.app.GitHubModel.GitHubUsersResponse;
import gbagretsov.ghsearch.app.UsersDisplay.UsersAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {

    public static final String QUERY = "query"; // Имя параметра, переданного из другой Activity

    RecyclerView recyclerView;
    List<GitHubUser> users;

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
        String query = intent.getStringExtra(QUERY);

        // Устанавливаем отображение списка найденных пользователей
        users = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.search_results_users_recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        UsersAdapter adapter = new UsersAdapter(users, this);
        recyclerView.setAdapter(adapter);

        // Выполняем запрос
        // TODO: Пагинация, бесконечный скролл
        // TODO: Добавить spinning loader
        sendRequest(query, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_results, menu);
        return true;
    }

    // Выполняем запрос
    private void sendRequest(String query, final Context context){
        // Запрос выполняется асинхронно, также присутствует обработка ошибок
        App.getApi().getData(query).enqueue(new Callback<GitHubUsersResponse>() {
            @Override
            public void onResponse(Call<GitHubUsersResponse> call, Response<GitHubUsersResponse> response) {
                // Данные успешно пришли, но надо проверить response.body() на null
                if (response.body() != null &&
                        response.body().getGitHubUsers() != null &&
                       !response.body().getGitHubUsers().isEmpty()) {
                    // Сохраняем в память и отображаем результат
                    users.addAll(response.body().getGitHubUsers());
                    recyclerView.getAdapter().notifyDataSetChanged();
                } else {
                    // Ничего не найдено
                    Toast.makeText(context, getText(R.string.users_not_found), Toast.LENGTH_LONG).show();
                    ((Activity) context).finish();
                }
            }
            @Override
            public void onFailure(Call<GitHubUsersResponse> call, Throwable t) {
                // Произошла ошибка
                Toast.makeText(context, getText(R.string.error_occured), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
