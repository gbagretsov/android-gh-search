package gbagretsov.ghsearch.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import gbagretsov.ghsearch.app.GitHubModel.GitHubUser;
import gbagretsov.ghsearch.app.UsersDisplay.UsersAdapter;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Фрагмент-вкладка с избранными пользователями (закладки)
 */
public class FavouriteFragment extends Fragment {

    public static String FAVOURITE = "gbagretsov.ghsearch.app.FAVOURITE";
    private HashMap<String, String> favouriteUsers; // логин -> аватар
    private RecyclerView recyclerView;
    private SharedPreferences prefs;
    ArrayList<GitHubUser> usersList;

    public FavouriteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);

        // Устанавливаем отображение списка избранных пользователей
        favouriteUsers = new HashMap<>();

        recyclerView = (RecyclerView) view.findViewById(R.id.favourite_recycle_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        // Получаем избранных пользователей
        prefs = view.getContext().getSharedPreferences(FAVOURITE, 0);
        favouriteUsers = (HashMap<String, String>) prefs.getAll();

        usersList = new ArrayList<>();
        for (String s : favouriteUsers.keySet()) {
            GitHubUser gitHubUser = new GitHubUser();
            gitHubUser.setLogin(s);
            gitHubUser.setAvatarUrl(favouriteUsers.get(s));
            usersList.add(gitHubUser);
        }

        UsersAdapter adapter = new UsersAdapter(usersList, getActivity());
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Заново создаём список
        prefs = getActivity().getSharedPreferences(FAVOURITE, 0);
        favouriteUsers = (HashMap<String, String>) prefs.getAll();
        usersList = new ArrayList<>();
        for (String s : favouriteUsers.keySet()) {
            GitHubUser gitHubUser = new GitHubUser();
            gitHubUser.setLogin(s);
            gitHubUser.setAvatarUrl(favouriteUsers.get(s));
            usersList.add(gitHubUser);
        }
        // TODO: список не обновляется
    }
}
