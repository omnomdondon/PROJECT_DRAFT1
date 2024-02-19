/*
package com.example.project_draft1;


import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {
    private static final int YOUR_PERMISSION_REQUEST_CODE = 123;
    private BalloonAnimator balloonAnimator;
    private AudioRecord audioRecord;
    private boolean isBlowing = false;

    private static final double BLOWING_THRESHOLD = 0.1;  // Adjust the threshold based on experimentation

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.RECORD_AUDIO
            }, YOUR_PERMISSION_REQUEST_CODE);

        balloonAnimator = new BalloonAnimator(findViewById(R.id.balloonImageView));
        Button startButton = findViewById(R.id.start_btn);

        startButton.setOnClickListener(view -> {
            if (!isBlowing) {
                startBlowingSimulation();
            } else {
                stopBlowingSimulation();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == YOUR_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can proceed with your audio recording logic
            } else {
                // Permission denied, handle accordingly (e.g., show a message or disable the feature)
                Toast.makeText(this, "Permission denied. Cannot record audio.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startBlowingSimulation() {
        isBlowing = true;
        Log.d("YourTag", "Blowing simulation started");

        // Initialize and start capturing microphone input
        int bufferSize = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);
        audioRecord.startRecording();

        // Start a thread to analyze microphone input
        new Thread(this::analyzeMicrophoneInput).start();
    }

    private void stopBlowingSimulation() {
        isBlowing = false;

        // Stop capturing microphone input
        if (audioRecord != null) {
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;

            // Deflate the balloon when blowing stops
            runOnUiThread(() -> balloonAnimator.deflateBalloon());
        }
    }

    private void analyzeMicrophoneInput() {
        short[] buffer = new short[1024];
        while (isBlowing) {
            int bytesRead = audioRecord.read(buffer, 0, buffer.length);

            if (bytesRead > 0) {
                // Apply noise filtering
                double[] filteredBuffer = applyNoiseFilter(buffer, bytesRead);

                // Analyze amplitude or frequency to determine blowing intensity
                double intensity = calculateBlowingIntensity(filteredBuffer, bytesRead);
                Log.d("YourTag", "Intensity: " + intensity);

                // Update balloon size based on blowing intensity
                updateBalloonSize(intensity);
            }
        }
    }

    private double[] applyNoiseFilter(short[] buffer, int bytesRead) {
        // Implement a noise filtering algorithm (e.g., low-pass filter)
        // You may want to use a more advanced filtering algorithm based on your requirements
        double[] filteredBuffer = new double[bytesRead];
        for (int i = 0; i < bytesRead; i++) {
            // Simple low-pass filter example
            filteredBuffer[i] = 0.1 * buffer[i] + 0.9 * filteredBuffer[i];
        }
        return filteredBuffer;
    }

    private double calculateBlowingIntensity(double[] buffer, int bytesRead) {
        // Example: Calculate intensity based on amplitude
        int maxAmplitude = 0;
        for (int i = 0; i < bytesRead; i++)
            if (Math.abs(buffer[i]) > maxAmplitude) maxAmplitude = Math.abs((int) buffer[i]);
        return maxAmplitude / 32767.0; // Normalize amplitude to a range between 0 and 1
    }

    private void updateBalloonSize(final double intensity) {
        // Apply threshold to consider it as blowing
        if (intensity > BLOWING_THRESHOLD) {
            // Update balloon size on the UI thread
            runOnUiThread(() -> {
                // Adjust the multiplier based on your desired sensitivity
                float newSize = (float) (1.0 + 150.0 * intensity);
                balloonAnimator.inflateBalloon(newSize);
            });
        }
    }
}

*/

