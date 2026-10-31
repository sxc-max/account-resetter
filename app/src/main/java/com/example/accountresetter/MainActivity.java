package com.example.accountresetter;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.*;

import java.util.UUID;

public class MainActivity extends Activity {
    private android.content.SharedPreferences prefs;
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences("test_account", MODE_PRIVATE);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(1);
        layout.setPadding(35, 50, 35, 35);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView title = new TextView(this);
        title.setText("账号重置器");
        title.setTextSize(28);
        layout.addView(title);

        status = new TextView(this);
        status.setTextSize(18);
        layout.addView(status);

        Button create = new Button(this);
        create.setText("创建 / 重新生成测试账号");
        layout.addView(create);

        Button reset = new Button(this);
        reset.setText("一键重置");
        layout.addView(reset);

        Button delete = new Button(this);
        delete.setText("删除本地测试账号");
        layout.addView(delete);

        TextView note = new TextView(this);
        note.setText("仅管理本应用的本地测试账号，不影响微信或其他游戏账号。");
        layout.addView(note);

        setContentView(layout);
        refresh();

        create.setOnClickListener(v -> generate());

        reset.setOnClickListener(v ->
            new AlertDialog.Builder(this)
                .setTitle("确认重置")
                .setMessage("将生成一个新的本地测试账号。")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", (d, w) -> generate())
                .show()
        );

        delete.setOnClickListener(v ->
            new AlertDialog.Builder(this)
                .setTitle("确认删除")
                .setMessage("删除本应用保存的测试账号？")
                .setNegativeButton("取消", null)
                .setPositiveButton("删除", (d, w) -> {
                    prefs.edit().clear().apply();
                    refresh();
                })
                .show()
        );
    }

    private void generate() {
        String id = "TEST-" +
            UUID.randomUUID().toString()
            .substring(0, 8).toUpperCase();

        prefs.edit().putString("account", id).apply();
        refresh();
    }

    private void refresh() {
        String id = prefs.getString("account", null);

        status.setText(id == null
            ? "当前没有测试账号"
            : "当前测试账号：\\n" + id);
    }
}
