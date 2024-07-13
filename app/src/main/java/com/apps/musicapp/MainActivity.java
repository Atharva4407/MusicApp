package com.apps.musicapp;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private TextView songTitle;
    private ImageView songImage;
    private SeekBar seekBar;
    private Button prevButton, playPauseButton, nextButton, logoutButton, shuffleButton;
    private MediaPlayer mediaPlayer;
    private ArrayList<Integer> songs;
    private ArrayList<Integer> songImages;
    private int currentSongIndex = 0;
    private Handler handler = new Handler();
    private Spinner playbackSpeedSpinner;
    private ArrayAdapter<CharSequence> playbackSpeedAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        songTitle = findViewById(R.id.songTitle);
        songImage = findViewById(R.id.songImage);
        seekBar = findViewById(R.id.seekBar);
        prevButton = findViewById(R.id.prevButton);
        playPauseButton = findViewById(R.id.playPauseButton);
        nextButton = findViewById(R.id.nextButton);
        logoutButton = findViewById(R.id.logoutButton);
        shuffleButton = findViewById(R.id.shuffleButton);
        playbackSpeedSpinner = findViewById(R.id.playbackSpeedSpinner);

        // Initialize arrays
        songs = new ArrayList<>();
        Collections.addAll(songs, R.raw.sample_audio, R.raw.song1, R.raw.one_piece_grand_line,R.raw.song3,R.raw.song4);

        songImages = new ArrayList<>();
        Collections.addAll(songImages, R.drawable.songimg1, R.drawable.songmg2, R.drawable.songimg3_1, R.drawable.songimg7, R.drawable.common_song_image);

        // Initialize playback speed spinner
        playbackSpeedAdapter = ArrayAdapter.createFromResource(this,
                R.array.playback_speed_options, android.R.layout.simple_spinner_item);
        playbackSpeedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        playbackSpeedSpinner.setAdapter(playbackSpeedAdapter);

        // Set listener for playback speed changes
        playbackSpeedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String speed = (String) parent.getItemAtPosition(position);
                setPlaybackSpeed(speed);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Initialize MediaPlayer with the first song
        mediaPlayer = MediaPlayer.create(this, songs.get(currentSongIndex));
        seekBar.setMax(mediaPlayer.getDuration());
        updateUI();
        updateSeekBar();

        // Set click listeners for playback controls
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    playPauseButton.setText("Play");
                } else {
                    mediaPlayer.start();
                    playPauseButton.setText("Pause");
                    updateSeekBar();
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNextSong();
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPreviousSong();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement logout functionality here
                // For example, redirect to login screen or perform logout action
                // This depends on your application's authentication flow
                // For demo purposes, you can finish() the activity
                finish();
            }
        });

        shuffleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shuffleSongs();
            }
        });

        // Set completion listener to play the next song when current song completes
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playNextSong();
            }
        });

        // SeekBar change listener
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    // Method to play the next song
    private void playNextSong() {
        if (currentSongIndex < songs.size() - 1) {
            currentSongIndex++;
        } else {
            currentSongIndex = 0;
        }
        changeSong();
    }

    // Method to play the previous song
    private void playPreviousSong() {
        if (currentSongIndex > 0) {
            currentSongIndex--;
        } else {
            currentSongIndex = songs.size() - 1;
        }
        changeSong();
    }

    // Method to change the song
    private void changeSong() {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = MediaPlayer.create(this, songs.get(currentSongIndex));
        setPlaybackSpeed(getSelectedPlaybackSpeed()); // Apply selected playback speed
        seekBar.setMax(mediaPlayer.getDuration());
        updateUI();
        updateSeekBar();
        mediaPlayer.start();
        playPauseButton.setText("Pause");
    }

    // Method to update UI elements with current song details
    // Method to update UI elements with current song details
    private void updateUI() {
        String title = "Playing song " + (currentSongIndex + 1);
        songTitle.setText(title);
        songImage.setImageResource(songImages.get(currentSongIndex));
    }


    // Method to update SeekBar progress
    private void updateSeekBar() {
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        if (mediaPlayer.isPlaying()) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateSeekBar();
                }
            }, 1000);
        }
    }

    // Method to set playback speed
    private void setPlaybackSpeed(String speed) {
        float playbackSpeed = 1.0f; // Default normal speed
        switch (speed) {
            case "0.5x":
                playbackSpeed = 0.5f;
                break;
            case "1.0x":
                playbackSpeed = 1.0f;
                break;
            case "1.5x":
                playbackSpeed = 1.5f;
                break;
            case "2.0x":
                playbackSpeed = 2.0f;
                break;
            // Add more cases if needed
        }
        mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(playbackSpeed));
    }

    // Method to get selected playback speed from Spinner
    private String getSelectedPlaybackSpeed() {
        return (String) playbackSpeedSpinner.getSelectedItem();
    }

    // Method to shuffle songs
    private void shuffleSongs() {
        Collections.shuffle(songs);
        currentSongIndex = 0; // Reset to the first song after shuffling
        changeSong(); // Play the first shuffled song
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        handler.removeCallbacksAndMessages(null);
    }
}
