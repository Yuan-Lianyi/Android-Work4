package cn.edu.sicnu.cardgame;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends Activity {
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        Player.init(this);
        setContentView(R.layout.activity_main);
        findViewById(R.id.edit).setOnClickListener(v -> startActivity(new Intent(this, EditActivity.class)));
        findViewById(R.id.play).setOnClickListener(v -> startActivity(new Intent(this, GameActivity.class)));
    }
    @Override protected void onResume() {
        super.onResume();
        ((TextView)findViewById(R.id.profile)).setText(getString(R.string.profile,
            Player.name, Player.gender, Player.number, Player.phone, Player.email, Player.hobby, Player.best));
    }
    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_about) {
            showAboutDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /** 菜单项“关于”：弹出应用说明对话框。 */
    private void showAboutDialog() {
        new AlertDialog.Builder(this)
            .setTitle(R.string.about_title)
            .setMessage(getString(R.string.about_message, Player.name, Player.number))
            .setPositiveButton(R.string.about_ok, null)
            .show();
    }
}