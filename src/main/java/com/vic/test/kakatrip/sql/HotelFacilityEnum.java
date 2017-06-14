package com.vic.test.kakatrip.sql;

/**
 * 酒店设施枚举
 * Created by vic
 * Create time : 2017/5/4 下午12:17
 */
public enum HotelFacilityEnum {

    CarPark(1, "停车场", "/img/icon/icon_hotel_park.png"),
    FreeCarPark(2, "免费停车", "/img/icon/icon_hotel_freepark.png"),
    Transit(3, "机场班车", "/img/icon/icon_hotel_transit.png"),
    Swim(4, "泳池", "/img/icon/icon_hotel_swim.png"),
    Gym(5, "健身", "/img/icon/icon_hotel_gym.png"),
    Tennis(6, "网球场", "/img/icon/icon_hotel_tennis.png"),
    Sauna(7, "桑拿", "/img/icon/icon_hotel_sauna.png"),
    Spa(8, "Spa", "/img/icon/icon_hotel_spa.png"),
    KTV(9, "卡拉OK", "/img/icon/icon_hotel_ktv.png"),
    Wifi(10, "Wi-Fi", "/img/icon/icon_hotel_wifi.png"),
    Business(11, "商务中心", "/img/icon/icon_hotel_business.png"),
    Transfer(12, "接机", "/img/icon/icon_hotel_transfer.png"),
    HotelBar(13, "行政酒廊", "/img/icon/icon_hotel_bar.png"),
    NoPet(14, "禁止宠物", "/img/icon/icon_hotel_nopet.png"),
    Pet(15, "可带宠物", "/img/icon/icon_hotel_pet.png"),
    NoSmoke(16, "无烟客房", "/img/icon/icon_hotel_nosmoke.png"),
    Family(17, "家庭间", "/img/icon/icon_hotel_family.png"),
    Restaurant(18, "餐厅", "/img/icon/icon_hotel_restaurant.png"),
    ChinaFood(19, "中餐厅", "/img/icon/icon_hotel_chinafood.png"),
    WestFood(20, "西餐厅", "/img/icon/icon_hotel_westfood.png"),
    JapanFood(21, "日式餐厅", "/img/icon/icon_hotel_japanfood.png"),
    Cafeteria(22,"自助餐厅","/img/icon/icon_hotel_cafeteria.png"),
    RoomService(23, "送餐服务", "/img/icon/icon_hotel_roomservice.png"),
    LobbyBar(24, "大堂吧", "/img/icon/icon_hotel_lobbybar.png"),
    CoffeeShop(25, "咖啡厅", "/img/icon/icon_hotel_coffeeshop.png"),
    Pub(26, "酒吧", "/img/icon/icon_hotel_pub.png"),
    Taxi(27, "叫车服务", "/img/icon/icon_hotel_taxi.png"),
    Wedding(28, "婚宴服务", "/img/icon/icon_hotel_wedding.png"),
    Wakeup(29, "叫醒服务", "/img/icon/icon_hotel_wakeup.png"),
    Laundry(30, "洗衣服务", "/img/icon/icon_hotel_laundry.png"),
    Luggage(31, "行李寄存", "/img/icon/icon_hotel_luggage.png"),
    ATM(32, "ATM", "/img/icon/icon_hotel_atm.png"),
    BarrierFree(33, "无障碍通道", "/img/icon/icon_hotel_barrierfree.png");

    public int facilityId;
    public String text;
    public String logo;

    HotelFacilityEnum(int facilityId, String text, String logo) {
        this.facilityId = facilityId;
        this.text = text;
        this.logo = logo;
    }

    public static HotelFacilityEnum getEnumByFacilityId(int facilityId) {
        if (facilityId <= 0) {
            return null;
        }
        for (HotelFacilityEnum anEnum : HotelFacilityEnum.values()) {
            if (facilityId == anEnum.facilityId) {
                return anEnum;
            }
        }
        return null;
    }
}
