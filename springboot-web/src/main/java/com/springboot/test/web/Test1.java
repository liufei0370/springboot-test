package com.springboot.test.web;

import com.springboot.test.web.doc.ReadDoc2007;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.usermodel.*;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liufei
 * @date 2019/1/28 15:19
 */
public class Test1 {
    public static void main(String[] args) throws Exception {
        String filePath = "C:\\Users\\liufei\\Desktop\\人事\\人员卡片模板.docx";
        try {
            InputStream is = FileMagic.prepareToCheckMagic(new FileInputStream(filePath));
            FileMagic fm = FileMagic.valueOf(is);
            System.out.println(fm);
            if (fm == FileMagic.OLE2) {
                WordExtractor ex = new WordExtractor(is);
                String text2003 = ex.getText();
                System.out.println(text2003);
            }else {
                XWPFDocument doc = ReadDoc2007.read2007(filePath,getParams());
                writeDoc(doc,"C:\\Users\\liufei\\Desktop\\人事\\word2007.docx");
                ReadDoc2007.Word2007ToHtml("C:\\Users\\liufei\\Desktop\\人事\\","word2007",".docx","C:\\Users\\liufei\\Desktop\\人事\\");
            }
            System.out.println("success");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeDoc(XWPFDocument doc,String filePath){
        FileOutputStream out;
        try {
            out = new FileOutputStream(filePath);
            doc.write(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String,Object> getParams(){
        Map<String,Object> params = new HashMap<>();
        params.put("${人员编号}","编号测试");
        params.put("${人员姓名}","姓名测试");
        params.put("${职务}","职务测试");
        return params;
    }

    public static void read2003(String filePath) throws Exception{
        InputStream is = new FileInputStream(filePath);
        HWPFDocument doc = new HWPFDocument(is);
        //输出书签信息
        printInfo(doc.getBookmarks());
        //输出文本
        System.out.println(doc.getDocumentText());
        Range range = doc.getRange();
        printInfo(range);
        //读表格
        readTable(range);
        //读列表
        readList(range);
        //把当前HWPFDocument写到输出流中
        doc.write(new FileOutputStream("D:\\test.doc"));
        is.close();
    }

    /**
     * 输出书签信息
     * @param bookmarks
     */
    private static void printInfo(Bookmarks bookmarks) {
        int count = bookmarks.getBookmarksCount();
        System.out.println("书签数量：" + count);
        Bookmark bookmark;
        for (int i=0; i<count; i++) {
            bookmark = bookmarks.getBookmark(i);
            System.out.println("书签" + (i+1) + "的名称是：" + bookmark.getName());
            System.out.println("开始位置：" + bookmark.getStart());
            System.out.println("结束位置：" + bookmark.getEnd());
        }
    }

    /**
     * 读表格
     * 每一个回车符代表一个段落，所以对于表格而言，每一个单元格至少包含一个段落，每行结束都是一个段落。
     * @param range
     */
    private static void readTable(Range range) {
        //遍历range范围内的table。
        TableIterator tableIter = new TableIterator(range);
        Table table;
        TableRow row;
        TableCell cell;
        while (tableIter.hasNext()) {
            table = tableIter.next();
            int rowNum = table.numRows();
            for (int j=0; j<rowNum; j++) {
                row = table.getRow(j);
                int cellNum = row.numCells();
                for (int k=0; k<cellNum; k++) {
                    cell = row.getCell(k);
                    //输出单元格的文本
                    System.out.println(cell.text().trim());
                }
            }
        }
    }

    /**
     * 读列表
     * @param range
     */
    private static void readList(Range range) {
        int num = range.numParagraphs();
        Paragraph para;
        for (int i=0; i<num; i++) {
            para = range.getParagraph(i);
            if (para.isInList()) {
                System.out.println("list: " + para.text());
            }
        }
    }

    /**
     * 输出Range
     * @param range
     */
    private static void printInfo(Range range) {
        //获取段落数
        int paraNum = range.numParagraphs();
        System.out.println(paraNum);
        for (int i=0; i<paraNum; i++) {
            System.out.println("段落" + (i+1) + "：" + range.getParagraph(i).text());
        }
        int secNum = range.numSections();
        System.out.println(secNum);
        Section section;
        for (int i=0; i<secNum; i++) {
            section = range.getSection(i);
            System.out.println(section.getMarginLeft());
            System.out.println(section.getMarginRight());
            System.out.println(section.getMarginTop());
            System.out.println(section.getMarginBottom());
            System.out.println(section.getPageHeight());
            System.out.println(section.text());
        }
    }
}
