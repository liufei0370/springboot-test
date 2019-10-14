package com.springboot.test.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liufei
 * @date 2019/1/2 14:43
 */
public class BaseController {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected HttpServletRequest request;

    protected Integer getPageNum() {
        Integer pageNumber = 1;
        try {
            pageNumber = Integer.valueOf(request.getParameter("pageNumber"));
            if (pageNumber == null) {
                pageNumber = 1;
            }
        } catch (Exception e) {
            logger.error("pageNumber参数传值异常", e);
        }
        return pageNumber;
    }

    /*@InitBinder
    public void initBinder(ServletRequestDataBinder binder) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));

    }*/
}
