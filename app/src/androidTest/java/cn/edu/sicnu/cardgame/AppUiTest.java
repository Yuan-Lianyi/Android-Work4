package cn.edu.sicnu.cardgame;

import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.*;

@SuppressWarnings("deprecation")
public class AppUiTest extends ActivityInstrumentationTestCase2<MainActivity> {
    public AppUiTest() { super(MainActivity.class); }
    public void testEditPlayAndRestart() throws Throwable {
        MainActivity main = getActivity();
        Instrumentation.ActivityMonitor editMonitor = getInstrumentation().addMonitor(EditActivity.class.getName(), null, false);
        runTestOnUiThread(() -> main.findViewById(R.id.edit).performClick());
        Activity edit = editMonitor.waitForActivityWithTimeout(5000);
        assertNotNull(edit);
        String original = Player.name;
        runTestOnUiThread(() -> {
            ((EditText)edit.findViewById(R.id.name)).setText(R.string.game);
            edit.findViewById(R.id.save).performClick();
        });
        getInstrumentation().waitForIdleSync();
        assertEquals(main.getString(R.string.game), Player.name);
        assertTrue(((TextView)main.findViewById(R.id.profile)).getText().toString().contains(Player.name));
        Instrumentation.ActivityMonitor gameMonitor = getInstrumentation().addMonitor(GameActivity.class.getName(), null, false);
        runTestOnUiThread(() -> main.findViewById(R.id.play).performClick());
        Activity game = gameMonitor.waitForActivityWithTimeout(5000);
        assertNotNull(game);
        getInstrumentation().waitForIdleSync();
        android.view.View firstCard = ((GridLayout)game.findViewById(R.id.cards)).getChildAt(0);
        assertTrue(firstCard.getWidth() > 0);
        assertEquals(87f / 57f, (float)firstCard.getHeight() / firstCard.getWidth(), 0.02f);
        runTestOnUiThread(() -> ((GridLayout)game.findViewById(R.id.cards)).getChildAt(0).performClick());
        assertEquals(game.getString(R.string.score, -1), ((TextView)game.findViewById(R.id.score)).getText().toString());
        runTestOnUiThread(() -> game.findViewById(R.id.restart).performClick());
        assertEquals(game.getString(R.string.score, 0), ((TextView)game.findViewById(R.id.score)).getText().toString());
        runTestOnUiThread(() -> { game.findViewById(R.id.exit).performClick(); Player.name = original; });
        getInstrumentation().removeMonitor(editMonitor);
        getInstrumentation().removeMonitor(gameMonitor);
    }
}
