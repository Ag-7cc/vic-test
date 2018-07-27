package com.vic.test.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.common.usermodel.Hyperlink;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Office格式的软件的工具类
 * Created by vic
 * Create time : 2017/9/29 上午10:22
 */
public class OfficeUtil {

    /**
     * 正则，用于匹配0串
     */
    private static final Pattern PATTERN_ZEROS = Pattern.compile("[0]*");

    public static final Integer EXPORT_COUNT = 10000;

    /**
     * 读取Excel，并返回数据字符串数组
     *
     * @param file excel文件
     * @return 数据字符串数组
     * @throws IOException 当file为不支持的文件类型
     */
    public static List<List<String>> readExcel(File file) throws IOException {
        return readExcel(file, null, null);
    }

    public static List<List<String>> readExcel(File file, Integer index) throws IOException {
        return readExcel(file, index, null);
    }

    public static List<List<String>> readExcel(File file, String name) throws IOException {
        return readExcel(file, null, name);
    }

    public static List<List<String>> readExcel(File file, Integer index, String name) throws IOException {
        String fileName = file.getName();
        String extension = fileName.lastIndexOf('.') == -1 ? "" : fileName.substring(fileName.lastIndexOf('.') + 1);
        switch (extension) {
            case "xls":
                return read2003Excel(file, index, name);
            case "xlsx":
                return read2007Excel(file, index, name);
            default:
                throw new IOException("不支持的文件类型");
        }
    }

    /**
     * 读取 office 2003 excel
     */
    private static List<List<String>> read2003Excel(File file, Integer index, String name) throws IOException {
        List<List<String>> list = new LinkedList<>();
        HSSFWorkbook hwb = new HSSFWorkbook(new FileInputStream(file));
        HSSFSheet sheet = (HSSFSheet) getSheet(hwb, index, name);
        int recordValueSize = sheet.getRow(sheet.getFirstRowNum()).getLastCellNum(); // 每行记录字段数，防止有些行，有很多空记录
        for (int i = sheet.getFirstRowNum(); i <= sheet.getPhysicalNumberOfRows(); i++) {
            HSSFRow row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            List<String> linked = new LinkedList<>();
            for (int j = row.getFirstCellNum(); j < recordValueSize; j++) {
                HSSFCell cell = row.getCell(j);
                if (cell == null) {
                    linked.add("");
                } else {
                    linked.add(parseString(cell));
                }
            }
            list.add(linked);
        }
        return list;
    }


