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
 *
 * @author Christopher Buenrostro
 * Instrumented UI test suite for AdminDeleteUserActivity.
 *
 * These tests confirm the following UI behaviors:
 *  - EditText fields retain or clear values depending on input validity.
 *  - Delete button logic updates UI state without performing backend actions.
 *  - The "Return to Main" TextView properly closes the activity.
 *
 * This test class intentionally focuses on *view-level behavior only* and does
 * not interact with the database; instead, it validates state changes resulting
 * from user interactions.
 */
@RunWith(AndroidJUnit4.class)
public class AdminDeleteUserActivityTest {

  // ========================================================================
  // 1. CASE: Both text fields are empty → user taps "Delete"
  // EXPECTATION: Activity should leave both fields unchanged (still empty).
  // ========================================================================
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

        // Validate UI references exist
        assertNotNull(username);
        assertNotNull(confirm);
        assertNotNull(deleteButton);

        // Initial state check
        assertEquals("", username.getText().toString());
        assertEquals("", confirm.getText().toString());

        // Simulate user clicking "Delete"
        deleteButton.performClick();

        // Post-condition: fields remain empty since no valid input
        assertEquals("", username.getText().toString());
        assertEquals("", confirm.getText().toString());
      });
    }
  }

  // ========================================================================
  // 2. CASE: User enters matching usernames → taps "Delete"
  // EXPECTATION: Activity recognizes valid input and clears both fields.
  // ========================================================================
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

        // Input: Matching names (valid delete action)
        username.setText("deleteMe");
        confirm.setText("deleteMe");

        // Trigger delete action
        deleteButton.performClick();

        // Post-condition: both input fields should be cleared
        assertEquals("", username.getText().toString());
        assertEquals("", confirm.getText().toString());
      });
    }
  }

  // ========================================================================
  // 3. CASE: User taps "Return to Main"
  // EXPECTATION: Activity should close (finish or destroy).
  // ========================================================================
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

        // Simulate navigation action
        returnText.performClick();

        // Activity should now be in the process of shutting down
        assertTrue(activity.isFinishing() || activity.isDestroyed());
      });
    }
  }
}
