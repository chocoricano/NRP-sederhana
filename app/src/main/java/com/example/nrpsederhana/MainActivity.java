package com.example.nrpsederhana;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText nrp, nama;
    private Button simpan, ambildata, Update, delete;
    private SQLiteDatabase dbku;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nrp = (EditText) findViewById(R.id.nrp);
        nama = (EditText) findViewById(R.id.nama);
        simpan = (Button) findViewById(R.id.Simpan);
        ambildata = (Button) findViewById(R.id.ambildata);
        Update = (Button) findViewById(R.id.update);
        delete = (Button) findViewById(R.id.delete);
        simpan.setOnClickListener(operasi);
        ambildata.setOnClickListener(operasi);
        delete.setOnClickListener(operasi);
        Update.setOnClickListener(operasi);

        dbHelper = new DBHelper(this);
        dbku = dbHelper.getWritableDatabase();
    }

    @Override
    protected void onStop() {
        dbku.close();
        super.onStop();
    }

    View.OnClickListener operasi = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.Simpan) {
                simpan();
            } else if (v.getId() == R.id.ambildata) {
                ambildata();
            } else if (v.getId() == R.id.update) {
                update();
            }
            else if (v.getId() == R.id.delete) {
                delete();
            }
        }
    };

    private void simpan() {
        ContentValues dataku = new ContentValues();

        dataku.put("nrp", nrp.getText().toString());
        dataku.put("nama", nama.getText().toString());
        dbku.insert("mhs", null, dataku);
        Toast.makeText(this, "Data Tersimpan", Toast.LENGTH_LONG).show();
    }

    @SuppressLint("Range")
    private void ambildata() {
        Cursor cur = dbku.rawQuery("SELECT * FROM mhs WHERE nrp='" +
                nrp.getText().toString() + "'", null);
        if (cur.getCount() > 0) {
            Toast.makeText(this, "Data Ditemukan Sejumlah " +
                    cur.getCount(), Toast.LENGTH_LONG).show();
            cur.moveToFirst();
            nama.setText(cur.getString(cur.getColumnIndex("nama")));
        } else
            Toast.makeText(this, "Data Tidak Ditemukan", Toast.LENGTH_LONG).show();
    }

    private void update(){   ContentValues dataku = new ContentValues();
        dataku.put("nrp",nrp.getText().toString());
        dataku.put("nama",nama.getText().toString());
        dbku.update("mhs",dataku,"nrp='"+nrp.getText().toString()+"'",null);
        Toast.makeText(this,"Data Terupdate",Toast.LENGTH_LONG).show();
    }
    private void delete()
    {
        dbku.delete("mhs","nrp='"+nrp.getText().toString()+"'",null);
        Toast.makeText(this,"Data Terhapus",Toast.LENGTH_LONG).show();
    }

    private static class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context) {
            super(context, "database_name.db", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS mhs (nrp TEXT, nama TEXT);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
