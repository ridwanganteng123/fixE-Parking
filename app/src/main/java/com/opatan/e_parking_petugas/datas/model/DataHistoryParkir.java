package com.opatan.e_parking_petugas.datas.model;

public class DataHistoryParkir {

    private String waktu_masuk, tanggal, siswa, hari;

    public DataHistoryParkir() {
    }

    public DataHistoryParkir(String tanggal, String hari, String waktu_masuk, String siswa) {
        this.tanggal = tanggal;
        this.hari = hari;
        this.waktu_masuk = waktu_masuk;
        this.siswa = siswa;
    }

    public String getHari() {
        return hari;
    }

    public void setHari(String hari) {
        this.hari = hari;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getWaktu_masuk() {
        return waktu_masuk;
    }

    public void setWaktu_masuk(String waktu_masuk) {
        this.waktu_masuk = waktu_masuk;
    }

    public String getSiswa() {
        return siswa;
    }

    public void setSiswa(String siswa) {
        this.siswa = siswa;
    }

}
