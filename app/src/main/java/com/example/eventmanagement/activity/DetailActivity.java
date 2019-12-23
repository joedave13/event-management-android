package com.example.eventmanagement.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.eventmanagement.R;
import com.example.eventmanagement.helper.DatabaseHelper;

public class DetailActivity extends AppCompatActivity {
    Cursor cursor;
    DatabaseHelper dbHelper;
    TextView tvID, tvNama;
    Button btnKembali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        dbHelper = new DatabaseHelper(this);
        tvID = findViewById(R.id.tvIDDetail);
        tvNama = findViewById(R.id.tvNamaDetail);
        btnKembali = findViewById(R.id.btnKembaliDetail);

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM event WHERE nama = '" + getIntent().getStringExtra("nama") + "'", null);
        cursor.moveToFirst();

        if (cursor.getCount() > 0){
            cursor.moveToPosition(0);
            tvID.setText(cursor.getString(0).toString());
            tvNama.setText(cursor.getString(1).toString());
        }

        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
