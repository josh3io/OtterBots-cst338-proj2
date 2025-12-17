package edu.csumb.cst338.otterbots.rockpaperscissors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * UI tests for AdminDeleteUserActivity.
 * These tests assert EditText state and navigation behavior only,
 */
@RunWith(AndroidJUnit4.class)
public class AdminDeleteUserActivityTest {

    // ------------------------------------------------------------
    // 1. Empty fields: click delete -> fields stay empty
    // ------------------------------------------------------------
    @Test
    public void whenFieldsEmpty_afterClickDelete_fieldsRemainEmpty() {
        Intent intent = AdminDeleteUserActivity.createIntent(
                ApplicationProvider.getApplicationContext()
        );

        try (ActivityScenario<AdminDeleteUserActivity> scenario =
                     ActivityScenario.launch(intent)) {

            scenario.onActivity(activity -> {
                EditText username = activity.findViewById(R.id.usernameEditTextView);
                EditText confirm = activity.findViewById(R.id.confirmUsernameEditTextView);
                Button deleteButton = activity.findViewById(R.id.deleteUserButton);

                assertNotNull(username);
                assertNotNull(confirm);
                assertNotNull(deleteButton);

                assertEquals("", username.getText().toString());
                assertEquals("", confirm.getText().toString());

                // Click delete with empty fields
                deleteButton.performClick();

                // AFTER click: fields should still be empty
                assertEquals("", username.getText().toString());
                assertEquals("", confirm.getText().toString());
            });
        }
    }

    // ------------------------------------------------------------
    // 2. Matching usernames: click delete -> fields get cleared
    // ------------------------------------------------------------
    @Test
    public void whenUsernamesMatch_afterClickDelete_fieldsAreCleared() {
        Intent intent = AdminDeleteUserActivity.createIntent(
                ApplicationProvider.getApplicationContext()
        );

        try (ActivityScenario<AdminDeleteUserActivity> scenario =
                     ActivityScenario.launch(intent)) {

            scenario.onActivity(activity -> {
                EditText username = activity.findViewById(R.id.usernameEditTextView);
                EditText confirm = activity.findViewById(R.id.confirmUsernameEditTextView);
                Button deleteButton = activity.findViewById(R.id.deleteUserButton);

                assertNotNull(username);
                assertNotNull(confirm);
                assertNotNull(deleteButton);

                // Fill both fields with the same username (matching)
                username.setText("deleteMe");
                confirm.setText("deleteMe");

                // Click delete
                deleteButton.performClick();

                // AFTER click: activity clears both fields
                assertEquals("", username.getText().toString());
                assertEquals("", confirm.getText().toString());
            });
        }
    }

    // ------------------------------------------------------------
    // 3. Clicking "return to main" finishes activity
    // ------------------------------------------------------------
    @Test
    public void clickingReturnToMain_afterClick_activityIsClosing() {
        Intent intent = AdminDeleteUserActivity.createIntent(
                ApplicationProvider.getApplicationContext()
        );

        try (ActivityScenario<AdminDeleteUserActivity> scenario =
                     ActivityScenario.launch(intent)) {

            scenario.onActivity(activity -> {
                TextView returnText = activity.findViewById(R.id.returnToMainTextView);
                assertNotNull(returnText);

                // Click the "return" text
                returnText.performClick();

                // AFTER click -> activity should be finishing or destroyed
                assertTrue(activity.isFinishing() || activity.isDestroyed());
            });
        }
    }
}
