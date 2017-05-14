package gbagretsov.ghsearch.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
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

        GitHubUser stub = new GitHubUser();
        stub.setAvatarUrl("https://avatars1.githubusercontent.com/u/9006613?v=3");

        stub.setLogin("gbagretsov");
        stub.setName("Georgiy Bagretsov");
        stub.setType("User");

        // Personal info
        stub.setCompany("Microsoft google");
        stub.setLocation("Saint-Petersburg");
        stub.setEmail("mysecretmail@mail.com");
        stub.setBlog("https://facebook.com");

        // stats
        stub.setFollowers(20);
        stub.setPublicRepos(10);
        stub.setPublicGists(5);

        showUserCard(stub);
        // TODO: включить потом
        /*
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
        });*/
    }

    private void showUserCard(GitHubUser user) {
        ImageView avatar = (ImageView) findViewById(R.id.user_card_avatar);
        Picasso.with(getApplicationContext()).load(user.getAvatarUrl()).fit()
                //.placeholder(R.drawable.user_placeholder) // TODO: placeholder
                //.error(R.drawable.user_placeholder_error)
                .into(avatar);

        TextView login = (TextView) findViewById(R.id.user_card_login);
        login.setText(user.getLogin());
        login.setSelected(true); // Для того, чтобы работала бегущая строка (marquee), если длинный текст

        TextView fullName = (TextView) findViewById(R.id.user_card_full_name);
        fullName.setText(user.getName());
        fullName.setSelected(true);

        // Персональная информация, контакты

        TextView company = (TextView) findViewById(R.id.user_card_company);
        company.setText(user.getCompany());
        company.setSelected(true);

        TextView location = (TextView) findViewById(R.id.user_card_location);
        location.setText(user.getLocation());
        location.setSelected(true);

        TextView email = (TextView) findViewById(R.id.user_card_email);
        email.setText(user.getEmail());
        email.setSelected(true);

        TextView blog = (TextView) findViewById(R.id.user_card_blog);
        blog.setText(user.getBlog());
        blog.setSelected(true);

        // TODO: статистика, иконки

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_card, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
