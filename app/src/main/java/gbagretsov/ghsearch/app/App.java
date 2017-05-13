package gbagretsov.ghsearch.app;

import android.app.Application;
import gbagretsov.ghsearch.app.GitHubModel.GitHubApi;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * В этом классе определён метод для доступа к API GitHub.
 * Этот метод можно вызывать в любом месте приложения.
 * Найдено на просторах Хабра.
 */
public class App extends Application {
    private static GitHubApi gitHubApi;
    private Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/") // Базовая часть адреса
                .addConverterFactory(GsonConverterFactory.create()) // Конвертер, необходимый для преобразования JSON'а в объекты
                .build();
        gitHubApi = retrofit.create(GitHubApi.class); // Создаем объект, при помощи которого будем выполнять запросы
    }

    public static GitHubApi getApi() {
        return gitHubApi;
    }
}
