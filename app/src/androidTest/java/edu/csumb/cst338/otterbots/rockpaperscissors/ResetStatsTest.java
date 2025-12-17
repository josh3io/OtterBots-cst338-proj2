package edu.csumb.cst338.otterbots.rockpaperscissors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Intent;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ResetStatsTest {
    @Test
        public void launches_andTitleUsesUsernameFromIntent() {
            Intent intent = ResetStats.createIntent(
                    ApplicationProvider.getApplicationContext(),
                    -1,
                    "Murtaza"
            );

            try (ActivityScenario<ResetStats> scenario = ActivityScenario.launch(intent)) {

                scenario.onActivity(activity -> {
                    TextView title = activity.findViewById(R.id.Title);
                    assertNotNull(title);

                    String titleText = title.getText().toString();

                    assertTrue(titleText.contains("Murtaza"));
                    assertTrue(titleText.toLowerCase().contains("stats"));
                });
            }
        }

        @Test
        public void viewsExist_buttonsAndLabelsExist() {
            Intent intent = ResetStats.createIntent(
                    ApplicationProvider.getApplicationContext(),
                    -1,
                    "TestUser"
            );

            try (ActivityScenario<ResetStats> scenario = ActivityScenario.launch(intent)) {

                scenario.onActivity(activity -> {
                    assertNotNull(activity.findViewById(R.id.Title));

                    assertNotNull(activity.findViewById(R.id.WinsLabel));
                    assertNotNull(activity.findViewById(R.id.LossesLabel));
                    assertNotNull(activity.findViewById(R.id.TiesLabel));
                    assertNotNull(activity.findViewById(R.id.CurrentStreakLabel));
                    assertNotNull(activity.findViewById(R.id.MaxStreakLabel));
                    assertNotNull(activity.findViewById(R.id.GamesPlayedLabel));

                    assertNotNull(activity.findViewById(R.id.ConfirmResetButton));
                    assertNotNull(activity.findViewById(R.id.ReturnButton));
                });
            }
        }
}
