package sg.edu.tp.musicstream;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

public class PlaySongActivity extends AppCompatActivity {
    public static ArrayList<Song> fav = new ArrayList<>();
    private Playlist playlist;

    private String title = "";
    private String artiste = "";
    private String fileLink = "";
    private int drawable;
    private int currentIndex = -1;
    private MediaPlayer player = new MediaPlayer();
    private ImageButton btnPlayPause = null;

    SeekBar seekbar;
    Handler handler = new Handler(Looper.getMainLooper());
    ImageButton repeatBtn;
    ImageButton shuffleBtn;
    Boolean repeatFlag = false;
    Boolean shuffleFlag = false;
    ArrayList<Integer> history = new ArrayList<>();
    ImageView backBtn;
    String activityFrom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ImageButton add = findViewById(R.id.add1);
        btnPlayPause = findViewById(R.id.btnPlayPause);
        Bundle songData = this.getIntent().getExtras();
        currentIndex = songData.getInt("index");
        activityFrom = songData.getString("activity");
        Log.d("PlaySongActivity", "index is " + currentIndex);
        playlist = new Playlist(songData.getIntArray("playlist"));
        displaySongBasedOnIndex();
        playSong(fileLink);
        seekbar = findViewById(R.id.seekBar);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (player != null && player.isPlaying()) {
                    player.seekTo(seekBar.getProgress());

                }

            }
        });

        repeatBtn = findViewById(R.id.repeatBtn);
        shuffleBtn = findViewById(R.id.shuffleBtn);
        handler.post(new ProgressBarUpdater());
    }

    private void removeSaved() {

    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = gson.toJson(fav);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("favlist", json);
        editor.apply();
    }


    public void repeatSong(View view) {

        if (repeatFlag) {
            repeatBtn.setImageResource(R.drawable.not_loop);
            Toast.makeText(this, "Loop off", Toast.LENGTH_SHORT).show();
        } else {
            repeatBtn.setImageResource(R.drawable.loop);
            Toast.makeText(this, "Loop on", Toast.LENGTH_SHORT).show();
        }
        repeatFlag = !repeatFlag;

    }

    public void shuffleSong(View view) {
        if (shuffleFlag) {
            shuffleBtn.setImageResource(R.drawable.play_order);
            Toast.makeText(this, "Shuffle off", Toast.LENGTH_SHORT).show();
        } else {
            shuffleBtn.setImageResource(R.drawable.shuffle);
            Toast.makeText(this, "Shuffle on", Toast.LENGTH_SHORT).show();
        }
        shuffleFlag = !shuffleFlag;

    }

    public void addToPlaylist(View view) {
        Song song = playlist.getCurrentSong(currentIndex);
        if (fav.contains(song)) {

            fav.remove(song);
            return;
        }
        fav.add(song);
    }

    class ProgressBarUpdater implements Runnable {
        @Override
        public void run() {
            if (player != null && player.isPlaying()) {
                seekbar.setMax(player.getDuration());//set seekbar length to represent duration of song
                seekbar.setProgress(player.getCurrentPosition());
            }
            handler.postDelayed(this, 1000);
        }
    }

    public void displaySongBasedOnIndex() {
        Song song = playlist.getCurrentSong(currentIndex);
        title = song.getTitle();
        artiste = song.getArtiste();
        fileLink = song.getFileLink();
        drawable = song.getDrawable();
        TextView txtTitle = findViewById(R.id.txtSongTitle);
        txtTitle.setText(title);
        TextView txtArtiste = findViewById(R.id.txtArtist);
        txtArtiste.setText(artiste);
        ImageView iCoverArt = findViewById(R.id.thumbNail);
        iCoverArt.setImageResource(drawable);
        ImageButton add = findViewById(R.id.add1);
        Song firstMatch = fav.stream().filter(s -> song.getTitle().equals(s.getTitle()) && song.getArtiste().equals(s.getArtiste())).findAny().orElse(null);
        if (firstMatch != null) {
            add.setImageResource(R.drawable.liked);
        } else {
            add.setImageResource(R.drawable.like);
        }
        add.setOnClickListener(v -> {
            Song match = fav.stream().filter(s -> song.getTitle().equals(s.getTitle()) && song.getArtiste().equals(s.getArtiste())).findAny().orElse(null);
            if (match != null) {
                Log.d("PlaySongActivity", match.getTitle());
                fav.remove(match);
                saveData();
                add.setImageResource(R.drawable.like);
                Toast.makeText(this, "Removed from favourites", Toast.LENGTH_SHORT).show();
                Log.d("PlaySongActivity", fav.stream().map(s -> s.getTitle()).reduce((s1, s2) -> s1 + " " + s2).toString());

                return;
            }
            fav.add(song);
            saveData();
            add.setImageResource(R.drawable.liked);
            Toast.makeText(this, "Added to favourites", Toast.LENGTH_SHORT).show();
            Log.d("PlaySongActivity", fav.stream().map(s -> s.getTitle()).reduce((s1, s2) -> s1 + " " + s2).toString());
        });

    }

    public void playSong(String songUrl) {
        try {
            player.reset();
            player.setDataSource(songUrl);
            player.prepare();
            player.start();

            gracefullyStopsWhenMusicEnds();

            btnPlayPause.setImageResource(R.drawable.pausebutton);
            setTitle(title);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playPrevious(View view) {
        if (shuffleFlag && !history.isEmpty()) {
            currentIndex = history.remove(history.size() - 1);
        } else {
            currentIndex = playlist.getPrevSong(currentIndex);
        }
        Log.d("temasek", "After playPrevious, the index is now :" + currentIndex);
        displaySongBasedOnIndex();
        playSong(fileLink);

    }

    public void playNext(View view) {
        if (shuffleFlag) {
            history.add(currentIndex);
            currentIndex = playlist.getRandomSong(currentIndex);
        } else {
            currentIndex = playlist.getNextSong(currentIndex);
        }
        displaySongBasedOnIndex();
        playSong(fileLink);
    }

    public void playOrPauseMusic(View view) {
        if (player.isPlaying()) {
            player.pause();
            btnPlayPause.setImageResource(R.drawable.playbutton);
        } else {
            player.start();
            seekbar.setMax(player.getDuration());//set seekbar length to represent duration of song
            btnPlayPause.setImageResource(R.drawable.pausebutton);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }

    @Override
    public void finish() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }

        Class previous;
        switch (activityFrom) {
            case "fav":
                previous = favActivity.class;
                break;
            case "main":
                previous = MainActivity.class;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + activityFrom);
        }
        Intent intent = new Intent(this, previous);
        startActivity(intent);
    }

    private void gracefullyStopsWhenMusicEnds() {

        Context ctx = this;
        player.setOnCompletionListener(mp -> {
            if (repeatFlag) {
                playOrPauseMusic(null);

            } else {
                btnPlayPause.setImageResource(R.drawable.playbutton);
            }
            Toast.makeText(ctx, "Song ended", Toast.LENGTH_SHORT).show();
        });
    }
}