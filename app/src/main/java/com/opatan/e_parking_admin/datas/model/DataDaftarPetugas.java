package com.opatan.e_parking_admin.datas.model;

public class DataDaftarPetugas {

    private String nama, petugasId, tgl_lahir, pwd, email, nis, level, imageURL;

    public DataDaftarPetugas() {

    }

    public DataDaftarPetugas(String petugasId, String nama, String tgl_lahir, String pwd, String email, String nis, String level, String imageURL) {
        this.petugasId = petugasId;
        this.nama = nama;
        this.tgl_lahir = tgl_lahir;
        this.pwd = pwd;
        this.email = email;
        this.nis = nis;
        this.level = level;
        this.imageURL = imageURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getPetugasId() {
        return petugasId;
    }

    public void setPetugasId(String petugasId) {
        this.petugasId = petugasId;
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

    public String getNis() {
        return nis;
    }

    public void setNis(String nis) {
        this.nis = nis;
    }
}
