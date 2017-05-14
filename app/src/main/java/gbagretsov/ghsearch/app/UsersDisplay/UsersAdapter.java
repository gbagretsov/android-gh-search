package gbagretsov.ghsearch.app.UsersDisplay;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import gbagretsov.ghsearch.app.GitHubModel.GitHubUser;
import gbagretsov.ghsearch.app.R;

import java.util.List;

/**
 * Этот класс управляет отображением списка пользователей
 */
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    private List<GitHubUser> users;
    private final Context context;

    public UsersAdapter(List<GitHubUser> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        // Получаем нужного пользователя и отображаем информацию о нём
        GitHubUser user = users.get(i);

        Picasso.with(context).load(user.getAvatarUrl()).fit()
                //.placeholder(R.drawable.user_placeholder) // TODO: placeholder
                //.error(R.drawable.user_placeholder_error)
                .transform(new CircularTransformation(0))
                .into(viewHolder.avatar);
        viewHolder.login.setText(user.getLogin());
        // TODO: полное имя, количество репозиториев, followers
        viewHolder.fullName.setText("Full name here");
    }

    @Override
    public int getItemCount() {
        return users == null ? 0 : users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView avatar;
        private TextView login;
        private TextView fullName;

        public ViewHolder(View itemView) {
            super(itemView);
            avatar   = (ImageView) itemView.findViewById(R.id.user_avatar);
            login    = (TextView)  itemView.findViewById(R.id.user_login);
            fullName = (TextView)  itemView.findViewById(R.id.full_name);
        }
    }

}
