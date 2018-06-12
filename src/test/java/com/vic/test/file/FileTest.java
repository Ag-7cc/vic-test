package com.vic.test.file;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

/**
 * Created by vic
 * Create time : 2018/4/1 17:52
 */
public class FileTest extends TestCase {


    public void test1() throws IOException {
        File file = new File("/Users/vic/returnList2.json");

        FileSystemResource resource = new FileSystemResource(file);
        InputStream inputStream = resource.getInputStream();


        byte[] bContent = new byte[inputStream.available()];

        inputStream.read(bContent);
        inputStream.close();

        String sContent = new String(bContent);


//        System.out.println(sContent);

        JSONObject jsonObject = JSON.parseObject(sContent);
        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("list");


        String sql = new String("UPDATE `SHOP_ReturnDetail` SET `RefundPrice` = %s WHERE `ReturnID` = %s;");
        jsonArray.stream().flatMap(o -> ((JSONObject)o).getJSONArray("detailList").stream()).forEach(o -> {
            JSONObject returnDetail = (JSONObject) o;
            Integer returnId = returnDetail.getInteger("returnId");
            String refund = returnDetail.getString("printRefund");
            int refundPrice = new BigDecimal(Double.parseDouble(refund)).multiply(new BigDecimal(100)).intValue();
            System.out.println(String.format(sql, refundPrice, returnId));
        });

    }
}
