package np.com.aawaz.csitentrance.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import np.com.aawaz.csitentrance.R;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {
    private ArrayList<Integer> scores;
    private ArrayList<String> names;
    private LayoutInflater inflater;


    public LeaderboardAdapter(Context context, ArrayList<String> names, ArrayList<Integer> scores) {
        inflater = LayoutInflater.from(context);
        this.names = names;
        this.scores = scores;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.leader_board_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(names.get(position));
        holder.score.setText(scores.get(position) + "");
        holder.numbering.setText(position + 4 + ". ");
    }

    @Override
    public int getItemCount() {
        return names.size() > 7 ? 7 : names.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, score, numbering;

        public ViewHolder(View itemView) {
            super(itemView);
            numbering = itemView.findViewById(R.id.numbering);
            name = itemView.findViewById(R.id.scoreboardName);
            score = itemView.findViewById(R.id.scoreboardScore);
        }
    }
}
