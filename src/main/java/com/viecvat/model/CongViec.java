package com.viecvat.model;

import java.sql.Timestamp;

public class CongViec {
    private int maCongViec;
    private int nguoiThueId;
    private String tieuDe;
    private String moTa;
    private String diaDiem;
    private double giaTien;
    private Timestamp hanChot;
    private String trangThai; 
    private Timestamp ngayDang;
    
    // Các trường nối bảng (Join)
    private String tenDanhMuc; 
    private String tenNguoiThue;
    
    // [MỚI] Điểm uy tín của người thuê
    private float diemUyTin;

    // [MỚI] ID của người làm (để trả lương khi xong việc - Có thể null nếu chưa ai nhận)
    private int nguoiLamId; 

    // [MỚI - QUAN TRỌNG] Số lượng tuyển dụng
    private int soLuongCan;      // Số lượng cần tuyển (VD: 2)
    private int soLuongDaTuyen;  // Số lượng đã duyệt (VD: 1)

    public CongViec() {
    }

    public CongViec(int maCongViec, int nguoiThueId, String tieuDe, String moTa, String diaDiem, double giaTien, Timestamp hanChot, String trangThai, Timestamp ngayDang, String tenDanhMuc, String tenNguoiThue) {
        this.maCongViec = maCongViec;
        this.nguoiThueId = nguoiThueId;
        this.tieuDe = tieuDe;
        this.moTa = moTa;
        this.diaDiem = diaDiem;
        this.giaTien = giaTien;
        this.hanChot = hanChot;
        this.trangThai = trangThai;
        this.ngayDang = ngayDang;
        this.tenDanhMuc = tenDanhMuc;
        this.tenNguoiThue = tenNguoiThue;
    }

    // --- GETTER & SETTER CŨ ---
    public int getMaCongViec() { return maCongViec; }
    public void setMaCongViec(int maCongViec) { this.maCongViec = maCongViec; }

    public int getNguoiThueId() { return nguoiThueId; }
    public void setNguoiThueId(int nguoiThueId) { this.nguoiThueId = nguoiThueId; }

    public String getTieuDe() { return tieuDe; }
    public void setTieuDe(String tieuDe) { this.tieuDe = tieuDe; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public String getDiaDiem() { return diaDiem; }
    public void setDiaDiem(String diaDiem) { this.diaDiem = diaDiem; }

    public double getGiaTien() { return giaTien; }
    public void setGiaTien(double giaTien) { this.giaTien = giaTien; }

    public Timestamp getHanChot() { return hanChot; }
    public void setHanChot(Timestamp hanChot) { this.hanChot = hanChot; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public Timestamp getNgayDang() { return ngayDang; }
    public void setNgayDang(Timestamp ngayDang) { this.ngayDang = ngayDang; }

    public String getTenDanhMuc() { return tenDanhMuc; }
    public void setTenDanhMuc(String tenDanhMuc) { this.tenDanhMuc = tenDanhMuc; }

    public String getTenNguoiThue() { return tenNguoiThue; }
    public void setTenNguoiThue(String tenNguoiThue) { this.tenNguoiThue = tenNguoiThue; }

    public float getDiemUyTin() { return diemUyTin; }
    public void setDiemUyTin(float diemUyTin) { this.diemUyTin = diemUyTin; }

    public int getNguoiLamId() { return nguoiLamId; }
    public void setNguoiLamId(int nguoiLamId) { this.nguoiLamId = nguoiLamId; }

    // --- [MỚI] GETTER & SETTER SỐ LƯỢNG ---
    public int getSoLuongCan() { return soLuongCan; }
    public void setSoLuongCan(int soLuongCan) { this.soLuongCan = soLuongCan; }

    public int getSoLuongDaTuyen() { return soLuongDaTuyen; }
    public void setSoLuongDaTuyen(int soLuongDaTuyen) { this.soLuongDaTuyen = soLuongDaTuyen; }
    
    // Hàm hỗ trợ hiển thị tiến độ trên giao diện (VD: 1/2)
    public String getTienDo() {
        return soLuongDaTuyen + "/" + soLuongCan;
    }
}