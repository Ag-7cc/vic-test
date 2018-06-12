package com.vic.test.simulate;

import com.alibaba.fastjson.JSON;
import com.vic.test.util.DigestUtil;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang.ArrayUtils;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by vic
 * Create time : 2018/3/24 13:54
 */
@Log4j
public class PokerLoginSimulate {

    private String userName = "15821244303";
    private String password = "atw669";

    public static void main(String[] args) {
        PokerLoginSimulate pokerLoginSimulate = new PokerLoginSimulate();
//        pokerLoginSimulate.login();
//        pokerLoginSimulate.account();
        pokerLoginSimulate.validRoomList();
    }

    public void login() {
        try {
            String url = "https://api.poker.top/login?plat=1&v=5.7&uuid=96F08D50-AB55-452B-8D25-7A149FA952B3&model=phone&sv=OSVersion(version:%20%2210.2%22)";

            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            // header
            httpPost.setHeader("Content-type", "application/x-www-form-urlencoded; charset=utf-8");
            httpPost.setHeader("User-Agent", "Poker/5.7 (com.texas.poker.top; build:2; iOS 10.2.0) Alamofire/4.6.0");

            // params
            List<NameValuePair> params = new ArrayList();
            params.add(new BasicNameValuePair("phone", userName));
            params.add(new BasicNameValuePair("password", DigestUtil.digest(password, DigestUtil.MD5)));
            httpPost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));

            HttpResponse response = httpClient.execute(httpPost);

            Header[] headers = response.getHeaders("set-cookie");
            if(Objects.nonNull(headers) && headers.length > 0){
                headers[0].getValue().split(";");
            }
            Header cookies = response.getLastHeader("set-cookie");
            if(Objects.nonNull(cookies) && ArrayUtils.isNotEmpty(cookies.getElements())){
                for (HeaderElement element : cookies.getElements()) {
                    log.info(element.getName() + "=" + element.getValue());
                }
            }
            log.info(">>>>>>>>>>>>>>>>>");
            String result = EntityUtils.toString(response.getEntity(), "utf-8");
            log.info(result);
            log.info(">>>>>>>>>>>>>>>>>");
            log.info(JSON.toJSONString(response));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void account(){
        String cookie =  "connect.sid=s%3AbCvmHFmz86DRvCSfnYmgQikO4i_XzpSo.%2FnxBjxjMwNBUsSypUQ2anZ6cUPpK8vlu0iUOeUhW8Q8";

        try {


            String url = "https://api.poker.top/account/5ab5d514c225f21bcf60b53e?plat=1&v=5.7&uuid=76F08D50-AB55-452B-8D25-7A149FA952B3&model=phone&sv=OSVersion%28version%3A%20%2210.2%22%29&detail=1";

            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            // header
            httpGet.setHeader("Content-type", "application/x-www-form-urlencoded; charset=utf-8");
            httpGet.setHeader("User-Agent", "Poker/5.7 (com.texas.poker.top; build:2; iOS 10.2.0) Alamofire/4.6.0");
            httpGet.setHeader("Cookie", cookie);
            // params

            HttpResponse response = httpClient.execute(httpGet);

            String result = EntityUtils.toString(response.getEntity(), "utf-8");
            log.info(result);
            log.info(">>>>>>>>>>>>>>>>>");
            log.info(JSON.toJSONString(response));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void validRoomList(){

        String cookie =  "connect.sid=s%3AbCvmHFmz86DRvCSfnYmgQikO4i_XzpSo.%2FnxBjxjMwNBUsSypUQ2anZ6cUPpK8vlu0iUOeUhW8Q8";

        try {
            String url = "https://api.poker.top/valid-room-list-v2?plat=1&v=5.7&uuid=76F08D50-AB55-452B-8D25-7A149FA952B3&model=phone&sv=OSVersion%28version%3A%20%2210.2%22%29&skip=0&type=2";

            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            // header
            httpGet.setHeader("Content-type", "application/x-www-form-urlencoded; charset=utf-8");
            httpGet.setHeader("User-Agent", "Poker/5.7 (com.texas.poker.top; build:2; iOS 10.2.0) Alamofire/4.6.0");
            httpGet.setHeader("Cookie", cookie);
            // params

            HttpResponse response = httpClient.execute(httpGet);

            String result = EntityUtils.toString(response.getEntity(), "utf-8");
            log.info(result);
            log.info(">>>>>>>>>>>>>>>>>");
            log.info(JSON.toJSONString(response));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
