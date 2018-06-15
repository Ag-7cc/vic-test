package com.vic.test.tuanrourou.shop;

import com.google.common.collect.Lists;
import com.vic.test.util.ConnectionUtil;
import com.vic.test.util.OfficeUtil;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by vic
 * Create time : 2018/6/15 14:59
 */
@Log4j
public class StorageStockCostUpdater {


    public static void main(String[] args) throws IOException {
        Connection connection = ConnectionUtil.getConnection(ConnectionUtil.TRR_PRD);

        List<StorageCost> excelCosts = loadExcel("/data/tmp/单位成本表6.15.xlsx");
        System.out.println("更新数量》》" + excelCosts.size());
        Map<Integer, StorageCost> excelCostMap = excelCosts.stream().collect(Collectors.toMap(StorageCost::getId, Function.identity()));

        Set<Integer> collect = excelCosts.stream().map(StorageCost::getId).collect(Collectors.toSet());
        List<StorageCost> oldCost = findStorageByIds(connection, collect);


        List<StorageCost> diffCost = Lists.newArrayList();
        oldCost.stream().forEach(storageCost -> {
            StorageCost excelCost = excelCostMap.get(storageCost.getId());
            if (storageCost.getCost() != excelCost.getCost()) {
                String updateSql = "UPDATE `SHOP_StorageStock` SET `Cost` = %s WHERE `StorageStockID` = %s; -- cost:%s c:%s p:%s ";
                System.out.println(String.format(updateSql, excelCost.getCost(), storageCost.getId(), storageCost.getCost(), storageCost.getCityId(), storageCost.getProductId()));
                storageCost.setNewCost(excelCost.getCost());
                diffCost.add(storageCost);
            }
        });
        updateOutStockCost(diffCost);
        instAlterCostLog(diffCost);
    }

    public static void updateOutStockCost(List<StorageCost> diffCost) {
        diffCost.stream().forEach(storageCost -> {
            String updateSql = "UPDATE `SHOP_Log_OutStockCost` SET `Cost` = %s WHERE `BizType` = 1 AND `CityID` = %s AND `ProductID` = %s;";
            System.out.println(String.format(updateSql, storageCost.getNewCost(), storageCost.cityId, storageCost.getProductId()));
        });
    }

    public static void instAlterCostLog(List<StorageCost> diffCost) {
        String insertSql = "INSERT INTO `SHOP_Log_AlterCost` (`CityID`,`ProductID`,`Cost`,`NewCost`) VALUES ";
        System.out.println(insertSql);
        AtomicInteger index = new AtomicInteger(0);
        diffCost.stream().forEach(storageCost -> {
            String sql = (index.get() > 0 ? "," : "") + "(%s,%s,%s,%s)";
            index.incrementAndGet();
            System.out.println(String.format(sql, storageCost.getCityId(), storageCost.getProductId(), storageCost.getCost(), storageCost.getNewCost()));
        });
    }

    public static List<StorageCost> findStorageByIds(Connection connection, Set<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Lists.newArrayList();
        }
        StringBuffer sql = new StringBuffer("SELECT * FROM `SHOP_StorageStock` WHERE `StorageStockID` IN ");
        String[] strings = ids.stream().map(String::valueOf).collect(Collectors.toList()).toArray(new String[]{});
        String join = String.join(",", strings);
        sql.append("(");
        sql.append(join);
        sql.append(")");

        List<StorageCost> storageCosts = Lists.newArrayList();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int storageStockID = resultSet.getInt("StorageStockID");
                int cost = resultSet.getInt("Cost");
                int cityID = resultSet.getInt("CityID");
                int productID = resultSet.getInt("ProductID");
                StorageCost storageCost = new StorageCost();
                storageCost.setId(storageStockID);
                storageCost.setCost(cost);
                storageCost.setCityId(cityID);
                storageCost.setProductId(productID);
                storageCosts.add(storageCost);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return storageCosts;
    }

    public static List<StorageCost> loadExcel(String filePath) throws IOException {
        File file = new File(filePath);
        List<List<String>> rows = OfficeUtil.readExcel(file, 0);

        List<StorageCost> storageCosts = Lists.newArrayList();
        rows.stream()
                .skip(1)
                .filter(cols -> cols.size() >= 4)
                .forEach(cols -> {
                    try {
                        StorageCost storageCost = new StorageCost();
                        storageCost.setId(Integer.parseInt(cols.get(0)));
                        storageCost.setSku(cols.get(1));
                        storageCost.setProductName(cols.get(2));
                        storageCost.setCostStr(cols.get(3));
                        storageCost.setCost(new BigDecimal(cols.get(3)).multiply(new BigDecimal("100")).intValue());
                        storageCosts.add(storageCost);
                    } catch (Exception e) {
                        log.info(cols);
                    }
                });
        return storageCosts;
    }

    @Data
    public static class StorageCost {
        private int id;
        private String sku;
        private String productName;
        private int cityId;
        private int productId;
        private String costStr;
        private int cost;
        private int newCost;
    }
}
