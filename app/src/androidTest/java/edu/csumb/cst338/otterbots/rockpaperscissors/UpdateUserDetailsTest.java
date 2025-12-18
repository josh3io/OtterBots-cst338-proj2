package edu.csumb.cst338.otterbots.rockpaperscissors;
/**
 * Description: Tests for UpdateUserDetails activity to ensure UI components
 * are present and initialized correctly.
 * Author: Murtaza Badri
 * Since: 2025.12.09
 */
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Intent;
import android.widget.EditText;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UpdateUserDetailsTest {

  @Test
  public void launches_withIntent_andViewsExist() {
    // Create intent with a fake userId
    Intent intent = UpdateUserDetails.createIntent(
        ApplicationProvider.getApplicationContext(),
        1
    );
    // Launch the activity
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
  //Ensures input fields start empty when the screen loads.
  @Test
  public void inputFields_startEmpty() {
    // Create intent with a fake userId
    Intent intent = UpdateUserDetails.createIntent(
        ApplicationProvider.getApplicationContext(),
        1
    );
    // Launch the activity
    try (ActivityScenario<UpdateUserDetails> scenario = ActivityScenario.launch(intent)) {

      scenario.onActivity(activity -> {
        // Grab input fields
        EditText newUsername = activity.findViewById(R.id.NewUsername);
        EditText newPassword = activity.findViewById(R.id.NewPassword);
        EditText confirmPassword = activity.findViewById(R.id.ConfirmPassword);

        assertNotNull(newUsername);
        assertNotNull(newPassword);
        assertNotNull(confirmPassword);
        // Verify all inputs are empty by default
        assertEquals("", newUsername.getText().toString());
        assertEquals("", newPassword.getText().toString());
        assertEquals("", confirmPassword.getText().toString());
      });
    }
  }
}
