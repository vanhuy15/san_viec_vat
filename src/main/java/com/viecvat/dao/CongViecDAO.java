package com.viecvat.dao;

import com.viecvat.context.DBContext;
import com.viecvat.model.CongViec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CongViecDAO {

    // ================================================================
    // PHẦN 1: CÁC HÀM LẤY DỮ LIỆU (GIỮ NGUYÊN)
    // ================================================================

    public List<CongViec> getAllOpenJobs() {
        return searchAndFilterJobs(null, 0, 1, 100);
    }

    public CongViec getJobById(int id) {
        String sql = "SELECT cv.*, dm.ten_danh_muc, nd.ho_ten, nd.diem_uy_tin " +
                     "FROM CongViec cv " +
                     "LEFT JOIN DanhMuc dm ON cv.ma_danh_muc = dm.ma_danh_muc " +
                     "JOIN NguoiDung nd ON cv.nguoi_thue_id = nd.ma_nguoi_dung " +
                     "WHERE cv.ma_cong_viec = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapResultSetToJob(rs);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public List<CongViec> getJobsByOwnerId(int userId) {
        List<CongViec> list = new ArrayList<>();
        String sql = "SELECT cv.*, dm.ten_danh_muc, nd.ho_ten, nd.diem_uy_tin " +
                     "FROM CongViec cv " +
                     "LEFT JOIN DanhMuc dm ON cv.ma_danh_muc = dm.ma_danh_muc " +
                     "JOIN NguoiDung nd ON cv.nguoi_thue_id = nd.ma_nguoi_dung " +
                     "WHERE cv.nguoi_thue_id = ? " +
                     "ORDER BY cv.ngay_dang DESC";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapResultSetToJob(rs));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    private CongViec mapResultSetToJob(ResultSet rs) throws SQLException {
        CongViec job = new CongViec();
        job.setMaCongViec(rs.getInt("ma_cong_viec"));
        job.setNguoiThueId(rs.getInt("nguoi_thue_id"));
        job.setTieuDe(rs.getString("tieu_de"));
        job.setMoTa(rs.getString("mo_ta"));
        job.setDiaDiem(rs.getString("dia_diem"));
        job.setGiaTien(rs.getDouble("gia_tien"));
        job.setHanChot(rs.getTimestamp("han_chot"));
        job.setTrangThai(rs.getString("trang_thai"));
        job.setNgayDang(rs.getTimestamp("ngay_dang"));
        try { job.setTenDanhMuc(rs.getString("ten_danh_muc")); } catch (Exception e) {}
        try { job.setTenNguoiThue(rs.getString("ho_ten")); } catch (Exception e) {}
        try { job.setDiemUyTin(rs.getFloat("diem_uy_tin")); } catch (Exception e) {}
        try { job.setNguoiLamId(rs.getInt("nguoi_lam_id")); } catch (Exception e) {}
        
        try { job.setSoLuongCan(rs.getInt("so_luong_can")); } catch (Exception e) { job.setSoLuongCan(1); }
        try { job.setSoLuongDaTuyen(rs.getInt("so_luong_da_tuyen")); } catch (Exception e) { job.setSoLuongDaTuyen(0); }
        
        return job;
    }

    // ================================================================
    // PHẦN 2: INSERT (GIỮ NGUYÊN)
    // ================================================================

    public boolean insertJob(int userId, int cateId, String title, String desc, String location, double price, String deadline, int soLuong) {
        String sql = "INSERT INTO CongViec (nguoi_thue_id, ma_danh_muc, tieu_de, mo_ta, dia_diem, gia_tien, han_chot, trang_thai, ngay_dang, so_luong_can, so_luong_da_tuyen) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, 'MO', NOW(), ?, 0)";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId); 
            ps.setInt(2, cateId); 
            ps.setString(3, title);
            ps.setString(4, desc); 
            ps.setString(5, location); 
            ps.setDouble(6, price);
            ps.setString(7, deadline); 
            ps.setInt(8, soLuong);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // ================================================================
    // PHẦN 3: LOGIC DUYỆT (GIỮ NGUYÊN)
    // ================================================================

    public String approveWorker(int jobId, int workerId, int ownerId) {
        Connection conn = null;
        try {
            conn = DBContext.getConnection();
            conn.setAutoCommit(false); 

            String sqlCheck = "SELECT gia_tien, trang_thai, han_chot, so_luong_can, so_luong_da_tuyen FROM CongViec WHERE ma_cong_viec = ?";
            PreparedStatement psCheck = conn.prepareStatement(sqlCheck);
            psCheck.setInt(1, jobId);
            ResultSet rsCheck = psCheck.executeQuery();
            
            if (!rsCheck.next()) return "Công việc không tồn tại!";
            String currentStatus = rsCheck.getString("trang_thai");
            
            if (!"MO".equals(currentStatus) && !"DA_GIAO".equals(currentStatus)) return "Công việc này đã đóng hoặc hoàn thành!";
            
            int can = rsCheck.getInt("so_luong_can");
            int daTuyen = rsCheck.getInt("so_luong_da_tuyen");
            
            if (daTuyen >= can) return "Đã tuyển đủ người!";

            double giaTien = rsCheck.getDouble("gia_tien");

            String sqlBal = "SELECT so_du_vi FROM NguoiDung WHERE ma_nguoi_dung = ?";
            PreparedStatement psBal = conn.prepareStatement(sqlBal);
            psBal.setInt(1, ownerId);
            ResultSet rsBal = psBal.executeQuery();
            if (!rsBal.next()) return "Tài khoản lỗi!";
            if (rsBal.getDouble("so_du_vi") < giaTien) return "Số dư ví không đủ!";

            String sqlDeduct = "UPDATE NguoiDung SET so_du_vi = so_du_vi - ? WHERE ma_nguoi_dung = ?";
            PreparedStatement psDed = conn.prepareStatement(sqlDeduct);
            psDed.setDouble(1, giaTien); psDed.setInt(2, ownerId); psDed.executeUpdate();

            String sqlLog = "INSERT INTO GiaoDich (ma_nguoi_dung, so_tien, loai_giao_dich, mo_ta, trang_thai, ngay_tao) VALUES (?, ?, 'THANH_TOAN_VIEC', ?, 'THANH_CONG', NOW())";
            PreparedStatement psLog = conn.prepareStatement(sqlLog);
            psLog.setInt(1, ownerId); psLog.setDouble(2, -giaTien); psLog.setString(3, "Thanh toán tạm giữ job #" + jobId); psLog.executeUpdate();

            int newDaTuyen = daTuyen + 1;
            String newStatus = (newDaTuyen >= can) ? "DA_GIAO" : "MO";
            
            String sqlJob = "UPDATE CongViec SET so_luong_da_tuyen = ?, trang_thai = ?, nguoi_lam_id = ? WHERE ma_cong_viec = ?";
            PreparedStatement psJob = conn.prepareStatement(sqlJob);
            psJob.setInt(1, newDaTuyen);
            psJob.setString(2, newStatus);
            psJob.setInt(3, workerId);
            psJob.setInt(4, jobId);
            psJob.executeUpdate();

            String sqlApp = "UPDATE UngTuyen SET trang_thai = 'DA_CHON' WHERE ma_cong_viec = ? AND nguoi_ung_tuyen_id = ?";
            PreparedStatement psApp = conn.prepareStatement(sqlApp);
            psApp.setInt(1, jobId); psApp.setInt(2, workerId); psApp.executeUpdate();

            if (newDaTuyen >= can) {
                String sqlRej = "UPDATE UngTuyen SET trang_thai = 'TU_CHOI' WHERE ma_cong_viec = ? AND trang_thai = 'CHO_DUYET'";
                PreparedStatement psRej = conn.prepareStatement(sqlRej);
                psRej.setInt(1, jobId); psRej.executeUpdate();
            }

            conn.commit();
            return "SUCCESS";
        } catch (Exception e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            return "Lỗi: " + e.getMessage();
        } finally {
            try { if (conn != null) { conn.setAutoCommit(true); conn.close(); } } catch (SQLException ex) {}
        }
    }

    // ================================================================
    // PHẦN 4: FILTER & SEARCH (GIỮ NGUYÊN)
    // ================================================================

    public int countJobs(String keyword, int cateId) {
        String sql = "SELECT COUNT(*) FROM CongViec WHERE trang_thai = 'MO'";
        if (keyword != null && !keyword.isEmpty()) sql += " AND tieu_de LIKE ?";
        if (cateId > 0) sql += " AND ma_danh_muc = ?";
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            int index = 1;
            if (keyword != null && !keyword.isEmpty()) ps.setString(index++, "%" + keyword + "%");
            if (cateId > 0) ps.setInt(index++, cateId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    public List<CongViec> searchAndFilterJobs(String keyword, int cateId, int pageIndex, int pageSize) {
        List<CongViec> list = new ArrayList<>();
        String sql = "SELECT cv.*, dm.ten_danh_muc, nd.ho_ten, nd.diem_uy_tin FROM CongViec cv " +
                     "LEFT JOIN DanhMuc dm ON cv.ma_danh_muc = dm.ma_danh_muc " +
                     "JOIN NguoiDung nd ON cv.nguoi_thue_id = nd.ma_nguoi_dung " +
                     "WHERE cv.trang_thai = 'MO'";
                     
        if (keyword != null && !keyword.isEmpty()) sql += " AND cv.tieu_de LIKE ?";
        if (cateId > 0) sql += " AND cv.ma_danh_muc = ?";
        sql += " ORDER BY cv.ngay_dang DESC LIMIT ?, ?";
        
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            int paramIndex = 1;
            if (keyword != null && !keyword.isEmpty()) ps.setString(paramIndex++, "%" + keyword + "%");
            if (cateId > 0) ps.setInt(paramIndex++, cateId);
            int offset = (pageIndex - 1) * pageSize; 
            ps.setInt(paramIndex++, offset); ps.setInt(paramIndex++, pageSize);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) { list.add(mapResultSetToJob(rs)); }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // ================================================================
    // PHẦN 5: CÁC HÀM XỬ LÝ TRẠNG THÁI (ĐÃ CẬP NHẬT LOGIC ĐA NGƯỜI)
    // ================================================================

    // 1. Worker Báo cáo hoàn thành
    public boolean workerReportCompletion(int jobId, int workerId) {
        Connection conn = null;
        try {
            conn = DBContext.getConnection();
            // [UPDATE] Cho phép báo cáo cả khi Job vẫn là 'MO' (đang tuyển người khác)
            // Miễn là người này đã được chọn và đang làm việc
            String sql = "UPDATE CongViec SET trang_thai = 'CHO_XAC_NHAN' " +
                         "WHERE ma_cong_viec = ? AND nguoi_lam_id = ? " +
                         "AND (trang_thai = 'DA_GIAO' OR trang_thai = 'MO')";
                         
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, jobId);
            ps.setInt(2, workerId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); } 
        finally { try { if (conn != null) conn.close(); } catch (SQLException e) {} }
        return false;
    }

    // 2. Chủ nhà xác nhận & Đánh giá (GIỮ NGUYÊN)
    public String confirmAndRateWorker(int jobId, int ownerId, int stars, String comment) {
        Connection conn = null;
        try {
            conn = DBContext.getConnection();
            conn.setAutoCommit(false); 

            String sqlGetJob = "SELECT gia_tien, nguoi_lam_id, trang_thai FROM CongViec WHERE ma_cong_viec = ? AND nguoi_thue_id = ?";
            PreparedStatement psJob = conn.prepareStatement(sqlGetJob);
            psJob.setInt(1, jobId);
            psJob.setInt(2, ownerId);
            ResultSet rs = psJob.executeQuery();
            
            if (!rs.next()) return "Lỗi: Không tìm thấy job hoặc bạn không phải chủ!";
            
            double giaTien = rs.getDouble("gia_tien");
            int workerId = rs.getInt("nguoi_lam_id");
            String status = rs.getString("trang_thai");

            if ("HOAN_THANH".equals(status)) return "Công việc đã hoàn thành rồi!";

            double luongThucNhan = giaTien * 0.95; 
            double phiHoaHong = giaTien * 0.05;

            String sqlPayWorker = "UPDATE NguoiDung SET so_du_vi = so_du_vi + ? WHERE ma_nguoi_dung = ?";
            PreparedStatement psPay = conn.prepareStatement(sqlPayWorker);
            psPay.setDouble(1, luongThucNhan); psPay.setInt(2, workerId); psPay.executeUpdate();

            String sqlPayAdmin = "UPDATE NguoiDung SET so_du_vi = so_du_vi + ? WHERE vai_tro = 'ADMIN' LIMIT 1";
            PreparedStatement psAdm = conn.prepareStatement(sqlPayAdmin);
            psAdm.setDouble(1, phiHoaHong); psAdm.executeUpdate();

            String sqlUp = "UPDATE CongViec SET trang_thai = 'HOAN_THANH' WHERE ma_cong_viec = ?";
            PreparedStatement psUp = conn.prepareStatement(sqlUp);
            psUp.setInt(1, jobId); psUp.executeUpdate();

            String sqlRev = "INSERT INTO danhgia(ma_cong_viec, nguoi_danh_gia_id, nguoi_duoc_danh_gia_id, so_sao, nhan_xet, ngay_danh_gia) VALUES (?, ?, ?, ?, ?, NOW())";
            PreparedStatement psRev = conn.prepareStatement(sqlRev);
            psRev.setInt(1, jobId); psRev.setInt(2, ownerId); psRev.setInt(3, workerId); psRev.setInt(4, stars); psRev.setString(5, comment); psRev.executeUpdate();

            String sqlRep = "UPDATE NguoiDung SET diem_uy_tin = (SELECT AVG(so_sao) FROM danhgia WHERE nguoi_duoc_danh_gia_id = ?) WHERE ma_nguoi_dung = ?";
            PreparedStatement psRep = conn.prepareStatement(sqlRep);
            psRep.setInt(1, workerId); psRep.setInt(2, workerId); psRep.executeUpdate();

            String sqlLog = "INSERT INTO GiaoDich(ma_nguoi_dung, so_tien, loai_giao_dich, mo_ta, trang_thai, ngay_tao) VALUES (?, ?, 'NHAN_LUONG', ?, 'THANH_CONG', NOW())";
            PreparedStatement psLog = conn.prepareStatement(sqlLog);
            psLog.setInt(1, workerId); psLog.setDouble(2, luongThucNhan); psLog.setString(3, "Nhận lương job #" + jobId); psLog.executeUpdate();

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

    // 3. Người làm hủy việc
    public boolean workerCancelJob(int jobId, int workerId) {
        Connection conn = null;
        try {
            conn = DBContext.getConnection();
            conn.setAutoCommit(false); 

            // [UPDATE] Cho phép hủy cả khi Job là 'MO' (nếu mình đang làm)
            String sqlGet = "SELECT gia_tien, nguoi_thue_id FROM CongViec " +
                            "WHERE ma_cong_viec = ? AND nguoi_lam_id = ? " +
                            "AND (trang_thai = 'DA_GIAO' OR trang_thai = 'MO')";
                            
            PreparedStatement psGet = conn.prepareStatement(sqlGet);
            psGet.setInt(1, jobId);
            psGet.setInt(2, workerId);
            ResultSet rs = psGet.executeQuery();
            
            if (rs.next()) {
                double amount = rs.getDouble("gia_tien");
                int ownerId = rs.getInt("nguoi_thue_id");

                String sqlRefund = "UPDATE NguoiDung SET so_du_vi = so_du_vi + ? WHERE ma_nguoi_dung = ?";
                PreparedStatement psRef = conn.prepareStatement(sqlRefund);
                psRef.setDouble(1, amount);
                psRef.setInt(2, ownerId);
                psRef.executeUpdate();

                String sqlLog = "INSERT INTO GiaoDich(ma_nguoi_dung, so_tien, loai_giao_dich, mo_ta, trang_thai, ngay_tao) VALUES(?, ?, 'HOAN_TIEN', ?, 'THANH_CONG', NOW())";
                PreparedStatement psLog = conn.prepareStatement(sqlLog);
                psLog.setInt(1, ownerId);
                psLog.setDouble(2, amount); 
                psLog.setString(3, "Hoàn tiền do người làm hủy job #" + jobId);
                psLog.executeUpdate();

                String sqlUp = "UPDATE CongViec SET trang_thai = 'DA_HUY' WHERE ma_cong_viec = ?";
                PreparedStatement psUp = conn.prepareStatement(sqlUp);
                psUp.setInt(1, jobId);
                psUp.executeUpdate();

                conn.commit(); 
                return true;
            }

        } catch (Exception e) {
            try { if(conn!=null) conn.rollback(); } catch(SQLException ex){}
            e.printStackTrace();
        } finally {
            try { if(conn!=null) { conn.setAutoCommit(true); conn.close(); } } catch(SQLException ex){}
        }
        return false;
    }
}