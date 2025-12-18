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
 * UI tests for GamePlayActivity. These tests focus on user input → UI output and navigation,
 * without trying to control or assert the random NPC choice or DB updates.
 */
@RunWith(AndroidJUnit4.class)
public class GamePlayActivityTest {

  private Intent createGamePlayIntent(int userId) {
    return GamePlayActivity.gamePlayActivityIntentFactory(
        ApplicationProvider.getApplicationContext(),
        userId
    );
  }

  // ------------------------------------------------------------
  // 1. Clicking "ROCK" sets the "you chose" output text
  // ------------------------------------------------------------
  @Test
  public void clickRockButton_updatesYouChoseOutputToRock() {
    Intent intent = createGamePlayIntent(42); // any userId is fine for UI test

    try (ActivityScenario<GamePlayActivity> scenario =
        ActivityScenario.launch(intent)) {

      scenario.onActivity(activity -> {
        ImageButton rockButton = activity.findViewById(R.id.rockPlayButton);
        TextView youChoseText = activity.findViewById(R.id.youChoseOutputTextView);

        assertNotNull(rockButton);
        assertNotNull(youChoseText);

        // Before click, it might be empty or default; we only care about after.
        rockButton.performClick();

        // ASSERT: "You chose" text reflects ROCK (using the same resource as the activity)
        String expected = activity.getString(R.string.rock_uppercase);
        assertEquals(expected, youChoseText.getText().toString());
      });
    }
  }

  // ------------------------------------------------------------
  // 2. Clicking "PAPER" sets the "you chose" output text
  // ------------------------------------------------------------
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

  // ------------------------------------------------------------
  // 3. Clicking "SCISSORS" sets the "you chose" output text
  // ------------------------------------------------------------
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

  // ------------------------------------------------------------
  // 4. Clicking "return" finishes the activity
  // ------------------------------------------------------------
  @Test
  public void clickingReturn_afterClick_activityIsClosing() {
    Intent intent = createGamePlayIntent(1);

    try (ActivityScenario<GamePlayActivity> scenario =
        ActivityScenario.launch(intent)) {

      scenario.onActivity(activity -> {
        TextView returnText = activity.findViewById(R.id.returnSelectableTextView);
        assertNotNull(returnText);

        returnText.performClick();

        assertTrue(activity.isFinishing() || activity.isDestroyed());
      });
    }
  }
}
