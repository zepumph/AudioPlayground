package mobilecomputing.com.audioplayground;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    final String notes[] = {"A", "A#/Bb", "B", "C", "D#/Eb", "E", "F", "F#/Gb", "G", "G#/Ab"};
    String currentNote = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView txtPickerOutput = (TextView) findViewById(R.id.txtOutput);


        NumberPicker notePicker = (NumberPicker) findViewById(R.id.notePicker);
        notePicker.setMinValue(0);
        notePicker.setMaxValue(notes.length - 1);
        notePicker.setDisplayedValues(notes);
        NumberPicker.OnValueChangeListener notePickerListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                txtPickerOutput.setText("Value: " + notes[newVal]);
            }

        };


        notePicker.setOnValueChangedListener(notePickerListener);

        Button playButton = (Button) findViewById(R.id.btn_play);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MediaPlayer myMediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.note_e);
                myMediaPlayer.start();
            }
        });
    }
}
