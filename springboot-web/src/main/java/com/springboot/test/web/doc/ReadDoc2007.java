package com.springboot.test.web.doc;

import org.apache.poi.xwpf.converter.core.BasicURIResolver;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.*;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * 读取2007的word文档
 * @author liufei
 * @date 2019/10/11 20:42
 */
public class ReadDoc2007 {
    public static XWPFDocument read2007(String filePath,Map<String,Object> params) throws Exception{
        XWPFDocument doc = new XWPFDocument(new FileInputStream(filePath));
        readParagraph(doc.getParagraphs(),params);
        readTables(doc,params);
        return doc;
    }

    /**
     * 读取表格信息
     * @param doc
     * @param params
     */
    public static void readTables(XWPFDocument doc,Map<String,Object> params){
        List<XWPFTable> tables = doc.getTables();
        for(XWPFTable table : tables){
            List<XWPFTableRow> rows = table.getRows();
            for(XWPFTableRow row : rows){
                List<XWPFTableCell> cells = row.getTableCells();
                for(XWPFTableCell cell : cells){
                    readParagraph(cell.getParagraphs(),params);
                }
            }
        }
    }

    /**
     * 读取段落信息
     * @param list
     */
    public static void readParagraph(List<XWPFParagraph> list,Map<String,Object> params){
        for(XWPFParagraph paragraph : list){
            StringBuffer out = new StringBuffer();
            List<XWPFRun> runs = paragraph.getRuns();
            XWPFRun start=null;
            //遍历所有段落中的runs
            for(int i = 0;i<runs.size();i++) {
                XWPFRun run = runs.get(i);
                out.append(run.getText(0));
                //判断当前有通配符${}
                if(start==null&&out.indexOf("${")!=-1){
                    //当前run中只拥有{，$被分隔到上一个run
                    if(!run.getText(0).contains("${")){
                        //将一个run去除通配符的$
                        runs.get(i-1).setText(runs.get(i-1).getText(0).substring(0,runs.get(i-1).getText(0).length()-1),0);
                    }
                    //设置需要将run的text置为空的run
                    start = run;
                }
                if(start!=null&&out.indexOf("}")!=-1){
                    for(String key : params.keySet()){
                        if(out.indexOf(key)!=-1){
                            Object value = params.get(key);
                            if (value instanceof String) {//文本替换
                                run.setText(value.toString(),0);
                                out.replace(out.indexOf(key),out.indexOf(key)+key.length(),value.toString());
                            }else if (value instanceof Map) {//图片替换
                                //从map中获取图片的宽、高、位置和描述信息，编写图片定义xml 放入
                            }
                            start = null;
                        }
                    }
                }
                //判断通配符匹配未结束,将run的text置为空
                if(start!=null){
                    run.setText(null,0);
                }
            }
        }
    }

    /**
     *
     * 2007版本word转换成html
     *
     * @param wordPath  word文件路径
     * @param wordName word文件名称无后缀
     * @param suffix   word文件后缀
     * @param htmlPath html存储地址
     * @return
     * @throws IOException
     */
    public static String Word2007ToHtml(String wordPath, String wordName, String suffix, String htmlPath)
            throws IOException {
        String htmlName = wordName + ".html";
        String imagePath = htmlPath + "image" + File.separator;
        // 判断html文件是否存在
        File htmlFile = new File(htmlPath + htmlName);
        if (htmlFile.exists()) {
            return htmlFile.getAbsolutePath();
        }
        // word文件
        File wordFile = new File(wordPath + File.separator + wordName + suffix);
        // 1) 加载word文档生成 XWPFDocument对象
        InputStream in = new FileInputStream(wordFile);
        XWPFDocument document = new XWPFDocument(in);
        // 2) 解析 XHTML配置 (这里设置IURIResolver来设置图片存放的目录)
        File imgFolder = new File(imagePath);
        XHTMLOptions options = XHTMLOptions.create();
        options.setExtractor(new FileImageExtractor(imgFolder));
        // html中图片的路径 相对路径
        options.URIResolver(new BasicURIResolver("image"));
        options.setIgnoreStylesIfUnused(false);
        options.setFragment(true);
        // 3) 将 XWPFDocument转换成XHTML
        // 生成html文件上级文件夹
        File folder = new File(htmlPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        OutputStream out = new FileOutputStream(htmlFile);
        XHTMLConverter.getInstance().convert(document, out, options);
        return htmlFile.getAbsolutePath();
    }
}
