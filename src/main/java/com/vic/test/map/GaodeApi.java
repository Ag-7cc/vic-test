package com.vic.test.map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.vic.test.util.ConnectionUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.net.URLEncoder;
import java.sql.*;
import java.util.List;
import java.util.Map;

/**
 * Created by vic
 * Create time : 2017/11/15 15:28
 */
public class GaodeApi {

    public static final Connection connection = ConnectionUtil.getConnection(ConnectionUtil.LOCALHOST_VIC);

    public static List<Map<String, String>> getDataList() {
        List<Map<String, String>> dataList = Lists.newArrayList();

        try {
            String sql = "select * from t_receipt_address";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String cName = resultSet.getString("cName");
                String address = resultSet.getString("address");
                String receiver = resultSet.getString("receiver");
                String phone = resultSet.getString("phone");
                String uId = resultSet.getString("uId");
                String isDefault = resultSet.getString("isDefault");
                String raId = resultSet.getString("raId");
                String cId = resultSet.getString("cId");

                Map map = Maps.newHashMap();
                map.put("raId", raId);
                map.put("cName", cName);
                map.put("address", address);
                map.put("receiver", receiver);
                map.put("phone", phone);
                map.put("uId", uId);
                map.put("cId", cId);
                map.put("isDefault", isDefault);

                dataList.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dataList;
    }


    public static void save(List<Map<String, String>> dataList) {
        try {

            StringBuffer sql = new StringBuffer("INSERT INTO `t_receipt_address_m` (`raId`,`uId`, `receiver`, `address`, `phone`, `isDefault`, `cId`, `cName`, `province`, `city`, `district`, `formatted_address`) VALUES ");

            for (int i = 0; i < dataList.size(); i++) {
                sql.append("(?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)" + (i < dataList.size() - 1 ? "," : ""));
            }
            PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
            for (int i = 0; i < dataList.size(); i++) {


                Map<String, String> resultSet = dataList.get(i);

                String raId = resultSet.get("raId");
                String uId = resultSet.get("uId");
                String receiver = resultSet.get("receiver");
                String address = resultSet.get("address");
                String phone = resultSet.get("phone");
                String isDefault = resultSet.get("isDefault");
                String cId = resultSet.get("cId");
                String cName = resultSet.get("cName");
                String province = resultSet.get("province");
                String city = resultSet.get("city");
                String district = resultSet.get("district");
                String formatted_address = resultSet.get("formatted_address");

                int begin = i * 12;
                preparedStatement.setString(begin + 1, raId);
                preparedStatement.setString(begin + 2, uId);
                preparedStatement.setString(begin + 3, receiver);
                preparedStatement.setString(begin + 4, address);
                preparedStatement.setString(begin + 5, phone);
                preparedStatement.setString(begin + 6, isDefault);
                preparedStatement.setString(begin + 7, cId);
                preparedStatement.setString(begin + 8, cName);
                preparedStatement.setString(begin + 9, province);
                preparedStatement.setString(begin + 10, city);
                preparedStatement.setString(begin + 11, district);
                preparedStatement.setString(begin + 12, formatted_address);
            }
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        List<Map<String, String>> dataList = getDataList();

        String url = "http://restapi.amap.com/v3/geocode/geo?key=787668f5993d7c823c198ccc4307386a&address=%s&city=%s";
        dataList.stream()./*limit(1).*/forEach(item -> {
                    try {
                        String cityName = item.get("cName");
                        String address = item.get("address");
                        cityName = URLEncoder.encode(cityName, "utf-8");
                        address = URLEncoder.encode(address, "utf-8");
                        String getUrl = String.format(url, address, cityName);

                        CloseableHttpClient httpClient = HttpClients.createDefault();
                        HttpGet httpGet = new HttpGet(getUrl);


                        CloseableHttpResponse execute = httpClient.execute(httpGet);
                        HttpEntity entity = execute.getEntity();

                        String string = EntityUtils.toString(entity);

                        JSONObject jsonObject = JSON.parseObject(string);
                        JSONArray geocodes = jsonObject.getJSONArray("geocodes");

                        if (geocodes.size() > 0) {
                            JSONObject geocode = geocodes.getJSONObject(0);
                            String district = geocode.getString("district");
                            String city = geocode.getString("city");
                            String province = geocode.getString("province");
                            String formatted_address = geocode.getString("formatted_address");

                            item.put("district", district);
                            item.put("city", city);
                            item.put("province", province);
                            item.put("formatted_address", formatted_address);
                            System.out.println(">>>>>"+item);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

        save(dataList);
        System.out.println("ok");
    }

}
