package com.example.pkke_parking;

public class DataDaftarSiswa {

    private int image;
    private String nama, nis;

    public DataDaftarSiswa(int image, String nama, String nis) {
        this.image = image;
        this.nama = nama;
        this.nis = nis;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNis() {
        return nis;
    }

    public void setNis(String nis) {
        this.nis = nis;
    }
}
