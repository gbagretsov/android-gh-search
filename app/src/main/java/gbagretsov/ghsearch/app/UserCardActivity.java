package gbagretsov.ghsearch.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import gbagretsov.ghsearch.app.GitHubModel.GitHubUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserCardActivity extends AppCompatActivity {

    public static final String USER_LOGIN = "user_login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_card);

        // Устанавливаем тулбар
        Toolbar toolbar = (Toolbar) findViewById(R.id.search_results_toolbar);
        setSupportActionBar(toolbar);

        // Показываем кнопку "Назад"
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Получаем переданный параметр
        Intent intent = getIntent();
        final String login = intent.getStringExtra(USER_LOGIN);

        App.getApi().getUser(login).enqueue(new Callback<GitHubUser>() {
            @Override
            public void onResponse(Call<GitHubUser> call, Response<GitHubUser> response) {
                // Данные успешно пришли, но надо проверить response.body() на null
                if (response.body() != null) {
                    showUserCard(response.body());
                }
            }

            @Override
            public void onFailure(Call<GitHubUser> call, Throwable t) {
                // Произошла ошибка
                Toast.makeText(getApplicationContext(), getText(R.string.error_occured), Toast.LENGTH_SHORT).show();
                // TODO: попробовать снова
            }
        });
    }

    private void showUserCard(GitHubUser user) {
        // TODO: показывать карточку
        Toast.makeText(this, user.getLogin(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_card, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
