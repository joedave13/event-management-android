package com.example.eventmanagement.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.eventmanagement.R;
import com.example.eventmanagement.helper.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class UpdateActivity extends AppCompatActivity {
    EditText editId, editNama, editKeterangan;
    TextView tvTanggal, tvWaktuMulai, tvWaktuSelesai;
    Button btnTanggal, btnWaktuMulai, btnWaktuSelesai, btnUpdate;
    private DatePickerDialog datePicker;
    private TimePickerDialog timePickerStart, timePickerEnd;
    private SimpleDateFormat formatter;
    Cursor cursor;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        editId = findViewById(R.id.editIdEventUpdate);
        editNama = findViewById(R.id.editNamaEventUpdate);
        editKeterangan = findViewById(R.id.editKeteranganUpdate);
        tvTanggal = findViewById(R.id.tvTanggalDipilihUpdate);
        tvWaktuMulai = findViewById(R.id.tvWaktuMulaiDipilihUpdate);
        tvWaktuSelesai = findViewById(R.id.tvWaktuSelesaiDipilihUpdate);
        btnTanggal = findViewById(R.id.btnTanggalEventUpdate);
        btnWaktuMulai = findViewById(R.id.btnWaktuMulaiUpdate);
        btnWaktuSelesai = findViewById(R.id.btnWaktuSelesaiUpdate);
        btnUpdate = findViewById(R.id.btnUpdate);
        dbHelper = new DatabaseHelper(this);
        formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        editId.setEnabled(false);

        btnTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });

        btnWaktuMulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimeStartDialog();
            }
        });

        btnWaktuSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimeEndDialog();
            }
        });

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM event WHERE nama = '" + getIntent().getStringExtra("nama") + "'", null);
        cursor.moveToFirst();

        if (cursor.getCount() > 0){
            cursor.moveToPosition(0);
            editId.setText(cursor.getString(0).toString());
            editNama.setText(cursor.getString(1).toString());
            tvTanggal.setText(cursor.getString(2).toString());
            editKeterangan.setText(cursor.getString(3).toString());
            tvWaktuMulai.setText(cursor.getString(4).toString());
            tvWaktuSelesai.setText(cursor.getString(5).toString());
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.execSQL("UPDATE event SET nama = '" + editNama.getText().toString() + "', tanggal = '" + tvTanggal.getText().toString() + "', keterangan = '" + editKeterangan.getText().toString() + "', waktu_mulai = '" + tvWaktuMulai.getText().toString() + "', waktu_selesai = '" + tvWaktuSelesai.getText().toString() + "' WHERE id = '" + editId.getText().toString() + "'");
                Toast.makeText(getApplicationContext(), "Event berhasil diubah.", Toast.LENGTH_LONG).show();
                MainActivity.ma.RefreshList();
                finish();
            }
        });
    }

    private void showDateDialog() {
        Calendar calendar = Calendar.getInstance();
        datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar date = Calendar.getInstance();
                date.set(year, month, day);
                tvTanggal.setText(formatter.format(date.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePicker.show();
    }

    private void showTimeStartDialog() {
        Calendar calendar = Calendar.getInstance();
        timePickerStart = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                tvWaktuMulai.setText(hour + " : " + minute);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(this));

        timePickerStart.show();
    }

    private void showTimeEndDialog() {
        Calendar calendar = Calendar.getInstance();
        timePickerEnd = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                tvWaktuSelesai.setText(hour + " : " + minute);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(this));

        timePickerEnd.show();
    }
}
