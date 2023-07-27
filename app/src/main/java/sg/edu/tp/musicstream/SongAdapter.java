package sg.edu.tp.musicstream;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter implements Filterable {
    Context ctx;
    ArrayList<Song> playlist;
    ArrayList<Song> playlistFiltered;
    Context context;

    public SongAdapter(Context ctx, ArrayList<Song> playlist) {
        this.ctx = ctx;
        this.playlist = playlist;
        this.playlistFiltered = new ArrayList<>(playlist);
        Log.d("SongAdapter", playlist.stream().map(Song::getTitle).reduce((s1, s2) -> s1 + " " + s2).toString());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_item, parent, false);
        return new SongViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SongViewHolder) {
            SongViewHolder h = (SongViewHolder) holder;
            Song song = playlistFiltered.get(position);
            h.title.setText(song.getTitle());
            h.artist.setText(song.getArtiste());
            h.thumbnail.setImageDrawable(ctx.getDrawable(song.getDrawable()));
            h.button.setOnClickListener(v -> {
                Intent intent = new Intent(ctx, PlaySongActivity.class);
                intent.putExtra("activity", "fav");
                intent.putExtra("index", SongCollection.playlist.searchSongById(song.getId()));
                intent.putExtra("playlist", playlistFiltered.stream().mapToInt(s -> SongCollection.playlist.searchSongById(s.getId())).toArray());
                ctx.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return playlistFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    filterResults.values = playlist;
                } else {
                    ArrayList<Song> filteredList = new ArrayList<>();
                    for (Song song : playlist) {
                        if (song.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(song);
                        }
                    }
                    filterResults.values = filteredList;
                }
                Log.d("temasek", "The filtered list size is: " + ((ArrayList<Song>) filterResults.values).size());

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                Log.d("temasek", "The filtered list size is: " + ((ArrayList<Song>) filterResults.values).size());
                playlistFiltered.clear();
                playlistFiltered.addAll((ArrayList<Song>) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }

    class SongViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView title;
        TextView artist;
        Button button;

        public SongViewHolder(@NonNull View v) {
            super(v);

            thumbnail = v.findViewById(R.id.thumbnail);
            title = v.findViewById(R.id.title);
            artist = v.findViewById(R.id.artist);
            button = v.findViewById(R.id.favbtn);
        }
    }
}

