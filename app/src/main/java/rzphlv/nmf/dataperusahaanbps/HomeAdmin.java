package rzphlv.nmf.dataperusahaanbps;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import rzphlv.nmf.dataperusahaanbps.RecyclerViewAdapter.dataListener;

public class HomeAdmin extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, dataListener {

    boolean doubleBackToExitPressedOnce = false;
    FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference reference;
    private ArrayList<data_perusahaan> dataPerusahaan;
    private SearchView searchView;
    ProgressBar load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mAuth = FirebaseAuth.getInstance();

        load = findViewById(R.id.loads);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        if (mAuth.getCurrentUser() == null) {
            navigationView.getMenu().findItem(R.id.nav_tambahadmin).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_tambahperusahaan).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_editprofil).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_editprofil).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
        }

        if (mAuth.getCurrentUser() != null) {
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
        }

        recyclerView = findViewById(R.id.tampilanRecyclerAdmin);

        searchView = findViewById(R.id.searchkonten);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                s = searchView.getQuery().toString();
                getSearch(s);
                return true;
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && searchView.getVisibility() == View.VISIBLE) {
                    searchView.clearFocus();
                } else if (dy < 0 && searchView.getVisibility() != View.VISIBLE) {
                    searchView.clearFocus();
                }
            }
        });

        getSupportActionBar().setTitle("Direktori Survei Perusahaan");
        MyRecyclerView();
        getData();
    }


    @Override
    protected void onStart() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView mNama = headerView.findViewById(R.id.nama);
        TextView mEmail = headerView.findViewById(R.id.email);
        if (mAuth.getCurrentUser() == null) {
            navigationView.setVisibility(View.INVISIBLE);
        }
        if (mAuth.getCurrentUser() != null) {
            if (mAuth.getCurrentUser().getDisplayName() != null) {
                mNama.setText(mAuth.getCurrentUser().getDisplayName());
            }
            mEmail.setText(mAuth.getCurrentUser().getEmail());
        }
        super.onStart();
    }

    //Berisi baris kode untuk mengambil data dari Database dan menampilkannya kedalam Adapter
    private void getData() {
        //Mendapatkan Referensi Database
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Data").child("Perusahaan").orderByChild("namaPerusahaan")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Inisialisasi ArrayList
                        dataPerusahaan = new ArrayList<>();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            //Mapping data pada DataSnapshot ke dalam objek mahasiswa
                            data_perusahaan perusahaan = snapshot.getValue(data_perusahaan.class);

                            //Mengambil Primary Key, digunakan untuk proses Update dan Delete
                            perusahaan.setKey(snapshot.getKey());
                            dataPerusahaan.add(perusahaan);
                        }

                        //Inisialisasi Adapter dan data Mahasiswa dalam bentuk Array
                        adapter = new RecyclerViewAdapter(dataPerusahaan, HomeAdmin.this);

                        //Memasang Adapter pada RecyclerView
                        recyclerView.setAdapter(adapter);
                        load.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
              /*
                Kode ini akan dijalankan ketika ada error dan
                pengambilan data error tersebut lalu memprint error nya
                ke LogCat
               */
                        Toast.makeText(getApplicationContext(), "Data Gagal Dimuat", Toast.LENGTH_LONG).show();
                        Log.e("MyListActivity", databaseError.getDetails() + " " + databaseError.getMessage());
                    }
                });
    }

    private void MyRecyclerView() {
        //Menggunakan Layout Manager, Dan Membuat List Secara Vertical
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //Membuat Underline pada Setiap Item Didalam List
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.line));
        recyclerView.addItemDecoration(itemDecoration);
    }

    @Override
    public void onDeleteData(data_perusahaan data, int position) {
        /*
         * Kode ini akan dipanggil ketika method onDeleteData
         * dipanggil dari adapter pada RecyclerView melalui interface.
         * kemudian akan menghapus data berdasarkan primary key dari data tersebut
         * Jika berhasil, maka akan memunculkan Toast
         */
        if (reference != null) {
            reference.child("Data")
                    .child("Perusahaan")
                    .child(data.getKey())
                    .removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(HomeAdmin.this, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                        }
                    });
            reference.child("Survei")
                    .child("Perusahaan")
                    .child(data.getKey())
                    .removeValue();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_tambahperusahaan) {
            startActivity(new Intent(this, TambahPerusahaan.class));
        } else if (id == R.id.nav_tambahadmin) {
            startActivity(new Intent(this, SignUpActivity.class));
        } else if (id == R.id.nav_login) {
            startActivity(new Intent(this, LoginActivity.class));
        } else if (id == R.id.nav_editprofil) {
            startActivity(new Intent(this, ProfileActivity.class));
        } else if (id == R.id.nav_info){
            startActivity (new Intent (this, Info.class));
        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getSearch(final String searchText) {
        //Mendapatkan Referensi Database
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Data").child("Perusahaan").orderByChild("namaPerusahaan"
        ).startAt(searchText.toUpperCase()).endAt(searchText + "\uf8ff")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Inisialisasi ArrayList
                        dataPerusahaan = new ArrayList<>();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            //Mapping data pada DataSnapshot ke dalam objek mahasiswa
                            data_perusahaan perusahaan = snapshot.getValue(data_perusahaan.class);

                            //Mengambil Primary Key, digunakan untuk proses Update dan Delete
                            perusahaan.setKey(snapshot.getKey());
                            dataPerusahaan.add(perusahaan);
                        }

                        //Inisialisasi Adapter dan data Mahasiswa dalam bentuk Array
                        adapter = new RecyclerViewAdapter(dataPerusahaan, HomeAdmin.this);

                        //Memasang Adapter pada RecyclerView
                        recyclerView.setAdapter(adapter);
                        load.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
              /*
                Kode ini akan dijalankan ketika ada error dan
                pengambilan data error tersebut lalu memprint error nya
                ke LogCat
               */
                        Toast.makeText(getApplicationContext(), "Data Gagal Dimuat", Toast.LENGTH_LONG).show();
                        Log.e("MyListActivity", databaseError.getDetails() + " " + databaseError.getMessage());
                    }
                });
    }

}
