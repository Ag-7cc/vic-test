package com.vic.test.spider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.vic.test.util.ConnectionUtil;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 携程酒店价格
 * Created by vic
 * Create time : 2017/6/5 上午10:56
 */
@Log4j
public class CtripHotelSpider {

    public static String host = "http://hotels.ctrip.com";

    private Set<String> cityIdSet = Sets.newHashSet();
    private Set<String> hotelIdSet = Sets.newHashSet();
    private Set<String> roomTypeIdSet = Sets.newHashSet();
    private Set<String> skuIdSet = Sets.newHashSet();

    private static ExecutorService threadPool = new ThreadPoolExecutor(20, 250, 100, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));

    public static void main(String[] args) throws IOException {
        CtripHotelSpider ctripHotelSpider = new CtripHotelSpider();
        ctripHotelSpider.execute();
    }


    public void execute() throws IOException {

        /**
         * cityList
         */
//        List<Map<String, String>> cityList = spiderCityList();
//        this.saveCityList(cityList);

        /**
         * hotelList
         */
//        List<Map<String, String>> spiderCityList = getCityList();
//        spiderCityList.stream().forEach(item -> {
//            log.info("city >> " + item.get("name"));
//            List<Hotel> hotelList = spiderHotelList(item.get("cityId"));
//            this.saveHotelList(hotelList, item.get("cityId"), item.get("name"));
//            this.updateCity(item.get("cityId"));
//        });
//        log.info("city end ");

        /**
         * roomList
         */
        List<Map<String, String>> hotelList = getHotelList();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String checkInDate = simpleDateFormat.format(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 1);
        String checkoutDate = simpleDateFormat.format(calendar.getTime());
        hotelList.stream()
//                .limit(1)
                .forEach(item -> {
                    threadPool.submit(() -> {
                        log.info("\t\thotel >> " + item.get("name"));
                        spiderRoomList(item.get("cityId"), item.get("hotelId"), item.get("href"), checkInDate, checkoutDate);
                    });

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
    }

    public List<Map<String, String>> spiderCityList() {
        List<Map<String, String>> cityList = Lists.newArrayList();
        String url = host + "/Domestic/Tool/AjaxGetCitySuggestion.aspx";
        Connection connect = Jsoup.connect(url);
        try {
            Document document = connect.get();
            String body = document.getElementsByTag("body").html();
            String flag = "cQuery.jsonpResponse.suggestion=";
            body = body.substring(body.indexOf("cQuery.jsonpResponse.suggestion=") + flag.length()).replaceAll("&quot;", "\"");

            JSONObject jsonObject = JSON.parseObject(body);
            List<Item> itemsList = Lists.newArrayList();
            for (String key : jsonObject.keySet()) {
                if (!"热门".equals(key)) {
                    List<Item> items = JSON.parseArray(jsonObject.getString(key), Item.class);
                    itemsList.addAll(items);
                }
            }

            itemsList.stream()
                    .sorted(Comparator.comparing(Item::getGroup))
//                .limit(1)
                    .forEach(item -> {
                        String[] split = item.getData().split("\\|");
                        if (split.length == 3) {
                            String enName = split[0];
                            String name = split[1];
                            String cityId = split[2];

                            if (!cityIdSet.contains(cityId)) {
                                cityIdSet.add(cityId);
                                Map<String, String> cityMap = Maps.newHashMap();
                                cityMap.put("enName", enName);
                                cityMap.put("name", name);
                                cityMap.put("cityId", cityId);
                                cityMap.put("group", item.getGroup());
                                cityMap.put("url", host + "/hotel/" + enName.toLowerCase() + cityId);
                                cityList.add(cityMap);
                            }
                        }
                    });

        } catch (IOException e) {
            e.printStackTrace();
        }
        return cityList;
    }

    public List<Map<String, String>> getCityList() {
        List<Map<String, String>> cityList = Lists.newArrayList();
        java.sql.Connection connection = ConnectionUtil.getConnection(ConnectionUtil.LOCALHOST_VIC);
        String sql = "SELECT `CityId`,`Name`,`EnName`,`Url` FROM `City` a WHERE `Sync` = 0 AND EXISTS(SELECT 1 FROM `KK_City` b WHERE a.`name` = b.`Name`)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Map<String, String> cityMap = Maps.newHashMap();
                cityMap.put("cityId", resultSet.getString("CityId"));
                cityMap.put("name", resultSet.getString("Name"));
                cityList.add(cityMap);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cityList;
    }

    public List<Hotel> spiderHotelList(String cityId) {
        List<Hotel> hotelList = Lists.newArrayList();

        try {
            String url = "/Domestic/Tool/AjaxHotelList.aspx";
            int page = 1;
            int pageCount = 1;
            do {
                log.info("\tcurPage=" + page);
                Connection.Response response = Jsoup.connect(host + url).timeout(20000)
                        .data("cityId", cityId)
                        .data("page", String.valueOf(page))
                        .execute();

                try {
                    JSONObject jsonObject = JSON.parseObject(response.body());

                    Document paging = Jsoup.parse(jsonObject.getString("paging"));
                    String tmpPage = paging.getElementById("txtpage").attr("value");
                    String tmpPageCount = paging.getElementById("txtpage").attr("data-pagecount");
                    if (StringUtils.isNumeric(tmpPage) && StringUtils.isNumeric(tmpPageCount)) {
                        page = Integer.parseInt(tmpPage);
                        pageCount = Integer.parseInt(tmpPageCount);
                    }

                    // insert
                    hotelList.addAll(JSON.parseArray(jsonObject.getString("hotelPositionJSON"), Hotel.class));
                } catch (JSONException e) {
                    log.error("\texception page=" + page);
                }

            } while (++page < pageCount);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return hotelList;
    }

    public List<Map<String, String>> getHotelList() {
        List<Map<String, String>> hotelList = Lists.newArrayList();
        java.sql.Connection connection = ConnectionUtil.getConnection(ConnectionUtil.LOCALHOST_VIC);
        String sql = "SELECT `HotelId`,`Name`,`CityId`,`CityName`,`Href` FROM `Hotel` WHERE `Sync` = 0";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Map<String, String> hotelMap = Maps.newHashMap();
                hotelMap.put("hotelId", resultSet.getString("HotelId"));
                hotelMap.put("name", resultSet.getString("Name"));
                hotelMap.put("cityId", resultSet.getString("CityId"));
                hotelMap.put("cityName", resultSet.getString("CityName"));
                hotelMap.put("href", resultSet.getString("Href"));
                hotelList.add(hotelMap);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hotelList;
    }

    public void spiderRoomList(String cityId, String hotelId, String href, String checkInDate, String checkoutDate) {
        try {
            String url = String.format(host + "/Domestic/tool/AjaxHote1RoomListForDetai1.aspx?psid=&MasterHotelID=%s&hotel=%s&EDM=F&roomId=&IncludeRoom=&city=%s&showspothotel=T&supplier=&IsDecoupleSpotHotelAndGroup=F&contrast=0&brand=0&startDate=%s&depDate=%s&IsFlash=F&RequestTravelMoney=F&hsids=&IsJustConfirm=&contyped=0&priceInfo=-1&equip=&filter=&productcode=&couponList=&abForHuaZhu=&TmFromList=F&eleven=cc45e76fcf734a2b9d8cd9b1d9ec3fbde4cee411ebbd7b0a003bba441e48dd79&callback=CASgijFRxvhXztWHRTV&_=1496715069274", hotelId, hotelId, cityId, checkInDate, checkoutDate);

            Connection.Response response = Jsoup.connect(url).header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                    .header("Content-Type", "x-www-form-urlencoded; charset=utf-8")
                    .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                    .header("Referer", href).timeout(20000).execute();

            JSONObject jsonObject = JSON.parseObject(response.body());
            if(null == jsonObject){
                return;
            }

            String html = jsonObject.getString("html");
            if (StringUtils.isBlank(html)) {
                return;
            }
            Document parse = Jsoup.parse(html);
            Element j_roomListTbl = parse.getElementById("J_RoomListTbl");
            Elements tr = j_roomListTbl.select("tr");
            String roomTypeName = "";
            String roomInfo = "";


            List<Map<String, String>> roomTypeList = Lists.newArrayList();
            List<Map<String, String>> skuList = Lists.newArrayList();
            for (Element element : tr) {
                if (element.hasAttr("brid") && !element.hasClass("last_room")) {
                    String text = element.select("td.room_type a.room_unfold").text().replace("查看详情", "").trim();
                    if (StringUtils.isNotBlank(text)) {
                        roomTypeName = text;
                    }
                    String attr = element.select("td.child_name").attr("data-baseroominfo");
                    if (StringUtils.isNotBlank(attr)) {
                        roomInfo = attr;
                    }

                    String roomTypeId = element.attr("brid");

                    String skuId = element.select("td.child_name").attr("data-roomid");
                    String name = element.select("td.child_name span.room_type_name").text();
                    String agent = element.select("td.child_name span.label_onsale_blue").text();
                    String bedType = element.select("td.child_name").attr("data-bed");
                    String bedName = element.select("td.col3").text();
                    String breakfastType = element.select("td.child_name").attr("data-bf");
                    String breakfastName = element.select("td.col4").text();
                    String networkType = element.select("td.child_name").attr("data-network");
                    String networkName = element.select("td.col5>span").text();
                    String guestNum = element.select("td.col_person>span").attr("title");
                    String cancelType = element.select("td.child_name").attr("data-policy");
                    String cancelName = element.select("td.col_policy>span.room_policy").text();
                    String confirmType = element.select("td.child_name").attr("data-reserve");
                    String confirmName = element.select("td.col_policy>span:eq(1)").text();
                    String price = element.select("td.col7>div.book_type>a").attr("data-price");
                    String payType = element.select("td.child_name").attr("data-pay");
                    String payName = element.select("td.col7>div.book_type span.payment_txt").text();

                    Map<String, String> roomTypeMap = Maps.newHashMap();
                    if (StringUtils.isNotBlank(roomTypeId) && StringUtils.isNotBlank(roomTypeName) && StringUtils.isNotBlank(roomInfo)) {
                        if (!roomTypeIdSet.contains(roomTypeId)) {
                            roomTypeMap.put("roomTypeId", roomTypeId);
                            roomTypeMap.put("hotelId", hotelId);
                            roomTypeMap.put("roomTypeName", roomTypeName);
                            roomTypeMap.put("roomInfo", roomInfo);
                            roomTypeList.add(roomTypeMap);
                            roomTypeIdSet.add(roomTypeId);
                        }
                    }

                    if (!skuIdSet.contains(skuId)) {
                        Map<String, String> skuMap = Maps.newHashMap();
                        skuMap.put("skuId", skuId);
                        skuMap.put("roomTypeId", roomTypeId);
                        skuMap.put("name", name);
                        skuMap.put("agent", agent);
                        skuMap.put("bedType", bedType);
                        skuMap.put("bedName", bedName);
                        skuMap.put("breakfastType", breakfastType);
                        skuMap.put("breakfastName", breakfastName);
                        skuMap.put("networkType", networkType);
                        skuMap.put("networkName", networkName);
                        skuMap.put("guestNum", guestNum);
                        skuMap.put("cancelType", cancelType);
                        skuMap.put("cancelName", cancelName);
                        skuMap.put("confirmType", confirmType);
                        skuMap.put("confirmName", confirmName);
                        skuMap.put("price", price);
                        skuMap.put("payType", payType);
                        skuMap.put("payName", payName);
                        skuMap.put("checkInDate", checkInDate);
                        skuMap.put("checkoutDate", checkoutDate);
                        skuList.add(skuMap);
                        skuIdSet.add(skuId);
                    }
                }
            }
            // insert
            this.saveRoomType(roomTypeList);
            this.saveSkuList(skuList);
            this.updateHotel(hotelId);
        } catch (Exception e) {
            log.error("exception hotelId=" + hotelId, e);
        }

    }

    public void saveCityList(List<Map<String, String>> cityList) {
        if (!CollectionUtils.isEmpty(cityList)) {
            java.sql.Connection connection = ConnectionUtil.getConnection(ConnectionUtil.LOCALHOST_VIC);
            String sql = "INSERT INTO `City` (`CityId`, `name`, `EnName`, `Group`, `Url`, `Sync`) VALUES (?, ?, ?, ?, ?, ?)";
            cityList.stream().forEach(map -> {
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, map.get("cityId"));
                    preparedStatement.setString(2, map.get("name"));
                    preparedStatement.setString(3, map.get("enName"));
                    preparedStatement.setString(4, map.get("group"));
                    preparedStatement.setString(5, map.get("url"));
                    preparedStatement.setInt(6, 0);
                    preparedStatement.execute();
                } catch (SQLException e) {
                    log.error("exception cityId " + map.get("cityId"), e);
                }
            });
        }
    }

    public void updateCity(String cityId) {
        java.sql.Connection connection = ConnectionUtil.getConnection(ConnectionUtil.LOCALHOST_VIC);
        String sql = "UPDATE `City` SET `Sync` = 1 WHERE `CityId` = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, cityId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveHotelList(List<Hotel> hotelList, String cityId, String cityName) {
        if (!CollectionUtils.isEmpty(hotelList)) {
            java.sql.Connection connection = ConnectionUtil.getConnection(ConnectionUtil.LOCALHOST_VIC);
            String sql = "INSERT INTO `Hotel` (`HotelId`, `Name`, `CityId`, `CityName`, `Href`, `Address`, `Star`, `StarDesc`, `Lat`, `Lon`, `ShortName`, `Score`, `DpScore`, `DpCount`,`Sync`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            hotelList.stream().forEach(hotel -> {
                try {
                    if (!hotelIdSet.contains(hotel.getId())) {
                        hotelIdSet.add(hotel.getId());
                        PreparedStatement preparedStatement = connection.prepareStatement(sql);
                        preparedStatement.setString(1, hotel.getId());
                        preparedStatement.setString(2, hotel.getName());
                        preparedStatement.setString(3, cityId);
                        preparedStatement.setString(4, cityName);
                        preparedStatement.setString(5, host + hotel.getUrl());
                        preparedStatement.setString(6, hotel.getAddress());
                        preparedStatement.setString(7, hotel.star);
                        preparedStatement.setString(8, hotel.getStardesc());
                        preparedStatement.setString(9, hotel.getLat());
                        preparedStatement.setString(10, hotel.getLon());
                        preparedStatement.setString(11, hotel.getShortName());
                        preparedStatement.setString(12, hotel.getScore());
                        preparedStatement.setString(13, hotel.getDpscore());
                        preparedStatement.setString(14, hotel.getDpcount());
                        preparedStatement.setInt(15, 0);
                        preparedStatement.execute();
                    }
                } catch (SQLException e) {
                    log.error("exception hotelId " + hotel.getId(), e);
                }
            });
        }
    }

    public void updateHotel(String hotelId) {
        java.sql.Connection connection = ConnectionUtil.getConnection(ConnectionUtil.LOCALHOST_VIC);
        String sql = "UPDATE `Hotel` SET `Sync` = 1 WHERE `HotelId` = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, hotelId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveRoomType(List<Map<String, String>> roomTypeList) {
        if (!CollectionUtils.isEmpty(roomTypeList)) {
            java.sql.Connection connection = ConnectionUtil.getConnection(ConnectionUtil.LOCALHOST_VIC);
            String sql = "INSERT INTO `RoomType` (`RoomTypeId`,`HotelId`, `Name`, `RoomInfo`) VALUES (?, ?, ?, ?) ";
            roomTypeList.stream().forEach(map -> {
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, map.get("roomTypeId"));
                    preparedStatement.setString(2, map.get("hotelId"));
                    preparedStatement.setString(3, map.get("roomTypeName"));
                    preparedStatement.setString(4, map.get("roomInfo"));
                    preparedStatement.execute();
                } catch (SQLException e) {
                    log.error("exception roomTypeId >>" + map.get("roomTypeId"), e);
                }
            });
        }
    }

    public void saveSkuList(List<Map<String, String>> skuList) {
        if (!CollectionUtils.isEmpty(skuList)) {
            java.sql.Connection connection = ConnectionUtil.getConnection(ConnectionUtil.LOCALHOST_VIC);
            String sql = "INSERT INTO `RoomSku` (`SkuId`, `RoomTypeId`, `Name`, `Agent`, `BedType`, `BedName`, `BreakfastType`, `BreakfastName`, `NetworkType`, `NetworkName`, `GuestNum`, `CancelType`, `CancelName`, `ConfirmType`, `ConfirmName`,`Price`, `PayType`,`PayName`,`CheckInDate`,`CheckoutDate`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ?, ?) ";

            skuList.stream().forEach(map -> {
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, map.get("skuId"));
                    preparedStatement.setString(2, map.get("roomTypeId"));
                    preparedStatement.setString(3, map.get("name"));
                    preparedStatement.setString(4, map.get("agent"));
                    preparedStatement.setString(5, map.get("bedType"));
                    preparedStatement.setString(6, map.get("bedName"));
                    preparedStatement.setString(7, map.get("breakfastType"));
                    preparedStatement.setString(8, map.get("breakfastName"));
                    preparedStatement.setString(9, map.get("networkType"));
                    preparedStatement.setString(10, map.get("networkName"));
                    preparedStatement.setString(11, map.get("guestNum"));
                    preparedStatement.setString(12, map.get("cancelType"));
                    preparedStatement.setString(13, map.get("cancelName"));
                    preparedStatement.setString(14, map.get("confirmType"));
                    preparedStatement.setString(15, map.get("confirmName"));
                    preparedStatement.setString(16, map.get("price"));
                    preparedStatement.setString(17, map.get("payType"));
                    preparedStatement.setString(18, map.get("payName"));
                    preparedStatement.setString(19, map.get("checkInDate"));
                    preparedStatement.setString(20, map.get("checkoutDate"));
                    // insert
                    preparedStatement.execute();
                } catch (SQLException e) {
                    log.error("exception skuId >>" + map.get("skuId"), e);
                }
            });
        }
    }

    @Data
    public static class Hotel {
        private String id;
        private String name;
        private String lat;
        private String lon;
        private String url;
        private String img;
        private String address;
        private String score;
        private String dpscore;
        private String dpcount;
        private String star;
        private String stardesc;
        private String shortName;
        private String isSingleRec;
    }

    @Data
    public static class Item {
        private String display;
        private String data;
        private String group;
        private String code;
    }
}
