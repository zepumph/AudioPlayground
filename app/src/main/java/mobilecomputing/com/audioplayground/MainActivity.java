package mobilecomputing.com.audioplayground;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import java.io.IOException;


public class MainActivity extends Activity {

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
        final Button playButton = (Button) findViewById(R.id.btn_play);

        if( playButton.getText().equals(getString(R.string.play))) {
            int note_id = this.getResources().getIdentifier(currentNote, "raw",
                    this.getPackageName());
            try {
                mp.reset();
                mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
    //            String path = getExternalFilesDir(null).toString() + "/";
    //            Log.d("MEDIA_PLAYER", "IN PATH:  " + path);
//                String path = "android.resource://"+getPackageName()+"/raw/" ;
//                Log.d("MEDIA_PLAYER" , "path: " + getPackageName() + currentNote);
                String resourcePath = "mobilecomputing.com.audioplayground/raw/" + currentNote + ".mp3";
                AssetFileDescriptor afd = getApplicationContext().getResources().openRawResourceFd(note_id);
                mp.setDataSource( afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                mp.prepare();
            }
            catch (IOException e){
                mp = MediaPlayer.create(MainActivity.this, R.raw.note_e);

                Log.e(ERROR, "Data source not set, defaulting to note E:  " + e.getMessage(),e);

            }
    //        Toast t = Toast.makeText(getApplicationContext(), currentNote, Toast.LENGTH_LONG);
    //        t.show();


                playButton.setText(R.string.stop);
                Log.d("MEDIA_PLAYER", "IN STart:    ");

                mp.start();
        }
        else if (playButton.getText().equals((getString(R.string.stop)))){
            playButton.setText(R.string.play);
            Log.d("MEDIA_PLAYER", "IN STOP:    ");

            //set volume fade
            float speed = .05f;
            for (float vol= 1; vol>.5; vol -= speed){
                mp.setVolume( vol, vol);
            }
            mp.pause();
            mp.reset();
//            myMediaPlayer.reset();
        }
        else{
            Log.e(ERROR, "playbutton not set correctly");
        }
    }
}