package com.example.project_draft1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {
    private static final int YOUR_PERMISSION_REQUEST_CODE = 123;
    private BalloonAnimator balloonAnimator;
    private AudioRecord audioRecord;
    private boolean isBlowing = false;

    private static final double BLOWING_THRESHOLD = 0.1;
    private static final int BALLOON_INFLATION_LIMIT = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.RECORD_AUDIO
            }, YOUR_PERMISSION_REQUEST_CODE);

        balloonAnimator = new BalloonAnimator(findViewById(R.id.balloonImageView));
        Button startButton = findViewById(R.id.start_btn);

        startButton.setOnClickListener(view -> {
            if (!isBlowing) {
                startBlowingSimulation();
            } else {
                stopBlowingSimulation();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == YOUR_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can proceed with your audio recording logic
            } else {
                // Permission denied, handle accordingly (e.g., show a message or disable the feature)
                Toast.makeText(this, "Permission denied. Cannot record audio.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startBlowingSimulation() {
        isBlowing = true;
        Log.d("YourTag", "Blowing simulation started");

        // Initialize and start capturing microphone input
        int bufferSize = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);
        audioRecord.startRecording();

        // Start a thread to analyze microphone input
        new Thread(this::analyzeMicrophoneInput).start();
    }

    private void stopBlowingSimulation() {
        isBlowing = false;

        // Stop capturing microphone input
        if (audioRecord != null) {
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;

            // Deflate the balloon when blowing stops
            runOnUiThread(() -> balloonAnimator.deflateBalloon());
        }
    }

    private void analyzeMicrophoneInput() {
        short[] buffer = new short[1024];
        int balloonInflationCount = 0;

        while (isBlowing && balloonInflationCount < BALLOON_INFLATION_LIMIT) {
            int bytesRead = audioRecord.read(buffer, 0, buffer.length);

            if (bytesRead > 0) {
                // Apply noise filtering
                double[] filteredBuffer = applyNoiseFilter(buffer, bytesRead);

                // Analyze amplitude or frequency to determine blowing intensity
                double intensity = calculateBlowingIntensity(filteredBuffer, bytesRead);
                Log.d("YourTag", "Intensity: " + intensity);

                // Update balloon size based on blowing intensity
                updateBalloonSize(intensity);

                // Check if balloon inflation limit is reached
                if (intensity > BLOWING_THRESHOLD) {
                    balloonInflationCount++;
                } else {
                    balloonInflationCount = 0; // Reset if blowing intensity falls below threshold
                }
            }
        }

        // Balloon blowing simulation ended
        runOnUiThread(() -> stopBlowingSimulation());
    }

    /*
    private double[] applyNoiseFilter(short[] buffer, int bytesRead) {

        // Implement a noise filtering algorithm (e.g., low-pass filter)
        // You may want to use a more advanced filtering algorithm based on your requirements
        double[] filteredBuffer = new double[bytesRead];
        for (int i = 0; i < bytesRead; i++) {
            // Simple low-pass filter example
            filteredBuffer[i] = 0.1 * buffer[i] + 0.9 * filteredBuffer[i];
        }
        return filteredBuffer;
    }
    */

    private double[] applyNoiseFilter(short[] buffer, int bytesRead) {
        // Implement a bandpass filter to focus on the blowing frequency range
        // You may need to adjust the parameters based on your testing and requirements

        double[] filteredBuffer = new double[bytesRead];
        double[] outputBuffer = new double[bytesRead];

        double lowFrequency = 500.0; // Adjust based on your testing
        double highFrequency = 2000.0; // Adjust based on your testing
        double sampleRate = 44100.0;

        // Design a bandpass filter
        Butterworth bandpassFilter = new Butterworth();
        bandpassFilter.bandPass(4, sampleRate, lowFrequency, highFrequency);

        for (int i = 0; i < bytesRead; i++) {
            filteredBuffer[i] = bandpassFilter.filter(buffer[i]);
        }

        // Apply a simple low-pass filter for additional noise reduction
        for (int i = 0; i < bytesRead; i++) {
            outputBuffer[i] = 0.1 * filteredBuffer[i] + 0.9 * outputBuffer[i];
        }

        return outputBuffer;
    }


    private double calculateBlowingIntensity(double[] buffer, int bytesRead) {
        // Example: Calculate intensity based on amplitude
        int maxAmplitude = 0;
        for (int i = 0; i < bytesRead; i++)
            if (Math.abs(buffer[i]) > maxAmplitude) maxAmplitude = Math.abs((int) buffer[i]);
        return maxAmplitude / 32767.0; // Normalize amplitude to a range between 0 and 1
    }

    private void updateBalloonSize(final double intensity) {
        // Apply threshold to consider it as blowing
        if (intensity > BLOWING_THRESHOLD) {
            // Update balloon size on the UI thread
            runOnUiThread(() -> {
                // Adjust the multiplier based on your desired sensitivity
                float newSize = (float) (1.0 + 150.0 * intensity);
                balloonAnimator.inflateBalloon(newSize);
            });
        }
    }
}
