package edu.csumb.cst338.otterbots.rockpaperscissors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import static edu.csumb.cst338.otterbots.rockpaperscissors.LandingActivity.EXTRA_IS_ADMIN;

import android.content.Intent;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LandingActivityTest {

    @Test
    public void whenUserNameMissing_useDefaultPlayerName() {
        // Arrange: Intent with no username extra, isAdmin = false (user view)
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), LandingActivity.class);

        // Note: Intentionally do not put EXTRA_USERNAME.
        intent.putExtra(EXTRA_IS_ADMIN, false);

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

    // TODO: Add tests for admin vs user layouts in a future MR.
}
