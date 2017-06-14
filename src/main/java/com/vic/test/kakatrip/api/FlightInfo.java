package com.vic.test.kakatrip.api;

import com.vic.test.util.DigestUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by vic
 * Create time : 2017/4/6 下午4:44
 */
public class FlightInfo {


    public static void main(String[] args) throws IOException {
        String url = "https://api.xiaoheer.com/ws/insurance/airdelay/check.asmx?op=checkbyFlightNO";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        // header
        httpPost.setHeader("Content-type", "text/xml; charset=utf-8");

        // params
        String key = "WRs0Rc";
        String hashCode = "c91213bec5ef40adb60467f000e18d75";
        String flightNo = "CZ6754";
        String flightDate = "2017-01-17 12:20";
        String notifyURL = "";
        String outStyle = "0";
        String curDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String sign = generateSign(hashCode, flightNo, flightDate, notifyURL, outStyle, key, curDate);

        System.err.println("sign====" + sign);
        String params = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Body>\n" +
                "    <checkbyFlightNO xmlns=\"http://xiaoher.jzdata.com/\">\n" +
                "      <hashCode>" + hashCode + "</hashCode>\n" +
                "      <FlightNo>" + flightNo + "</FlightNo>\n" +
                "      <flightdate>" + flightDate + "</flightdate>\n" +
                "      <NotifyURL>" + notifyURL + "</NotifyURL>\n" +
                "      <outstyle>" + outStyle + "</outstyle>\n" +
                "      <sign>" + sign + "</sign>\n" +
                "    </checkbyFlightNO>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";
        httpPost.setEntity(new StringEntity(params, "utf-8"));

        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity httpEntity = httpResponse.getEntity();
        String result = EntityUtils.toString(httpEntity);//取出应答字符串
        System.out.println(result);
    }

    public static String generateSign(String... params) {
        String str = Arrays.stream(params).reduce(String::concat).orElse("");
        return DigestUtil.digest(str, DigestUtil.MD5);
    }
}
