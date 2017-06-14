package com.vic.test.kakatrip.sql;


import java.sql.*;

/**
 * Created by vic
 * Create time : 2017/5/8 下午12:29
 */
public class HotelSql {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://10.99.99.137:3306/kakatrip?user=admin&password=yHrW4FFaOMGj&useUnicode=true&characterEncoding=UTF8";
        Connection connection = DriverManager.getConnection(url);

        String sql = "select * from Account where userId = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, 1);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            System.out.println(resultSet.getString("mobileNo")+"  "+resultSet.getString("userId"));
        }
    }


}
