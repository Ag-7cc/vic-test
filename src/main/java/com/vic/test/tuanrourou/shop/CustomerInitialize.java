package com.vic.test.tuanrourou.shop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.vic.test.util.ConnectionUtil;
import com.vic.test.util.OfficeUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: vic
 * @CreateTime : 2018/7/26 16:05
 */
public class CustomerInitialize {

    private Connection connection = ConnectionUtil.getConnection(ConnectionUtil.TRR_PRD);

    public static void main(String[] args) throws Exception {
        CustomerInitialize customerInitialize = new CustomerInitialize();
        customerInitialize.execute();
    }

    public void execute() throws Exception {
        readExcel();

    }

    public void readExcel() throws Exception {
        File file = new File("/Users/vic/Documents/work/团肉肉/需求/需求列表/钉钉CRM/全国销售对应信息.xls");
        for (int i = 1; i <= 2; i++) {
            List<List<String>> lists = OfficeUtil.readExcel(file, i);
            Set<String> userMobileSet = Sets.newHashSet();
            Set<String> saleNameSet = Sets.newHashSet();
            List<CustomerInfo> customerInfoList = lists.stream()
                    .skip(1)
                    .filter(list -> list.size() >= 3 && StringUtils.isNotBlank(list.get(0)))
                    .map(list -> {
                        CustomerInfo customerInfo = new CustomerInfo();
                        customerInfo.setUserMobile(StringUtils.trimToEmpty(list.get(0)));
                        customerInfo.setCustomerName(StringUtils.trimToEmpty(list.get(2)));
                        customerInfo.setSaleName(StringUtils.trimToEmpty(list.get(1)));

                        userMobileSet.add(StringUtils.trimToEmpty(list.get(0)));
                        saleNameSet.add(StringUtils.trimToEmpty(list.get(1)));
                        return customerInfo;
                    }).collect(Collectors.toList());

            Set<Integer> userIdSet = Sets.newHashSet();
            Map<String, UserInfo> userByMobiles = findUserByMobiles(Lists.newArrayList(userMobileSet));
            customerInfoList.stream()
                    .filter(customerInfo -> Objects.nonNull(userByMobiles.get(customerInfo.getUserMobile())))
                    .forEach(customerInfo -> {
                        UserInfo userInfo = userByMobiles.get(customerInfo.getUserMobile());
                        customerInfo.setUserId(userInfo.getUserId());
                        customerInfo.setMobile(userInfo.getMobile());
                        userIdSet.add(userInfo.getUserId());
                    });
            Map<Integer, OrderInfo> orderInfoMap = findOrderByUserId(Lists.newArrayList(userIdSet));
            Map<String, Integer> salesMap = findSales();
            customerInfoList.stream()
                    .filter(customerInfo -> Objects.nonNull(orderInfoMap.get(customerInfo.getUserId())))
                    .forEach(customerInfo -> {
                        OrderInfo orderInfo = orderInfoMap.get(customerInfo.getUserId());
                        customerInfo.setLinkName(orderInfo.getContactName());
                        customerInfo.setProvinceId(orderInfo.getContactProvinceID());
                        customerInfo.setCityId(orderInfo.getContactCityID());
                        customerInfo.setDistrictId(orderInfo.getContactDistrictID());
                        customerInfo.setAddress(orderInfo.getContactAddress());
                        customerInfo.setCustomerType(orderInfo.getCustomerType());
                        customerInfo.setDemandType(orderInfo.getDemandType());
                        customerInfo.setDemandTypeDetail(orderInfo.getDemandTypeDetail());

                        Integer saleId = salesMap.getOrDefault(customerInfo.getSaleName(), 0);
                        customerInfo.setSaleId(saleId);
                    });

            List<CustomerInfo> customerInfos = customerInfoList.stream().filter(customerInfo -> Objects.nonNull(customerInfo.getUserId()) && Objects.nonNull(customerInfo.getDemandType())).collect(Collectors.toList());
            saveCustomer(customerInfos);
            System.out.println(userMobileSet.size());
        }
    }

