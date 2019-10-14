package com.springboot.test.web;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author liufei
 * @date 2019/10/12 11:09
 */
public class WordReplaceUtil {
    public static Map<String, String> params = new HashMap<String, String>();
    public static void main(String[] args) {
        String readPath = "C:\\Users\\liufei\\Desktop\\人事\\人员卡片模板.docx";
        String writePath = "C:\\Users\\liufei\\Desktop\\人事\\word2007.docx";
        initParams();
        try {
            XWPFDocument doc = generateWord(params,readPath);
            //replaceFooterAndHeader(doc);
            doc.write(new FileOutputStream(new File(writePath)));
        } catch (Exception e) {
            System.out.println("Error");
            e.printStackTrace();
        }
        System.out.println("End");
    }
    //获取文件类型
    public static String getFileSufix(String fileName){
        int index = fileName.lastIndexOf(".");
        return fileName.substring(index + 1);
    }
    //初始化替换数据
    private static void initParams() {
        params.put("${人员编号}", "编号测试");
        params.put("${人员姓名}", "姓名测试");
        params.put("${职务}", "职务测试");
    }

    public static void replaceFooterAndHeader(XWPFDocument doc){
        List<XWPFParagraph> footers = doc.getHeaderFooterPolicy().getDefaultFooter().getParagraphs();
        List<XWPFParagraph> headers = doc.getHeaderFooterPolicy().getDefaultHeader().getParagraphs();
        //处理页脚
        for (XWPFParagraph paragraph : footers) {
            List<XWPFRun> runs = paragraph.getRuns();
            for (XWPFRun run : runs) {
                String text = run.getText(0);
                if(StringUtils.isNotEmpty(text)){
                    for(Entry<String, String> entry : params.entrySet()){
                        String key = entry.getKey();
                        if(text.indexOf(key) != -1){
                            Object value = entry.getValue();
                            if(value instanceof String){
                                text = text.replace(key, value.toString());
                                run.setText(text,0);
                            }
                        }
                    }
                }
            }
        }
//处理页眉
        for (XWPFParagraph paragraph : headers) {
            List<XWPFRun> runs = paragraph.getRuns();
            for (XWPFRun run : runs) {
                String text = run.getText(0);
                if(StringUtils.isNotEmpty(text)){
                    for(Entry<String, String> entry : params.entrySet()){
                        String key = entry.getKey();
                        if(text.indexOf(key) != -1){
                            Object value = entry.getValue();
                            if(value instanceof String){
                                text = text.replace(key, value.toString());
                                run.setText(text,0);
                            }
                        }
                    }
                }
            }
        }
    }

    public static XWPFDocument generateWord(Map<String, String> param, String filePath) {
        XWPFDocument doc = null;
        try {
            OPCPackage pack = POIXMLDocument.openPackage(filePath);
            doc = new XWPFDocument(pack);
            if (param != null && param.size() > 0) {
            //处理段落
                List<XWPFParagraph> paragraphList = doc.getParagraphs();
                processParagraphs(paragraphList, param, doc);
                //处理表格
                Iterator<XWPFTable> it = doc.getTablesIterator();
                while (it.hasNext()) {
                    XWPFTable table = it.next();
                    List<XWPFTableRow> rows = table.getRows();
                    for (XWPFTableRow row : rows) {
                        List<XWPFTableCell> cells = row.getTableCells();
                        for (XWPFTableCell cell : cells) {
                            List<XWPFParagraph> paragraphListTable = cell.getParagraphs();
                            processParagraphs(paragraphListTable, param, doc);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc;
    }

    public static void processParagraphs(List<XWPFParagraph> paragraphList,Map<String, String> param,XWPFDocument doc){
        if(paragraphList != null && paragraphList.size() > 0){
            for(XWPFParagraph paragraph:paragraphList){
                List<XWPFRun> runs = paragraph.getRuns();
                for (XWPFRun run : runs) {
                    String text = run.getText(0);
                    System.out.println(text);
                    if(text != null){
                        for (Entry<String, String> entry : param.entrySet()) {
                            String key = entry.getKey();
                            if(text.indexOf(key) != -1){
                                Object value = entry.getValue();
                                if (value instanceof String) {//文本替换
                                    text = text.replace(key, value.toString());
                                    run.setText(text,0);
                                }
                            }
                        }
//if(isSetText){
//run.setText(text,0);
//}
                    }
                }
            }
        }
    }
}
