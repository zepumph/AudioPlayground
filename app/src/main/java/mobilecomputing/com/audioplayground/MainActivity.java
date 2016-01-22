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


public class MainActivity extends AppCompatActivity {

    public static final String ERROR = "ERROR";


    final String notes[] = {"A", "Bb", "B", "C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab"};
    String currentNote = "note_a";

    MediaPlayer mp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NumberPicker notePicker = (NumberPicker) findViewById(R.id.note_picker);
        notePicker.setMinValue(0);
        notePicker.setMaxValue(notes.length - 1);
        notePicker.setDisplayedValues(notes);
        NumberPicker.OnValueChangeListener notePickerListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                currentNote = "note_" + notes[newVal].toLowerCase();
            }
        };
        notePicker.setOnValueChangedListener(notePickerListener);
        mp = MediaPlayer.create(getApplicationContext(), R.raw.note_a);
    }


    public void playStop(View view){
        final Button playStopButton = (Button) findViewById(R.id.btn_play);

        if( playStopButton.getText().equals(getString(R.string.play))) {
            play(playStopButton);
        }
        else if (playStopButton.getText().equals((getString(R.string.stop)))){
            stop(playStopButton);
        }
        else{
            Log.e(ERROR, "playbutton not set correctly");
        }
    }

    public void play(Button playButton){
        int note_id = this.getResources().getIdentifier(currentNote, "raw",
                this.getPackageName());
        try {
            mp.reset();
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);

            AssetFileDescriptor afd = getApplicationContext().getResources().openRawResourceFd(note_id);
            mp.setDataSource( afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mp.setLooping(true);
            mp.prepare();
        }
        catch (IOException e){
            mp = MediaPlayer.create(MainActivity.this, R.raw.note_e);
            Log.e(ERROR, "Data source not set, defaulting to note E:  " + e.getMessage(),e);
        }

        playButton.setText(R.string.stop);
        mp.start();
    }
    public void stop(Button stopButton){
        stopButton.setText(R.string.play);

        //set volume fade, currently not working I think
        float speed = .05f;
        for (float vol= 1; vol>.2; vol -= speed){
            mp.setVolume( vol, vol);
        }
        mp.pause();
        mp.reset();
        mp.setVolume(1.f,1.f);
    }
}
