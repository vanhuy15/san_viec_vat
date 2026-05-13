package com.viecvat.model;

import java.sql.Timestamp;

public class KhieuNai {
    private int maKhieuNai;
    private int maCongViec;
    private int nguoiKhieuNaiId;
    private String lyDo;
    private String anhBangChung;
    private String trangThaiXuLy; // 'CHO_XU_LY', 'DANG_XU_LY', 'DA_GIAI_QUYET'
    private String ketQuaXuLy;
    private Timestamp ngayTao;

    // [MỚI] Thuộc tính hiển thị (không lưu trong bảng khieunai) dùng trong AdminDAO
    private String ghiChu; 

    public KhieuNai() {
    }

    public KhieuNai(int maCongViec, int nguoiKhieuNaiId, String lyDo) {
        this.maCongViec = maCongViec;
        this.nguoiKhieuNaiId = nguoiKhieuNaiId;
        this.lyDo = lyDo;
    }

    // Getters and Setters
    public int getMaKhieuNai() { return maKhieuNai; }
    public void setMaKhieuNai(int maKhieuNai) { this.maKhieuNai = maKhieuNai; }

    public int getMaCongViec() { return maCongViec; }
    public void setMaCongViec(int maCongViec) { this.maCongViec = maCongViec; }

    public int getNguoiKhieuNaiId() { return nguoiKhieuNaiId; }
    public void setNguoiKhieuNaiId(int nguoiKhieuNaiId) { this.nguoiKhieuNaiId = nguoiKhieuNaiId; }

    public String getLyDo() { return lyDo; }
    public void setLyDo(String lyDo) { this.lyDo = lyDo; }

    public String getAnhBangChung() { return anhBangChung; }
    public void setAnhBangChung(String anhBangChung) { this.anhBangChung = anhBangChung; }

    public String getTrangThaiXuLy() { return trangThaiXuLy; }
    public void setTrangThaiXuLy(String trangThaiXuLy) { this.trangThaiXuLy = trangThaiXuLy; }

    public String getKetQuaXuLy() { return ketQuaXuLy; }
    public void setKetQuaXuLy(String ketQuaXuLy) { this.ketQuaXuLy = ketQuaXuLy; }

    public Timestamp getNgayTao() { return ngayTao; }
    public void setNgayTao(Timestamp ngayTao) { this.ngayTao = ngayTao; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}