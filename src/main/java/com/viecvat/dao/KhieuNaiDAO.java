package com.viecvat.dao;

import com.viecvat.context.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class KhieuNaiDAO {

    /**
     * Tạo khiếu nại mới và chuyển trạng thái công việc sang 'CHO_DUYET' (Code cũ)
     */
    public boolean taoKhieuNai(int maCongViec, int nguoiKhieuNaiId, String lyDo) {
        Connection conn = null;
        PreparedStatement psInsert = null;
        PreparedStatement psUpdateJob = null;
        boolean result = false;

        try {
            conn = DBContext.getConnection();
            conn.setAutoCommit(false); 

            // Insert vào bảng khieunai
            String sqlInsert = "INSERT INTO khieunai(ma_cong_viec, nguoi_khieu_nai_id, ly_do, trang_thai_xu_ly, ngay_tao) VALUES(?, ?, ?, 'CHO_XU_LY', NOW())";
            psInsert = conn.prepareStatement(sqlInsert);
            psInsert.setInt(1, maCongViec);
            psInsert.setInt(2, nguoiKhieuNaiId);
            psInsert.setString(3, lyDo);
            psInsert.executeUpdate();

            // Update trạng thái công việc -> 'CHO_DUYET'
            String sqlUpdate = "UPDATE congviec SET trang_thai = 'CHO_DUYET' WHERE ma_cong_viec = ?";
            psUpdateJob = conn.prepareStatement(sqlUpdate);
            psUpdateJob.setInt(1, maCongViec);
            psUpdateJob.executeUpdate();

            conn.commit();
            result = true;

        } catch (Exception e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
        } finally {
            try { if (psInsert != null) psInsert.close(); } catch (Exception e) {}
            try { if (psUpdateJob != null) psUpdateJob.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
        return result;
    }

    /**
     * [UPDATE MỚI] Xử lý khiếu nại: Hoàn tiền hoặc Trả lương
     * type = 1: Hoàn tiền (Hủy Job)
     * type = 2: Trả lương (Hoàn thành Job)
     */
    public String giaiQuyetKhieuNai(int maKhieuNai, int type, String ghiChuAdmin) {
        Connection conn = null;
        try {
            conn = DBContext.getConnection();
            conn.setAutoCommit(false); // Bắt đầu Transaction

            // 1. Lấy thông tin chi tiết Job từ mã khiếu nại
            String sqlGet = "SELECT cv.ma_cong_viec, cv.gia_tien, cv.nguoi_thue_id, cv.nguoi_lam_id " +
                            "FROM khieunai kn " +
                            "JOIN congviec cv ON kn.ma_cong_viec = cv.ma_cong_viec " +
                            "WHERE kn.ma_khieu_nai = ?";
            PreparedStatement psGet = conn.prepareStatement(sqlGet);
            psGet.setInt(1, maKhieuNai);
            ResultSet rs = psGet.executeQuery();

            if (!rs.next()) return "Không tìm thấy dữ liệu!";

            int jobId = rs.getInt("ma_cong_viec");
            double giaTien = rs.getDouble("gia_tien");
            int ownerId = rs.getInt("nguoi_thue_id");
            int workerId = rs.getInt("nguoi_lam_id");

            // 2. Xử lý theo loại hành động
            if (type == 1) { 
                // === CASE 1: HOÀN TIỀN CHO NGƯỜI THUÊ ===
                
                // Cộng tiền lại cho chủ Job
                String sqlRefund = "UPDATE nguoidung SET so_du_vi = so_du_vi + ? WHERE ma_nguoi_dung = ?";
                PreparedStatement psRef = conn.prepareStatement(sqlRefund);
                psRef.setDouble(1, giaTien);
                psRef.setInt(2, ownerId);
                psRef.executeUpdate();

                // Ghi log giao dịch
                insertTransaction(conn, ownerId, giaTien, "HOAN_TIEN", "Hoàn tiền khiếu nại Job #" + jobId + ". Admin note: " + ghiChuAdmin);

                // Cập nhật Job -> DA_HUY
                String sqlJob = "UPDATE congviec SET trang_thai = 'DA_HUY' WHERE ma_cong_viec = ?";
                PreparedStatement psJob = conn.prepareStatement(sqlJob);
                psJob.setInt(1, jobId);
                psJob.executeUpdate();

                // Kết quả xử lý
                updateReportStatus(conn, maKhieuNai, "Hoàn tiền cho người thuê. Note: " + ghiChuAdmin);

            } else if (type == 2) {
                // === CASE 2: TRẢ LƯƠNG NGƯỜI LÀM (95%) ===
                
                if (workerId == 0) return "Job chưa có người làm, không thể trả lương!";

                double luongThucNhan = giaTien * 0.95;
                double phiAdmin = giaTien * 0.05;

                // Cộng tiền Worker
                String sqlPayWorker = "UPDATE nguoidung SET so_du_vi = so_du_vi + ? WHERE ma_nguoi_dung = ?";
                PreparedStatement psPay = conn.prepareStatement(sqlPayWorker);
                psPay.setDouble(1, luongThucNhan);
                psPay.setInt(2, workerId);
                psPay.executeUpdate();

                // Cộng tiền Admin
                String sqlPayAdmin = "UPDATE nguoidung SET so_du_vi = so_du_vi + ? WHERE vai_tro = 'ADMIN' LIMIT 1";
                PreparedStatement psAdm = conn.prepareStatement(sqlPayAdmin);
                psAdm.setDouble(1, phiAdmin);
                psAdm.executeUpdate();

                // Ghi log giao dịch
                insertTransaction(conn, workerId, luongThucNhan, "NHAN_LUONG", "Nhận lương khiếu nại Job #" + jobId + ". Admin note: " + ghiChuAdmin);

                // Cập nhật Job -> HOAN_THANH
                String sqlJob = "UPDATE congviec SET trang_thai = 'HOAN_THANH' WHERE ma_cong_viec = ?";
                PreparedStatement psJob = conn.prepareStatement(sqlJob);
                psJob.setInt(1, jobId);
                psJob.executeUpdate();

                // Kết quả xử lý
                updateReportStatus(conn, maKhieuNai, "Trả lương cho người làm. Note: " + ghiChuAdmin);
            }

            conn.commit();
            return "SUCCESS";

        } catch (Exception e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
            return "Lỗi Server: " + e.getMessage();
        } finally {
            try { if (conn != null) { conn.setAutoCommit(true); conn.close(); } } catch (SQLException ex) {}
        }
    }

    // --- Các hàm hỗ trợ Private ---
    
    private void updateReportStatus(Connection conn, int reportId, String resultText) throws SQLException {
        String sql = "UPDATE khieunai SET trang_thai_xu_ly = 'DA_GIAI_QUYET', ket_qua_xu_ly = ? WHERE ma_khieu_nai = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, resultText);
        ps.setInt(2, reportId);
        ps.executeUpdate();
    }

    private void insertTransaction(Connection conn, int userId, double amount, String type, String desc) throws SQLException {
        String sql = "INSERT INTO giaodich(ma_nguoi_dung, so_tien, loai_giao_dich, mo_ta, trang_thai, ngay_tao) VALUES(?, ?, ?, ?, 'THANH_CONG', NOW())";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, userId);
        ps.setDouble(2, amount);
        ps.setString(3, type);
        ps.setString(4, desc);
        ps.executeUpdate();
    }
}