package com.vic.test.tuanrourou.trade.dto;

import lombok.Data;

import java.util.List;

/**
 * Created by vic
 * Create time : 2017/8/4 下午3:16
 */
@Data
public class SellOrderDTO {
    private int sellId;
    private int userId;
    private int categoryId;
    private String productName;
    private String productDesc;
    private String brand;
    private int specification;
    private int price;
    private List<Integer> tags;
    private List<String> images;
    private int cityId;
    private String cityName;
    private String stockAddress;
    private String contactName;
    private String contactPhone;
    private int deleteStatus;

    private String accountName;
}
