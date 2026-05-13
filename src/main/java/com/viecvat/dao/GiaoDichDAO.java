package com.viecvat.dao;

import com.viecvat.context.DBContext;
import com.viecvat.model.GiaoDich;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GiaoDichDAO {

    // PHẦN 1: CÁC HÀM DÀNH CHO USER (LẤY LỊCH SỬ, TẠO YÊU CẦU)

    // 1. LẤY LỊCH SỬ GIAO DỊCH 
    public List<GiaoDich> getHistoryByUserId(int userId) {
        List<GiaoDich> list = new ArrayList<>();
        String sql = "SELECT * FROM giaodich WHERE ma_nguoi_dung = ? ORDER BY ngay_tao DESC";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    GiaoDich gd = new GiaoDich();
                    gd.setMaGiaoDich(rs.getInt("ma_giao_dich"));
                    gd.setMaNguoiDung(rs.getInt("ma_nguoi_dung"));
                    gd.setSoTien(rs.getDouble("so_tien"));
                    gd.setLoaiGiaoDich(rs.getString("loai_giao_dich"));
                    gd.setMoTa(rs.getString("mo_ta"));
                    gd.setNgayTao(rs.getTimestamp("ngay_tao"));
                    gd.setTrangThai(rs.getString("trang_thai"));
                    list.add(gd);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. TẠO YÊU CẦU NẠP TIỀN
    public boolean taoYeuCauNapTien(int userId, double soTien, String noiDung) {
        String sql = "INSERT INTO giaodich (ma_nguoi_dung, so_tien, loai_giao_dich, mo_ta, trang_thai, ngay_tao) " +
                     "VALUES (?, ?, 'NAP_TIEN', ?, 'CHO_DUYET', NOW())";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setDouble(2, soTien);
            ps.setString(3, noiDung);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 3. TẠO YÊU CẦU RÚT TIỀN (UPDATE LOGIC: TRỪ TIỀN NGAY LẬP TỨC)
    public String taoYeuCauRutTien(int userId, double soTien, double soDuHienTai) {
        if (soDuHienTai < soTien) {
            return "Số dư hiện tại không đủ để rút!";
        }
        if (soTien < 50000) {
            return "Số tiền rút tối thiểu là 50.000 VNĐ!";
        }
        
        Connection conn = null;
        try {
            conn = DBContext.getConnection();
            conn.setAutoCommit(false); // Bắt đầu Transaction an toàn

            // B1. Trừ tiền ví User ngay lập tức (Để tránh User tiêu hết tiền trong lúc chờ duyệt)
            String sqlTru = "UPDATE nguoidung SET so_du_vi = so_du_vi - ? WHERE ma_nguoi_dung = ?";
            PreparedStatement psTru = conn.prepareStatement(sqlTru);
            psTru.setDouble(1, soTien);
            psTru.setInt(2, userId);
            psTru.executeUpdate();

            // B2. Tạo log giao dịch Rút tiền
            String sqlLog = "INSERT INTO giaodich (ma_nguoi_dung, so_tien, loai_giao_dich, mo_ta, trang_thai, ngay_tao) " +
                            "VALUES (?, ?, 'RUT_TIEN', 'Rút tiền về tài khoản ngân hàng', 'CHO_DUYET', NOW())";
            PreparedStatement psLog = conn.prepareStatement(sqlLog);
            psLog.setInt(1, userId);
            psLog.setDouble(2, soTien);
            psLog.executeUpdate();

            conn.commit(); // Xác nhận thành công
            return "SUCCESS";
        } catch (Exception e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {} // Hoàn tác nếu lỗi
            return "Lỗi hệ thống: " + e.getMessage();
        } finally {
            try { if (conn != null) { conn.setAutoCommit(true); conn.close(); } } catch (SQLException ex) {}
        }
    }

    // 4. LẤY DANH SÁCH GIAO DỊCH CÓ PHÂN TRANG & BỘ LỌC CHO USER (Giữ nguyên code cũ)
    public int countHistoryFilter(int userId, String type) {
        String sql = "SELECT COUNT(*) FROM giaodich WHERE ma_nguoi_dung = ?";
        if (type != null && !type.isEmpty() && !type.equals("ALL")) {
            if ("OTHERS".equals(type)) { 
                sql += " AND loai_giao_dich NOT IN ('NAP_TIEN', 'RUT_TIEN')";
            } else {
                sql += " AND loai_giao_dich = '" + type + "'";
            }
        }
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    public List<GiaoDich> getHistoryFilter(int userId, String type, int page, int pageSize) {
        List<GiaoDich> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM giaodich WHERE ma_nguoi_dung = ?");
        
        if (type != null && !type.isEmpty() && !type.equals("ALL")) {
            if ("OTHERS".equals(type)) { 
                sql.append(" AND loai_giao_dich NOT IN ('NAP_TIEN', 'RUT_TIEN')");
            } else {
                sql.append(" AND loai_giao_dich = '").append(type).append("'");
            }
        }
        sql.append(" ORDER BY ngay_tao DESC LIMIT ? OFFSET ?");
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            ps.setInt(1, userId);
            ps.setInt(2, pageSize);
            ps.setInt(3, (page - 1) * pageSize);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                GiaoDich gd = new GiaoDich();
                gd.setMaGiaoDich(rs.getInt("ma_giao_dich"));
                gd.setMaNguoiDung(rs.getInt("ma_nguoi_dung"));
                gd.setSoTien(rs.getDouble("so_tien"));
                gd.setLoaiGiaoDich(rs.getString("loai_giao_dich"));
                gd.setMoTa(rs.getString("mo_ta"));
                gd.setNgayTao(rs.getTimestamp("ngay_tao"));
                gd.setTrangThai(rs.getString("trang_thai"));
                list.add(gd);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // PHẦN 2: CÁC HÀM DÀNH CHO ADMIN (DUYỆT, HỦY, LẤY LIST KÈM BANK INFO)

    // 5. ADMIN LẤY DANH SÁCH CHỜ DUYỆT CŨ (Giữ lại để tương thích code cũ nếu còn dùng)
    public List<GiaoDich> getPendingTransactions() {
        List<GiaoDich> list = new ArrayList<>();
        String sql = "SELECT gd.*, nd.ten_ngan_hang, nd.so_tai_khoan_ngan_hang, nd.ten_chu_tai_khoan " +
                     "FROM giaodich gd " +
                     "JOIN nguoidung nd ON gd.ma_nguoi_dung = nd.ma_nguoi_dung " +
                     "WHERE gd.trang_thai = 'CHO_DUYET' ORDER BY gd.ngay_tao ASC";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                GiaoDich gd = new GiaoDich();
                gd.setMaGiaoDich(rs.getInt("ma_giao_dich"));
                gd.setMaNguoiDung(rs.getInt("ma_nguoi_dung"));
                gd.setSoTien(rs.getDouble("so_tien"));
                gd.setLoaiGiaoDich(rs.getString("loai_giao_dich"));
                gd.setMoTa(rs.getString("mo_ta"));
                gd.setNgayTao(rs.getTimestamp("ngay_tao"));
                gd.setTrangThai(rs.getString("trang_thai"));
                gd.setTenNganHang(rs.getString("ten_ngan_hang"));
                gd.setSoTaiKhoanNganHang(rs.getString("so_tai_khoan_ngan_hang"));
                gd.setTenChuTaiKhoan(rs.getString("ten_chu_tai_khoan"));
                list.add(gd);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 6. [UPDATE] ADMIN DUYỆT GIAO DỊCH (XỬ LÝ LOGIC CỘNG/TRỪ TIỀN)
    public boolean duyetGiaoDich(int maGD) {
        Connection conn = null;
        try {
            conn = DBContext.getConnection();
            conn.setAutoCommit(false); // Bắt đầu Transaction
            
            // Lấy thông tin giao dịch đang chờ duyệt
            String sqlGet = "SELECT * FROM giaodich WHERE ma_giao_dich = ? AND trang_thai='CHO_DUYET'";
            PreparedStatement psGet = conn.prepareStatement(sqlGet);
            psGet.setInt(1, maGD);
            ResultSet rs = psGet.executeQuery();
            
            if (rs.next()) {
                String loai = rs.getString("loai_giao_dich");
                int userId = rs.getInt("ma_nguoi_dung");
                double amount = rs.getDouble("so_tien");
                
                if ("NAP_TIEN".equals(loai)) {
                    // Logic NẠP TIỀN: Duyệt -> Cộng tiền vào ví User
                    String sqlCong = "UPDATE nguoidung SET so_du_vi = so_du_vi + ? WHERE ma_nguoi_dung=?";
                    PreparedStatement psCong = conn.prepareStatement(sqlCong);
                    psCong.setDouble(1, amount);
                    psCong.setInt(2, userId);
                    psCong.executeUpdate();
                    
                } else if ("RUT_TIEN".equals(loai)) {
                    // Logic RÚT TIỀN: User đã bị trừ tiền lúc tạo lệnh rồi.
                    // Admin duyệt -> Chỉ cần đổi trạng thái thành công, không trừ thêm nữa.
                    // (Nếu muốn kiểm tra chắc chắn có thể check lại số dư, nhưng logic "Trừ trước" đã đảm bảo)
                }
                
                // Cập nhật trạng thái Giao dịch -> THANH_CONG
                String sqlUpdate = "UPDATE giaodich SET trang_thai='THANH_CONG' WHERE ma_giao_dich=?";
                PreparedStatement psUp = conn.prepareStatement(sqlUpdate);
                psUp.setInt(1, maGD);
                psUp.executeUpdate();
                
                conn.commit(); // Lưu thay đổi
                return true;
            }
        } catch (Exception e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
        } finally {
            try { if (conn != null) { conn.setAutoCommit(true); conn.close(); } } catch (SQLException ex) {}
        }
        return false;
    }

    // 7. [UPDATE] ADMIN HỦY GIAO DỊCH (XỬ LÝ HOÀN TIỀN NẾU RÚT)
    public boolean huyGiaoDich(int maGD) {
        Connection conn = null;
        try {
            conn = DBContext.getConnection();
            conn.setAutoCommit(false); // Bắt đầu Transaction

            // Lấy thông tin giao dịch
            String sqlGet = "SELECT * FROM giaodich WHERE ma_giao_dich = ? AND trang_thai='CHO_DUYET'";
            PreparedStatement psGet = conn.prepareStatement(sqlGet);
            psGet.setInt(1, maGD);
            ResultSet rs = psGet.executeQuery();

            if (rs.next()) {
                String loai = rs.getString("loai_giao_dich");
                int userId = rs.getInt("ma_nguoi_dung");
                double amount = rs.getDouble("so_tien");

                if ("RUT_TIEN".equals(loai)) {
                    // Logic HỦY RÚT TIỀN: Phải hoàn lại tiền vào ví cho User
                    String sqlRefund = "UPDATE nguoidung SET so_du_vi = so_du_vi + ? WHERE ma_nguoi_dung=?";
                    PreparedStatement psRef = conn.prepareStatement(sqlRefund);
                    psRef.setDouble(1, amount);
                    psRef.setInt(2, userId);
                    psRef.executeUpdate();
                }

                // Update trạng thái -> THAT_BAI
                String sqlUp = "UPDATE giaodich SET trang_thai='THAT_BAI' WHERE ma_giao_dich=?";
                PreparedStatement psUp = conn.prepareStatement(sqlUp);
                psUp.setInt(1, maGD);
                psUp.executeUpdate();

                conn.commit();
                return true;
            }
        } catch (Exception e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
        } finally {
            try { if (conn != null) { conn.setAutoCommit(true); conn.close(); } } catch (SQLException ex) {}
        }
        return false;
    }

    // 8. HỖ TRỢ PHÂN TRANG CHO ADMIN (Count)
    public int countTransactionsByType(String type) {
        String sql = "SELECT COUNT(*) FROM giaodich";
        if (type != null && !type.equals("ALL") && !type.isEmpty()) {
            sql += " WHERE loai_giao_dich = '" + type + "'";
        }
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    // 9. LẤY DANH SÁCH CHO ADMIN (CÓ JOIN THÔNG TIN BANK)
    // Hàm này rất quan trọng để nút QR hoạt động (lấy được số TK của User)
    public List<GiaoDich> getTransactionsForAdmin(String type, int page, int pageSize) {
        List<GiaoDich> list = new ArrayList<>();
        // JOIN để lấy thông tin Bank của User
        StringBuilder sql = new StringBuilder(
            "SELECT gd.*, nd.ten_ngan_hang, nd.so_tai_khoan_ngan_hang, nd.ten_chu_tai_khoan " +
            "FROM giaodich gd " +
            "JOIN nguoidung nd ON gd.ma_nguoi_dung = nd.ma_nguoi_dung "
        );

        if (type != null && !type.equals("ALL") && !type.isEmpty()) {
            sql.append(" WHERE gd.loai_giao_dich = '").append(type).append("'");
        }
        
        sql.append(" ORDER BY gd.ngay_tao DESC LIMIT ? OFFSET ?");

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            ps.setInt(1, pageSize);
            ps.setInt(2, (page - 1) * pageSize);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                GiaoDich gd = new GiaoDich();
                gd.setMaGiaoDich(rs.getInt("ma_giao_dich"));
                gd.setMaNguoiDung(rs.getInt("ma_nguoi_dung"));
                gd.setSoTien(rs.getDouble("so_tien"));
                gd.setLoaiGiaoDich(rs.getString("loai_giao_dich"));
                gd.setMoTa(rs.getString("mo_ta"));
                gd.setNgayTao(rs.getTimestamp("ngay_tao"));
                gd.setTrangThai(rs.getString("trang_thai"));
                
                // Set thông tin Bank (Quan trọng cho nút QR)
                gd.setTenNganHang(rs.getString("ten_ngan_hang"));
                gd.setSoTaiKhoanNganHang(rs.getString("so_tai_khoan_ngan_hang"));
                gd.setTenChuTaiKhoan(rs.getString("ten_chu_tai_khoan"));
                
                list.add(gd);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}