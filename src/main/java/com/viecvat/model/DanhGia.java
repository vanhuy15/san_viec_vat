package com.viecvat.model;

import java.sql.Timestamp;

public class DanhGia {
    private int maDanhGia;
    private int maCongViec;
    private int nguoiDanhGiaId;
    private int nguoiDuocDanhGiaId;
    private int soSao;
    private String nhanXet;
    private Timestamp ngayDanhGia;
    
    // Thuộc tính phụ (không có trong bảng danhgia, lấy từ bảng nguoidung qua JOIN)
    private String tenNguoiDanhGia;

    public DanhGia() {
    }

    // Getters and Setters
    public int getMaDanhGia() { return maDanhGia; }
    public void setMaDanhGia(int maDanhGia) { this.maDanhGia = maDanhGia; }

    public int getMaCongViec() { return maCongViec; }
    public void setMaCongViec(int maCongViec) { this.maCongViec = maCongViec; }

    public int getNguoiDanhGiaId() { return nguoiDanhGiaId; }
    public void setNguoiDanhGiaId(int nguoiDanhGiaId) { this.nguoiDanhGiaId = nguoiDanhGiaId; }

    public int getNguoiDuocDanhGiaId() { return nguoiDuocDanhGiaId; }
    public void setNguoiDuocDanhGiaId(int nguoiDuocDanhGiaId) { this.nguoiDuocDanhGiaId = nguoiDuocDanhGiaId; }

    public int getSoSao() { return soSao; }
    public void setSoSao(int soSao) { this.soSao = soSao; }

    public String getNhanXet() { return nhanXet; }
    public void setNhanXet(String nhanXet) { this.nhanXet = nhanXet; }

    public Timestamp getNgayDanhGia() { return ngayDanhGia; }
    public void setNgayDanhGia(Timestamp ngayDanhGia) { this.ngayDanhGia = ngayDanhGia; }

    public String getTenNguoiDanhGia() { return tenNguoiDanhGia; }
    public void setTenNguoiDanhGia(String tenNguoiDanhGia) { this.tenNguoiDanhGia = tenNguoiDanhGia; }
}