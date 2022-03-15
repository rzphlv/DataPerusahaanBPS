package rzphlv.nmf.dataperusahaanbps;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

//Class Adapter ini Digunakan Untuk Mengatur Bagaimana Data akan Ditampilkan
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {


    //Deklarasi Variable
    private ArrayList<data_perusahaan> listPerusahaan;
    private Context context;

    //Membuat Interfece
    public interface dataListener {
        void onDeleteData(data_perusahaan data, int position);
    }

    //Deklarasi objek dari Interfece
    dataListener listener;

    //Membuat Konstruktor, untuk menerima input dari Database
    public RecyclerViewAdapter(ArrayList<data_perusahaan> listPerusahaan, Context context) {
        this.listPerusahaan = listPerusahaan;
        this.context = context;
        listener = (HomeAdmin) context;
    }

    //ViewHolder Digunakan Untuk Menyimpan Referensi Dari View-View
    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView namaP, alamatP;
        private LinearLayout ListItem;

        ViewHolder(View itemView) {
            super(itemView);
            //Menginisialisasi View-View yang terpasang pada layout RecyclerView kita
            namaP = itemView.findViewById(R.id.nama_p);
            alamatP = itemView.findViewById(R.id.alamat_p);
            ListItem = itemView.findViewById(R.id.list_item);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Membuat View untuk Menyiapkan dan Memasang Layout yang Akan digunakan pada RecyclerView
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_design, parent, false);
        return new ViewHolder(V);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //Mengambil Nilai/Value yenag terdapat pada RecyclerView berdasarkan Posisi Tertentu
        final String nama_p = listPerusahaan.get(position).getNamaPerusahaan();
        final String alamat_p = listPerusahaan.get(position).getAlamat();
        final String badan_p = listPerusahaan.get(position).getBadan();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        if (badan_p != null){
            if (badan_p.contentEquals("Lainnya")){
                holder.namaP.setText(nama_p);
            }
            else {
                holder.namaP.setText(nama_p+", "+badan_p);
            }
        }
        if (badan_p == null){
            holder.namaP.setText(nama_p);
        }

        holder.alamatP.setText(alamat_p);


        holder.ListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                                  Berpindah Activity pada halaman layout updateData
                                  dan mengambil data pada listMahasiswa, berdasarkan posisinya
                                  untuk dikirim pada activity updateData
                                 */
                Bundle bundle = new Bundle();
                bundle.putString("dataNamaPerusahaan", listPerusahaan.get(position).getNamaPerusahaan());
                bundle.putString("dataBadan", listPerusahaan.get(position).getBadan());
                bundle.putString("dataAlamat", listPerusahaan.get(position).getAlamat());
                bundle.putString("dataKelurahan", listPerusahaan.get(position).getKelurahan());
                bundle.putString("dataKecamatan", listPerusahaan.get(position).getKecamatan());
                bundle.putString("dataTelponPerusahaan", listPerusahaan.get(position).getTlp_perusahaan());
                bundle.putString("dataEmailPerusahaan", listPerusahaan.get(position).getEmail_p());
                bundle.putString("dataEmailPerusahaan", listPerusahaan.get(position).getEmail_p());
                bundle.putString("dataNamaCP", listPerusahaan.get(position).getNama_cp());
                bundle.putString("dataKontakCP", listPerusahaan.get(position).getKontak_cp());
                bundle.putString("dataSurvei1", listPerusahaan.get(position).getSurvei1());
                bundle.putString("dataSurvei2", listPerusahaan.get(position).getSurvei2());
                bundle.putString("dataSurvei3", listPerusahaan.get(position).getSurvei3());
                bundle.putString("dataSurvei4", listPerusahaan.get(position).getSurvei4());
                bundle.putString("dataSurvei5", listPerusahaan.get(position).getSurvei5());
                bundle.putString("dataSurvei6", listPerusahaan.get(position).getSurvei6());
                bundle.putString("dataSurvei7", listPerusahaan.get(position).getSurvei7());
                bundle.putString("dataSurvei8", listPerusahaan.get(position).getSurvei8());
                bundle.putString("dataSurvei9", listPerusahaan.get(position).getSurvei9());
                bundle.putString("dataSurvei10", listPerusahaan.get(position).getSurvei10());
                bundle.putString("dataStatus1", listPerusahaan.get(position).getStatus1());
                bundle.putString("dataStatus2", listPerusahaan.get(position).getStatus2());
                bundle.putString("dataStatus3", listPerusahaan.get(position).getStatus3());
                bundle.putString("dataStatus4", listPerusahaan.get(position).getStatus4());
                bundle.putString("dataStatus5", listPerusahaan.get(position).getStatus5());
                bundle.putString("dataStatus6", listPerusahaan.get(position).getStatus6());
                bundle.putString("dataStatus7", listPerusahaan.get(position).getStatus7());
                bundle.putString("dataStatus8", listPerusahaan.get(position).getStatus8());
                bundle.putString("dataStatus9", listPerusahaan.get(position).getStatus9());
                bundle.putString("dataStatus10", listPerusahaan.get(position).getStatus10());
                bundle.putString("dataLatlng", listPerusahaan.get(position).getLatlng());
                bundle.putString("getPrimaryKey", listPerusahaan.get(position).getKey());
                Intent intent = new Intent(v.getContext(), LihatData.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        if (mAuth.getCurrentUser() != null) {
            //Menampilkan Menu Update dan Delete saat user melakukan long klik pada salah satu item
            holder.ListItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View view) {
                    final String[] action = {"Edit", "Delete"};
                    AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                    alert.setItems(action, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            switch (i) {
                                case 0:
                                /*
                                  Berpindah Activity pada halaman layout updateData
                                  dan mengambil data pada listMahasiswa, berdasarkan posisinya
                                  untuk dikirim pada activity updateData
                                 */
                                    Bundle bundle = new Bundle();
                                    bundle.putString("dataNamaPerusahaan", listPerusahaan.get(position).getNamaPerusahaan());
                                    bundle.putString("dataBadan", listPerusahaan.get(position).getBadan());
                                    bundle.putString("dataAlamat", listPerusahaan.get(position).getAlamat());
                                    bundle.putString("dataKelurahan", listPerusahaan.get(position).getKelurahan());
                                    bundle.putString("dataKecamatan", listPerusahaan.get(position).getKecamatan());
                                    bundle.putString("dataTelponPerusahaan", listPerusahaan.get(position).getTlp_perusahaan());
                                    bundle.putString("dataEmailPerusahaan", listPerusahaan.get(position).getEmail_p());
                                    bundle.putString("dataEmailPerusahaan", listPerusahaan.get(position).getEmail_p());
                                    bundle.putString("dataNamaCP", listPerusahaan.get(position).getNama_cp());
                                    bundle.putString("dataKontakCP", listPerusahaan.get(position).getKontak_cp());
                                    bundle.putString("dataSurvei1", listPerusahaan.get(position).getSurvei1());
                                    bundle.putString("dataSurvei2", listPerusahaan.get(position).getSurvei2());
                                    bundle.putString("dataSurvei3", listPerusahaan.get(position).getSurvei3());
                                    bundle.putString("dataSurvei4", listPerusahaan.get(position).getSurvei4());
                                    bundle.putString("dataSurvei5", listPerusahaan.get(position).getSurvei5());
                                    bundle.putString("dataSurvei6", listPerusahaan.get(position).getSurvei6());
                                    bundle.putString("dataSurvei7", listPerusahaan.get(position).getSurvei7());
                                    bundle.putString("dataSurvei8", listPerusahaan.get(position).getSurvei8());
                                    bundle.putString("dataSurvei9", listPerusahaan.get(position).getSurvei9());
                                    bundle.putString("dataSurvei10", listPerusahaan.get(position).getSurvei10());
                                    bundle.putString("dataStatus1", listPerusahaan.get(position).getStatus1());
                                    bundle.putString("dataStatus2", listPerusahaan.get(position).getStatus2());
                                    bundle.putString("dataStatus3", listPerusahaan.get(position).getStatus3());
                                    bundle.putString("dataStatus4", listPerusahaan.get(position).getStatus4());
                                    bundle.putString("dataStatus5", listPerusahaan.get(position).getStatus5());
                                    bundle.putString("dataStatus6", listPerusahaan.get(position).getStatus6());
                                    bundle.putString("dataStatus7", listPerusahaan.get(position).getStatus7());
                                    bundle.putString("dataStatus8", listPerusahaan.get(position).getStatus8());
                                    bundle.putString("dataStatus9", listPerusahaan.get(position).getStatus9());
                                    bundle.putString("dataStatus10", listPerusahaan.get(position).getStatus10());
                                    bundle.putString("dataLatlng", listPerusahaan.get(position).getLatlng());
                                    bundle.putString("getPrimaryKey", listPerusahaan.get(position).getKey());
                                    Intent intent = new Intent(view.getContext(), EditActivity.class);
                                    intent.putExtras(bundle);
                                    context.startActivity(intent);
                                    break;
                                case 1:
                                    //Menggunakan interface untuk mengirim data mahasiswa, yang akan dihapus
                                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                    builder.setMessage("Yakin ingin menghapus?")
                                            .setCancelable(false)
                                            .setPositiveButton("Ya",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog,
                                                                            int id) {
                                                            listener.onDeleteData(listPerusahaan.get(position), position);
                                                        }
                                                    })
                                            .setNegativeButton("Tidak", null).show();
                                    break;
                            }
                        }
                    });
                    alert.create();
                    alert.show();
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        //Menghitung Ukuran/Jumlah Data Yang Akan Ditampilkan Pada RecyclerView
        return listPerusahaan.size();
    }

    public void close(View view){


    }
}