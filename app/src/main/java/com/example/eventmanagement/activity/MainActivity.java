package com.example.eventmanagement.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.eventmanagement.R;
import com.example.eventmanagement.helper.DatabaseHelper;

public class MainActivity extends AppCompatActivity {
    String[] daftarKegiatan;
    ListView listKegiatan;
    Button btnTambahEvent;
    Menu menu;
    protected Cursor cursor;
    DatabaseHelper dbHelper;
    public static MainActivity ma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTambahEvent = findViewById(R.id.btnTambahEvent);
        listKegiatan = findViewById(R.id.listEvent);

        btnTambahEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), TambahActivity.class));
            }
        });

        ma = this;
        dbHelper = new DatabaseHelper(this);
        RefreshList();
    }

    public void RefreshList(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM event", null);
        daftarKegiatan = new String[cursor.getCount()];
        cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount(); i++){
            cursor.moveToPosition(i);
            daftarKegiatan[i] = cursor.getString(1).toString();
        }

        listKegiatan.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, daftarKegiatan));
        listKegiatan.setSelected(true);
        listKegiatan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {
                final String selection = daftarKegiatan[arg2];
                final CharSequence[] dialogItem = {"Detail", "Update", "Hapus"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Pilihan");
                builder.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                                intent.putExtra("nama", selection);
                                startActivity(intent);
                                break;
                            case 1:
                                Intent intent1 = new Intent(getApplicationContext(), UpdateActivity.class);
                                intent1.putExtra("nama", selection);
                                startActivity(intent1);
                                break;
                            case 2:
                                SQLiteDatabase db = dbHelper.getWritableDatabase();
                                db.execSQL("DELETE FROM event WHERE nama = '" + selection + "'");
                                Toast.makeText(getApplicationContext(), "Event telah dihapus.", Toast.LENGTH_LONG).show();
                                ma.RefreshList();
                                break;
                        }
                    }
                });
                builder.create().show();
            }
        });
        ((ArrayAdapter)listKegiatan.getAdapter()).notifyDataSetInvalidated();
    }
}
