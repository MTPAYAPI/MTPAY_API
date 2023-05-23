package com.test.pay;

/**
 * @Author wanglele
 * @Date 2020/12/30 14:41
 * @Version 1.0
 */
public class Constant {
    public final static String URL_ = "https://merchant-api.mtpay.io/"; // 域名
    public final static String key = "9B26EA1A57AEDAB6B65AC9ADE4660D0C"; // HMAC_SHA1加密算法的key(密钥) <由 MTPAY 分配,在app-API配置(pc商户后台--账户中心--API安全--密钥) 中获取>
    public final static String app_id = "94090357"; // 商户帐号ID(APPID) <由 MTPAY 分配,在app-API配置(pc在商户后台--账户中心--商户信息) 中获取>
    public final static String merchant_no = "3555"; //商户号(UID) <由 MTPAY 分配,在app-API配置(pc商户后台--账户中心--商户信息) 中获取>

}
