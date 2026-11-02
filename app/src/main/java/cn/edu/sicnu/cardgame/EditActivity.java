package cn.edu.sicnu.cardgame;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

public class EditActivity extends Activity {
    private final int[] fields = {R.id.name, R.id.gender, R.id.number, R.id.phone, R.id.email, R.id.hobby};
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        Player.init(this);
        setContentView(R.layout.activity_edit);
        String[] values = {Player.name, Player.gender, Player.number, Player.phone, Player.email, Player.hobby};
        if (state == null) for (int i = 0; i < fields.length; i++) ((EditText)findViewById(fields[i])).setText(values[i]);
        findViewById(R.id.save).setOnClickListener(v -> {
            Player.name = value(R.id.name); Player.gender = value(R.id.gender);
            Player.number = value(R.id.number); Player.phone = value(R.id.phone);
            Player.email = value(R.id.email); Player.hobby = value(R.id.hobby);
            finish();
        });
        findViewById(R.id.cancel).setOnClickListener(v -> finish());
    }
    private String value(int id) { return ((EditText)findViewById(id)).getText().toString().trim(); }
}
