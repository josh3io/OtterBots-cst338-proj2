package edu.csumb.cst338.otterbots.rockpaperscissors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UpdateUserDetailsTest {
    @Test
    public void launches_withIntent_andViewsExist() {
        // Arrange: create the intent the same way your app does
        Intent intent = UpdateUserDetails.createIntent(
                ApplicationProvider.getApplicationContext(),
                1
        );

        try (ActivityScenario<UpdateUserDetails> scenario = ActivityScenario.launch(intent)) {

            scenario.onActivity(activity -> {
                assertNotNull(activity.findViewById(R.id.Title));
                assertNotNull(activity.findViewById(R.id.CurrentUsername));
                assertNotNull(activity.findViewById(R.id.NewUsername));
                assertNotNull(activity.findViewById(R.id.NewPassword));
                assertNotNull(activity.findViewById(R.id.ConfirmPassword));
                assertNotNull(activity.findViewById(R.id.UpdateButton));
                assertNotNull(activity.findViewById(R.id.ReturnButton));
            });
        }
    }

    @Test
    public void inputFields_startEmpty() {
        Intent intent = UpdateUserDetails.createIntent(
                ApplicationProvider.getApplicationContext(),
                1
        );

        try (ActivityScenario<UpdateUserDetails> scenario = ActivityScenario.launch(intent)) {

            scenario.onActivity(activity -> {
                EditText newUsername = activity.findViewById(R.id.NewUsername);
                EditText newPassword = activity.findViewById(R.id.NewPassword);
                EditText confirmPassword = activity.findViewById(R.id.ConfirmPassword);

                assertNotNull(newUsername);
                assertNotNull(newPassword);
                assertNotNull(confirmPassword);

                assertEquals("", newUsername.getText().toString());
                assertEquals("", newPassword.getText().toString());
                assertEquals("", confirmPassword.getText().toString());
            });
        }
    }
}
