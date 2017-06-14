package com.vic.test.spider;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 航司联盟
 * 爬取航司星级等数据
 * Created by vic
 * Create time : 2017/2/21 上午11:42
 */
public class AllianceStartSpider {

    private String webSite = "http://www.airlinequality.com";
    String baseFolder = "/Users/vic/Documents/work/卡旅/airlineStarImages/";

    @Data
    static class AirlineInfo {
        private String url;
        private String enName;
        private String logo;
        private String ratingLogSmall;
        private String ratingLogoBig;
        private String ratingLogoLevel;
        private List<Map<String, Integer>> ratingsList;
    }

    public static void main(String[] args) {
        AllianceStartSpider spider = new AllianceStartSpider();
        spider.execute();
//        parseJsonDataToCSV();
    }


    public LinkedList<AirlineInfo> execute() {
        Map<String, String> cookies = getCookies();

        StopWatch stopWatch = new StopWatch("my stopWatch");

        stopWatch.start("initializing");

        // cookie中的参数可能会失效，失效的时候不能正常爬取数据，要重新更新下参数
        final String catAccCookies = "1";
        final String PHPSESSID = "ksru6cbcaei3pacarhkni6md52";
        final String _ga = "GA1.2.618954575.1487145149";
        final String _gat = "1";
        final String visid_incap_965359 = "ebdkHhixTLCoeHx5dlXLALcIpFgAAAAAQUIPAAAAAAA2nXYvyY+93Ig9InLV+t+x";
        // 以下为易变参数
        final String incap_ses_407_965359 = "sxjIUUUtikLegXVlb/WlBRxXrlgAAAAAbEWhknVWR7hm9QSOHG64kA==";
        final String incap_ses_541_965359 = "qdZtY5ZeuXP3qNyDzgSCBwtErFgAAAAAN/q7Ei0OphhhCsaQFj/SrQ==";
        final String incap_ses_571_965359 = "YbkuBxWskUPK744fmpnsB7ZbrFgAAAAAcZAsgOdre9HVCA7YhDaGwg==";

        String pagesUrl = "/review-pages/a-z-airline-reviews/";

        Connection connection = Jsoup.connect(webSite + pagesUrl)
                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537." + RandomUtils.nextInt(1000) + " (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
                .timeout(60000)
                .cookie("catAccCookies", catAccCookies)
                .cookie("PHPSESSID", PHPSESSID)
                .cookie("_ga", _ga)
                .cookie("_gat", _gat)
                .cookie("visid_incap_965359", visid_incap_965359)
                .cookie("incap_ses_407_965359", incap_ses_407_965359)
                .cookie("incap_ses_541_965359", incap_ses_541_965359)
                .cookie("incap_ses_571_965359", incap_ses_571_965359);

        for (String key : cookies.keySet()) {
            // cookies 获取不准确。。。
//            connection.cookie(key, cookies.get(key));
            System.err.println(key + "==" + cookies.get(key));
        }

        LinkedList<AirlineInfo> airlineInfoList = Lists.newLinkedList();
        LinkedList<String> errLink = Lists.newLinkedList();
        stopWatch.stop();
        try {

            Document doc = connection.get();
            stopWatch.start("processing-list");
            //log.info("～～～～～～～～～～～抓取开始～～～～～～～～～～～～");

            // 获取所有航司链接集合l
            LinkedList<String> list = this.parseToAirlineList(doc);
            stopWatch.stop();

            stopWatch.start("processing-detail");
            if (CollectionUtils.isEmpty(list)) {
                //log.error(doc);
            }
            // 测试时使用，避免数据过大影响测试效率
            int size = list.size() > 1 ? 1 : list.size();
            // 遍历所有链接，并将请求结果封装到结果集中
            AtomicInteger index = new AtomicInteger(0);
            list.stream().limit(size).forEach(href -> {
                //log.info(String.format("共%s条,第%s条 URL:%s", size, String.valueOf(index.incrementAndGet()), webSite + href));
                try {
                    Document detailDoc = connection.url(webSite + href).get();
                    airlineInfoList.addAll(parseToAirlineInfoList(detailDoc, webSite + href));
                } catch (Exception e) {
                    errLink.add(href);
                    //log.error("异常链接：" + href, e);
                }
            });
            stopWatch.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        stopWatch.start("processing-save");
        save(airlineInfoList, errLink);
        stopWatch.stop();
        //log.info(String.format("总计:%s 失败:%s", airlineInfoList.size(), errLink.size()));
        if (!CollectionUtils.isEmpty(errLink)) {
            //log.warn("~~~ errLink ~~~~~");
            //log.warn(JSONObject.toJSON(errLink));
            //log.warn("~~~~ errLink ~~~~");
        }
        //log.info("～～～～～～～～～～～抓取结束～～～～～～～～～～～～");
        //log.info(stopWatch.prettyPrint());
        return airlineInfoList;
    }

    /**
     * 获取所有的航司链接集合
     *
     * @param document
     * @return
     */
    public LinkedList<String> parseToAirlineList(Document document) {
        LinkedList<String> hrefList = Lists.newLinkedList();
        Elements elements = document.select("div.tabs-content div.a_z_col_group > ul.items > li a");
        elements.stream().forEach(e -> {
            String href = e.attr("href");
            if (StringUtils.isNotEmpty(href)) {
                hrefList.add(e.attr("href"));
            }
        });
        return hrefList;
    }

    /**
     * 解析详情页面的数据，封装对象
     *
     * @param document
     * @param url
     * @return
     */
    public LinkedList<AirlineInfo> parseToAirlineInfoList(Document document, String url) {
        LinkedList<AirlineInfo> airlineInfoList = Lists.newLinkedList();
        try {
            // 最外层DIV
            Elements elements = document.select("div.review-info");
            elements.forEach(e -> {
                // head
                String logo = e.select("div.review-heading > div.info > div.logo img").attr("src").trim();
                String enName = e.select("div.review-heading > div.info > h1").text().trim();
                String[] ratingLogoArr = e.select("div.review-heading > div.rating > img").attr("srcset").split(",");
                String ratingLogLevel = e.select("div.review-heading > div.rating > img").attr("alt").split(" ")[0].trim();

                // customer-rating
                List<Map<String, Integer>> ratingsList = Lists.newArrayList();
                Elements tdELements = e.select("div.customer-rating table.review-ratings tr");
                tdELements.stream().forEach(tr -> {
                    Map<String, Integer> map = Maps.newHashMap();
                    String key = tr.select("td.review-rating-header").text().trim();
                    Integer value = tr.select("td.review-rating-stars span.fill").size();
                    map.put(key, value);
                    ratingsList.add(map);
                });

                // 封装对象
                AirlineInfo airlineInfo = new AirlineInfo();
                airlineInfo.setUrl(url);
                airlineInfo.setEnName(enName);
                airlineInfo.setLogo(logo);
                airlineInfo.setRatingLogSmall(ratingLogoArr[0].trim().split(" ")[0].trim());
                airlineInfo.setRatingLogoBig(ratingLogoArr[1].trim().split(" ")[0].trim());
                airlineInfo.setRatingLogoLevel(ratingLogLevel);
                airlineInfo.setRatingsList(ratingsList);
                airlineInfoList.add(airlineInfo);
            });
        } catch (Exception e) {
            //log.error(e);
        }
        return airlineInfoList;
    }

    /**
     * 保存爬取的数据：图片保存本地，数据以json的形式保存至文件
     *
     * @param airlineInfoList
     */
    public void save(LinkedList<AirlineInfo> airlineInfoList, LinkedList<String> errLink) {
        airlineInfoList.stream().forEach(item -> {
            // logo
            writeImg(item.getLogo(), baseFolder + item.getEnName() + "_logo" + getSuffix(item.getLogo()));
            // smallRatingLogo
            writeImg(item.getRatingLogSmall(), baseFolder + item.getEnName() + "_s" + getSuffix(item.getRatingLogSmall()));
            // bigRatingLogo
            writeImg(item.getRatingLogoBig(), baseFolder + item.getEnName() + "_b" + getSuffix(item.getRatingLogoBig()));
        });
        if (!CollectionUtils.isEmpty(airlineInfoList)) {
            writeData(airlineInfoList, baseFolder + "data.json");
        }
        if (!CollectionUtils.isEmpty(errLink)) {
            writeData(errLink, baseFolder + "err.json");
        }
    }


    /**
     * 根据url获取文件后缀名，默认.jpg
     *
     * @param url
     * @return
     */
    public String getSuffix(String url) {
        String suffix = ".jpg";
        if (url.contains(".")) {
            suffix = url.substring(url.lastIndexOf("."));
        }
        return suffix;
    }

    /**
     * 写入图片到本地
     *
     * @param imgURL
     * @param file
     */
    public void writeImg(String imgURL, String file) {
        InputStream in = null;
        OutputStream out = null;
        try {
            if (StringUtils.isEmpty(imgURL) || !imgURL.startsWith("http:")) {
                //log.warn("异常数据file:" + file + " imgURL:" + imgURL);
                return;
            }

            URL url = new URL(imgURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(60000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("If-Modified-Since", "0");
            conn.setRequestProperty("Accept-Language",
                    "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
            conn.setRequestProperty("Accept",
                    "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            conn.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)");

            conn.setReadTimeout(60 * 1000);//因为不知道要下载的图片的大小，所以先设置为60秒

            int nResponseCode = conn.getResponseCode();
            if (nResponseCode == HttpURLConnection.HTTP_OK) {

                in = conn.getInputStream();
                out = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
                //log.info("图片保存成功：" + file);
            }
        } catch (IOException e) {
            //log.error("fileName:" + file, e);
        } finally {
            try {
                if (null != in) {
                    in.close();
                }
                if (null != out) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 写入json数据到本地
     *
     * @param object
     * @param file
     */
    public void writeData(Object object, String file) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(JSONObject.toJSON(object).toString());
            writer.flush();
            writer.close();
            //log.info("数据保存成功：" + file);
        } catch (IOException e) {
            //log.error("fileName:" + file, e);
        }
    }

    public Map<String, String> getCookies() {
        Connection.Response response = null;
        try {
            response = Jsoup.connect("http://www.airlinequality.com/review-pages/a-z-airline-reviews/").execute();
        } catch (IOException e) {
            //log.error(e);
        }
        Map<String, String> cookies = response.cookies();
        return cookies;
    }

    public static void parseJsonDataToCSV() {
        String sourceFile = "/Users/vic/Documents/work/卡旅/airlineStarImages/data.json";
        String targetFile = "/Users/vic/Documents/work/卡旅/airlineStarImages/data.csv";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(sourceFile))));
            FileOutputStream outputStream = new FileOutputStream(new File(targetFile), false);

            StringBuffer data = new StringBuffer();
            String tmp;
            while (null != (tmp = reader.readLine())) {
                data.append(tmp);
            }
            List<AirlineInfo> list = JSONObject.parseArray(data.toString(), AirlineInfo.class);
            // 写入标题行
            outputStream.write("\"enName\",\"level\",Food & Beverages,Inflight Entertainment,Seat Comfort,Staff Service,Value for Money\n".getBytes("UTF-8"));
            outputStream.flush();
            // 写入内容
            list.stream().forEach(item -> {
                if (item instanceof AirlineInfo) {
                    String dataLine = String.format("%s,%s,%s,%s,%s,%s,%s\n", item.getEnName(), item.getRatingLogoLevel(),
                            item.getRatingsList().stream().filter(k -> k.containsKey("Food & Beverages")).findFirst().get().get("Food & Beverages"),
                            item.getRatingsList().stream().filter(k -> k.containsKey("Inflight Entertainment")).findFirst().get().get("Inflight Entertainment"),
                            item.getRatingsList().stream().filter(k -> k.containsKey("Seat Comfort")).findFirst().get().get("Seat Comfort"),
                            item.getRatingsList().stream().filter(k -> k.containsKey("Staff Service")).findFirst().get().get("Staff Service"),
                            item.getRatingsList().stream().filter(k -> k.containsKey("Value for Money")).findFirst().get().get("Value for Money"));

                    try {
                        outputStream.write(dataLine.getBytes("UTF-8"));
                        outputStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });


            // 关闭流
            reader.close();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
