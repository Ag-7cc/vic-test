package com.vic.test.util;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Created by vic
 * Create time : 2017/6/9 下午2:10
 */
public class MapsUtil {
    /**
     * 获取百度地图经纬度地址
     */
    public static final String BAIDU_MAP_URL_V2 = "http://api.map.baidu.com/place/v2/search?query=%s&ak=%s&output=%s&page_size=%s&page_num=%s&scope=%s&region=%s";
    public static final String BAIDU_MAP_URL_V1_BY_ADDRESS = "http://api.map.baidu.com/geocoder?address=%s&key=%s&output=%s";
    public static final String BAIDU_MAP_URL_V1_BY_LOCATION = "http://api.map.baidu.com/geocoder?location=%s&key=%s&output=%s";
    // 1 起始地经纬度（墨卡托） 2 结束经纬度（墨卡托）
    public static final String BAIDU_MAP_URL_MOBILE_LOCATION = "http://map.baidu.com/mobile/webapp/search/search/qt=nav&sn=1$$$$%s$$%E8%B5%B7%E7%82%B9&en=1$$$$%s$$%E7%BB%88%E7%82%B9/vt=map/";
    /**
     * 百度坐标转换API
     * 1：源坐标
     * 2：源坐标类型
     * 3：目标坐标类型
     * 4：key
     */
    public static final String BAIDU_MAP_URL_CONVERT_COORDS = "http://api.map.baidu.com/geoconv/v1/?coords=%s&from=%s&to=%s&ak=%s";

    /**
     * 获取高德地图经纬度地址
     */
    public static final String GAODE_MAP_URL = "http://restapi.amap.com/v3/place/text?keywords=%s&key=%s&output=%s&offset=%s";
    /**
     * 高德经纬度转换API
     */
    public static final String GAODE_LNG_LAT_CONVERT = "http://restapi.amap.com/v3/assistant/coordinate/convert?locations=%s&key=%s&output=%s&coordsys=baidu";
    /**
     * 返回数据类型(json)
     */
    public static final String DATA_TYPE_JSON = "json";
    /**
     * 返回数据类型(html)
     */
    public static final String DATA_TYPE_HTML = "html";
    /**
     * 百度KEY
     */
    public static final String BAIDU_KEY_V1 = "7d9fbeb43e975cd1e9477a7e5d5e192a";
    public static final String BAIDU_KEY_V2 = "SF9foeRgH9VTYnYp6It1QBU9";
    public static final String BAIDU_KEY_ALL_WEB_SERVICE_KEY = "fN7QGBSTPKQwbXGTs66TpKSb";
    /**
     * 百度坐标转换 - 源坐标 - GPS设备获取的角度坐标
     */
    public static final String BAIDU_LON_LAT_CONVERT_SOURCE_TYPE_1 = "1";
    /**
     * 百度坐标转换 - 源坐标 - GPS获取的米制坐标、sogou地图所用坐标
     */
    public static final String BAIDU_LON_LAT_CONVERT_SOURCE_TYPE_2 = "2";
    /**
     * 百度坐标转换 - 源坐标 - google地图、soso地图、aliyun地图、mapabc地图和amap地图所用坐标
     */
    public static final String BAIDU_LON_LAT_CONVERT_SOURCE_TYPE_3 = "3";
    /**
     * 百度坐标转换 - 源坐标 - 3中列表地图坐标对应的米制坐标
     */
    public static final String BAIDU_LON_LAT_CONVERT_SOURCE_TYPE_4 = "4";
    /**
     * 百度坐标转换 - 源坐标/目标坐标 - 百度地图采用的经纬度坐标
     */
    public static final String BAIDU_LON_LAT_CONVERT_SOURCE_TYPE_5 = "5";
    /**
     * 百度坐标转换 - 源坐标/目标坐标 - 百度地图采用的米制坐标
     */
    public static final String BAIDU_LON_LAT_CONVERT_SOURCE_TYPE_6 = "6";
    /**
     * 百度坐标转换 - 源坐标 - mapbar地图坐标
     */
    public static final String BAIDU_LON_LAT_CONVERT_SOURCE_TYPE_7 = "7";
    /**
     * 百度坐标转换 - 源坐标 - 51地图坐标
     */
    public static final String BAIDU_LON_LAT_CONVERT_SOURCE_TYPE_8 = "8";
    /**
     * 高德Script KEY(获取坐标)
     */
    public static final String GAODE_KEY_SCRIPT = "754247673b7908249d3ce9bed7f9e07e";
    /**
     * 高德REST KEY(坐标转换)
     */
    public static final String GAODE_KEY_REST = "8325164e247e15eea68b59e89200988b";


    public static String getMapLatitudeAndLongitude(String url) throws IOException {
        HttpGet get = new HttpGet(url);
        CloseableHttpResponse response = HttpUtil.getHttpClient().execute(get);
        String responseText = EntityUtils.toString(response.getEntity());
        return responseText;
    }

    /**
     * 获取百度地图经纬度
     * 返回输入地址的经纬度坐标
     * @param query    详细地址（检索关键字，最多支持10个字符）
     * @param ak       用户的访问密钥
     * @param output   返回格式（json/xml）
     * @param pageSize 每页条数
     * @param pageNum  当前页
     * @param scope    检索结果详细程度（1：基本信息 2：详细信息）
     * @param region   省/市
     * @return lng(经度),lat(纬度)
     * @throws IOException
     */
    public static String getBaiDuMapLatitudeAndLongitude(String query, String ak, String output,
                                                         String pageSize, String pageNum, String scope,
                                                         String region) throws IOException {
        StringBuffer result = new StringBuffer();
        try {
            String url = String.format(MapsUtil.BAIDU_MAP_URL_V2, query.trim(), ak, output, pageSize, pageNum, scope, region);
            String str = MapsUtil.getMapLatitudeAndLongitude(url);
            String jsonStr = new JSONObject(str.toString()).getJSONArray("results").toString();
            jsonStr = jsonStr.substring(1, jsonStr.length() - 1);
            JSONObject jsonObject = new JSONObject(jsonStr).getJSONObject("location");
            result.append(jsonObject.get("lng"));
            result.append(",");
            result.append(jsonObject.get("lat"));
        }catch (Exception e){

        }
        return result.toString();
    }

