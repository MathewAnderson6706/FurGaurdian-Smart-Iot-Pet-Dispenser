package ca.furguardian.it.petwellness.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ca.furguardian.it.petwellness.R;
import ca.furguardian.it.petwellness.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private WebView webView;
    private ImageView fallbackImage;
    private Button refreshButton;
    private Button dispenseButton;
    private Button treatButton;
    private DatabaseReference streamRef;
    private DatabaseReference dispenseRef;
    private final Handler handler = new Handler();

    private final BroadcastReceiver streamRefreshReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Refresh the stream when the broadcast is received
            startStream();
            Toast.makeText(context, "Stream refreshed", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Obtain references to the views defined in the new layout
        webView = root.findViewById(R.id.webView);
        fallbackImage = root.findViewById(R.id.fallbackImage);
        refreshButton = root.findViewById(R.id.buttonRefresh);
        dispenseButton = root.findViewById(R.id.dispense);
        treatButton = root.findViewById(R.id.treat);

        // Configure the WebView
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        // Register the local broadcast receiver
        LocalBroadcastManager.getInstance(requireContext())
                .registerReceiver(streamRefreshReceiver, new IntentFilter("STREAM_READY_NOTIFICATION"));

        // Initialize Firebase Database reference for the camera stream
        streamRef = FirebaseDatabase.getInstance().getReference("cameraStream");
        dispenseRef = FirebaseDatabase.getInstance().getReference("dispense");

        // Set up the refresh button to try and reload the stream
        refreshButton.setOnClickListener(v -> startStream());

        setupButtonListeners();

        // Start listening for stream changes
        startStream();

        return root;
    }

    private void setupButtonListeners() {
        // Set up the refresh button to try and reload the stream
        refreshButton.setOnClickListener(v -> startStream());

        // Set up the normal dispense button
        dispenseButton.setOnClickListener(v -> {
            // Set the "normal" value under "dispense" to true
            dispenseRef.child("normal").setValue(true)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Dispense command sent", Toast.LENGTH_SHORT).show();

                        // Reset value to false after a delay
                        resetDispenseValueAfterDelay("normal");
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        // Set up the treat dispense button
        treatButton.setOnClickListener(v -> {
            // Set the "treat" value under "dispense" to true
            dispenseRef.child("treat").setValue(true)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Treat dispense command sent", Toast.LENGTH_SHORT).show();

                        // Reset value to false after a delay
                        resetDispenseValueAfterDelay("treat");
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }

    // Reset the value back to false after a short delay
    private void resetDispenseValueAfterDelay(String type) {
        handler.postDelayed(() -> {
            dispenseRef.child(type).setValue(false);
        }, 3000); // Reset after 3 seconds
    }

    private void startStream() {
        streamRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean streamReady = snapshot.child("stream_ready").getValue(Boolean.class);
                String streamUrl = snapshot.child("url").getValue(String.class);

                if (streamReady != null && streamReady && streamUrl != null && !streamUrl.isEmpty()) {
                    // If the stream is ready, load the URL and display the WebView
                    webView.setVisibility(View.VISIBLE);
                    fallbackImage.setVisibility(View.GONE);
                    webView.loadUrl(streamUrl);
                } else {
                    // If the stream is not ready, show the fallback image and hide the WebView
                    webView.setVisibility(View.GONE);
                    fallbackImage.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "Stream is not active", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error loading stream", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        startStream();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(streamRefreshReceiver);
        binding = null;
    }
}
