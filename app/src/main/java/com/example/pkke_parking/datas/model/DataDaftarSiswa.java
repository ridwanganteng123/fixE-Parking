package com.example.pkke_parking.datas.model;

public class DataDaftarSiswa {

    private String nama, siswaId, tgl_lahir, no_pol, pwd, email, no_sim, nis;

    public DataDaftarSiswa() {

    }

    public DataDaftarSiswa(String siswaId, String nama, String tgl_lahir, String no_pol, String pwd, String email, String no_sim, String nis) {
        this.siswaId = siswaId;
        this.nama = nama;
        this.tgl_lahir = tgl_lahir;
        this.no_pol = no_pol;
        this.pwd = pwd;
        this.email = email;
        this.no_sim = no_sim;
        this.nis = nis;
    }

    public String getSiswaId() {
        return siswaId;
    }

    public void setSiswaId(String siswaId) {
        this.siswaId = siswaId;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTgl_lahir() {
        return tgl_lahir;
    }

    public void setTgl_lahir(String tgl_lahir) {
        this.tgl_lahir = tgl_lahir;
    }

    public String getNo_pol() {
        return no_pol;
    }

    public void setNo_pol(String no_pol) {
        this.no_pol = no_pol;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNo_sim() {
        return no_sim;
    }

    public void setNo_sim(String no_sim) {
        this.no_sim = no_sim;
    }

    public String getNis() {
        return nis;
    }

    public void setNis(String nis) {
        this.nis = nis;
    }
}
