package com.vic.test.tuanrourou.trade;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.vic.test.tuanrourou.trade.dto.SellOrderDTO;
import com.vic.test.util.ConnectionUtil;
import com.vic.test.util.OfficeUtil;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by vic
 * Create time : 2018/4/26 13:42
 */
public class SellOrderHandler {


    public static void main(String[] args) throws IOException, SQLException {

        Connection connection = ConnectionUtil.getConnection(ConnectionUtil.TRR_BETA);
        Set<String> mobileSet = Sets.newHashSet("15821244303", "13812345678");
        Map<String, Integer> userIdMap = loadUserIdMap(connection, mobileSet);
        Map<String, Integer> categoryIdMap = loadCategoryIdMap(connection);
        Map<String, Integer> cityIdMap = loadCityIdMap(connection);
        connection.close();

        File file = new File("/Users/vic/Desktop/供应求购导入模版.xlsx");
        List<List<String>> rows = OfficeUtil.read2007Excel(file, null, "供应单");

        rows.stream().forEach(cols -> {
            cols.stream().forEach(cell -> {
                System.out.print(cell+" ");
            });
            System.out.println();
        });
    }

    public static SellOrderDTO parseToSellOrderDTO(List<String> cols, Map<String, Integer> userIdMap, Map<String, Integer> categoryIdMap, Map<String, Integer> cityIdMap) {

        SellOrderDTO sellOrderDTO = new SellOrderDTO();
        if (!CollectionUtils.isEmpty(cols) && cols.size() >= 14) {
            sellOrderDTO.setUserId(userIdMap.getOrDefault(cols.get(1), 0));

            sellOrderDTO.setCategoryId(categoryIdMap.getOrDefault(cols.get(2) + "-" + cols.get(3), 0));
            sellOrderDTO.setProductName(cols.get(4));
            sellOrderDTO.setBrand(cols.get(5));
//            sellOrderDTO.setSpecification();
        }
        return sellOrderDTO;
    }


    public static Map<String, Integer> loadCategoryIdMap(Connection connection) throws SQLException {
        String sql = String.format("SELECT b.`CategoryID`,CONCAT(a.`Name`,'-', b.`Name`) Name  FROM `TRD_Category` a LEFT JOIN `TRD_Category` b ON a.`CategoryID` = b.`ParentID` WHERE a.`ParentID`= 0");
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        Map<String, Integer> map = Maps.newHashMap();
        while (resultSet.next()) {
            map.put(resultSet.getString("Name"), resultSet.getInt("CategoryID"));
        }
        resultSet.close();
        preparedStatement.close();
        return map;
    }

    public static Map<String, Integer> loadUserIdMap(Connection connection, Set<String> mobileNoSet) throws SQLException {
        String sql = String.format("SELECT `UserID`,`MobileNo` FROM `Account` WHERE `MobileNo` IN (%s)", mobileNoSet.stream().reduce((s, s2) -> "'" + s.concat("','" + s2 + "'")).get());
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        Map<String, Integer> map = Maps.newHashMap();
        while (resultSet.next()) {
            map.put(resultSet.getString("MobileNo"), resultSet.getInt("UserID"));
        }
        resultSet.close();
        preparedStatement.close();
        return map;
    }


    public static Map<String, Integer> loadCityIdMap(Connection connection) throws SQLException {
        String sql = "SELECT `CityID`,`Name` FROM `City`";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        Map<String, Integer> map = Maps.newHashMap();
        while (resultSet.next()) {
            map.put(resultSet.getString("Name"), resultSet.getInt("CityID"));
        }
        resultSet.close();
        preparedStatement.close();
        return map;
    }
}
