package edu.csumb.cst338.otterbots.rockpaperscissors;

import static edu.csumb.cst338.otterbots.rockpaperscissors.LandingActivity.EXTRA_IS_ADMIN;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Intent;
import android.widget.TextView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented UI tests for {@link LandingActivity}.
 *
 * <p>This test suite verifies:
 * <ul>
 *   <li>Fallback behavior when username is missing (default "Player" name)</li>
 *   <li>Correct layout inflation and UI contents when admin mode is enabled</li>
 * </ul>
 *
 * <p>These tests use {@link ActivityScenario} to launch the activity in an isolated
 * instrumentation environment and validate visible UI state.
 *
 * <p>Author: Anthony Martinez
 * CST338 – Fall 2025
 */
@RunWith(AndroidJUnit4.class)
public class LandingActivityTest {

  /**
   * Verifies that LandingActivity displays a fallback default username ("Player")
   * when the Intent does not include EXTRA_USERNAME.
   *
   * <p>This ensures the UI never appears blank or broken if the username extra is missing.
   */
  @Test
  public void whenUserNameMissing_useDefaultPlayerName() {
    // Arrange: Launch activity with no username provided
    Intent intent = new Intent(ApplicationProvider.getApplicationContext(), LandingActivity.class);

    // Note: Intentionally do not put EXTRA_USERNAME.
    intent.putExtra(EXTRA_IS_ADMIN, false); // User view

    // Act: Launch LandingActivity with this intent.
    try (ActivityScenario<LandingActivity> scenario = ActivityScenario.launch(intent)) {
      scenario.onActivity(activity -> {
        // Find the title TextView
        TextView titleView = activity.findViewById(R.id.titleLandingTextView);
        assertNotNull(titleView);

        String text = titleView.getText().toString();
        // Assert: Text includes the default "Player" name.
        assertTrue(text.contains("Player"));
      });
    }
  }

  /**
   * Verifies that when EXTRA_IS_ADMIN = true:
   * <ul>
   *   <li>The admin layout is inflated instead of the user layout</li>
   *   <li>The username appears in the welcome message</li>
   *   <li>Admin-only UI elements (e.g., Add User) are present</li>
   * </ul>
   *
   * <p>NOTE: userId = -1 is used to prevent LiveData observers from overriding UI text.
   */
  @Test
  public void whenIsAdminTrue_adminLayoutLoadsAndDisplaysUsername() {

    // Arrange
    Intent intent = new Intent(ApplicationProvider.getApplicationContext(), LandingActivity.class);

    intent.putExtra(LandingActivity.EXTRA_USERNAME, "Anthony");
    intent.putExtra(LandingActivity.EXTRA_USER_ID, -1); // Prevents DB observer from firing
    intent.putExtra(EXTRA_IS_ADMIN, true);

    // Act + Assert
    try (ActivityScenario<LandingActivity> scenario = ActivityScenario.launch(intent)) {
      scenario.onActivity(activity -> {
        TextView titleView = activity.findViewById(R.id.titleLandingTextView);
        assertNotNull(titleView);

        String actualText = titleView.getText().toString();
        assertTrue(actualText.contains("Anthony"));

        // Admin layout ONLY - confirms correct layout was inflated
        TextView addUserView = activity.findViewById(R.id.addUserTextView);
        assertNotNull("Admin layout should include Add User view.", addUserView);
      });
    }
  }
}
