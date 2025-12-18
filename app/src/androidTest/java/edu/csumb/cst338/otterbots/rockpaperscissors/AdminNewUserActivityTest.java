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
 * Instrumented UI test suite for AdminNewUserActivity.
 *
 * @author Christopher Buenrostro
 *
 * These tests verify front-end behavior only and do not interact with
 * Room or any backend logic. Their purpose is to ensure:
 *   • Fields retain user-entered values when input validation fails.
 *   • Fields also retain values when validation succeeds (per UI design).
 *   • The “Return” navigation element correctly closes the activity.
 *
 * This keeps validation predictable and avoids silently altering user input.
 */
@RunWith(AndroidJUnit4.class)
public class AdminNewUserActivityTest {

  // ========================================================================
  // 1. CASE: Password fields do NOT match
  // EXPECTATION:
  //   • User-entered values remain unchanged after clicking "Add User"
  //   • Mismatch is detected and fields are NOT cleared by the UI
  // ========================================================================
  @Test
  public void whenPasswordsDoNotMatch_afterClick_fieldsStillContainValues() {
    Intent intent = AdminNewUserActivity.createIntent(
            ApplicationProvider.getApplicationContext()
    );

    try (ActivityScenario<AdminNewUserActivity> scenario =
                 ActivityScenario.launch(intent)) {

      scenario.onActivity(activity -> {
        EditText username = activity.findViewById(R.id.adminUsernameEditText);
        EditText password = activity.findViewById(R.id.adminPasswordEditText);
        EditText confirm = activity.findViewById(R.id.adminConfirmPasswordEditText);
        Button addUser = activity.findViewById(R.id.addUserButton);

        // Verify all UI references exist
        assertNotNull(username);
        assertNotNull(password);
        assertNotNull(confirm);
        assertNotNull(addUser);

        // Pre-fill fields using intentionally mismatched passwords
        username.setText("testUser");
        password.setText("1234");
        confirm.setText("0000");

        // Trigger "Add User" action
        addUser.performClick();

        // POST-CONDITIONS:
        // UI should leave existing values untouched
        assertEquals("testUser", username.getText().toString());
        assertEquals("1234", password.getText().toString());
        assertEquals("0000", confirm.getText().toString());

        // And confirm mismatch is still true
        assertTrue(!password.getText().toString().equals(confirm.getText().toString()));
      });
    }
  }

  // ========================================================================
  // 2. CASE: Password fields DO match
  // EXPECTATION:
  //   • UI keeps all entered field values as-is after clicking "Add Admin"
  //   • Matching passwords are recognized, but UI still does not clear fields
  // ========================================================================
  @Test
  public void whenPasswordsMatch_afterClick_fieldsStillContainValues() {
    Intent intent = AdminNewUserActivity.createIntent(
            ApplicationProvider.getApplicationContext()
    );

    try (ActivityScenario<AdminNewUserActivity> scenario =
                 ActivityScenario.launch(intent)) {

      scenario.onActivity(activity -> {
        EditText username = activity.findViewById(R.id.adminUsernameEditText);
        EditText password = activity.findViewById(R.id.adminPasswordEditText);
        EditText confirm = activity.findViewById(R.id.adminConfirmPasswordEditText);
        Button addAdmin = activity.findViewById(R.id.addAdminButton);

        assertNotNull(username);
        assertNotNull(password);
        assertNotNull(confirm);
        assertNotNull(addAdmin);

        // Provide consistent, matching passwords
        username.setText("adminTest");
        password.setText("abcd");
        confirm.setText("abcd");

        // Trigger the add-admin action
        addAdmin.performClick();

        // POST-CONDITION:
        // All input values should remain present
        assertEquals("adminTest", username.getText().toString());
        assertEquals("abcd", password.getText().toString());
        assertEquals("abcd", confirm.getText().toString());

        // Passwords should match at this stage
        assertEquals(password.getText().toString(), confirm.getText().toString());
      });
    }
  }

  // ========================================================================
  // 3. CASE: User taps the "Return" TextView
  // EXPECTATION:
  //   • Activity should transition into closing state (finish/destroy)
  // ========================================================================
  @Test
  public void clickingReturn_afterClick_activityIsClosing() {
    Intent intent = AdminNewUserActivity.createIntent(
            ApplicationProvider.getApplicationContext()
    );

    try (ActivityScenario<AdminNewUserActivity> scenario =
                 ActivityScenario.launch(intent)) {

      scenario.onActivity(activity -> {
        TextView returnText = activity.findViewById(R.id.returnSelectableTextView);
        assertNotNull(returnText);

        // Simulate user action
        returnText.performClick();

        // POST-CONDITION: Activity must be closing
        assertTrue(activity.isFinishing() || activity.isDestroyed());
      });
    }
  }
}
