package ca.furguardian.it.petwellness.ui.peted;
//       Justin Chipman - RCB – N01598472
//	     Imran Zafurallah - RCB - N01585098
//	     Zane Aransevia - RCB- N01351168
//	     Tevadi Brookes - RCC - N01582563

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Arrays;
import java.util.List;

import ca.furguardian.it.petwellness.R;
import ca.furguardian.it.petwellness.databinding.FragmentPetedBinding;

public class PetEd extends Fragment {

    private FragmentPetedBinding binding;
    private Spinner spinner;
    private WebView webView;

    private final List<String> petEducationTopics = Arrays.asList(
            "Pet Nutrition", "Grooming Tips", "Vaccination Schedule", "Training and Obedience", "Exercise Needs", "Adoption"
    );

    private final List<String> defaultUrls = Arrays.asList(
            "https://www.chewy.com",
            "https://hastingsvet.com",
            "https://example.com/vaccination_schedule",
            "https://www.youtube.com/playlist?list=PL1wCnaQRu4BG_RhOZaT4UNspBbRnf4IvJ",
            "https://www.youtube.com/watch?v=PzsrsRRWZYU",
            "https://pawsandtailsadoptions.weebly.com/"
    );

    private List<String> currentUrls;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PetEdViewModel petEdViewModel = new ViewModelProvider(this).get(PetEdViewModel.class);

        binding = FragmentPetedBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Set the text from ViewModel
        final TextView textView = binding.textPeted;
        petEdViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // Override back button functionality
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                new AlertDialog.Builder(requireContext())
                        .setIcon(R.mipmap.logo)
                        .setTitle(R.string.exit_app)
                        .setMessage(R.string.are_you_sure_you_want_to_exit)
                        .setPositiveButton(R.string.yes, (dialog, which) -> requireActivity().finish())
                        .setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.dismiss())
                        .show();
            }
        });

        // Initialize Spinner
        spinner = binding.spinner;
        setSpinnerData(petEducationTopics, defaultUrls); // Use default data

        return root;
    }

    private void setSpinnerData(List<String> topics, List<String> urls) {
        // Update current URLs
        currentUrls = urls;

        // Ensure Fragment is attached and spinner is initialized
        if (!isAdded() || spinner == null) {
            return; // Exit if Fragment is not in a valid state
        }

        requireActivity().runOnUiThread(() -> {
            try {
                // Initialize spinner with topics
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, topics);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                // Set OnItemSelectedListener
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            if (position < petEducationTopics.size() && petEducationTopics.get(position).equals(getString(R.string.vaccination_schedule1))) {
                                addEventToCalendar(
                                        getString(R.string.pet_vaccination),
                                        getString(R.string.pet_vaccination_schedule),
                                        System.currentTimeMillis() + 86400000
                                );
                            } else if (position < currentUrls.size()) {
                                initializeWebView(currentUrls.get(position)); // Lazy initialize WebView
                            }
                        } catch (IndexOutOfBoundsException e) {
                            e.printStackTrace(); // Log and handle index errors gracefully
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Handle when no item is selected, if necessary
                    }
                });
            } catch (IllegalStateException e) {
                e.printStackTrace(); // Log and handle invalid state gracefully
            }
        });
    }

    private void initializeWebView(String url) {
        if (webView == null) {
            webView = binding.webView;
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
            webView.setWebViewClient(new WebViewClient());
            webView.setWebChromeClient(new WebChromeClient());
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
        webView.loadUrl(url);
    }

    private void addEventToCalendar(String title, String description, long startTimeInMillis) {
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, title)
                .putExtra(CalendarContract.Events.DESCRIPTION, description)
                .putExtra(CalendarContract.Events.EVENT_LOCATION, "Pet Clinic")
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTimeInMillis)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, startTimeInMillis + 60 * 60 * 1000); // 1 hour duration

        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
