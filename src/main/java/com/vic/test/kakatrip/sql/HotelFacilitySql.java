package com.vic.test.kakatrip.sql;

import com.vic.test.util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by vic
 * Create time : 2017/6/1 下午4:51
 */
public class HotelFacilitySql {

    public static void main(String[] args) throws SQLException {
        Connection connection = ConnectionUtil.getConnection(ConnectionUtil.KAKATRIP);

        for (HotelFacilityEnum hotelFacilityEnum : HotelFacilityEnum.values()){
            String sql = "  INSERT INTO HT_Hotel_Facility (FacilityId,NAME,Logo) VALUES (?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, hotelFacilityEnum.facilityId);
            preparedStatement.setString(2,hotelFacilityEnum.text);
            preparedStatement.setString(3,hotelFacilityEnum.logo);
            preparedStatement.execute();
        }
    }
}
