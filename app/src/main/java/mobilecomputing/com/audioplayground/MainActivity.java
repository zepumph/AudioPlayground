package mobilecomputing.com.audioplayground;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    public static final String ERROR = "ERROR";


    private final static String NOTES[] = {"A", "Bb", "B", "C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab"};
    String currentNote = "note_a";
    int currentBPM = 100;

    MediaPlayer mp_tone;
    MediaPlayer mp_met;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initiateNotePicker();
        initiateBPMPicker();
        mp_tone = MediaPlayer.create(getApplicationContext(), R.raw.note_a);
        mp_met = MediaPlayer.create(getApplicationContext(), R.raw.tic);
        mp_met.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
//                mp.reset();
//                AssetFileDescriptor afd = getApplicationContext().getResources().openRawResourceFd(R.raw.tic);
//
//                try {
//                    mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
//            //        mp.prepare();
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
            }
        });

    }

    private void initiateBPMPicker() {
        NumberPicker np = (NumberPicker) findViewById(R.id.bpm_picker);
        final String bpms[] = new String[20];
        for( int i = 0; i < 20; i ++){
            int temp = (i * 10) +30;
            bpms[i] =  "" + temp;
        }

        np.setMinValue(0);
        np.setMaxValue(19);
        np.setDisplayedValues(bpms);
        NumberPicker.OnValueChangeListener bpmPickerListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                currentBPM = Integer.parseInt(bpms[newVal]);
            }
        };
        np.setOnValueChangedListener(bpmPickerListener);
    }
    public void initiateNotePicker(){
        NumberPicker notePicker = (NumberPicker) findViewById(R.id.note_picker);
        notePicker.setMinValue(0);
        notePicker.setMaxValue(NOTES.length - 1);
        notePicker.setDisplayedValues(NOTES);
        NumberPicker.OnValueChangeListener notePickerListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                currentNote = "note_" + NOTES[newVal].toLowerCase();
            }
        };
        notePicker.setOnValueChangedListener(notePickerListener);
    }





    public void toggleTone(View view){
        final Button playStopButton = (Button) findViewById(R.id.btn_tone);

        if( playStopButton.getText().equals(getString(R.string.play))) {
            playTone(playStopButton);
        }
        else if (playStopButton.getText().equals((getString(R.string.stop)))){
            stop(playStopButton, mp_tone);
        }
        else{
            Log.e(ERROR, "playbutton not set correctly");
        }
    }

    public void playTone(Button playButton){
        int note_id = this.getResources().getIdentifier(currentNote, "raw",
                this.getPackageName());
        try {
            mp_tone.reset();
            mp_tone.setAudioStreamType(AudioManager.STREAM_MUSIC);

            AssetFileDescriptor afd = getApplicationContext().getResources().openRawResourceFd(note_id);
            mp_tone.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mp_tone.setLooping(true);
            mp_tone.prepare();
        }
        catch (IOException e){
            mp_tone = MediaPlayer.create(MainActivity.this, R.raw.note_e);
            Log.e(ERROR, "Data source not set, defaulting to note E:  " + e.getMessage(),e);
        }

        playButton.setText(R.string.stop);
        mp_tone.start();
    }
    public void stop(Button stopButton, MediaPlayer mp){
        stopButton.setText(R.string.play);

        //set volume fade, currently not working I think
        float speed = .05f;
        for (float vol= 1; vol>.2; vol -= speed){
            mp_tone.setVolume(vol, vol);
        }
        mp.pause();
        mp.reset();
        mp.setVolume(1.f, 1.f);
    }

    public void toggleMetronome(View view){
        final Button toggleMet = (Button) findViewById(R.id.btn_metronome);
        final AssetFileDescriptor afd = getApplicationContext().getResources().openRawResourceFd(R.raw.tic);

        Timer timer = new Timer("MetronomeTimer", true);
        TimerTask tone = new TimerTask(){
            @Override
            public void run(){
                try {
                    mp_met.start();
                    mp_met.reset();
                    mp_met.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                    mp_met.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };

        if( toggleMet.getText().equals(getString(R.string.start))) {
            toggleMet.setText(R.string.stop);
            timer.scheduleAtFixedRate(tone, 0, 1000 / currentBPM + 1);
        }

        else if (toggleMet.getText().equals((getString(R.string.stop)))){

            toggleMet.setText((R.string.start));
            tone.cancel();
            timer.cancel();
        }
        else{
            Log.e(ERROR, "Metronome play button not set correctly");
        }
    }
}
