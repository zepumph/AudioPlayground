package mobilecomputing.com.audioplayground;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.io.IOException;


public class MainActivity extends Activity {

    public static final String ERROR = "ERROR";


    final String notes[] = {"A", "Bb", "B", "C", "Eb", "E", "F", "Gb", "G", "Ab"};
    String currentNote = "note_a";

    MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.note_a);

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

    }


    public void playStop(View view){
        final Button playButton = (Button) findViewById(R.id.btn_play);
        int note_id = this.getResources().getIdentifier(currentNote, "raw",
                this.getPackageName());
        try {
//            String path = getExternalFilesDir(null).toString() + "/";
            Log.d("MEDIA_PLAYER", "IN PATH:  " + path);

//            mp.setDataSource(;
        }
        catch (Resources.NotFoundException e){
            mp = MediaPlayer.create(MainActivity.this, R.raw.note_e);

            Log.e(ERROR, "Defaulting to note E:  " + e.getMessage(),e);

        }
//        Toast t = Toast.makeText(getApplicationContext(), currentNote, Toast.LENGTH_LONG);
//        t.show();

        if( playButton.getText().equals(getString(R.string.play))) {
            playButton.setText(R.string.stop);
            mp.start();
        }
        else if (playButton.getText().equals((getString(R.string.stop)))){
            playButton.setText(R.string.play);
            Log.d("MEDIA_PLAYER", "IN STOP:    ");

            mp.pause();
            mp.reset();
//            myMediaPlayer.reset();
        }
        else{
            Log.e(ERROR, "playbutton not set correctly");
        }
    }
}
