package com.springboot.test.web.controller.excel;

import com.springboot.test.beans.ExcelData;
import com.springboot.test.util.excel.ExportExcelUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liufei
 * @date 2019/3/21 15:36
 */
@RequestMapping(value = "excel")
@Controller
public class ExcelController {

    @RequestMapping(value = "export")
    public void export(HttpServletResponse response) throws Exception {
        ExcelData data = new ExcelData();
        data.setName("hello");
        List<String> titles = new ArrayList();
        for(int i=1;i<100;i++){
            if (i < 10) {
                titles.add("第1行0" + i + "列");
            } else {
                titles.add("第1行" + i + "列");
            }
        }
        data.setTitles(titles);

        List<List<Object>> rows = new ArrayList();
        for(int i=1;i<3000;i++){
            List<Object> row = new ArrayList();
            for(int j=1;j<100;j++){
                if (j < 10) {
                    row.add("第"+i+"行0" + j + "列");
                } else {
                    row.add("第"+i+"行" + j + "列");
                }
            }
            rows.add(row);
        }
        data.setRows(rows);
        ExportExcelUtils.exportExcel(response,"hello.xlsx",data);
    }
}
