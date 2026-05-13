package com.viecvat.model;

import java.sql.Timestamp;

public class NguoiDung {
    private int maNguoiDung;
    private String tenDangNhap;
    private String matKhau;
    private String hoTen;
    private String soDienThoai;
    private String email;
    private double soDuVi;
    private float diemUyTin;
    private String vaiTro;
    private Timestamp ngayTao;
    
    // [MỚI] Thêm thuộc tính này để AdminDAO không bị lỗi
    private String trangThai; // 'ACTIVE', 'LOCKED'

    // Thông tin ngân hàng
    private String tenNganHang;
    private String soTaiKhoanNganHang;
    private String tenChuTaiKhoan;

    // [MỚI] THÔNG TIN HỒ SƠ CÁ NHÂN (PROFILE)
    private String gioiThieu;
    private String kyNang;

    public NguoiDung() {
    }

    public NguoiDung(int maNguoiDung, String tenDangNhap, String matKhau, String hoTen, String soDienThoai, String email, double soDuVi, float diemUyTin, String vaiTro, Timestamp ngayTao) {
        this.maNguoiDung = maNguoiDung;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.soDuVi = soDuVi;
        this.diemUyTin = diemUyTin;
        this.vaiTro = vaiTro;
        this.ngayTao = ngayTao;
    }

    // --- GETTER & SETTER ---
    public int getMaNguoiDung() { return maNguoiDung; }
    public void setMaNguoiDung(int maNguoiDung) { this.maNguoiDung = maNguoiDung; }

    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }

    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public double getSoDuVi() { return soDuVi; }
    public void setSoDuVi(double soDuVi) { this.soDuVi = soDuVi; }

    public float getDiemUyTin() { return diemUyTin; }
    public void setDiemUyTin(float diemUyTin) { this.diemUyTin = diemUyTin; }

    public String getVaiTro() { return vaiTro; }
    public void setVaiTro(String vaiTro) { this.vaiTro = vaiTro; }

    public Timestamp getNgayTao() { return ngayTao; }
    public void setNgayTao(Timestamp ngayTao) { this.ngayTao = ngayTao; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getTenNganHang() { return tenNganHang; }
    public void setTenNganHang(String tenNganHang) { this.tenNganHang = tenNganHang; }

    public String getSoTaiKhoanNganHang() { return soTaiKhoanNganHang; }
    public void setSoTaiKhoanNganHang(String soTaiKhoanNganHang) { this.soTaiKhoanNganHang = soTaiKhoanNganHang; }

    public String getTenChuTaiKhoan() { return tenChuTaiKhoan; }
    public void setTenChuTaiKhoan(String tenChuTaiKhoan) { this.tenChuTaiKhoan = tenChuTaiKhoan; }

    // [MỚI] Getter & Setter cho Profile
    public String getGioiThieu() { return gioiThieu; }
    public void setGioiThieu(String gioiThieu) { this.gioiThieu = gioiThieu; }

    public String getKyNang() { return kyNang; }
    public void setKyNang(String kyNang) { this.kyNang = kyNang; }
}