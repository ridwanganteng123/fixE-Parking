package com.opatan.e_parking_user.datas.model;

public class DataHistoryParkir {

    private String waktu_masuk, tanggal, pemeriksa, hari;

    public DataHistoryParkir() {
    }

    public DataHistoryParkir(String waktu_masuk, String tanggal, String hari, String pemeriksa) {
        this.waktu_masuk = waktu_masuk;
        this.tanggal = tanggal;
        this.pemeriksa = pemeriksa;
        this.hari = hari;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getHari() {
        return hari;
    }

    public void setHari(String hari) {
        this.hari = hari;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getPemeriksa() {
        return pemeriksa;
    }

    public void setPemeriksa(String pemeriksa) {
        this.pemeriksa = pemeriksa;
    }

    public String getWaktu_masuk() {
        return waktu_masuk;
    }

    public void setWaktu_masuk(String waktu_masuk) {
        this.waktu_masuk = waktu_masuk;
    }
}
