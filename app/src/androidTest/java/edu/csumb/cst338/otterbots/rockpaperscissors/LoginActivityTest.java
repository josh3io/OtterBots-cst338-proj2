package edu.csumb.cst338.otterbots.rockpaperscissors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Unit style instrumentation tests for LoginActivity.
 */
@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

  @Test
  public void createLogoutIntent_targetLoginActivityWithClearBackStackFlags() {
    // Arrange
    Context context = ApplicationProvider.getApplicationContext();

    // Act
    Intent logoutIntent = LoginActivity.createLogoutIntent(context);

    // Assert: correct target Activity
    ComponentName component = logoutIntent.getComponent();
    assertNotNull(component);
    assertEquals(LoginActivity.class.getName(), component.getClassName());

    // Assert: flags CLEAR_TOP | NEW_TASK are set
    int expectedFlags = Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK;
    assertEquals(expectedFlags, logoutIntent.getFlags());
  }

  @Test
  public void whenLastUsernameSaved_prefillsUsernameFieldsOnLaunch() {
    // Arrange: write a fake saved username into SharedPreferences
    Context context = ApplicationProvider.getApplicationContext();
    String savedUsername = "SavedUser123";

    context.getSharedPreferences(LoginActivity.PREFS_NAME, Context.MODE_PRIVATE)
        .edit()
        .putString(LoginActivity.KEY_LAST_USERNAME, savedUsername)
        .commit(); // commit so it's visible before Activity starts

    // Act: launch LoginActivity
    try (ActivityScenario<LoginActivity> scenario = ActivityScenario.launch(LoginActivity.class)) {

      scenario.onActivity(activity -> {
        // Find the username EditText by its ID from the layout
        android.widget.EditText usernameEditText = activity.findViewById(
            R.id.userNameLoginEditText);

        assertNotNull(usernameEditText);
        String actualText = usernameEditText.getText().toString();

        // Assert: field is pre-filled with the saved username
        assertEquals(savedUsername, actualText);
      });
    }
  }


}