    public void saveCustomer(List<CustomerInfo> customerInfos) throws SQLException {
        String sql = "INSERT INTO `SHOP_Customer` ( `CustomerName`, `Mobile`, `LinkName`, `ProvinceID`, `CityID`, `DistrictID`, `Address`, `CustomerType`, `DemandType`, `DemandTypeDetail`,`SaleID`)\n" +
                "VALUES ";

        StringBuffer params = new StringBuffer();

        customerInfos.stream().forEach(customerInfo -> {
            params.append(",(?,?,?,?,?,?,?,?,?,?,?)");
        });
        String substring = params.toString().substring(1);

        PreparedStatement preparedStatement = connection.prepareStatement(sql + substring);
        AtomicInteger index = new AtomicInteger(0);
        customerInfos.stream().forEach(customerInfo -> {
            try {
                preparedStatement.setString(index.incrementAndGet(), customerInfo.getCustomerName());
                preparedStatement.setString(index.incrementAndGet(), customerInfo.getMobile());
                preparedStatement.setString(index.incrementAndGet(), customerInfo.getLinkName());
                preparedStatement.setInt(index.incrementAndGet(), customerInfo.getProvinceId());
                preparedStatement.setInt(index.incrementAndGet(), customerInfo.getCityId());
                preparedStatement.setInt(index.incrementAndGet(), customerInfo.getDistrictId());
                preparedStatement.setString(index.incrementAndGet(), customerInfo.getAddress());
                preparedStatement.setInt(index.incrementAndGet(), customerInfo.getCustomerType());
                preparedStatement.setInt(index.incrementAndGet(), customerInfo.getDemandType());
                preparedStatement.setString(index.incrementAndGet(), customerInfo.getDemandTypeDetail());
                preparedStatement.setInt(index.incrementAndGet(), customerInfo.getSaleId());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        preparedStatement.execute();
        System.out.println("保存成功"+customerInfos.size());

    }

    public Map<String, UserInfo> findUserByMobiles(List<String> mobiles) throws SQLException {
        Map<String, UserInfo> map = Maps.newHashMap();
        String sql = "SELECT * FROM `Account` WHERE `MobileNo` IN (%s)";
        StringBuffer param = new StringBuffer();
        mobiles.stream().forEach(s -> param.append(String.format(",'%s'", s)));

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(String.format(sql, param.toString().substring(1)));

        while (resultSet.next()) {
            int userID = resultSet.getInt("UserID");
            String mobileNo = resultSet.getString("MobileNo");
            String name = resultSet.getString("Name");
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(userID);
            userInfo.setMobile(mobileNo);
            userInfo.setName(name);
            map.put(mobileNo, userInfo);
        }
        return map;
    }

    public Map<Integer, OrderInfo> findOrderByUserId(List<Integer> userIds) throws SQLException {
        Map<Integer, OrderInfo> map = Maps.newHashMap();
        String sql = "SELECT * FROM `SHOP_Order` WHERE `UserID` IN (%s) order by `AddTime` DESC ";
        StringBuffer param = new StringBuffer();
        userIds.stream().forEach(s -> param.append(String.format(",%s", s)));

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(String.format(sql, param.toString().substring(1)));

        Set<Integer> orderIdSet = Sets.newHashSet();

        while (resultSet.next()) {
            int userID = resultSet.getInt("UserID");

            Integer orderID = resultSet.getInt("OrderID");
            Integer ContactProvinceID = resultSet.getInt("ContactProvinceID");
            Integer ContactCityID = resultSet.getInt("ContactCityID");
            Integer ContactDistrictID = resultSet.getInt("ContactDistrictID");
            String ShopName = resultSet.getString("ShopName");
            String ContactPhone = resultSet.getString("ContactPhone");
            String ContactName = resultSet.getString("ContactName");
            String ContactAddress = resultSet.getString("ContactAddress");

            orderIdSet.add(orderID);

            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderId(orderID);
            orderInfo.setUserId(userID);
            orderInfo.setContactProvinceID(ContactProvinceID);
            orderInfo.setContactCityID(ContactCityID);
            orderInfo.setContactDistrictID(ContactDistrictID);
            orderInfo.setShopName(ShopName);
            orderInfo.setContactPhone(ContactPhone);
            orderInfo.setContactName(ContactName);
            orderInfo.setContactAddress(ContactAddress);

            if (!map.containsKey(userID)) {
                map.put(userID, orderInfo);
            }
        }

        Map<Integer, List<OrderDetailInfo>> orderDetailMap = findOrderDetailByOrderId(Lists.newArrayList(orderIdSet));
        map.forEach((integer, orderInfo) -> {
            List<OrderDetailInfo> orderDetailInfos = orderDetailMap.get(orderInfo.getOrderId());
            if (!CollectionUtils.isEmpty(orderDetailInfos)) {
                int demandType = 0;
                int customerType = 0;
                List<JSONObject> objectList = Lists.newArrayList();
                for (OrderDetailInfo detailInfo : orderDetailInfos) {
                    customerType |= detailInfo.getCustomerType();
                    demandType |= detailInfo.getDemandType();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("type", demandType);
                    jsonObject.put("weight", detailInfo.getWeight());
                    objectList.add(jsonObject);
                }
                orderInfo.setCustomerType(customerType);
                orderInfo.setDemandType(demandType);
                orderInfo.setDemandTypeDetail(JSON.toJSONString(objectList));
            }
        });

        return map;
    }

    public Map<Integer, List<OrderDetailInfo>> findOrderDetailByOrderId(List<Integer> orderIds) throws SQLException {
        Map<Integer, Integer> productMap = findProductMap();
        Map<Integer, Integer> goodsCustomerTypeMap = findGoodsCustomerTypeMap();

        Map<Integer, List<OrderDetailInfo>> map = Maps.newHashMap();
        String sql = "SELECT * FROM `SHOP_OrderDetail` WHERE `OrderID` IN (%s)";
        StringBuffer param = new StringBuffer();
        orderIds.stream().forEach(s -> param.append(String.format(",%s", s)));

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(String.format(sql, param.toString().substring(1)));

        while (resultSet.next()) {
            Integer orderID = resultSet.getInt("OrderID");
            Integer ProductID = resultSet.getInt("ProductID");
            Integer GoodsID = resultSet.getInt("GoodsID");
            Integer Weight = resultSet.getInt("Weight");

            OrderDetailInfo orderDetailInfo = new OrderDetailInfo();
            orderDetailInfo.setOrderId(orderID);
            orderDetailInfo.setProductId(ProductID);
            orderDetailInfo.setGoodsId(GoodsID);
            orderDetailInfo.setWeight(Weight);

            orderDetailInfo.setDemandType(productMap.getOrDefault(ProductID, 0));
            orderDetailInfo.setCustomerType(goodsCustomerTypeMap.getOrDefault(GoodsID, 0));

            if (!map.containsKey(orderID)) {
                List<OrderDetailInfo> detailInfoList = map.getOrDefault(orderID, Lists.newArrayList());
                detailInfoList.add(orderDetailInfo);
                map.put(orderID, detailInfoList);
            }
        }
        return map;
    }


    public Map<Integer, CategoryInfo> findCategoryMap() throws SQLException {
        Map<Integer, CategoryInfo> map = Maps.newHashMap();
        String sql = "SELECT * FROM `SHOP_Category`";

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            int categoryID = resultSet.getInt("CategoryID");
            int parentID = resultSet.getInt("ParentID");
            CategoryInfo categoryInfo = new CategoryInfo();
            categoryInfo.setCategoryID(categoryID);
            categoryInfo.setParentID(parentID);
            map.put(categoryID, categoryInfo);
        }
        return map;
    }

    public Map<Integer, Integer> findGoodsCustomerTypeMap() throws SQLException {

        Map<Integer, Integer> goodsMap = Maps.newHashMap();

        Map<Integer, Integer> customerTypeMap = Maps.newHashMap();
        customerTypeMap.put(1, 1);
        customerTypeMap.put(2, 2);
        customerTypeMap.put(3, 4);
        customerTypeMap.put(33, 8);
        customerTypeMap.put(32, 16);
        customerTypeMap.put(31, 32);
        customerTypeMap.put(34, 64);

        Map<Integer, List<OrderDetailInfo>> map = Maps.newHashMap();
        String sql = "SELECT * FROM `SHOP_GoodsProperty` WHERE `PVID` IN(1,2,3,31,32,33,34)";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            Integer GoodsID = resultSet.getInt("GoodsID");
            Integer PVID = resultSet.getInt("PVID");

            Integer orDefault = goodsMap.getOrDefault(GoodsID, 0);
            orDefault |= customerTypeMap.getOrDefault(PVID, 0);
            goodsMap.put(GoodsID, orDefault);
        }
        return goodsMap;
    }

    public Map<Integer, Integer> findProductMap() throws SQLException {
        Map<Integer, Integer> productMap = Maps.newHashMap();

        String sql = "SELECT * FROM `SHOP_Product`";

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        Map<Integer, CategoryInfo> categoryDTOMap = findCategoryMap();
        Function<Integer, Integer> getParentId = categoryId -> {
            CategoryInfo categoryDTO = categoryDTOMap.get(categoryId);
            if (categoryDTO.getParentID() == 0) {
                return categoryDTO.getCategoryID();
            }
            return categoryDTO.getParentID();
        };

        Map<Integer, Integer> demandTypeMap = Maps.newHashMap();
        demandTypeMap.put(1, 1);
        demandTypeMap.put(2, 2);
        demandTypeMap.put(3, 4);
        demandTypeMap.put(4, 8);
        demandTypeMap.put(5, 16);


        while (resultSet.next()) {
            int ProductID = resultSet.getInt("ProductID");
            int CategoryID = resultSet.getInt("CategoryID");
            Integer apply = getParentId.apply(CategoryID);
            productMap.put(ProductID, demandTypeMap.getOrDefault(apply, 0));
        }

        return productMap;
    }


    public Map<String, Integer> findSales() throws SQLException {
        Map<String, Integer> saleNameMap = Maps.newHashMap();

        String sql = "SELECT * FROM `CON_Account` WHERE `Status` = 'Normal'";

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            int AccountId = resultSet.getInt("AccountId");
            String Name = resultSet.getString("Name");
            saleNameMap.put(Name, AccountId);
        }
        return saleNameMap;
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomerInfo {
        private Integer userId;
        private String userMobile;
        private Integer customerId;
        private String customerName;
        private String mobile;
        private String linkName;
        private Integer provinceId;
        private Integer cityId;
        private Integer districtId;
        private String address;
        private Integer customerType;
        private Integer demandType;
        private String demandTypeDetail;
        private String saleName;
        private Integer saleId;
    }

    @Data
    public static class OrderInfo {
        private Integer userId;
        private Integer orderId;
        private int contactProvinceID;
        private int contactCityID;
        private int contactDistrictID;
        private String ShopName;
        private String contactPhone;
        private String contactName;
        private String contactAddress;

        private Integer customerType;
        private Integer demandType;
        private String demandTypeDetail;
    }

    @Data
    public static class OrderDetailInfo {
        private Integer orderId;
        private Integer productId;
        private Integer goodsId;
        private Integer weight;
        private Integer demandType;
        private Integer customerType;
    }

    @Data
    public static class UserInfo {
        private Integer userId;
        private String name;
        private String mobile;
    }

    @Data
    public static class CategoryInfo {
        private int categoryID;
        private int parentID;
    }
}
