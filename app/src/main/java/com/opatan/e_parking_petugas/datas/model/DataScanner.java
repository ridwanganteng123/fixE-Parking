package com.opatan.e_parking_petugas.datas.model;

public class DataScanner {

    private String siswaId, waktu_masuk, pemeriksa;

    public DataScanner() {
    }

    public DataScanner(String siswaId, String waktu_masuk, String pemeriksa) {
        this.siswaId = siswaId;
        this.waktu_masuk = waktu_masuk;
        this.pemeriksa = pemeriksa;
    }

    public String getPemeriksa() {
        return pemeriksa;
    }

    public void setPemeriksa(String pemeriksa) {
        this.pemeriksa = pemeriksa;
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
