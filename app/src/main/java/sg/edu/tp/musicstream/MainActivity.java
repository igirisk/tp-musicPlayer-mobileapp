package sg.edu.tp.musicstream;
//Given

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Integer> favplaylist =new ArrayList<Integer>();
    static boolean loadedData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        if (!loadedData) {
            loadData();
            loadedData = true;
        }
    }

    public void sendDataToActivity(int index){
        Intent intent = new Intent(this, PlaySongActivity.class);
        intent.putExtra("activity","main");
        intent.putExtra("index", index);
        intent.putExtra("playlist", SongCollection.playlist.indices());
        startActivity(intent);
    }

    public void handleSelection(View myView) {
        if (myView.getId() == -1) {
            return;
        }
        String resourceId = getResources().getResourceEntryName(myView.getId());
        Log.d("temasek", "The index in the array for this song is :" + resourceId);
        int currentArrayIndex = SongCollection.playlist.searchSongById(resourceId);
        Log.d("temasek", "The index in the array for this song is :" + currentArrayIndex);
        sendDataToActivity(currentArrayIndex);
    }
    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("favlist", null);
        Type type = new TypeToken<ArrayList<Song>>() {}.getType();
        ArrayList<Song> fromGson = gson.fromJson(json, type);
        if (fromGson != null) {
            PlaySongActivity.fav.clear();
            PlaySongActivity.fav.addAll(fromGson);
        }

//        if (PlaySongActivity.fav == null) {
//            PlaySongActivity.fav = new ArrayList<>();
//        }
    }

    public void gotoFavoriteActivity(View view) {
        Intent intent = new Intent(this, favActivity.class);
        startActivity(intent);
    }
}