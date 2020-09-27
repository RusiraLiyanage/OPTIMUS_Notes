package com.example.optimusnote;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.File;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AudiolistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AudiolistFragment extends Fragment implements AudioListAdapter.onItemListClick{

    private ConstraintLayout playerSheet;
    private BottomSheetBehavior bottomSheetBehavior;
    private RecyclerView audioList;
    private File[] allFiles;

    private AudioListAdapter audioListAdapter;

    private MediaPlayer mediaPlayer = null;
    private boolean isPlaying = false;

    private File fileToPlay = null;

    //UI Elements
    private ImageButton playBtn;
    private TextView playerHeader;
    private TextView playerFileName;

    private SeekBar playerSeekB;
    private Handler seekBarHandler;
    private Runnable updateSeek;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AudiolistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AudiolistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AudiolistFragment newInstance(String param1, String param2) {
        AudiolistFragment fragment = new AudiolistFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_audiolist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        playerSheet = view.findViewById(R.id.player_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(playerSheet);
        audioList = view.findViewById(R.id.rec_list_view);

        playBtn = view.findViewById(R.id.play_btn);
        playerHeader = view.findViewById(R.id.player_header_title);
        playerFileName = view.findViewById(R.id.file_name);

        playerSeekB = view.findViewById(R.id.player_Sb);

        String path = getActivity().getExternalFilesDir("/").getAbsolutePath();
        File directory = new File(path);
        allFiles = directory.listFiles();

        audioListAdapter = new AudioListAdapter(allFiles,this);
        audioList.setHasFixedSize(true);
        audioList.setLayoutManager(new LinearLayoutManager(getContext()));
        audioList.setAdapter(audioListAdapter);


        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState == BottomSheetBehavior.STATE_COLLAPSED){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPlaying){
                    pauseAudio();
                }else{
                    if(fileToPlay != null){
                        resumeAudio();
                    }

                }
            }
        });

        playerSeekB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                pauseAudio();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(fileToPlay !=null){
                    int progress = seekBar.getProgress();
                    mediaPlayer.seekTo(progress);
                    resumeAudio();
                }
            }
        });
    }

    @Override
    public void onClickListner(File file, int position) {
        //Log.d("PLAY_LOG","file_playing"+file.getName());
        if(isPlaying){
            stopAudio();

            playAudio(fileToPlay);

        }
        else{
            fileToPlay = file;
            playAudio(fileToPlay);

        }
    }

    private void pauseAudio(){
        mediaPlayer.pause();
        isPlaying = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            playBtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.player_play_btn,null));
        }
        seekBarHandler.removeCallbacks(updateSeek);
    }

    private void resumeAudio(){
        mediaPlayer.start();
        isPlaying =true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            playBtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.player_pause_btn,null));
        }
        updateRunnable();
        seekBarHandler.postDelayed(updateSeek,0);
    }

    private void stopAudio() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            playBtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.player_play_btn,null));
        }
        playerHeader.setText("stop");
        isPlaying = false;
        mediaPlayer.stop();
        seekBarHandler.removeCallbacks(updateSeek);
    }


    private void playAudio(File fileToPlay) {

        mediaPlayer = new MediaPlayer();

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        try {
            mediaPlayer.setDataSource(fileToPlay.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            playBtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.player_pause_btn,null));
        }
        playerFileName.setText(fileToPlay.getName());
        playerHeader.setText("playing");

        //playTheAudio
        isPlaying=true;

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stopAudio();
                playerHeader.setText("Finished");
            }
        });

        playerSeekB.setMax(mediaPlayer.getDuration());

        seekBarHandler = new Handler();
        updateRunnable();
        seekBarHandler.postDelayed(updateSeek,0);
    }

    private void updateRunnable() {
        updateSeek = new Runnable() {
            @Override
            public void run() {
                playerSeekB.setProgress(mediaPlayer.getCurrentPosition());
                seekBarHandler.postDelayed(this,500);
            }
        };
    }

    @Override
    public void onStop() {
        super.onStop();
        if(isPlaying){
            stopAudio();
        }
    }
}