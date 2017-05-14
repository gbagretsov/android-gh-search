package gbagretsov.ghsearch.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import gbagretsov.ghsearch.app.GitHubModel.GitHubUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Класс, отрисовывающий карточки пользователя
 */
public class UserCardActivity extends AppCompatActivity {

    public static final String USER_LOGIN = "user_login";
    SharedPreferences prefs;
    boolean isFavourite; // Проверка, является ли текущий пользователь избранным
    String login, avatarUrl;
    ImageButton toggleFavourite;

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
        login = intent.getStringExtra(USER_LOGIN);

        // Проверяем, избранный ли пользователь
        prefs = getSharedPreferences(FavouriteFragment.FAVOURITE, 0);
        isFavourite = !prefs.getString(login, "").isEmpty();

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
        ImageView avatar = (ImageView) findViewById(R.id.user_card_avatar);
        avatarUrl = user.getAvatarUrl();
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

        toggleFavourite = (ImageButton) findViewById(R.id.btn_toggle_favourite);
        setImageViewIconColor( // В зависимости от того, избранный ли пользователь
                toggleFavourite,
                isFavourite ? R.color.accent : R.color.icon_secondary,
                R.drawable.ic_favorite_white_24dp);
        toggleFavourite.setOnClickListener(toggleFavouriteClickListener);

        // Персональная информация, контакты
        TextView company = (TextView) findViewById(R.id.user_card_company);
        String s = user.getCompany();
        company.setText(s == null || s.isEmpty() ? getString(R.string.not_specified) : s);
        company.setSelected(true);

        TextView location = (TextView) findViewById(R.id.user_card_location);
        s = user.getLocation();
        location.setText(s == null || s.isEmpty() ? getString(R.string.not_specified) : s);
        location.setSelected(true);

        TextView email = (TextView) findViewById(R.id.user_card_email);
        s = user.getEmail();
        email.setText(s == null || s.isEmpty() ? getString(R.string.not_available) : s);
        email.setSelected(true);

        TextView blog = (TextView) findViewById(R.id.user_card_blog);
        s = user.getBlog();
        blog.setText(s == null || s.isEmpty() ? getString(R.string.not_specified) : s);
        blog.setSelected(true);

        // Статистика
        TextView publicRepos = (TextView) findViewById(R.id.user_card_public_repos);
        publicRepos.setText(shortenNumericString(user.getPublicRepos()));
        publicRepos.setSelected(true);

        TextView publicGists = (TextView) findViewById(R.id.user_card_public_gists);
        publicGists.setText(shortenNumericString(user.getPublicGists()));
        publicGists.setSelected(true);

        TextView followers = (TextView) findViewById(R.id.user_card_followers);
        followers.setText(shortenNumericString(user.getFollowers()));
        followers.setSelected(true);

        // Иконки
        ImageView iconCompany  = (ImageView) findViewById(R.id.icon_user_card_company);
        ImageView iconLocation = (ImageView) findViewById(R.id.icon_user_card_location);
        ImageView iconEmail    = (ImageView) findViewById(R.id.icon_user_card_email);
        ImageView iconblog     = (ImageView) findViewById(R.id.icon_user_card_blog);

        setImageViewIconColor(iconCompany,  R.color.primary, R.drawable.ic_worker);
        setImageViewIconColor(iconLocation, R.color.primary, R.drawable.ic_location_on_white_24dp);
        setImageViewIconColor(iconEmail,    R.color.primary, R.drawable.ic_email_white_24dp);
        setImageViewIconColor(iconblog,     R.color.primary, R.drawable.ic_link_variant);

    }

    private View.OnClickListener toggleFavouriteClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            isFavourite = !isFavourite;

            if (isFavourite) {
                prefs.edit().putString(login, avatarUrl).apply(); // Храним адрес аватарки
            } else {
                prefs.edit().remove(login).apply();
            }

            // Меняем цвет
            setImageViewIconColor(
                    toggleFavourite,
                    isFavourite ? R.color.accent : R.color.icon_secondary,
                    R.drawable.ic_favorite_white_24dp);
        }
    };

    private void setImageViewIconColor(ImageView view, int color, int icon) {
        Drawable drawable = this.getResources().getDrawable(icon);
        assert drawable != null;
        drawable.setColorFilter
                (new PorterDuffColorFilter(getResources().getColor(color), PorterDuff.Mode.SRC_IN));
        view.setImageDrawable(drawable);
    }

    // Если количество больше 10 тыс., то последние три цифры меняем на "К"
    private String shortenNumericString(int n) {
        if (n >= 10000) {
            return String.valueOf(n).substring(0, String.valueOf(n).length() - 3).concat("K");
        } else {
            return String.valueOf(n);
        }
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
