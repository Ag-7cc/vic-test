package com.vic.test.cron;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by vic
 * Create time : 2017/5/7 下午3:36
 */
@Component
@EnableScheduling
@Slf4j
public class Rush {
    @Scheduled(fixedRate = 1000)
    public void rushDB() {
        submit();
    }

    public static void main(String[] args) {
        Rush rush = new Rush();
        rush.submit();
    }

    public void submit() {
        try {
            String url = "http://www.gegegancn.com/pay/znsub.php";
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            // header
            httpPost.setHeader("Content-type", "application/x-www-form-urlencoded; charset=utf-8");

            // params
            String cardNo = "80133" + getRandomNum(11);
            String email = getRandomNum(9) + "@qq.com";
            String password = getRandomNum(8);
            String vipLevel = getVipLevel();

            List<NameValuePair> params = new ArrayList();
            params.add(new BasicNameValuePair("zn_biaoti", vipLevel));
            params.add(new BasicNameValuePair("zn_name", cardNo));
            params.add(new BasicNameValuePair("zn_neirong", password));
            params.add(new BasicNameValuePair("zn_youxiang", email));
            httpPost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));

            HttpResponse response = httpClient.execute(httpPost);

            String result = EntityUtils.toString(response.getEntity(), "utf-8");

            String log = result + "cardNo=" + cardNo + " password=" + password + " email=" + email + " vipLevel=" + vipLevel;
            System.out.println(log);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getRandomNum(int length) {
        Random random = new Random();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int a = random.nextInt(10);
            stringBuffer.append(String.valueOf(a));
        }
        return stringBuffer.toString();
    }

    public static String getVipLevel() {
        String[] levelArr = new String[]{"铜牌VIP", "银牌VIP", "金牌VIP", "钻石VIP"};
        return levelArr[new Random().nextInt(4)];
    }
}
