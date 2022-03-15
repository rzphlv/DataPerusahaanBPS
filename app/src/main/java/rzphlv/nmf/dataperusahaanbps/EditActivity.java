package rzphlv.nmf.dataperusahaanbps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class EditActivity extends AppCompatActivity {

    //Deklarasi Variable
    private EditText nama_perusahaanBaru, alamatBaru, tlp_perusahaanBaru, email_pBaru, nama_cpBaru, kontak_cpBaru;
    private EditText survei1, survei2, survei3, survei4, survei5, survei6, survei7, survei8, survei9, survei10;
    private AutoCompleteTextView kelurahanBaru, kecamatanBaru;
    private Spinner status1, status2, status3, status4, status5, status6, status7, status8, status9, status10;
    private Spinner tahun, badanBaru;
    private Button update, OK;
    private DatabaseReference database;
    private FirebaseAuth auth;
    private String cekperusahaan;
    private EditText kota, LatLng;
    private ImageView logo;
    private ProgressBar progressBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        progressBar2 = findViewById(R.id.progressBar2);
        progressBar2.setVisibility(View.GONE);

        nama_perusahaanBaru = findViewById(R.id.new_nama_p);
        badanBaru = findViewById(R.id.new_badan_usaha);
        alamatBaru = findViewById(R.id.new_alamat);
        kota = findViewById(R.id.new_kota);
        kelurahanBaru = findViewById(R.id.new_kelurahan);
        kecamatanBaru = findViewById(R.id.new_kecamatan);
        tlp_perusahaanBaru = findViewById(R.id.new_tlp_perusahaan);
        email_pBaru = findViewById(R.id.new_email_p);
        nama_cpBaru = findViewById(R.id.new_nama_cp);
        kontak_cpBaru = findViewById(R.id.new_kontak_cp);
        LatLng = findViewById(R.id.latlng_p);

        logo = findViewById(R.id.logobpscover);

        survei1 = findViewById(R.id.new_survei1);
        survei2 = findViewById(R.id.new_survei2);
        survei3 = findViewById(R.id.new_survei3);
        survei4 = findViewById(R.id.new_survei4);
        survei5 = findViewById(R.id.new_survei5);
        survei6 = findViewById(R.id.new_survei6);
        survei7 = findViewById(R.id.new_survei7);
        survei8 = findViewById(R.id.new_survei8);
        survei9 = findViewById(R.id.new_survei9);
        survei10 = findViewById(R.id.new_survei10);

        status1 = findViewById(R.id.new_status1);
        status2 = findViewById(R.id.new_status2);
        status3 = findViewById(R.id.new_status3);
        status4 = findViewById(R.id.new_status4);
        status5 = findViewById(R.id.new_status5);
        status6 = findViewById(R.id.new_status6);
        status7 = findViewById(R.id.new_status7);
        status8 = findViewById(R.id.new_status8);
        status9 = findViewById(R.id.new_status9);
        status10 = findViewById(R.id.new_status10);

        tahun = findViewById(R.id.thn);

        String[] keca = getResources().getStringArray(R.array.kecamatan);
        String[] kelu = getResources().getStringArray(R.array.kelurahan);

        kota.setText("Bandar Lampung");
        kota.setEnabled(false);

        ArrayAdapter<String> kecam = new ArrayAdapter<String>(this,
                R.layout.custom_list_item, R.id.text_view_list_item, keca);
        kecamatanBaru.setAdapter(kecam);

        ArrayAdapter<String> kelur = new ArrayAdapter<String>(this,
                R.layout.custom_list_item, R.id.text_view_list_item, kelu);
        kelurahanBaru.setAdapter(kelur);

        update = findViewById(R.id.update);

        //Mendapatkan Instance autentikasi dan Referensi dari Database
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        int number = Calendar.getInstance().get(Calendar.YEAR);
        String y = new Integer(number).toString();
        ArrayAdapter thn = (ArrayAdapter) tahun.getAdapter();
        int thnyear = thn.getPosition(y);
        tahun.setSelection(thnyear);
        getData();

        tahun.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getSurvei(tahun.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                getSurvei(tahun.getSelectedItem().toString());
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar2.setVisibility(View.VISIBLE);
                //Mendapatkan Data Perusahaan yang akan dicek
                cekperusahaan = nama_perusahaanBaru.getText().toString();

                //Mengecek agar tidak ada data yang kosong, saat proses update
                if (isEmpty(cekperusahaan)) {
                    nama_perusahaanBaru.setError("Tidak boleh kosong");
                    nama_perusahaanBaru.requestFocus();
                    //Toast.makeText(EditActivity.this, "Data tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show();
                } else {
                    //Menjalankan proses update data

                    data_perusahaan setPerusahaan = new data_perusahaan();
                    setPerusahaan.setNamaPerusahaan(nama_perusahaanBaru.getText().toString());
                    setPerusahaan.setBadan(badanBaru.getSelectedItem().toString());
                    setPerusahaan.setAlamat(alamatBaru.getText().toString());
                    setPerusahaan.setKelurahan(kelurahanBaru.getText().toString());
                    setPerusahaan.setKecamatan(kecamatanBaru.getText().toString());
                    setPerusahaan.setTlp_perusahaan(tlp_perusahaanBaru.getText().toString());
                    setPerusahaan.setEmail_p(email_pBaru.getText().toString());
                    setPerusahaan.setNama_cp(nama_cpBaru.getText().toString());
                    setPerusahaan.setKontak_cp(kontak_cpBaru.getText().toString());

                    data_perusahaan setSurvei = new data_perusahaan();
                    setSurvei.setSurvei1(survei1.getText().toString());
                    setSurvei.setSurvei2(survei2.getText().toString());
                    setSurvei.setSurvei3(survei3.getText().toString());
                    setSurvei.setSurvei4(survei4.getText().toString());
                    setSurvei.setSurvei5(survei5.getText().toString());
                    setSurvei.setSurvei6(survei6.getText().toString());
                    setSurvei.setSurvei7(survei7.getText().toString());
                    setSurvei.setSurvei8(survei8.getText().toString());
                    setSurvei.setSurvei9(survei9.getText().toString());
                    setSurvei.setSurvei10(survei10.getText().toString());

                    setSurvei.setStatus1(status1.getSelectedItem().toString());
                    setSurvei.setStatus2(status2.getSelectedItem().toString());
                    setSurvei.setStatus3(status3.getSelectedItem().toString());
                    setSurvei.setStatus4(status4.getSelectedItem().toString());
                    setSurvei.setStatus5(status5.getSelectedItem().toString());
                    setSurvei.setStatus6(status6.getSelectedItem().toString());
                    setSurvei.setStatus7(status7.getSelectedItem().toString());
                    setSurvei.setStatus8(status8.getSelectedItem().toString());
                    setSurvei.setStatus9(status9.getSelectedItem().toString());
                    setSurvei.setStatus10(status10.getSelectedItem().toString());

                    setPerusahaan.setLatlng(LatLng.getText().toString());

                    updatePerusahaan(setPerusahaan);
                    updateSurvei(setSurvei);
                }
            }
        });
    }

    // Mengecek apakah ada data yang kosong, sebelum diupdate
    private boolean isEmpty(String s) {
        return TextUtils.isEmpty(s);
    }

    //Menampilkan data yang akan di update
    private void getData() {
        final String getNamaPerusahaan = getIntent().getExtras().getString("dataNamaPerusahaan");
        final String getBadan = getIntent().getExtras().getString("dataBadan");
        final String getAlamat = getIntent().getExtras().getString("dataAlamat");
        final String getKelurahan = getIntent().getExtras().getString("dataKelurahan");
        final String getKecamatan = getIntent().getExtras().getString("dataKecamatan");
        final String getTlpPerusahaan = getIntent().getExtras().getString("dataTelponPerusahaan");
        final String getEmail = getIntent().getExtras().getString("dataEmailPerusahaan");
        final String getNamaCP = getIntent().getExtras().getString("dataNamaCP");
        final String getKontakCP = getIntent().getExtras().getString("dataKontakCP");

        final String getLatLng = getIntent().getExtras().getString("dataLatlng");

        ArrayAdapter bdn = (ArrayAdapter) badanBaru.getAdapter();

        int bdnush = bdn.getPosition(getBadan);

        nama_perusahaanBaru.setText(getNamaPerusahaan);
        badanBaru.setSelection(bdnush);
        alamatBaru.setText(getAlamat);
        kelurahanBaru.setText(getKelurahan);
        kecamatanBaru.setText(getKecamatan);
        tlp_perusahaanBaru.setText(getTlpPerusahaan);
        email_pBaru.setText(getEmail);
        nama_cpBaru.setText(getNamaCP);
        kontak_cpBaru.setText(getKontakCP);

        LatLng.setText(getLatLng);
    }

    private void getSurvei(final String year) {
        String getKey = getIntent().getExtras().getString("getPrimaryKey");
        database = FirebaseDatabase.getInstance().getReference();
        database.child("Survei").child("Perusahaan").child(getKey).child(year)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String getSurvei1 = dataSnapshot.child("survei1").getValue(String.class);
                        String getSurvei2 = dataSnapshot.child("survei2").getValue(String.class);
                        String getSurvei3 = dataSnapshot.child("survei3").getValue(String.class);
                        String getSurvei4 = dataSnapshot.child("survei4").getValue(String.class);
                        String getSurvei5 = dataSnapshot.child("survei5").getValue(String.class);
                        String getSurvei6 = dataSnapshot.child("survei6").getValue(String.class);
                        String getSurvei7 = dataSnapshot.child("survei7").getValue(String.class);
                        String getSurvei8 = dataSnapshot.child("survei8").getValue(String.class);
                        String getSurvei9 = dataSnapshot.child("survei9").getValue(String.class);
                        String getSurvei10 = dataSnapshot.child("survei10").getValue(String.class);

                        String getStatus1 = dataSnapshot.child("status1").getValue(String.class);
                        String getStatus2 = dataSnapshot.child("status2").getValue(String.class);
                        String getStatus3 = dataSnapshot.child("status3").getValue(String.class);
                        String getStatus4 = dataSnapshot.child("status4").getValue(String.class);
                        String getStatus5 = dataSnapshot.child("status5").getValue(String.class);
                        String getStatus6 = dataSnapshot.child("status6").getValue(String.class);
                        String getStatus7 = dataSnapshot.child("status7").getValue(String.class);
                        String getStatus8 = dataSnapshot.child("status8").getValue(String.class);
                        String getStatus9 = dataSnapshot.child("status9").getValue(String.class);
                        String getStatus10 = dataSnapshot.child("status10").getValue(String.class);

                        survei1.setText(getSurvei1);
                        survei2.setText(getSurvei2);
                        survei3.setText(getSurvei3);
                        survei4.setText(getSurvei4);
                        survei5.setText(getSurvei5);
                        survei6.setText(getSurvei6);
                        survei7.setText(getSurvei7);
                        survei8.setText(getSurvei8);
                        survei9.setText(getSurvei9);
                        survei10.setText(getSurvei10);

                        ArrayAdapter s1 = (ArrayAdapter) status1.getAdapter();
                        ArrayAdapter s2 = (ArrayAdapter) status2.getAdapter();
                        ArrayAdapter s3 = (ArrayAdapter) status3.getAdapter();
                        ArrayAdapter s4 = (ArrayAdapter) status4.getAdapter();
                        ArrayAdapter s5 = (ArrayAdapter) status5.getAdapter();
                        ArrayAdapter s6 = (ArrayAdapter) status6.getAdapter();
                        ArrayAdapter s7 = (ArrayAdapter) status7.getAdapter();
                        ArrayAdapter s8 = (ArrayAdapter) status8.getAdapter();
                        ArrayAdapter s9 = (ArrayAdapter) status9.getAdapter();
                        ArrayAdapter s10 = (ArrayAdapter) status10.getAdapter();

                        int st1pos = s1.getPosition(getStatus1);
                        int st2pos = s2.getPosition(getStatus2);
                        int st3pos = s3.getPosition(getStatus3);
                        int st4pos = s4.getPosition(getStatus4);
                        int st5pos = s5.getPosition(getStatus5);
                        int st6pos = s6.getPosition(getStatus6);
                        int st7pos = s7.getPosition(getStatus7);
                        int st8pos = s8.getPosition(getStatus8);
                        int st9pos = s9.getPosition(getStatus9);
                        int st10pos = s10.getPosition(getStatus10);

                        status1.setSelection(st1pos);
                        status2.setSelection(st2pos);
                        status3.setSelection(st3pos);
                        status4.setSelection(st4pos);
                        status5.setSelection(st5pos);
                        status6.setSelection(st6pos);
                        status7.setSelection(st7pos);
                        status8.setSelection(st8pos);
                        status9.setSelection(st9pos);
                        status10.setSelection(st10pos);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


    }

    //Proses Update data yang sudah ditentukan
    private void updatePerusahaan(data_perusahaan perusahaan) {
        String getKey = getIntent().getExtras().getString("getPrimaryKey");
        database.child("Data")
                .child("Perusahaan")
                .child(getKey)
                .setValue(perusahaan)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBar2.setVisibility(View.GONE);
                        Toast.makeText(EditActivity.this, "Data Berhasil diubah", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void updateSurvei(data_perusahaan survei) {
        String getKey = getIntent().getExtras().getString("getPrimaryKey");
        database.child("Survei")
                .child("Perusahaan")
                .child(getKey).child(tahun.getSelectedItem().toString())
                .setValue(survei)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBar2.setVisibility(View.GONE);
                        Toast.makeText(EditActivity.this, "Data Berhasil diubah", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}