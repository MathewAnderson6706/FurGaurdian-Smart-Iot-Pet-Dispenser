package ca.furguardian.it.petwellness.model;
//       Justin Chipman - RCB – N01598472
//	     Imran Zafurallah - RCB - N01585098
//	     Zane Aransevia - RCB- N01351168
//	     Tevadi Brookes - RCC - N01582563

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;

public class FeedbackModel {

    private final DatabaseReference feedbackRef;

    public FeedbackModel() {
        // Initialize Firebase reference for feedback
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        feedbackRef = database.getReference("feedback");
    }

    public void submitFeedback(String name, String phone, String email, String comment, float rating, Context context, FeedbackCallback callback) {
        // Prepare feedback data
        String deviceModel = android.os.Build.MODEL;
        Map<String, Object> feedback = new HashMap<>();
        feedback.put("name", name);
        feedback.put("phone", phone);
        feedback.put("email", email);
        feedback.put("comment", comment);
        feedback.put("rating", rating);
        feedback.put("deviceModel", deviceModel);

        // Add feedback data to Firebase
        feedbackRef.push().setValue(feedback)
                .addOnSuccessListener(aVoid -> {
                    // Notify success through callback
                    if (callback != null) {
                        callback.onSuccess();
                    }
                })
                .addOnFailureListener(e -> {
                    // Notify failure through callback
                    if (callback != null) {
                        callback.onFailure(e.getMessage());
                    }
                });
    }


    public interface FeedbackCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }


}
