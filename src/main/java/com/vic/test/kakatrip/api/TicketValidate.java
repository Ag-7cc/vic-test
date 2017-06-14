package com.vic.test.kakatrip.api;

import com.vic.test.util.DigestUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by vic
 * Create time : 2017/4/6 下午4:44
 */
public class TicketValidate {


    public static void main(String[] args) throws IOException, DocumentException {

        String url = "https://api.xiaoheer.com/ws/id5/contrastTicket.asmx?op=Contrast";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        // header
        httpPost.setHeader("Content-type", "text/xml; charset=utf-8");

        // params
        String ticketNo = "1761196199134";//必传
        String passName = "任吉";//不必传，名字不匹配或不传时，返回信息中带乘机人信息
        String flightDate = "2017-02-11 23:00";//必传
        String flightNo = "EK0303";//必传
        String fare = "0";//必传

        String hashCode = "c91213bec5ef40adb60467f000e18d75";//必传
        String key = "WRs0Rc";//必传
        String curDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String sign = generateSign(fare, flightDate, flightNo, hashCode, passName, ticketNo, key, curDate);
        System.err.println("sign:" + sign);

        // 1.1版本
        String params = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                "  <soap:Body>\n" +
                "    <Contrast xmlns=\"https://api.xiaoheer.com/\">\n" +
                "      <Hashcode>" + hashCode + "</Hashcode>\n" +
                "      <ticketNo>" + ticketNo + "</ticketNo>\n" +
                "      <passName>" + passName + "</passName>\n" +
                "      <flightdate>" + flightDate + "</flightdate>\n" +
                "      <FlightNo>" + flightNo + "</FlightNo>\n" +
                "      <fare>" + fare + "</fare>\n" +
                "      <sign>" + sign + "</sign>\n" +
                "    </Contrast>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";

        // 1.2版本
        String params2 = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                "  <soap12:Body>\n" +
                "    <Contrast xmlns=\"https://api.xiaoheer.com/\">\n" +
                "      <Hashcode>" + hashCode + "</Hashcode>\n" +
                "      <ticketNo>" + ticketNo + "</ticketNo>\n" +
                "      <passName>" + passName + "</passName>\n" +
                "      <flightdate>" + flightDate + "</flightdate>\n" +
                "      <FlightNo>" + flightNo + "</FlightNo>\n" +
                "      <fare>" + fare + "</fare>\n" +
                "      <sign>" + sign + "</sign>\n" +
                "    </Contrast>\n" +
                "  </soap12:Body>\n" +
                "</soap12:Envelope>";

        httpPost.setEntity(new StringEntity(params2, "utf-8"));

        StopWatch stopWatch = new StopWatch("");
        stopWatch.start("request");
        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
        stopWatch.stop();
        HttpEntity httpEntity = httpResponse.getEntity();
        System.out.println(EntityUtils.toString(httpEntity));

        System.err.println(stopWatch.prettyPrint());

    }

    public static String generateSign(String... params) {
        return DigestUtil.digest(Arrays.stream(params).reduce(String::concat).orElse(""), DigestUtil.MD5);
    }

    public static String concatStr(String... str) {
        return Arrays.stream(str).reduce("", (a, b) -> a.concat(b + " "));
    }

    public static void parseResult(HttpEntity httpEntity) throws IOException, DocumentException {
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(httpEntity.getContent());

        Element result;

        try {
            result = document.getRootElement().element("Body").element("ContrastResponse").element("ContrastResult");
        } catch (NullPointerException e) {
            System.out.println("未找到正确的数据");
            return;
        }

        String repErrCode = null, repErrContent = null;
        String repName = null, repFlightDate = null, repFlightNo = null, repFare = null, repStatus = null, repRealFare = null;
        String source = null;
        for (Iterator<Element> iterator = result.elementIterator(); iterator.hasNext(); ) {
            Element element = iterator.next();
            switch (element.getName()) {
                case "ErrorRes":
                    repErrCode = element.elementText("Err_code");
                    repErrContent = element.elementText("Err_content");
                    break;
                case "ReportList":
                    repName = element.elementText("Name");
                    repFlightDate = element.elementText("FlightDate");
                    repFlightNo = element.elementText("FlightNo");
                    repFare = element.elementText("Fare");
                    repStatus = element.elementText("status");
                    repRealFare = element.elementText("RealFare");
                    break;
                case "source":
                    source = element.getText();
                    break;
            }
        }
        System.err.println("repErrCode=" + repErrCode + ",repErrContent=" + repErrContent);
        System.err.println(concatStr(repName, repFlightDate, repFlightNo, repFare, repStatus, repRealFare));
        System.err.println(source);
    }
}
