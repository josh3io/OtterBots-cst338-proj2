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
 * Instrumented UI tests for AdminNewUserActivity.
 * These tests interact only with the UI (EditTexts, Buttons, TextView)
 */
@RunWith(AndroidJUnit4.class)
public class AdminNewUserActivityTest {

    // ------------------------------------------------------------
    // 1. Mismatched passwords test
    // ------------------------------------------------------------
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

                assertNotNull(username);
                assertNotNull(password);
                assertNotNull(confirm);
                assertNotNull(addUser);

                // Fill fields
                username.setText("testUser");
                password.setText("1234");
                confirm.setText("0000");

                // Perform click
                addUser.performClick();

                // Assertions AFTER the click
                assertEquals("testUser", username.getText().toString());
                assertEquals("1234", password.getText().toString());
                assertEquals("0000", confirm.getText().toString());

                // Confirm passwords still do NOT match
                assertTrue(
                        !password.getText().toString()
                                .equals(confirm.getText().toString())
                );
            });
        }
    }

    // ------------------------------------------------------------
    // 2. Matching password test
    // ------------------------------------------------------------
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

                // Fill fields
                username.setText("adminTest");
                password.setText("abcd");
                confirm.setText("abcd");

                // Perform click
                addAdmin.performClick();

                // Assertions AFTER the click
                assertEquals("adminTest", username.getText().toString());
                assertEquals("abcd", password.getText().toString());
                assertEquals("abcd", confirm.getText().toString());

                // Confirm passwords DO match
                assertEquals(
                        password.getText().toString(),
                        confirm.getText().toString()
                );
            });
        }
    }

    // ------------------------------------------------------------
    // 3. Clicking "return" finishes activity
    // ------------------------------------------------------------
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

                // Perform click
                returnText.performClick();

                // Assert AFTER click → activity should be finishing or destroyed
                assertTrue(activity.isFinishing() || activity.isDestroyed());
            });
        }
    }
}
