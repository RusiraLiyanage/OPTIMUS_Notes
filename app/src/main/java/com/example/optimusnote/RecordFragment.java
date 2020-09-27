package com.example.optimusnote;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecordFragment extends Fragment implements View.OnClickListener {

    private NavController navController;
    private ImageButton listBtn;
    private ImageButton redBtn;
    private TextView fileNameTxt;


    private boolean isRec = false;

    private String recPermission = Manifest.permission.RECORD_AUDIO;

    private int PERMISSION_CODE = 21;

    private MediaRecorder mediaRecorder;

    private Chronometer timer;

    private String recFile;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecordFragment newInstance(String param1, String param2) {
        RecordFragment fragment = new RecordFragment();
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
        return inflater.inflate(R.layout.fragment_record, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        listBtn = view.findViewById(R.id.rec_list);
        redBtn = view.findViewById(R.id.record_btn);
        timer = view.findViewById(R.id.recordTimer);
        fileNameTxt = view.findViewById(R.id.rec_fileName);

        listBtn.setOnClickListener(this);
        redBtn.setOnClickListener(this);
        //timer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rec_list:

                if(isRec){
                    AlertDialog.Builder alterDialog = new AlertDialog.Builder(getContext());
                    alterDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            navController.navigate(R.id.action_recordFragment_to_audiolistFragment);
                            isRec = false;
                        }
                    });
                    alterDialog.setNegativeButton("Cancel",null);
                    alterDialog.setTitle("Audio still Recording");
                    alterDialog.setMessage("Are you sure you want to stop the recording");
                    alterDialog.create().show();

                }else{
                    navController.navigate(R.id.action_recordFragment_to_audiolistFragment);
                }
            break;

            case R.id.record_btn:
                if(isRec){
                    //stop Recording
                    stopRecording();
                    redBtn.setImageDrawable(getResources().getDrawable(R.drawable.record_btn_stopped));
                    isRec = false;
                }
                else{
                    //start recording
                    if(checkPermission()){
                        startRecording();
                        redBtn.setImageDrawable(getResources().getDrawable(R.drawable.record_btn_recording));
                        isRec=true;}
                }
                break;
        }
    }

    private void startRecording() {
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();
        String recPath = getActivity().getExternalFilesDir("/").getAbsolutePath();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss", Locale.CANADA);
        Date now = new Date();
        recFile = "Recording_"+formatter.format(now)+".3gpp";
        fileNameTxt.setText("Recording,File Name:"+recFile);

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(recPath+"/"+recFile);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaRecorder.start();
    }

    private void stopRecording() {
        timer.stop();
        fileNameTxt.setText("Recording Stopped,File Saved:"+recFile);
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
    }
    private boolean checkPermission() {
        if(ActivityCompat.checkSelfPermission(getContext(),recPermission ) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else{

            ActivityCompat.requestPermissions(getActivity(), new String[]{recPermission},PERMISSION_CODE);
            return false;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(isRec) {
            stopRecording();
        }
    }
}