package com.cwl.barrelrace.barrelracegame.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cwl.barrelrace.barrelracegame.R;
import com.cwl.barrelrace.barrelracegame.model.Player;

import java.util.List;

public class HighScoresAdapter extends ArrayAdapter<Player> {
    private LayoutInflater layoutInflater;
    private int resource;
    private List<Player> players;
    private Context context;

    public HighScoresAdapter(Context context, int resource, List<Player> players) {
        super(context, resource, players);
        this.resource = resource;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.players = players;
        this.context = context;
    }
    /* Method for setting the content of the listView */
    public View getView(int position, View convertView, ViewGroup parents) {
        View view = convertView;
        ContactListViewHolder viewHolder;
        if(convertView == null) {
            view = layoutInflater.inflate(resource, null);
            viewHolder = new ContactListViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ContactListViewHolder) view.getTag();
        }
        Player player = players.get(position);

        if(player != null) {
            if(viewHolder.name != null) {
                viewHolder.name.setText(player.getName());
            }
            if(viewHolder.time != null) {
                viewHolder.time.setText(player.getTime());
            }
        }
        return view;
    }

    class ContactListViewHolder{
        public TextView name;
        public TextView time;

        public ContactListViewHolder(View view) {
            name = view.findViewById(R.id.high_score_player);
            time = view.findViewById(R.id.time_score);
        }
    }

}
