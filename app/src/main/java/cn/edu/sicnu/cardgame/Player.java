package cn.edu.sicnu.cardgame;

import android.content.Context;

// 仅在本次进程中保存玩家信息，不写入文件。
final class Player {
    static String name, gender, number, phone, email, hobby;
    static int best;
    static void init(Context c) {
        if (name != null) return;
        name = c.getString(R.string.student_name);
        gender = c.getString(R.string.male);
        number = c.getString(R.string.student_id);
        phone = email = hobby = c.getString(R.string.empty);
    }
}
