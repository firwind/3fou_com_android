package com.zhiyicx.thinksnsplus.data.beans.request;

/**
 * @Describe
 * @Author zl
 * @Date 2017/8/22
 * @Contact master.jungle68@gmail.com
 */
public class BindAccountRequstBean {
    private String access_token;
    private String login;
    private String password;
    private String phone;
    private String verifiable_code;
    private String user_code;
    private String verifiable_type;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BindAccountRequstBean(String access_token, String login, String password) {
        this.access_token = access_token;
        this.login = login;
        this.password = password;
    }

    public BindAccountRequstBean(String access_token, String login, String password, String phone, String verifiable_code, String verifiable_type, String user_code, String name) {
        this.access_token = access_token;
        this.login = login;
        this.password = password;
        this.phone = phone;
        this.verifiable_code = verifiable_code;
        this.user_code = user_code;
        this.verifiable_type = verifiable_type;
        this.name = name;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVerifiable_code() {
        return verifiable_code;
    }

    public void setVerifiable_code(String verifiable_code) {
        this.verifiable_code = verifiable_code;
    }

    public String getUser_code() {
        return user_code;
    }

    public void setUser_code(String user_code) {
        this.user_code = user_code;
    }

    public String getVerifiable_type() {
        return verifiable_type;
    }

    public void setVerifiable_type(String verifiable_type) {
        this.verifiable_type = verifiable_type;
    }
}
