package by.bsuir.productlistapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ListView songsListView;

    public static Button nirvanaButton;
    public static Button grobButton;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (MainActivity.isNetworkAvailable()) MainActivity.songImageDownloader.clearQueue();
    }

    public static void quickFixFilterButtonsText(){
        nirvanaButton.setText("Nirvana (" + MainActivity.NIRVANA_SONGS_COUNT + ")");
        grobButton.setText("GrOb (" + MainActivity.GROB_SONGS_COUNT + ")");
    }

    public static void handleFilterButtons() {
        if (MainActivity.nirvanaIsSelectedFilter) {
            nirvanaButton.setBackgroundColor(Color.GREEN);
            grobButton.setBackgroundColor(Color.WHITE);
        } else {
            nirvanaButton.setBackgroundColor(Color.WHITE);
            grobButton.setBackgroundColor(Color.GREEN);
        }
        nirvanaButton.setTextColor(Color.BLACK);
        grobButton.setTextColor(Color.BLACK);

        nirvanaButton.setText("Nirvana (" + MainActivity.NIRVANA_SONGS_COUNT + ")");
        grobButton.setText("GrOb (" + MainActivity.GROB_SONGS_COUNT + ")");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        songsListView = view.findViewById(R.id.songsListView);
        filterList(MainActivity.nirvanaIsSelectedFilter);
        songsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Song selectedSong = (Song) songsListView.getItemAtPosition(position);
                Intent showDetail = new Intent(MainActivity.mainActivityContext, DetailActivity.class);
                showDetail.putExtra("id", selectedSong.getId() - 1);
                startActivity(showDetail);
            }
        });


        nirvanaButton = view.findViewById(R.id.NirvanaFilter);
        nirvanaButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                filterList(true);
                nirvanaButton.setBackgroundColor(Color.GREEN);
                grobButton.setBackgroundColor(Color.WHITE);
            }
        });

        grobButton = view.findViewById(R.id.GrobFilter);
        grobButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                filterList(false);
                nirvanaButton.setBackgroundColor(Color.WHITE);
                grobButton.setBackgroundColor(Color.GREEN);
            }
        });

        handleFilterButtons();

        if (!MainActivity.isNetworkAvailable()) {
            TextView internetConnectionLost = (TextView) view.findViewById(R.id.warningTextView);
            internetConnectionLost.setVisibility(View.VISIBLE);
        }

        return view;
    }


    private void filterList(boolean status) {
        MainActivity.nirvanaIsSelectedFilter = status;
        ArrayList<Song> filteredSongs = new ArrayList<>();

        for (Song song : MainActivity.songsList) {
            final String searchString = "nirvana";

            if (MainActivity.nirvanaIsSelectedFilter) {
                if (song.getArtist().toLowerCase(Locale.ROOT).contains(searchString)) {
                    filteredSongs.add(song);
                }
            } else {
                if (!song.getArtist().toLowerCase(Locale.ROOT).contains(searchString)) {
                    filteredSongs.add(song);
                }
            }
        }

        SongAdapter adapter = new SongAdapter(MainActivity.mainActivityContext, 0, filteredSongs);
        songsListView.setAdapter(adapter);
    }
}