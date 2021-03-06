package com.zhiyicx.thinksnsplus.data.beans.integration;

import java.io.Serializable;

/**
 * @Describe 糖果  doc {https://slimkit.github.io/docs/api-v2-users.html}
 * @Author zl
 * @Date 2018/1/22
 * @Contact master.jungle68@gmail.com
 */
public class IntegrationBean implements Serializable {

    private static final long serialVersionUID = 6734690561948710212L;
    /**
     * owner_id : 1
     * type : 1
     * sum : 9400
     * created_at : 2018-01-17 06:57:18
     * updated_at : 2018-01-18 06:57:24
     */

    private int owner_id;
    private int type;
    private long sum;
    private long red_candy;
    private long candy_total;
    private String created_at;
    private String updated_at;
    private boolean is_sweet;
    private int today_total;

    public long getRed_candy() {
        return red_candy;
    }

    public void setRed_candy(long red_candy) {
        this.red_candy = red_candy;
    }

    public long getCandy_total() {
        return candy_total;
    }

    public void setCandy_total(long candy_total) {
        this.candy_total = candy_total;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getSum() {
        return sum;
    }

    public void setSum(long sum) {
        this.sum = sum;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public boolean isIs_sweet() {
        return is_sweet;
    }

    public void setIs_sweet(boolean is_sweet) {
        this.is_sweet = is_sweet;
    }

    public int getToday_total() {
        return today_total;
    }

    public void setToday_total(int today_total) {
        this.today_total = today_total;
    }

    @Override
    public String toString() {
        return "IntegrationBean{" +
                "owner_id=" + owner_id +
                ", type=" + type +
                ", sum=" + sum +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", is_sweet=" + is_sweet +
                ", today_total=" + today_total +
                ", candy_total=" + candy_total +
                ", red_candy=" + red_candy +
                '}';
    }
}
