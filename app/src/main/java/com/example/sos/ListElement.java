package com.example.sos;

public class ListElement {
    private int element_type;

    private String durum;
    private String username;
    private String olayturu;
    private String olaybilgi;
    private String konum;
    private String tarih;
    private String image;

    public ListElement(int element_type, String durum, String username, String olayturu, String olaybilgi, String konum, String tarih, String image) {
        this.element_type = element_type;
        this.durum = durum;
        this.username = username;
        this.olayturu = olayturu;
        this.olaybilgi = olaybilgi;
        this.konum = konum;
        this.tarih = tarih;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public String getDurum() {
        return durum;
    }

    public String getUsername() {
        return username;
    }

    public String getKonum() {
        return konum;
    }

    public String getTarih() {
        return tarih;
    }

    public int getElement_type() {
        return element_type;
    }

    public String getOlayturu() {
        return olayturu;
    }

    public String getOlaybilgi() {
        return olaybilgi;
    }
}