package com.example.pkke_parking;

public class DataHistoryParkir {

    String waktu;
    String tanggal;

    public DataHistoryParkir(String tanggal, String waktu){
        this.waktu = waktu;
        this.tanggal = tanggal;
    }

    public String getTanggal(){
        return tanggal;
    }

    public void setTanggal(String tanggal){
        this.tanggal = tanggal;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }
}
