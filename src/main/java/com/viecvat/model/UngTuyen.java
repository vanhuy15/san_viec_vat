package com.viecvat.model;

import java.sql.Timestamp;

public class UngTuyen {
    private int maUngTuyen;
    private int maCongViec;
    private int nguoiUngTuyenId;
    private String loiNhan;
    private String trangThai; // Trạng thái đơn ứng tuyển (CHO_DUYET, DA_CHON, TU_CHOI)
    private Timestamp thoiGian;
    
    // --- CÁC TRƯỜNG BỔ SUNG (JOIN BẢNG) ---
    private String tenNguoiUngTuyen;
    private String sdtNguoiUngTuyen;
    private float diemUyTin; 
    
    // Dùng cho trang "Việc tôi ứng tuyển"
    private String tenCongViec;
    private String tenNguoiThue;
    private String trangThaiCongViec; // Trạng thái của CÔNG VIỆC (MO, DA_GIAO, CHO_XAC_NHAN...)
    private double giaTien;           // [MỚI] Giá tiền công việc

    public UngTuyen() {
    }

    // --- GETTER & SETTER ---
    public int getMaUngTuyen() { return maUngTuyen; }
    public void setMaUngTuyen(int maUngTuyen) { this.maUngTuyen = maUngTuyen; }

    public int getMaCongViec() { return maCongViec; }
    public void setMaCongViec(int maCongViec) { this.maCongViec = maCongViec; }

    public int getNguoiUngTuyenId() { return nguoiUngTuyenId; }
    public void setNguoiUngTuyenId(int nguoiUngTuyenId) { this.nguoiUngTuyenId = nguoiUngTuyenId; }

    public String getLoiNhan() { return loiNhan; }
    public void setLoiNhan(String loiNhan) { this.loiNhan = loiNhan; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public Timestamp getThoiGian() { return thoiGian; }
    public void setThoiGian(Timestamp thoiGian) { this.thoiGian = thoiGian; }

    public String getTenNguoiUngTuyen() { return tenNguoiUngTuyen; }
    public void setTenNguoiUngTuyen(String tenNguoiUngTuyen) { this.tenNguoiUngTuyen = tenNguoiUngTuyen; }

    public String getSdtNguoiUngTuyen() { return sdtNguoiUngTuyen; }
    public void setSdtNguoiUngTuyen(String sdtNguoiUngTuyen) { this.sdtNguoiUngTuyen = sdtNguoiUngTuyen; }

    public float getDiemUyTin() { return diemUyTin; }
    public void setDiemUyTin(float diemUyTin) { this.diemUyTin = diemUyTin; }

    public String getTenCongViec() { return tenCongViec; }
    public void setTenCongViec(String tenCongViec) { this.tenCongViec = tenCongViec; }

    public String getTenNguoiThue() { return tenNguoiThue; }
    public void setTenNguoiThue(String tenNguoiThue) { this.tenNguoiThue = tenNguoiThue; }

    // [QUAN TRỌNG] Getter Setter cho trạng thái công việc
    public String getTrangThaiCongViec() { return trangThaiCongViec; }
    public void setTrangThaiCongViec(String trangThaiCongViec) { this.trangThaiCongViec = trangThaiCongViec; }
    
    // [MỚI] Getter Setter cho giá tiền
    public double getGiaTien() { return giaTien; }
    public void setGiaTien(double giaTien) { this.giaTien = giaTien; }
}