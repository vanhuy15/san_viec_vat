package com.viecvat.dao;

import com.viecvat.context.DBContext;
import com.viecvat.model.DanhMuc;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DanhMucDAO {

    public List<DanhMuc> getAll() {
        List<DanhMuc> list = new ArrayList<>();
        String sql = "SELECT * FROM DanhMuc";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                list.add(new DanhMuc(
                    rs.getInt("ma_danh_muc"),
                    rs.getString("ten_danh_muc")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}