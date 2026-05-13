package com.viecvat.model;

import java.sql.Timestamp;

public class GiaoDich {
    private int maGiaoDich;
    private int maNguoiDung;
    private double soTien;
    private String loaiGiaoDich;
    private String moTa;
    private Timestamp ngayTao;
    private String trangThai;

    // [ĐÃ SỬA] Đổi tên biến về tiếng Việt để khớp với AdminDAO
    private String tenNganHang;
    private String soTaiKhoanNganHang;
    private String tenChuTaiKhoan;

    public GiaoDich() {}

    public GiaoDich(int maGiaoDich, int maNguoiDung, double soTien, String loaiGiaoDich, String moTa, Timestamp ngayTao, String trangThai) {
        this.maGiaoDich = maGiaoDich;
        this.maNguoiDung = maNguoiDung;
        this.soTien = soTien;
        this.loaiGiaoDich = loaiGiaoDich;
        this.moTa = moTa;
        this.ngayTao = ngayTao;
        this.trangThai = trangThai;
    }

    // --- GETTER & SETTER GỐC ---
    public int getMaGiaoDich() { return maGiaoDich; }
    public void setMaGiaoDich(int maGiaoDich) { this.maGiaoDich = maGiaoDich; }
    
    public int getMaNguoiDung() { return maNguoiDung; }
    public void setMaNguoiDung(int maNguoiDung) { this.maNguoiDung = maNguoiDung; }
    
    public double getSoTien() { return soTien; }
    public void setSoTien(double soTien) { this.soTien = soTien; }
    
    public String getLoaiGiaoDich() { return loaiGiaoDich; }
    public void setLoaiGiaoDich(String loaiGiaoDich) { this.loaiGiaoDich = loaiGiaoDich; }
    
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
    
    public Timestamp getNgayTao() { return ngayTao; }
    public void setNgayTao(Timestamp ngayTao) { this.ngayTao = ngayTao; }
    
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    // --- [ĐÃ SỬA] GETTER & SETTER CHO BANK (KHỚP VỚI AdminDAO) ---
    public String getTenNganHang() { return tenNganHang; }
    public void setTenNganHang(String tenNganHang) { this.tenNganHang = tenNganHang; }
    
    public String getSoTaiKhoanNganHang() { return soTaiKhoanNganHang; }
    public void setSoTaiKhoanNganHang(String soTaiKhoanNganHang) { this.soTaiKhoanNganHang = soTaiKhoanNganHang; }
    
    public String getTenChuTaiKhoan() { return tenChuTaiKhoan; }
    public void setTenChuTaiKhoan(String tenChuTaiKhoan) { this.tenChuTaiKhoan = tenChuTaiKhoan; }
}