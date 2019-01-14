package com.springboot.test.beans;

import com.springboot.test.beans.enums.JPushCategoryEnum;

/**
 * 极光推送信息包装类
 * @author liufei
 * @date 2019/1/11 15:56
 */
public class JPushInfo {
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;

    /**
     * 推送设备ios：苹果，android：安卓，winphone:windows，all：所有
     */
    private JPushCategoryEnum category;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public JPushCategoryEnum getCategory() {
        return category;
    }

    public void setCategory(JPushCategoryEnum category) {
        this.category = category;
    }
}
