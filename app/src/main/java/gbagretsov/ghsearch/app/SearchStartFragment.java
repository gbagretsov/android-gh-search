package gbagretsov.ghsearch.app;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import gbagretsov.ghsearch.app.GitHubModel.GitHubUsersResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchStartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchStartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchStartFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private EditText queryEditText; // Поле для ввода запроса

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment SearchStartFragment.
     */
    public static SearchStartFragment newInstance() {
        return new SearchStartFragment();
    }

    public SearchStartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_start, container, false);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(fabClickListener);

        queryEditText = (EditText) view.findViewById(R.id.query);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Разобраться с интерфейсом
        public void onFragmentInteraction(Uri uri);
    }

    // Обработка нажатия на floating action button
    private View.OnClickListener fabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO: Добавлять дополнительные параметры к запросу
            String q = queryEditText.getText().toString();

            final Context context = v.getContext();

            if (q.isEmpty()) {
                Toast.makeText(context, getText(R.string.query_is_empty), Toast.LENGTH_SHORT).show();
                return;
            }

            // Выполняем запрос
            // TODO: перенести выполнение запроса в новую Activity
            App.getApi().getData(q).enqueue(new Callback<GitHubUsersResponse>() {
                @Override
                public void onResponse(Call<GitHubUsersResponse> call, Response<GitHubUsersResponse> response) {
                    // TODO: Данные успешно пришли, но надо проверить response.body() на null
                    if (response.body() != null &&
                            response.body().getGitHubUsers() != null &&
                           !response.body().getGitHubUsers().isEmpty()) {
                        Log.d("API", response.body().getGitHubUsers().get(0).getLogin());
                    } else {
                        // Ничего не найдено
                        Toast.makeText(context, getText(R.string.users_not_found), Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(Call<GitHubUsersResponse> call, Throwable t) {
                    // Произошла ошибка
                    Toast.makeText(context, getText(R.string.error_occured), Toast.LENGTH_SHORT).show();
                }
            });

        }
    };
}
