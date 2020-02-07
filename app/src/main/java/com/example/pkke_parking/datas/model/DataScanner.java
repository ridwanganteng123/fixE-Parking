package com.example.pkke_parking.datas.model;

public class DataScanner {

    private String siswaId, waktu_masuk;

    public DataScanner() {
    }

    public DataScanner(String siswaId, String waktu_masuk) {
        this.siswaId = siswaId;
        this.waktu_masuk = waktu_masuk;
    }

    public String getSiswaId() {
        return siswaId;
    }

    public void setSiswaId(String siswaId) {
        this.siswaId = siswaId;
    }

    public String getWaktu_masuk() {
        return waktu_masuk;
    }

    public void setWaktu_masuk(String waktu_masuk) {
        this.waktu_masuk = waktu_masuk;
    }
}
