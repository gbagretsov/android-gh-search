package gbagretsov.ghsearch.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;


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

            // Запускаем новую Activity и передаём запрос
            // TODO: при возвращении текущая Activity создаётся заново
            Intent intent = new Intent(context, SearchResultsActivity.class);
            intent.putExtra(SearchResultsActivity.QUERY, q);
            startActivity(intent);

        }
    };
}