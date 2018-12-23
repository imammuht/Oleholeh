
package com.sunubismo.oleholeh.model.rating;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Datum {

    @SerializedName("gambar")
    private String mGambar;
    @SerializedName("id")
    private String mId;
    @SerializedName("komentar")
    private String mKomentar;
    @SerializedName("nama")
    private String mNama;
    @SerializedName("nilai")
    private String mNilai;

    public String getGambar() {
        return mGambar;
    }

    public void setGambar(String gambar) {
        mGambar = gambar;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getKomentar() {
        return mKomentar;
    }

    public void setKomentar(String komentar) {
        mKomentar = komentar;
    }

    public String getNama() {
        return mNama;
    }

    public void setNama(String nama) {
        mNama = nama;
    }

    public String getNilai() {
        return mNilai;
    }

    public void setNilai(String nilai) {
        mNilai = nilai;
    }

}
