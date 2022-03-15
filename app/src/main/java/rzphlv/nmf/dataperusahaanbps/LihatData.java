package rzphlv.nmf.dataperusahaanbps;

import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class LihatData extends AppCompatActivity {
    private TextView nama_perusahaanBaru, alamatBaru, kota, kelurahanBaru, kecamatanBaru, tlp_perusahaanBaru, email_pBaru, nama_cpBaru, kontak_cpBaru;
    private TextView survei1, survei2, survei3, survei4, survei5, survei6, survei7, survei8, survei9, survei10;
    private TextView status1, status2, status3, status4, status5, status6, status7, status8, status9, status10;
    private TextView s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, sp;
    private ImageButton callPr, callCp;
    private Button Lokasi;
    private Spinner tahun;
    private DatabaseReference database;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_data);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAuth = FirebaseAuth.getInstance();

        nama_perusahaanBaru = findViewById(R.id.new_nama_p);
        alamatBaru = findViewById(R.id.new_alamat);
        kota = findViewById(R.id.new_kota);
        kelurahanBaru = findViewById(R.id.new_kelurahan);
        kecamatanBaru = findViewById(R.id.new_kecamatan);
        tlp_perusahaanBaru = findViewById(R.id.new_tlp_perusahaan);
        email_pBaru = findViewById(R.id.new_email_p);
        nama_cpBaru = findViewById(R.id.new_nama_cp);
        kontak_cpBaru = findViewById(R.id.new_kontak_cp);
        Lokasi = findViewById(R.id.lokasi);

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

        s1 = findViewById(R.id.s1);
        s2 = findViewById(R.id.s2);
        s3 = findViewById(R.id.s3);
        s4 = findViewById(R.id.s4);
        s5 = findViewById(R.id.s5);
        s6 = findViewById(R.id.s6);
        s7 = findViewById(R.id.s7);
        s8 = findViewById(R.id.s8);
        s9 = findViewById(R.id.s9);
        s10 = findViewById(R.id.s10);

        sp = findViewById(R.id.sp);

        callPr = findViewById(R.id.callPr);
        callCp = findViewById(R.id.callCp);

        kota.setText("Bandar Lampung");

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

        final String getLatlng = getIntent().getExtras().getString("dataLatlng");
        if (isEmpty(getLatlng)) {
            Lokasi.setEnabled(false);
            Lokasi.setText("Lokasi belum ada");
        }
        Lokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + getLatlng + "");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        });

        if (mAuth.getCurrentUser() == null) {
            nama_cpBaru.setVisibility(View.GONE);
            kontak_cpBaru.setVisibility(View.GONE);
            callCp.setVisibility(View.GONE);
            TextView jdlNamaCP = findViewById(R.id.judulNamaCP);
            jdlNamaCP.setVisibility(View.GONE);
            TextView jdlKontakCP = findViewById(R.id.judulKontakCP);
            jdlKontakCP.setVisibility(View.GONE);
        }

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

        if (getBadan != null) {
            if (getBadan.contentEquals("Lainnya")) {
                nama_perusahaanBaru.setText(getNamaPerusahaan);
            } else {
                nama_perusahaanBaru.setText(getNamaPerusahaan + ", " + getBadan);
            }
        }

        if (getBadan == null) {
            nama_perusahaanBaru.setText(getNamaPerusahaan);
        }


        alamatBaru.setText(getAlamat);
        kelurahanBaru.setText(getKelurahan);
        kecamatanBaru.setText(getKecamatan);
        tlp_perusahaanBaru.setText(getTlpPerusahaan);
        email_pBaru.setText(getEmail);
        nama_cpBaru.setText(getNamaCP);
        kontak_cpBaru.setText(getKontakCP);

        if (isEmpty(getTlpPerusahaan)) {
            callPr.setEnabled(false);
        }
        if (isEmpty(getKontakCP)) {
            callCp.setEnabled(false);
        }

        callPr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + getTlpPerusahaan));
                startActivity(callIntent);
            }
        });

        callCp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + getKontakCP));
                startActivity(callIntent);
            }
        });


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

                        status1.setText(getStatus1);
                        status2.setText(getStatus2);
                        status3.setText(getStatus3);
                        status4.setText(getStatus4);
                        status5.setText(getStatus5);
                        status6.setText(getStatus6);
                        status7.setText(getStatus7);
                        status8.setText(getStatus8);
                        status9.setText(getStatus9);
                        status10.setText(getStatus10);

                        if (mAuth.getCurrentUser() == null){
                            sp.setVisibility(View.GONE);
                            RelativeLayout thn = findViewById(R.id.layoutS);
                            thn.setVisibility(View.GONE);
                        }

                        if (mAuth.getCurrentUser() != null) {
                            if (!isEmpty(getSurvei1)) {
                                s1.setVisibility(View.VISIBLE);
                                survei1.setVisibility(View.VISIBLE);
                                status1.setVisibility(View.VISIBLE);
                            }
                            if (!isEmpty(getSurvei2)) {
                                s2.setVisibility(View.VISIBLE);
                                survei2.setVisibility(View.VISIBLE);
                                status2.setVisibility(View.VISIBLE);
                            }
                            if (!isEmpty(getSurvei3)) {
                                s3.setVisibility(View.VISIBLE);
                                survei3.setVisibility(View.VISIBLE);
                                status3.setVisibility(View.VISIBLE);
                            }
                            if (!isEmpty(getSurvei4)) {
                                s4.setVisibility(View.VISIBLE);
                                survei4.setVisibility(View.VISIBLE);
                                status4.setVisibility(View.VISIBLE);
                            }
                            if (!isEmpty(getSurvei5)) {
                                s5.setVisibility(View.VISIBLE);
                                survei5.setVisibility(View.VISIBLE);
                                status5.setVisibility(View.VISIBLE);
                            }
                            if (!isEmpty(getSurvei6)) {
                                s6.setVisibility(View.VISIBLE);
                                survei6.setVisibility(View.VISIBLE);
                                status6.setVisibility(View.VISIBLE);
                            }
                            if (!isEmpty(getSurvei7)) {
                                s7.setVisibility(View.VISIBLE);
                                survei7.setVisibility(View.VISIBLE);
                                status7.setVisibility(View.VISIBLE);
                            }
                            if (!isEmpty(getSurvei8)) {
                                s8.setVisibility(View.VISIBLE);
                                survei8.setVisibility(View.VISIBLE);
                                status8.setVisibility(View.VISIBLE);
                            }
                            if (!isEmpty(getSurvei9)) {
                                s9.setVisibility(View.VISIBLE);
                                survei9.setVisibility(View.VISIBLE);
                                status9.setVisibility(View.VISIBLE);
                            }
                            if (!isEmpty(getSurvei10)) {
                                s10.setVisibility(View.VISIBLE);
                                survei10.setVisibility(View.VISIBLE);
                                status10.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


    }
}
