package com.vic.test.tuanrourou.trade.dto;

import lombok.Data;

import java.util.List;

/**
 * Created by vic
 * Create time : 2017/8/4 上午11:04
 */
@Data
public class BuyOrderDTO {
    private int buyId;
    private int userId;
    private int categoryId;
    private String productName;
    private String productDesc;
    private int weight;
    private int cityId;
    private List<Integer> tags;
    private String cityName;
    private String receiverAddress;
    private String contactName;
    private String contactPhone;
    private int deleteStatus;

    private String accountName;
}