    /**
     * 获取高德地图经纬度
     * 返回输入地址的经纬度坐标
     * @param address 检索关键字
     * @param key 用户的访问密钥
     * @param offset 每页条数
     * @param output 返回结果类型（json/xml）
     * @return lng(经度),lat(纬度)
     * @throws IOException
     */
    public static String getGaoDeMapLatitudeAndLongitude(String address,String key,String output,String offset) throws IOException {
        String url = String.format(MapsUtil.GAODE_MAP_URL,address,key,output,offset);
        String str = MapsUtil.getMapLatitudeAndLongitude(url);
        String jsonStr = new JSONObject(str).get("pois").toString();
        jsonStr = jsonStr.substring(1, jsonStr.length() - 1);
        JSONObject jsonObject = new JSONObject(jsonStr);
        StringBuffer result = new StringBuffer();
        result.append(jsonObject.get("location"));
        return result.toString();
    }

    /**
     * 讲其他坐标系转经纬度换为高德经纬度
     * @param locations 检索关键字
     * @param key 用户的访问密钥
     * @param output 返回家过类型(json/xml)
     * @param coordsys 源坐标系
     * @return lng(经度),lat(纬度)
     * @throws IOException
     */
    public static String convertBaiDuToGaoDe(String locations,String key,String output,String coordsys) throws IOException {
        String url = String.format(MapsUtil.GAODE_LNG_LAT_CONVERT,locations,key,output,coordsys);
        String str = MapsUtil.getMapLatitudeAndLongitude(url);
        String jsons = new JSONObject(str).get("locations").toString();
        return jsons;
    }

    /**
     * 获取百度地图V1版URL
     *
     * @param x 坐标 X轴
     * @param y 坐标 Y轴
     * @return
     */
    public static String getBaiDuMapsV1Url(String x, String y){
        return String.format(BAIDU_MAP_URL_V1_BY_LOCATION, y + "," + x, BAIDU_KEY_V1, DATA_TYPE_HTML);
    }

    /**
     * 百度地图坐标转换
     *
     * @param locations 经纬度
     * @param from 源坐标类型 参照http://developer.baidu.com/map/index.php?title=webapi/guide/changeposition
     * @param to 目的坐标类型 参照http://developer.baidu.com/map/index.php?title=webapi/guide/changeposition
     * @param key Key
     * @return
     */
    public static String[] convertBaiduLatitudeAndLongitude(String locations, String from, String to, String key) throws IOException {
        String url = String.format(MapsUtil.BAIDU_MAP_URL_CONVERT_COORDS,locations,from,to,key);
        String str = MapsUtil.getMapLatitudeAndLongitude(url);
        String jsonStr = new JSONObject(str).get("result").toString();
        jsonStr = jsonStr.substring(1, jsonStr.length() - 1);
        JSONObject jsonObject = new JSONObject(jsonStr);
        return new String[]{jsonObject.get("x").toString(), jsonObject.get("y").toString()};
    }

    /**
     * 获取手机WEB百度导航URL（WEB版预约门诊用）
     *
     * @param beginLocation 起始经纬度
     * @param endLocation 结束经纬度
     * @return
     * @throws IOException
     */
    public static String getBaiduMapsMobileUrl(String beginLocation, String endLocation) throws IOException {
        String [] beginLocationStr = convertBaiduLatitudeAndLongitude(beginLocation, "5", "6", BAIDU_KEY_ALL_WEB_SERVICE_KEY);
        String [] endLocationStr = convertBaiduLatitudeAndLongitude(endLocation, "5", "6", BAIDU_KEY_ALL_WEB_SERVICE_KEY);
        DecimalFormat decimalFormat = new DecimalFormat("0.########");
        String beginLon = decimalFormat.format(Double.parseDouble(beginLocationStr[0]));
        String beginLat = decimalFormat.format(Double.parseDouble(beginLocationStr[1]));
        String endLon = decimalFormat.format(Double.parseDouble(endLocationStr[0]));
        String endLat = decimalFormat.format(Double.parseDouble(endLocationStr[1]));
        String url = "http://map.baidu.com/mobile/webapp/search/search/qt=nav&sn=1$$$$" + beginLon + "," + beginLat + "$$%E8%B5%B7%E7%82%B9&en=1$$$$" + endLon + "," + endLat + "$$%E7%BB%88%E7%82%B9/vt=map/";
        return url;
    }

    /**
     * 计算两经纬度点之间的距离（单位：米）
     * @param lng1  经度
     * @param lat1  纬度
     * @param lng2
     * @param lat2
     * @return
     */
    public static double getDistance(double lng1,double lat1,double lng2,double lat2){
        double radLat1 = Math.toRadians(lat1);
        double radLat2 = Math.toRadians(lat2);
        double a = radLat1 - radLat2;
        double b = Math.toRadians(lng1) - Math.toRadians(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1)
                * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * 6378137.0;// 取WGS84标准参考椭球中的地球长半径(单位:m)
        s = Math.round(s * 10000) / 10000;
        return s;
    }
}
