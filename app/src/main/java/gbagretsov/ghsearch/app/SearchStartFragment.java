package gbagretsov.ghsearch.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import java.util.*;


/**
 * Фрагмент-вкладка с параметрами поиска. Также содержит floating action button,
 * по нажатию на которую и происходит поиск.
 */
public class SearchStartFragment extends Fragment {

    private AutoCompleteTextView queryEditText; // Поле для ввода запроса
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

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

        queryEditText = (AutoCompleteTextView) view.findViewById(R.id.query);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Получаем настройки, в которых будем хранить и из которых будем брать историю поиска
        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        // Получаем историю поиска
        Map<String, Long> prefsCopy = new HashMap<>(((Map<String, Long>) sharedPref.getAll()));
        prefsCopy = sortByValueDesc(prefsCopy);

        String[] history = prefsCopy.keySet().toArray(new String[prefsCopy.values().size()]);

        // В дополнение к показу вариантов при вводе символа добавлен listener на клик,
        // чтобы выпадающий список показывался сразу же
        final ArrayAdapter<String> adapter = new ArrayAdapter<>
                (getContext(), android.R.layout.simple_dropdown_item_1line, history);
        queryEditText.setAdapter(adapter);
        queryEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryEditText.showDropDown();
            }
        });
    }

    // Обработка нажатия на floating action button (сам listener устанавливается в методе onCreateView)
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

            // Сохраняем запрос в историю вместе с меткой времени (при повторном сохранении того же ключа
            // сохранится последняя запись с самой новой меткой)
            editor = sharedPref.edit();
            editor.putLong(q, Calendar.getInstance().getTimeInMillis());
            editor.apply();

            // Запускаем новую Activity и передаём запрос
            Intent intent = new Intent(context, SearchResultsActivity.class);
            intent.putExtra(SearchResultsActivity.QUERY, q);
            startActivity(intent);

        }
    };

    // Сортировка HashMap по убыванию значений. Используется для вывода истории.
    // Взято здесь: http://stackoverflow.com/questions/109383/sort-a-mapkey-value-by-values-java
    private static <K, V extends Comparable<? super V>> Map<K, V> sortByValueDesc(Map<K, V> map )
    {
        List<Map.Entry<K, V>> list =
                new LinkedList<>( map.entrySet() );
        Collections.sort( list, new Comparator<Map.Entry<K, V>>()
        {
            @Override
            public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
            {
                return ( o2.getValue() ).compareTo( o1.getValue() );
            }
        } );

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list)
        {
            result.put( entry.getKey(), entry.getValue() );
        }
        return result;
    }
}
