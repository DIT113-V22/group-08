package com.quinstedt.islandRush.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.quinstedt.islandRush.R;
import com.quinstedt.islandRush.database.PlayerScore;

import java.text.SimpleDateFormat;
import java.util.Date;

/*Source:
 https://www.geeksforgeeks.org/how-to-perform-crud-operations-in-room-database-in-android**/

public class PlayerScoreRVAdapter extends ListAdapter<PlayerScore, PlayerScoreRVAdapter.ViewHolder> {

    // creating a variable for on item click listener.
    private OnItemClickListener listener;

    // creating a constructor class for our adapter class.
    public PlayerScoreRVAdapter() {
        super(DIFF_CALLBACK);
    }

    // creating a call back for item of recycler view.
    private static final DiffUtil.ItemCallback<PlayerScore> DIFF_CALLBACK = new DiffUtil.ItemCallback<PlayerScore>() {
        @Override
        public boolean areItemsTheSame(PlayerScore oldItem, PlayerScore newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(PlayerScore oldItem,PlayerScore newItem) {
            // below line is to check the player name and race duration.
            return oldItem.getPlayerName().equals(newItem.getPlayerName()) &&
                    (oldItem.getTime()==newItem.getTime());
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // below line is use to inflate our layout
        // file for each item of our recycler view.
        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.playerscore_rv_item, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // below line of code is use to set data to
        // each item of our recycler view.
        PlayerScore playerScore = getPlayerAt(position);
        holder.playerNameTV.setText(playerScore.getPlayerName());
        int time= playerScore.getTime();
        String timeFormat = convertToTime(time);
        holder.raceDurationTV.setText(timeFormat);
    }

    private String convertToTime(int time) {
        SimpleDateFormat simple = new SimpleDateFormat("mm:ss");
        long millis = time * 1000;
        Date result = new Date(millis);
        return String.valueOf(simple.format(result));
    }

    // creating a method to get course modal for a specific position.
    public PlayerScore getPlayerAt(int position) {
        return getItem(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // view holder class to create a variable for each view.
        TextView playerNameTV, raceDurationTV;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing each view of our recycler view.
            playerNameTV = itemView.findViewById(R.id.idTVPlayerName);
            raceDurationTV = itemView.findViewById(R.id.idTVRaceDuration);

            // adding on click listener for each item of recycler view.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // inside on click listener we are passing
                    // position to our item of recycler view.
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(PlayerScore playerScore);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
