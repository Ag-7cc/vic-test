package com.vic.test.kakatrip.sql;


import com.vic.test.util.ConnectionUtil;

import java.sql.*;

/**
 * Created by vic
 * Create time : 2017/5/8 下午12:29
 */
public class HotelSql {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        Connection connection = ConnectionUtil.getConnection(ConnectionUtil.KAKATRIP);

        String sql = "select * from Account where userId = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, 1);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            System.out.println(resultSet.getString("mobileNo")+"  "+resultSet.getString("userId"));
        }
    }


}
