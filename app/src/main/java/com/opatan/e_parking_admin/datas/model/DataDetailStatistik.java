package com.opatan.e_parking_admin.datas.model;

import java.util.ArrayList;

public class DataDetailStatistik {

    private String waktu_masuk, siswa , petugas, imageUrl;

    public DataDetailStatistik(String waktu_masuk , String siswa, String petugas, String imageUrl) {
        this.waktu_masuk = waktu_masuk;
        this.siswa = siswa;
        this.petugas = petugas;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getWaktu_masuk() {
        return waktu_masuk;
    }

    public void setWaktu_masuk(String waktu_masuk) {
        this.waktu_masuk = waktu_masuk;
    }

    public String getPetugas() {
        return petugas;
    }

    public void setPetugas(String petugas) {
        this.petugas = petugas;
    }

    public String getSiswa() {
        return siswa;
    }

    public void setSiswa(String siswa) {
        this.siswa = siswa;
    }

}
