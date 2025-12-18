package edu.csumb.cst338.otterbots.rockpaperscissors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Intent;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * UI test suite for GamePlayActivity.
 *
 * These tests focus strictly on:
 *   • Verifying that button clicks correctly update visible UI output
 *   • Ensuring navigation elements trigger expected Activity lifecycle behavior
 *
 * IMPORTANT:
 *  This suite intentionally does NOT assert:
 *    — NPC random move generation
 *    — Win/loss logic
 *    — Database updates
 *  because those systems are non-deterministic or external to UI behavior.
 *
 * Author: Christopher Buenrostro
 */
@RunWith(AndroidJUnit4.class)
public class GamePlayActivityTest {

  /**
   * Convenience factory method for generating an Intent for GamePlayActivity.
   * A userId is required by the Activity but irrelevant for UI-only testing.
   */
  private Intent createGamePlayIntent(int userId) {
    return GamePlayActivity.gamePlayActivityIntentFactory(
            ApplicationProvider.getApplicationContext(),
            userId
    );
  }

  // ========================================================================
  // 1. CASE: Player taps the ROCK button
  // EXPECTATION:
  //   • "You chose" output text updates to ROCK (uppercase resource string)
  // ========================================================================
  @Test
  public void clickRockButton_updatesYouChoseOutputToRock() {
    Intent intent = createGamePlayIntent(42);

    try (ActivityScenario<GamePlayActivity> scenario =
                 ActivityScenario.launch(intent)) {

      scenario.onActivity(activity -> {
        ImageButton rockButton = activity.findViewById(R.id.rockPlayButton);
        TextView youChoseText = activity.findViewById(R.id.youChoseOutputTextView);

        assertNotNull(rockButton);
        assertNotNull(youChoseText);

        // Execute the user action: choose ROCK
        rockButton.performClick();

        // Validate UI update using string resource shared with activity logic
        String expected = activity.getString(R.string.rock_uppercase);
        assertEquals(expected, youChoseText.getText().toString());
      });
    }
  }

  // ========================================================================
  // 2. CASE: Player taps the PAPER button
  // EXPECTATION:
  //   • "You chose" output text updates to PAPER
  // ========================================================================
  @Test
  public void clickPaperButton_updatesYouChoseOutputToPaper() {
    Intent intent = createGamePlayIntent(7);

    try (ActivityScenario<GamePlayActivity> scenario =
                 ActivityScenario.launch(intent)) {

      scenario.onActivity(activity -> {
        ImageButton paperButton = activity.findViewById(R.id.paperPlayButton);
        TextView youChoseText = activity.findViewById(R.id.youChoseOutputTextView);

        assertNotNull(paperButton);
        assertNotNull(youChoseText);

        paperButton.performClick();

        String expected = activity.getString(R.string.paper_uppercase);
        assertEquals(expected, youChoseText.getText().toString());
      });
    }
  }

  // ========================================================================
  // 3. CASE: Player taps the SCISSORS button
  // EXPECTATION:
  //   • "You chose" output displays SCISSORS
  // ========================================================================
  @Test
  public void clickScissorsButton_updatesYouChoseOutputToScissors() {
    Intent intent = createGamePlayIntent(99);

    try (ActivityScenario<GamePlayActivity> scenario =
                 ActivityScenario.launch(intent)) {

      scenario.onActivity(activity -> {
        ImageButton scissorsButton = activity.findViewById(R.id.scissorsPlayButton);
        TextView youChoseText = activity.findViewById(R.id.youChoseOutputTextView);

        assertNotNull(scissorsButton);
        assertNotNull(youChoseText);

        scissorsButton.performClick();

        String expected = activity.getString(R.string.scissors_uppercase);
        assertEquals(expected, youChoseText.getText().toString());
      });
    }
  }

  // ========================================================================
  // 4. CASE: Tapping the "Return" TextView
  // EXPECTATION:
  //   • Activity transitions to finishing/destroyed state (user exits screen)
  // ========================================================================
  @Test
  public void clickingReturn_afterClick_activityIsClosing() {
    Intent intent = createGamePlayIntent(1);

    try (ActivityScenario<GamePlayActivity> scenario =
                 ActivityScenario.launch(intent)) {

      scenario.onActivity(activity -> {
        TextView returnText = activity.findViewById(R.id.returnSelectableTextView);
        assertNotNull(returnText);

        // Simulate navigation click
        returnText.performClick();

        // Verify Activity is shutting down
        assertTrue(activity.isFinishing() || activity.isDestroyed());
      });
    }
  }
}
