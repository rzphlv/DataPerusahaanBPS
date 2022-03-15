package rzphlv.nmf.dataperusahaanbps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TambahPerusahaan extends AppCompatActivity implements View.OnClickListener {

    //Deklarasi Variable
    private ProgressBar progressBar;
    private EditText nama_perusahaan, alamat, kota, tlp_perusahaan, email_p, nama_cp, kontak_cp, latlng;
    private AutoCompleteTextView kelurahan, kecamatan;
    private Spinner badan_usaha;
    private FirebaseAuth auth;
    private Button Simpan, batalkan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_perusahaan);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);

        //Inisialisasi ID (Button)
        Simpan = findViewById(R.id.save);
        Simpan.setOnClickListener(this);

        batalkan = findViewById(R.id.batalkan);
        batalkan.setOnClickListener(this);

        auth = FirebaseAuth.getInstance(); //Mendapakan Instance Firebase Autentifikasi

        String[] keca = getResources().getStringArray(R.array.kecamatan);
        String[] kelu = getResources().getStringArray(R.array.kelurahan);

        //Inisialisasi ID (EditText)
        nama_perusahaan = findViewById(R.id.nama_perusahaan);
        badan_usaha = findViewById(R.id.badan_usaha);
        alamat = findViewById(R.id.alamat);
        kelurahan = findViewById(R.id.kelurahan);
        kecamatan = findViewById(R.id.kecamatan);
        tlp_perusahaan = findViewById(R.id.tlp_perusahaan);
        email_p = findViewById(R.id.email_p);
        nama_cp = findViewById(R.id.nama_cp);
        kontak_cp = findViewById(R.id.kontak_cp);
        latlng = findViewById(R.id.latlng_p);
        kota = findViewById(R.id.kota);

        ArrayAdapter<String> kecam = new ArrayAdapter<String>(this,
                R.layout.custom_list_item, R.id.text_view_list_item, keca);
        kecamatan.setAdapter(kecam);

        ArrayAdapter<String> kelur = new ArrayAdapter<String>(this,
                R.layout.custom_list_item, R.id.text_view_list_item, kelu);
        kelurahan.setAdapter(kelur);

        kota.setText("Bandar Lampung");
        kota.setEnabled(false);
    }

    // Mengecek apakah ada data yang kosong, akan digunakan pada Tutorial Selanjutnya
    private boolean isEmpty(String s) {
        return TextUtils.isEmpty(s);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.save:
                progressBar.setVisibility(View.VISIBLE);
                //Mendapatkan UserID dari pengguna yang Terautentikasi

                //Mendapatkan Instance dari Database
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference getReference;
                //Menyimpan Data yang diinputkan User kedalam Variable
                String getNamaPerusahaan = nama_perusahaan.getText().toString();
                String getBadan = badan_usaha.getSelectedItem().toString();
                String getAlamat = alamat.getText().toString();
                String getKelurahan = kelurahan.getText().toString();
                String getKecamatan = kecamatan.getText().toString();
                String getTlpPerusahaan = tlp_perusahaan.getText().toString();
                String getEmailP = email_p.getText().toString();
                String getNamaCP = nama_cp.getText().toString();
                String getKontakCP = kontak_cp.getText().toString();
                String getLatlng = latlng.getText().toString();

                getReference = database.getReference(); // Mendapatkan Referensi dari Database
                // Mengecek apakah ada data yang kosong
                if (isEmpty(getNamaPerusahaan)) {
                    //Jika Ada, maka akan menampilkan pesan singkan seperti berikut ini.
                    nama_perusahaan.setError("Nama perusahaan tidak boleh kosong");
                    nama_perusahaan.requestFocus();
                    progressBar.setVisibility(View.GONE);
                } else {

                    getReference.child("Data").child("Perusahaan").push()
                            .setValue(new data_perusahaan(getNamaPerusahaan, getBadan, getAlamat, getKelurahan, getKecamatan,
                                    getTlpPerusahaan, getEmailP, getNamaCP, getKontakCP, getLatlng))
                            .addOnSuccessListener(this, new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    //Peristiwa ini terjadi saat user berhasil menyimpan datanya kedalam Database
                                    progressBar.setVisibility(View.GONE);
                                    nama_perusahaan.setText("");
                                    alamat.setText("");
                                    kelurahan.setText("");
                                    kecamatan.setSelected(false);
                                    tlp_perusahaan.setText("");
                                    email_p.setText("");
                                    nama_cp.setText("");
                                    kontak_cp.setText("");
                                    latlng.setText("");
                                    Toast.makeText(TambahPerusahaan.this, "Data Tersimpan", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                break;

            case R.id.batalkan:
                finish();
                // Digunakan untuk melihat data yang sudah tersimpan didalam Database
                // Akan digunakan pada Tutorial Berikutnya, mengenai penggunaan Fungsi Read
                break;
        }
    }
}
