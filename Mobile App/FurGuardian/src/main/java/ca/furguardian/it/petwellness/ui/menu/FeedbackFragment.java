package ca.furguardian.it.petwellness.ui.menu;
//       Justin Chipman - RCB – N01598472
//	     Imran Zafurallah - RCB - N01585098
//	     Zane Aransevia - RCB- N01351168
//	     Tevadi Brookes - RCC - N01582563

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.os.CountDownTimer;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import java.util.concurrent.TimeUnit;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import ca.furguardian.it.petwellness.R;
import ca.furguardian.it.petwellness.controller.InputValidator;
import ca.furguardian.it.petwellness.model.FeedbackModel;

public class FeedbackFragment extends Fragment {

    private EditText nameEditText, phoneEditText, emailEditText, commentEditText;
    private RatingBar ratingBar;
    private Button submitButton;
    private ProgressBar progressBar;
    private FeedbackModel feedbackModel;
    private TextView counterTextView;
    private SharedPreferences sharedPreferences;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);

        // Initialize views
        nameEditText = view.findViewById(R.id.nameEditText);
        phoneEditText = view.findViewById(R.id.phoneEditText);
        emailEditText = view.findViewById(R.id.emailEditText);
        commentEditText = view.findViewById(R.id.commentEditText);
        ratingBar = view.findViewById(R.id.ratingBar);
        submitButton = view.findViewById(R.id.submitButton);
        progressBar = view.findViewById(R.id.progressBar);
        counterTextView = view.findViewById(R.id.counterTextView);
        sharedPreferences = requireContext().getSharedPreferences("feedback_preferences", Context.MODE_PRIVATE);


        // Check if feedback was recently submitted
        long lastSubmittedTime = sharedPreferences.getLong("lastSubmittedTime", 0);
        if (lastSubmittedTime > 0) {
            long elapsedTime = System.currentTimeMillis() - lastSubmittedTime;
            if (elapsedTime < TimeUnit.DAYS.toMillis(1)) {
                startCountdown(TimeUnit.DAYS.toMillis(1) - elapsedTime);
            }
        }


        feedbackModel = new FeedbackModel();

        // Set up submit button
        submitButton.setOnClickListener(v -> validateAndSubmitFeedback());

        return view;
    }

    private void validateAndSubmitFeedback() {
        String name = nameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String comment = commentEditText.getText().toString().trim();
        float rating = ratingBar.getRating();

        // Validate inputs
        if (!InputValidator.isValidPhoneNumber(phone)) {
            showToast(getString(R.string.please_enter_a_valid_phone_number));
        } else if (!InputValidator.isValidEmail(email)) {
            showToast(getString(R.string.please_enter_a_valid_email_address));
        } else if (comment.isEmpty()) {
            showToast(getString(R.string.please_provide_comments));
        } else {
            // Proceed with submission
            submitFeedback(name, phone, email, comment, rating);
        }
    }

    private void submitFeedback(String name, String phone, String email, String comment, float rating) {
        progressBar.setVisibility(View.VISIBLE);
        submitButton.setEnabled(false);

        feedbackModel.submitFeedback(name, phone, email, comment, rating, getContext(), new FeedbackModel.FeedbackCallback() {
            @Override
            public void onSuccess() {
                // Keep the progress bar visible for a short delay
                new Handler().postDelayed(() -> {
                    progressBar.setVisibility(View.GONE);

                    // Save submission time
                    long currentTime = System.currentTimeMillis();
                    sharedPreferences.edit().putLong("lastSubmittedTime", currentTime).apply();

                    // Start countdown and update UI
                    startCountdown(TimeUnit.DAYS.toMillis(1));

                    // Show success dialog
                    new AlertDialog.Builder(requireContext())
                            .setTitle(getString(R.string.thank_you))
                            .setMessage(getString(R.string.your_feedback_has_been_submitted_successfully))
                            .setPositiveButton(getString(R.string.ok), (dialog, which) -> resetFields())
                            .setCancelable(false)
                            .show();
                }, 2000); // Delay of 2 seconds
            }

            @Override
            public void onFailure(String errorMessage) {
                progressBar.setVisibility(View.GONE);
                submitButton.setEnabled(true);
                showToast(getString(R.string.failed_to_submit_feedback) + errorMessage);
            }
        });
    }


    private void resetFields() {
        nameEditText.setText("");
        phoneEditText.setText("");
        emailEditText.setText("");
        commentEditText.setText("");
        ratingBar.setRating(0);
    }

    private void startCountdown(long millis) {
        submitButton.setEnabled(false);
        submitButton.setBackgroundColor(getResources().getColor(R.color.grey));
        counterTextView.setVisibility(View.VISIBLE);

        new CountDownTimer(millis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String time = String.format("%02d:%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                counterTextView.setText("Try again in: " + time);
            }

            @Override
            public void onFinish() {
                submitButton.setEnabled(true);
                Context context = null;
                submitButton.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));

                counterTextView.setVisibility(View.GONE);
            }
        }.start();
    }


    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
