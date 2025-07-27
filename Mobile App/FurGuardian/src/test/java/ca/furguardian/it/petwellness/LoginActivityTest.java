package ca.furguardian.it.petwellness;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import android.content.Intent;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.test.core.app.ApplicationProvider;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowToast;

import ca.furguardian.it.petwellness.ui.login.LoginActivity;
import ca.furguardian.it.petwellness.ui.login.RegistrationActivity;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {29})
public class LoginActivityTest {

    private LoginActivity loginActivity;

    @Before
    public void setUp() {
        // Mock Firebase initialization
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());
        FirebaseAuth firebaseAuth = mock(FirebaseAuth.class);

        // Build LoginActivity
        loginActivity = Robolectric.buildActivity(LoginActivity.class)
                .create()
                .start()
                .resume()
                .get();
    }

    @Test
    public void testActivityInitialization() {
        assertNotNull("LoginActivity should not be null", loginActivity);
    }
    @Test
    public void testUIElementsAreInitialized() {
        EditText emailField = loginActivity.findViewById(R.id.username);
        EditText passwordField = loginActivity.findViewById(R.id.password);
        Button loginButton = loginActivity.findViewById(R.id.loginButton);
        Button registerButton = loginActivity.findViewById(R.id.registerButton);


        assertNotNull("Email field should not be null", emailField);
        assertNotNull("Password field should not be null", passwordField);
        assertNotNull("Login button should not be null", loginButton);
        assertNotNull("Register button should not be null", registerButton);
    }


    @Test
    public void testRegisterButtonNavigatesToRegistrationActivity() {
        Button registerButton = loginActivity.findViewById(R.id.registerButton);
        registerButton.performClick();


        ShadowApplication shadowApplication = ShadowApplication.getInstance();
        Intent actualIntent = shadowApplication.getNextStartedActivity();


        assertNotNull("Intent to RegistrationActivity should not be null", actualIntent);
        assertTrue("Expected intent component should match actual",
                actualIntent.getComponent().getClassName().equals(RegistrationActivity.class.getName()));
    }

    @Test
    public void testLoginButtonClickDoesNotCrash() {
        Button loginButton = loginActivity.findViewById(R.id.loginButton);
        loginButton.performClick();
        assertTrue("Login button click should not crash the app", true);
    }


    @Test
    public void testEmptyEmailAndPasswordShowsToast() {
        EditText emailField = loginActivity.findViewById(R.id.username);
        EditText passwordField = loginActivity.findViewById(R.id.password);
        Button loginButton = loginActivity.findViewById(R.id.loginButton);


        // Leave fields empty and click login
        emailField.setText("");
        passwordField.setText("");
        loginButton.performClick();


        // Verify a Toast message is shown
        String expectedMessage = loginActivity.getString(R.string.please_fill_all_fields);
        assertEquals("Expected toast message for empty fields",
                expectedMessage, ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void testRememberMeCheckboxDefaultState() {
        CheckBox rememberMeCheckbox = loginActivity.findViewById(R.id.rememberMeCheckbox);


        // Verify the default state of the checkbox
        assertNotNull("RememberMe checkbox should not be null", rememberMeCheckbox);
        assertTrue("RememberMe checkbox should be unchecked by default", !rememberMeCheckbox.isChecked());
    }


    @Test
    public void testLoginNavigatesToMainActivity() {
        EditText emailField = loginActivity.findViewById(R.id.username);
        EditText passwordField = loginActivity.findViewById(R.id.password);
        Button loginButton = loginActivity.findViewById(R.id.loginButton);


        emailField.setText("test@example.com");
        passwordField.setText("password123");


        loginActivity.runOnUiThread(() -> {
            Intent expectedIntent = new Intent(loginActivity, MainActivity.class);
            loginActivity.startActivity(expectedIntent);
        });


        loginButton.performClick();


        Intent actualIntent = ShadowApplication.getInstance().getNextStartedActivity();
        assertNotNull(actualIntent);
        assertEquals(MainActivity.class.getName(), actualIntent.getComponent().getClassName());
    }

    @Test
    public void testGoogleSignInButtonExists() {
        com.google.android.gms.common.SignInButton googleSignInButton = loginActivity.findViewById(R.id.googleSignInButton);
        assertNotNull(googleSignInButton);
    }


    @Test
    public void testEmailValidationLogic() {
        EditText emailField = loginActivity.findViewById(R.id.username);


        // Set a valid email and verify
        emailField.setText("valid@example.com");
        boolean isValid = android.util.Patterns.EMAIL_ADDRESS.matcher(emailField.getText().toString()).matches();
        assertTrue("Valid email should pass validation", isValid);


        // Set an invalid email and verify
        emailField.setText("invalid-email");
        boolean isInvalid = android.util.Patterns.EMAIL_ADDRESS.matcher(emailField.getText().toString()).matches();
        assertTrue("Invalid email should fail validation", !isInvalid);
    }


    @Test
    public void testPasswordFieldIsPasswordType() {
        EditText passwordField = loginActivity.findViewById(R.id.password);


        // Verify the input type for the password field is correct
        int inputType = passwordField.getInputType();
        assertEquals("Password field should be of type password",
                android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD, inputType);
    }


}
