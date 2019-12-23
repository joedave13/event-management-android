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

public class TambahActivity extends AppCompatActivity {
    EditText editNamaEvent, editKeteranganEvent;
    TextView tvTanggal, tvWaktuMulai, tvWaktuSelesai;
    Button btnTanggal, btnWaktuMulai, btnWaktuSelesai, btnSubmit;
    private DatePickerDialog picker;
    private TimePickerDialog timePickerStart, timePickerEnd;
    private SimpleDateFormat formatter;
    Cursor cursor;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah);

        formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        editNamaEvent = findViewById(R.id.editNamaEvent);
        editKeteranganEvent = findViewById(R.id.editKeteranganEvent);
        tvTanggal = findViewById(R.id.tvTanggalDipilih);
        tvWaktuMulai = findViewById(R.id.tvWaktuMulaiDipilih);
        tvWaktuSelesai = findViewById(R.id.tvWaktuSelesaiDipilih);
        btnTanggal = findViewById(R.id.btnTanggalEvent);
        btnWaktuMulai = findViewById(R.id.btnWaktuMulai);
        btnWaktuSelesai = findViewById(R.id.btnWaktuSelesai);
        btnSubmit = findViewById(R.id.btnSubmitTambah);
        dbHelper = new DatabaseHelper(this);

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

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.execSQL("INSERT INTO event (nama, tanggal, keterangan, waktu_mulai, waktu_selesai) VALUES ('" + editNamaEvent.getText().toString() + "', '" + tvTanggal.getText().toString() + "', '" + editKeteranganEvent.getText().toString() + "', '" + tvWaktuMulai.getText().toString() + "', '" + tvWaktuSelesai.getText().toString() + "');");
                Toast.makeText(getApplicationContext(), "Event Ditambahkan", Toast.LENGTH_SHORT).show();
                MainActivity.ma.RefreshList();
                finish();
            }
        });
    }

    private void showDateDialog(){
        Calendar calendar = Calendar.getInstance();
        picker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar date = Calendar.getInstance();
                date.set(year, month, day);

                tvTanggal.setText(formatter.format(date.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        picker.show();
    }

    private void showTimeStartDialog(){
        Calendar calendar = Calendar.getInstance();
        timePickerStart = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                tvWaktuMulai.setText(hour + " : " + minute);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(this));

        timePickerStart.show();
    }

    private void showTimeEndDialog(){
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
