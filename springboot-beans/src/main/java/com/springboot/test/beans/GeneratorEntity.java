package com.springboot.test.beans;

/**
 * 自动生成测试类Bean
 * @author liufei
 * @date 2019/5/27 16:53
 */
public class GeneratorEntity {
    private Long id;
    private String name;
    private String password;
    private Integer sex;//0：男，1：女
    private boolean deleted;//是否删除，0：未删除，1：已删除
    private Double score;//分值

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
