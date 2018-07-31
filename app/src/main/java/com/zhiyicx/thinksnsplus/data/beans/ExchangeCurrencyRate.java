package com.zhiyicx.thinksnsplus.data.beans;

/**
 * author: huwenyong
 * date: 2018/7/31 16:52
 * description:
 * version:
 */

public class ExchangeCurrencyRate {

    private String currency;
    private String currency_exchange;

    private String exchange_rate;
    private double balance;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrency_exchange() {
        return currency_exchange;
    }

    public void setCurrency_exchange(String currency_exchange) {
        this.currency_exchange = currency_exchange;
    }

    public String getExchange_rate() {
        return exchange_rate;
    }

    public void setExchange_rate(String exchange_rate) {
        this.exchange_rate = exchange_rate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