    /**
     * 读取 office 2003 excel
     */
    public static List<List<String>> read2003Excel(MultipartFile file, Integer index, String name) throws IOException, InvalidFormatException {
        List<List<String>> list = new LinkedList<>();
        Workbook xwb = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = getSheet(xwb, index, name);
        int recordValueSize = sheet.getRow(sheet.getFirstRowNum()).getLastCellNum(); // 每行记录字段数，防止有些行，有很多空记录
        for (int i = sheet.getFirstRowNum(); i <= sheet.getPhysicalNumberOfRows(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            List<String> linked = new LinkedList<>();
            for (int j = row.getFirstCellNum(); j < recordValueSize; j++) {
                Cell cell = row.getCell(j);
                if (cell == null) {
                    linked.add("");
                } else {
                    linked.add(parseString(cell));
                }
            }
            list.add(linked);
        }
        return list;
    }

    /**
     * 读取Office 2007 excel
     */
    public static List<List<String>> read2007Excel(File file, Integer index, String name) throws IOException {
        List<List<String>> list = new LinkedList<>();
        XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file));
        XSSFSheet sheet = (XSSFSheet) getSheet(xwb, index, name);
        int recordValueSize = sheet.getRow(sheet.getFirstRowNum()).getLastCellNum(); // 每行记录字段数，防止有些行，有很多空记录
        for (int i = sheet.getFirstRowNum(); i <= sheet.getPhysicalNumberOfRows(); i++) {
            XSSFRow row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            List<String> linked = new LinkedList<>();
            for (int j = row.getFirstCellNum(); j < recordValueSize; j++) {
                XSSFCell cell = row.getCell(j);
                if (cell == null) {
                    linked.add("");
                } else {
                    linked.add(parseString(cell));
                }
            }
            list.add(linked);
        }
        return list;
    }

    /**
     *读取excel
     * 读取Office 2007 excel
     */
    public static List<List<String>> read2007Excel(MultipartFile file, Integer index, String name) throws IOException, InvalidFormatException {
        List<List<String>> list = new LinkedList<>();
        Workbook xwb = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = getSheet(xwb, index, name);
        int recordValueSize = sheet.getRow(sheet.getFirstRowNum()).getLastCellNum(); // 每行记录字段数，防止有些行，有很多空记录
        for (int i = sheet.getFirstRowNum(); i <= sheet.getPhysicalNumberOfRows(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            List<String> linked = new LinkedList<>();
            for (int j = row.getFirstCellNum(); j < recordValueSize; j++) {
                Cell cell = row.getCell(j);
                if (cell == null) {
                    linked.add("");
                } else {
                    linked.add(parseString(cell));
                }
            }
            list.add(linked);
        }
        return list;
    }

    private static Sheet getSheet(Workbook book, Integer index, String name) {
        if (index != null && name != null) {
            throw new RuntimeException("index,name只能有一个不为空！");
        }
        Sheet sheet;
        if (index != null) {
            sheet = book.getSheetAt(index);
        } else if (name != null) {
            sheet = book.getSheet(name);
        } else {
            sheet = book.getSheetAt(0);
        }
        return sheet;
    }

    /**
     * 获取cell的value值
     */
    private static String parseString(Cell cell) {
        int cellType = cell.getCellType();
        String value;
        switch (cellType) {
            case Cell.CELL_TYPE_NUMERIC:
                DecimalFormat decimalFormat = new DecimalFormat("0.###");
                value = decimalFormat.format(new BigDecimal(cell.getNumericCellValue()));
                break;
            case Cell.CELL_TYPE_STRING:
                value = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                value = cell.getBooleanCellValue() ? "是" : "否";
                break;
            case Cell.CELL_TYPE_FORMULA:
            case Cell.CELL_TYPE_BLANK:
            case Cell.CELL_TYPE_ERROR:
            default:
                value = "";
                break;
        }
        return value;
    }

    /**
     * 特殊方法。将象"123.0"类型的字符串的".0"去掉<br />
     * 若不符合这样的条件，返回null
     *
     * @param value 字符串
     * @return 字符串
     */
    private static String parseInt(String value) {
        String[] strs = value.split("\\.");
        if (strs.length != 2) {
            return null;
        }
        if (StringUtils.isNumeric(strs[0]) && PATTERN_ZEROS.matcher(strs[1]).matches()) {
            return strs[0];
        }
        return null;
    }

    /**
     * 导出2007版本Excel
     * @param sheetName
     * @param columnWidths
     * @param columnNames
     * @param values
     * @return
     */
    public static XSSFWorkbook buildExcel2007(String sheetName, List<Integer> columnWidths, List<String> columnNames, List<List<Object>> values){
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFCellStyle style =  wb.createCellStyle();
        style.setFillPattern(HSSFCellStyle.ALIGN_LEFT);
        style.setFillPattern(HSSFCellStyle.ALIGN_LEFT);
        XSSFSheet sheet = wb.createSheet(sheetName);

        for (int i = 0; i < columnWidths.size(); i ++) {
            sheet.setColumnWidth(i, columnWidths.get(i));
        }

        XSSFRow row = sheet.createRow(0);
        for (int i = 0; i < columnNames.size(); i ++) {
            row.createCell(i).setCellValue(columnNames.get(i));
        }

        for (int i = 0; i < values.size(); i ++) {
            XSSFRow newRow = sheet.createRow(i + 1);
            for (int j = 0; j < values.get(i).size(); j ++) {
                newRow.createCell(j).setCellValue(values.get(i).get(j).toString());
            }
        }
        return wb;
    }

    /**
     * 导出2007版本Excel(附带超链接版)
     * @param sheetName
     * @param columnWidths
     * @param columnNames
     * @param values
     * @return
     */
    public static XSSFWorkbook buildExcel2007Click(String sheetName, List<Integer> columnWidths, List<String> columnNames, List<List<Object>> values, Map<Integer, Map<Integer, String>> urlMaps){
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFCellStyle style =  wb.createCellStyle();
        XSSFFont hlinkfont = wb.createFont();
        hlinkfont.setColor(HSSFColor.BLUE.index);
        style.setFont(hlinkfont);
        XSSFSheet sheet = wb.createSheet(sheetName);
        CreationHelper createHelper = wb.getCreationHelper();
        for (int i = 0; i < columnWidths.size(); i ++) {
            sheet.setColumnWidth(i, columnWidths.get(i));
        }

        XSSFRow row = sheet.createRow(0);
        for (int i = 0; i < columnNames.size(); i ++) {
            row.createCell(i).setCellValue(columnNames.get(i));
        }

        for (int i = 0; i < values.size(); i ++) {
            XSSFRow newRow = sheet.createRow(i + 1);
            XSSFCell cell;
            Map<Integer, String> urlMap = urlMaps.get(i);
            for (int j = 0; j < values.get(i).size(); j ++) {
                cell = newRow.createCell(j);
                cell.setCellValue(values.get(i).get(j).toString());
                if(urlMap != null && urlMap.containsKey(j)){
                    XSSFHyperlink link = (XSSFHyperlink) createHelper.createHyperlink(Hyperlink.LINK_URL);
                    link.setAddress(urlMap.get(j));
                    cell.setHyperlink(link);
                    cell.setCellStyle(style);
                }
            }
        }
        return wb;
    }

    public static HSSFWorkbook buildExcel2003(String sheetName, List<Integer> columnWidths, List<String> columnNames, List<List<Object>> values) {
        HSSFWorkbook wb = new HSSFWorkbook();
        buildExcelSheet2003(wb, sheetName, columnWidths, columnNames, values);
        return wb;
    }

    public static void buildExcel2003(HSSFWorkbook wb, String sheetName, List<Integer> columnWidths, List<String> columnNames, List<List<Object>> values) {
        buildExcelSheet2003(wb, sheetName, columnWidths, columnNames, values);
    }

    public static HSSFWorkbook buildExcelSheet2003(HSSFWorkbook wb, String sheetName, List<Integer> columnWidths, List<String> columnNames, List<List<Object>> values){
        HSSFCellStyle style =  wb.createCellStyle();
        style.setFillPattern(HSSFCellStyle.ALIGN_LEFT);
        HSSFSheet sheet = wb.createSheet(sheetName);

        for (int i = 0; i < columnWidths.size(); i ++) {
            sheet.setColumnWidth(i, columnWidths.get(i));
        }

        HSSFRow row = sheet.createRow(0);
        for (int i = 0; i < columnNames.size(); i ++) {
            row.createCell(i).setCellValue(columnNames.get(i));
        }

        for (int i = 0; i < values.size(); i ++) {
            HSSFRow newRow = sheet.createRow(i + 1);
            for (int j = 0; j < values.get(i).size(); j ++) {
                Object val = values.get(i).get(j);
                HSSFCell cell = newRow.createCell(j);
                if (val instanceof Number) {
                    cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    cell.setCellValue(((Number) val).doubleValue());
                } else {
                    if (val == null) {
                        cell.setCellValue("");
                    } else {
                        cell.setCellValue(String.valueOf(val));
                    }
                }
//                if (val instanceof String) {
//                    newRow.createCell(j).setCellValue((String) val);
//                } else if (val instanceof Double) {
//                    newRow.createCell(j).setCellType();
//                }
            }
        }
        return wb;
    }

    /**
     * 分sheet页导出数据，默认每页一万条
     *
     * @param sheetName
     * @param columnWidths
     * @param columnNames
     * @param values
     * @param sheetCount 每sheet页的条数
     * @return
     */
    public static HSSFWorkbook buildExcelSheets2003(String sheetName, List<Integer> columnWidths, List<String> columnNames, List<List<Object>> values,Integer sheetCount) {
        HSSFWorkbook wb = new HSSFWorkbook();
        buildExcelSheets2003(wb, sheetName, columnWidths, columnNames, values,sheetCount);
        return wb;
    }

    public static void buildExcelSheets2003(HSSFWorkbook wb, String sheetName, List<Integer> columnWidths, List<String> columnNames, List<List<Object>> values,Integer sheetCount){
        HSSFCellStyle style =  wb.createCellStyle();
        style.setFillPattern(HSSFCellStyle.ALIGN_LEFT);
        HSSFSheet sheet = wb.createSheet(sheetName);

        for (int i = 0; i < columnWidths.size(); i ++) {
            sheet.setColumnWidth(i, columnWidths.get(i));
        }

        HSSFRow row = sheet.createRow(0);
        for (int i = 0; i < columnNames.size(); i ++) {
            row.createCell(i).setCellValue(columnNames.get(i));
        }

        //每个sheet中的总显示数,默认一万条
        if(!(sheetCount > 0)){
            sheetCount = EXPORT_COUNT;
        }
        int intRowCount = values.size(); //总记录数
        int allSheetCount = (intRowCount + sheetCount -1)/sheetCount;//计算总工作表数,分表
        int q = 0;//sheet数
        int p = 1;

        if(allSheetCount > 1){
            for (int i = 0; i < values.size(); i ++) {
                HSSFRow newRow = null;//表内容
                if(p <= sheetCount){
                    newRow = sheet.createRow(p);
                    for (int j = 0; j < values.get(i).size(); j ++) {
                        Object val = values.get(i).get(j);
                        HSSFCell cell = newRow.createCell(j);
                        if (val instanceof Number) {
                            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                            cell.setCellValue(((Number) val).doubleValue());
                        } else {
                            if (val == null) {
                                cell.setCellValue("");
                            } else {
                                cell.setCellValue(String.valueOf(val));
                            }
                        }
                    }
                    p++;
                } else {
                    p = 1;
                    q++;
                    sheet = wb.createSheet(sheetName + q);
                    for (int k = 0; k < columnWidths.size(); k ++) {
                        sheet.setColumnWidth(k, columnWidths.get(k));
                    }
                    row = sheet.createRow(0);
                    for (int k = 0; k < columnNames.size(); k ++) {
                        row.createCell(k).setCellValue(columnNames.get(k));
                    }
                }
            }
        } else {
            for (int i = 0; i < values.size(); i ++) {
                HSSFRow newRow = sheet.createRow(i + 1);
                for (int j = 0; j < values.get(i).size(); j ++) {
                    Object val = values.get(i).get(j);
                    HSSFCell cell = newRow.createCell(j);
                    if (val instanceof Number) {
                        cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                        cell.setCellValue(((Number) val).doubleValue());
                    } else {
                        if (val == null) {
                            cell.setCellValue("");
                        } else {
                            cell.setCellValue(String.valueOf(val));
                        }
                    }
                }
            }
        }
    }

    /**
     * 生成2003版Excel(附带超链接版）
     * @param wb
     * @param sheetName
     * @param columnWidths
     * @param columnNames
     * @param values
     * @param urlMaps Map<Integer//行索引, Map<Integer//超链接位置索引, String//超链接URL>>
     * @return
     */
    public static HSSFWorkbook buildExcelSheet2003Click(HSSFWorkbook wb, String sheetName, List<Integer> columnWidths, List<String> columnNames, List<List<Object>> values, Map<Integer, Map<Integer, String>> urlMaps){
        HSSFCellStyle highLinkStyle = wb.createCellStyle();
        HSSFFont highLinkFont = wb.createFont();
        highLinkFont.setUnderline(HSSFFont.U_SINGLE);
        highLinkFont.setColor(HSSFColor.BLUE.index);
        highLinkStyle.setFont(highLinkFont);
        HSSFSheet sheet = wb.createSheet(sheetName);

        for (int i = 0; i < columnWidths.size(); i ++) {
            sheet.setColumnWidth(i, columnWidths.get(i));
        }

        HSSFRow row = sheet.createRow(0);
        for (int i = 0; i < columnNames.size(); i ++) {
            row.createCell(i).setCellValue(columnNames.get(i));
        }

        for (int i = 0; i < values.size(); i ++) {
            HSSFRow newRow = sheet.createRow(i + 1);
            Map<Integer, String> urlMap = urlMaps.get(i);
            for (int j = 0; j < values.get(i).size(); j ++) {
                Object val = values.get(i).get(j);
                HSSFCell cell = newRow.createCell(j);
                if (val instanceof Number) {
                    cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    cell.setCellValue(((Number) val).doubleValue());
                } else {
                    if (val == null) {
                        cell.setCellValue("");
                    } else {
                        cell.setCellValue(String.valueOf(val));
                    }
                }
                if(null != urlMap && urlMap.containsKey(j)){
                    HSSFHyperlink link = new HSSFHyperlink(HSSFHyperlink.LINK_URL);
                    link.setAddress(urlMap.get(j));
                    cell.setHyperlink(link);
                    cell.setCellStyle(highLinkStyle);
                }
//                if (val instanceof String) {
//                    newRow.createCell(j).setCellValue((String) val);
//                } else if (val instanceof Double) {
//                    newRow.createCell(j).setCellType();
//                }
            }
        }
        return wb;
    }

    /**
     * 二级表头Excel
     *
     * @param sheetName     表头
     * @param columnWidths  列宽
     * @param values        表数据
     * @param jsonTitle     表头JSON字符串
     * @return
     */
    public static HSSFWorkbook buildTwoLevelTitleExcel2003(String sheetName, List<Integer> columnWidths, List<List<Object>> values, String jsonTitle){
        LinkedHashMap<String, List<String>> jsonMap = JSON.parseObject(jsonTitle, new TypeReference<LinkedHashMap<String, List<String>>>() {});

        Map<Integer[], String> titleMap = new LinkedHashMap<>();
        int colIndex = 0;
        for (Map.Entry<String, List<String>> entry : jsonMap.entrySet()) {
            String firstRow = entry.getKey();
            List<String> secondRow = entry.getValue();
            if (secondRow.isEmpty()) {
                titleMap.put(new Integer[]{0, 1, colIndex, colIndex}, firstRow);
                colIndex ++;
            } else {
                titleMap.put(new Integer[]{0, 0, colIndex, (colIndex + secondRow.size() -1)}, firstRow);
                colIndex += secondRow.size();
            }
        }

        colIndex = 0;
        for (Map.Entry<String, List<String>> entry : jsonMap.entrySet()) {
            List<String> secondRow = entry.getValue();
            if (secondRow.isEmpty()) {
                colIndex ++;
            } else {
                for (int i = 0; i < secondRow.size(); i++) {
                    titleMap.put(new Integer[]{1, 1, colIndex + i, colIndex + i}, secondRow.get(i));
                }
                colIndex += secondRow.size();
            }
        }

        return buildExcel2003ByTitle(sheetName, columnWidths, values, titleMap);
    }

    /**
     * 自定义表头
     *
     * @param sheetName
     * @param columnWidths
     * @param values
     * @param titleMap Map<String,Integer[]>
     *                 key:[0]起始行索引 [1]结束行索引 [2]起始列索引 [3]结束列索引
     *                 value(Integer[])：需要显示在表格的内容
     *                 注：全部索引从0开始
     * @return
     */
    public static HSSFWorkbook buildExcel2003ByTitle(String sheetName, List<Integer> columnWidths, List<List<Object>> values, Map<Integer[], String> titleMap){
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet(sheetName);

        for (int i = 0; i < columnWidths.size(); i ++) {
            sheet.setColumnWidth(i, columnWidths.get(i));
        }
        HSSFCellStyle columnHeadStyle = wb.createCellStyle();
        columnHeadStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
        columnHeadStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
        columnHeadStyle.setWrapText(true);

        int currentRowIndex = -1;
        HSSFRow row = null;
        HSSFCell cellTitle = null;
        for(Integer[] index : titleMap.keySet()){
            String title = titleMap.get(index);
            sheet.addMergedRegion(new CellRangeAddress(index[0], index[1], index[2], index[3]));
            if(currentRowIndex != index[0] || row == null) {
                row = sheet.createRow(index[0]);
                currentRowIndex = index[0];
            }
            cellTitle = row.createCell(index[2]);
            cellTitle.setCellValue(new HSSFRichTextString(title));
            cellTitle.setCellStyle(columnHeadStyle);
        }

        for (int i = 0; i < values.size(); i ++, currentRowIndex ++) {
            HSSFRow newRow = sheet.createRow(currentRowIndex + 1);
            for (int j = 0; j < values.get(i).size(); j ++) {
                Object val = values.get(i).get(j);
                HSSFCell cell = newRow.createCell(j);
                if (val instanceof Number) {
                    cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    cell.setCellValue(((Number) val).doubleValue());
                } else {
                    if (val == null) {
                        cell.setCellValue("");
                    } else {
                        cell.setCellValue(String.valueOf(val));
                    }
                }
                cell.setCellStyle(columnHeadStyle);
            }
        }
        return wb;
    }

    public static HSSFWorkbook buildExcel(String sheetName, List<Integer> columnWidths, List<String> columnNames, List<List<Object>> values){
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFCellStyle style =  wb.createCellStyle();
        style.setFillPattern(HSSFCellStyle.ALIGN_LEFT);
        style.setFillPattern(HSSFCellStyle.ALIGN_LEFT);
        HSSFSheet sheet = wb.createSheet(sheetName);

        for (int i = 0; i < columnWidths.size(); i++) {
            sheet.setColumnWidth(i, columnWidths.get(i));
        }

        HSSFRow row = sheet.createRow(0);
        for (int i = 0; i < columnNames.size(); i ++) {
            row.createCell(i).setCellValue(columnNames.get(i));
        }

        for (int i = 0; i < values.size(); i ++) {
            HSSFRow newRow = sheet.createRow(i + 1);
            for (int j = 0; j < values.get(i).size(); j ++) {
                Object data = values.get(i).get(j);
                HSSFCell cell = newRow.createCell(j);
                Boolean isNumber = getIsNumber(data);
                if (isNumber) {
                    cell.setCellValue(Double.parseDouble(data.toString()));
                } else {
                    cell.setCellValue(data.toString());
                }
            }
        }
        return wb;
    }

    /**
     * 判断数据是否为Number类型
     *
     * @param data
     * @return
     */
    protected static Boolean getIsNumber(Object data) {
        if (data instanceof Number) {
            return true;
        }
        return false;
    }

    /**
     *  向excel中添加新sheet
     *
     * @param sourceWorkbook
     * @param sheetName sheet名称
     * @param columnWidths 列大小
     * @param columnNames 列标题
     * @param values 值
     * @return
     */
    public static HSSFWorkbook addSheet(HSSFWorkbook sourceWorkbook, String sheetName, List<Integer> columnWidths, List<String> columnNames, List<List<String>> values ) {
        if (sourceWorkbook == null) {
            // 创建excel对象
            sourceWorkbook = new HSSFWorkbook();
            HSSFCellStyle style =  sourceWorkbook.createCellStyle();
            style.setFillPattern(HSSFCellStyle.ALIGN_LEFT);
            style.setFillPattern(HSSFCellStyle.ALIGN_LEFT);
        }

        // 新增sheet
        HSSFSheet sheet = sourceWorkbook.createSheet(sheetName);
        // 添加数据
        {
            for (int i = 0; i < columnWidths.size(); i ++) {
                sheet.setColumnWidth(i, columnWidths.get(i));
            }

            HSSFRow row = sheet.createRow(0);
            for (int i = 0; i < columnNames.size(); i ++) {
                row.createCell(i).setCellValue(columnNames.get(i));
            }

            for (int i = 0; i < values.size(); i ++) {
                HSSFRow newRow = sheet.createRow(i + 1);
                for (int j = 0; j < values.get(i).size(); j ++) {
                    newRow.createCell(j).setCellValue(values.get(i).get(j));
                }
            }
        }
        return sourceWorkbook;
    }

    /**
     * 两行标题导出（第一行为总标题）
     * @param sheetName
     * @param columnWidths
     * @param columnNames
     * @param values
     * @return
     */
    public static HSSFWorkbook buildExcelBySecondRowTitle(String title, String sheetName, List<Integer> columnWidths, List<String> columnNames, List<List<String>> values){
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFCellStyle style =  wb.createCellStyle();
        style.setFillPattern(HSSFCellStyle.ALIGN_LEFT);
        style.setFillPattern(HSSFCellStyle.ALIGN_LEFT);
        HSSFSheet sheet = wb.createSheet(sheetName);

        for (int i = 0; i < columnWidths.size(); i ++) {
            sheet.setColumnWidth(i, columnWidths.get(i));
        }

        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);         //水平居中
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  //垂直居中
        cellStyle.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index); //背景颜色
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
        cellStyle.setFont(font);
        //创建第一行总标题并且合并所有列
        HSSFRow firstRow = sheet.createRow(0);
        HSSFCell fcell = firstRow.createCell(0);
        fcell.setCellValue(title);
        sheet.addMergedRegion(new CellRangeAddress(0,1,0,columnNames.size() - 1));
        fcell.setCellStyle(cellStyle);

        //创建第二行小标题行
        HSSFRow row = sheet.createRow(2);
        row.setRowStyle(cellStyle);
        for (int i = 0; i < columnNames.size(); i ++) {
            HSSFCell scell = row.createCell(i);
            scell.setCellValue(columnNames.get(i));
            scell.setCellStyle(cellStyle);
        }

        //创建数据行
        for (int i = 0; i < values.size(); i ++) {
            HSSFRow newRow = sheet.createRow(i + 3);
            for (int j = 0; j < values.get(i).size(); j ++) {
                newRow.createCell(j).setCellValue(values.get(i).get(j));
            }
        }
        return wb;
    }

    /**
     * 两行标题导出（第一行为总标题）
     * @param sheetName
     * @param columnWidths
     * @param columnNames
     * @param values
     * @return
     */
    public static XSSFWorkbook buildExcel2007BySecondRowTitle(String title, String sheetName, List<Integer> columnWidths, List<String> columnNames, List<List<String>> values){
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFCellStyle style =  wb.createCellStyle();
        style.setFillPattern(HSSFCellStyle.ALIGN_LEFT);
        style.setFillPattern(HSSFCellStyle.ALIGN_LEFT);
        XSSFSheet sheet = wb.createSheet(sheetName);

        for (int i = 0; i < columnWidths.size(); i ++) {
            sheet.setColumnWidth(i, columnWidths.get(i));
        }

        XSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);         //水平居中
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  //垂直居中
        cellStyle.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index); //背景颜色
        XSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
        cellStyle.setFont(font);
        //创建第一行总标题并且合并所有列
        XSSFRow firstRow = sheet.createRow(0);
        XSSFCell fcell = firstRow.createCell(0);
        fcell.setCellValue(title);
        sheet.addMergedRegion(new CellRangeAddress(0,1,0,columnNames.size() - 1));
        fcell.setCellStyle(cellStyle);

        //创建第二行小标题行
        XSSFRow row = sheet.createRow(2);
        row.setRowStyle(cellStyle);
        for (int i = 0; i < columnNames.size(); i ++) {
            XSSFCell scell = row.createCell(i);
            scell.setCellValue(columnNames.get(i));
            scell.setCellStyle(cellStyle);
        }

        //创建数据行
        for (int i = 0; i < values.size(); i ++) {
            XSSFRow newRow = sheet.createRow(i + 3);
            for (int j = 0; j < values.get(i).size(); j ++) {
                newRow.createCell(j).setCellValue(values.get(i).get(j));
            }
        }
        return wb;
    }


    /**
     * 自定义表头 多个页面同时导出
     *
     * @param sheetName
     * @param columnWidths
     * @param values
     * @param titleMap Map<String,Integer[]>
     *                 key:[0]起始行索引 [1]结束行索引 [2]起始列索引 [3]结束列索引
     *                 value(Integer[])：需要显示在表格的内容
     *                 注：全部索引从0开始
     * @return
     */
    public static HSSFWorkbook buildExcel2003ByTitles(HSSFWorkbook wb ,String sheetName, List<Integer> columnWidths, List<List<Object>> values, Map<Integer[], String> titleMap){
        HSSFSheet sheet = wb.createSheet(sheetName);

        for (int i = 0; i < columnWidths.size(); i ++) {
            sheet.setColumnWidth(i, columnWidths.get(i));
        }
        HSSFCellStyle columnHeadStyle = wb.createCellStyle();
        columnHeadStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
        columnHeadStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中

        int currentRowIndex = -1;
        HSSFRow row = null;
        HSSFCell cellTitle = null;
        for(Integer[] index : titleMap.keySet()){
            String title = titleMap.get(index);
            sheet.addMergedRegion(new CellRangeAddress(index[0], index[1], index[2], index[3]));
            if(currentRowIndex != index[0] || row == null) {
                row = sheet.createRow(index[0]);
                currentRowIndex = index[0];
            }
            cellTitle = row.createCell(index[2]);
            cellTitle.setCellValue(new HSSFRichTextString(title));
            cellTitle.setCellStyle(columnHeadStyle);
        }

        for (int i = 0; i < values.size(); i ++, currentRowIndex ++) {
            HSSFRow newRow = sheet.createRow(currentRowIndex + 1);
            for (int j = 0; j < values.get(i).size(); j ++) {
                Object val = values.get(i).get(j);
                HSSFCell cell = newRow.createCell(j);
                if (val instanceof Number) {
                    cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    cell.setCellValue(((Number) val).doubleValue());
                } else {
                    if (val == null) {
                        cell.setCellValue("");
                    } else {
                        cell.setCellValue(String.valueOf(val));
                    }
                }
                cell.setCellStyle(columnHeadStyle);
            }
        }
        return wb;
    }

    public static void downLoadExcel(HttpServletResponse response, String fileName, HSSFWorkbook wb) {
        fileName = fileName + ".xls";
        response.setContentType("application/x-msexcel; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        OutputStream os = null;
        try {
            response.setHeader("Content-Disposition", "attachment; filename=\""
                    + new String(fileName.getBytes("GBK"), "iso8859-1")
                    + "\"");
            os = response.getOutputStream();
            wb.write(os);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                os = null;
            }
        }
    }
}
