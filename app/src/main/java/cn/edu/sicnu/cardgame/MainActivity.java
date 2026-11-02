package cn.edu.sicnu.cardgame;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
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
}
