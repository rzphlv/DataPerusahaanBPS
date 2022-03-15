package rzphlv.nmf.dataperusahaanbps;

public class data_perusahaan {
    //Deklarasi Variable
    private String nama_perusahaan;
    private String badan;
    private String alamat;
    private String kelurahan;
    private String kecamatan;
    private String tlp_perusahaan;
    private String email_p;
    private String nama_cp;
    private String kontak_cp;
    private String survei1, survei2, survei3, survei4, survei5, survei6, survei7, survei8, survei9, survei10;
    private String status1, status2, status3, status4, status5, status6, status7, status8, status9, status10;
    private String latlng;

    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getBadan() {
        return badan;
    }

    public void setBadan(String badan) {
        this.badan = badan;
    }

    public String getNamaPerusahaan() {
        return nama_perusahaan;
    }

    public void setNamaPerusahaan(String nama_perusahaan) {
        this.nama_perusahaan = nama_perusahaan;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getKelurahan() {
        return kelurahan;
    }

    public void setKelurahan(String kelurahan) {
        this.kelurahan = kelurahan;
    }

    public String getKecamatan() {
        return kecamatan;
    }

    public void setKecamatan(String kecamatan) {
        this.kecamatan = kecamatan;
    }

    public String getTlp_perusahaan() {
        return tlp_perusahaan;
    }

    public void setTlp_perusahaan(String tlp_perusahaan) {
        this.tlp_perusahaan = tlp_perusahaan;
    }

    public String getEmail_p() {
        return email_p;
    }

    public void setEmail_p(String email_p) {
        this.email_p = email_p;
    }

    public String getNama_cp() {
        return nama_cp;
    }

    public void setNama_cp(String nama_cp) {
        this.nama_cp = nama_cp;
    }

    public String getKontak_cp() {
        return kontak_cp;
    }

    public void setKontak_cp(String kontak_cp) {
        this.kontak_cp = kontak_cp;
    }

    //Survei
    public String getSurvei1() {
        return survei1;
    }

    public void setSurvei1(String survei1) {
        this.survei1 = survei1;
    }

    public String getSurvei2() {
        return survei2;
    }

    public void setSurvei2(String survei2) {
        this.survei2 = survei2;
    }

    public String getSurvei3() {
        return survei3;
    }

    public void setSurvei3(String survei3) {
        this.survei3 = survei3;
    }

    public String getSurvei4() {
        return survei4;
    }

    public void setSurvei4(String survei4) {
        this.survei4 = survei4;
    }

    public String getSurvei5() {
        return survei5;
    }

    public void setSurvei5(String survei5) {
        this.survei5 = survei5;
    }

    public String getSurvei6() {
        return survei6;
    }

    public void setSurvei6(String survei6) {
        this.survei6 = survei6;
    }

    public String getSurvei7() {
        return survei7;
    }

    public void setSurvei7(String survei7) {
        this.survei7 = survei7;
    }

    public String getSurvei8() {
        return survei8;
    }

    public void setSurvei8(String survei8) {
        this.survei8 = survei8;
    }

    public String getSurvei9() {
        return survei9;
    }

    public void setSurvei9(String survei9) {
        this.survei9 = survei9;
    }

    public String getSurvei10() {
        return survei10;
    }

    public void setSurvei10(String survei10) {
        this.survei10 = survei10;
    }

    //Status
    public String getStatus1() {
        return status1;
    }

    public void setStatus1(String status1) {
        this.status1 = status1;
    }

    public String getStatus2() {
        return status2;
    }

    public void setStatus2(String status2) {
        this.status2 = status2;
    }

    public String getStatus3() {
        return status3;
    }

    public void setStatus3(String status3) {
        this.status3 = status3;
    }

    public String getStatus4() {
        return status4;
    }

    public void setStatus4(String status4) {
        this.status4 = status4;
    }

    public String getStatus5() {
        return status5;
    }

    public void setStatus5(String status5) {
        this.status5 = status5;
    }

    public String getStatus6() {
        return status6;
    }

    public void setStatus6(String status6) {
        this.status6 = status6;
    }

    public String getStatus7() {
        return status7;
    }

    public void setStatus7(String status7) {
        this.status7 = status7;
    }

    public String getStatus8() {
        return status8;
    }

    public void setStatus8(String status8) {
        this.status8 = status8;
    }

    public String getStatus9() {
        return status9;
    }

    public void setStatus9(String status9) {
        this.status9 = status9;
    }

    public String getStatus10() {
        return status10;
    }

    public void setStatus10(String status10) {
        this.status10 = status10;
    }

    public String getLatlng() {
        return latlng;
    }

    public void setLatlng(String latlng) {
        this.latlng = latlng;
    }

    //Membuat Konstuktor kosong untuk membaca data snapshot
    public data_perusahaan() {
    }

    //Konstruktor dengan beberapa parameter, untuk mendapatkan Input Data dari User
    public data_perusahaan(String nama_perusahaan,
                           String badan,
                           String alamat,
                           String kelurahan,
                           String kecamatan,
                           String tlp_perusahaan,
                           String email_p,
                           String nama_cp,
                           String kontak_cp,
                           String latlng) {

        this.nama_perusahaan = nama_perusahaan;
        this.badan = badan;
        this.alamat = alamat;
        this.kelurahan = kelurahan;
        this.kecamatan = kecamatan;
        this.tlp_perusahaan = tlp_perusahaan;
        this.email_p = email_p;
        this.nama_cp = nama_cp;
        this.kontak_cp = kontak_cp;

        this.latlng = latlng;
    }

    public data_perusahaan(String survei1, String survei2, String survei3, String survei4, String survei5,
                           String survei6, String survei7, String survei8, String survei9, String survei10,
                           String status1, String status2, String status3, String status4, String status5,
                           String status6, String status7, String status8, String status9, String status10) {                                   //Tanggal

        //SURVEI
        this.survei1 = survei1;
        this.survei2 = survei2;
        this.survei3 = survei3;
        this.survei4 = survei4;
        this.survei5 = survei5;
        this.survei6 = survei6;
        this.survei7 = survei7;
        this.survei8 = survei8;
        this.survei9 = survei9;
        this.survei10 = survei10;

        //STATUS
        this.status1 = status1;
        this.status2 = status2;
        this.status3 = status3;
        this.status4 = status4;
        this.status5 = status5;
        this.status6 = status6;
        this.status7 = status7;
        this.status8 = status8;
        this.status9 = status9;
        this.status10 = status10;
    }
}
