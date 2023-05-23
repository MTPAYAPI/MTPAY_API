package com.test.pay;


import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 法币出金
 */
public class WithdrawalFiat {


    public static void main(String[] args) {
//        withdrawal();
//        withdrawalHistory();
//        withdrawalDelete();
//        withdrawalRate("CNY");
        withdrawalOrderQuery("TEST1618221736");
    }

    /**
     * 申请出金
     * @return
     */
    public static String withdrawal(){
        //curl -X POST -H "content-type:application/json" -H "access_key:3A1CDB61128A3E693F0F3D7884F226E82E253650" -H "app_id:41043343" -d '{"bankNumber":"535224124242","bankCode":"","amount":1000.0,"iBank":"344244","accountName":"测试","swiftCode":"","bankName":"中国银行","type":"1","bankAddress":"","fiatCurrency":"CNY","customerId":"110","subbranch":"北京西二旗分行","account":"345345245","remarks":"","merchantNo":"9357"}' https://merchant-api.mtpay.io/api/recharge/customer/withdrawal?timestamp=1609747081
        long timestamp = System.currentTimeMillis()/1000;
        String pay_url = Constant.URL_ + "api/recharge/customer/withdrawal?timestamp="+timestamp;
        Map<String, String> heads = new HashMap<>();
        heads.put("content-type", "application/json");
        String params = timestamp+"";
        String access_key = HMACSHA1.hamcsha1(params.getBytes(), Constant.key.getBytes());
        heads.put("access_key", access_key);
        heads.put("app_id", Constant.app_id);
        Map<String, Object> param = new HashMap<>();
        param.put("merchantNo",Constant.merchant_no);//商户号
        param.put("merchantOrderNo","TEST"+timestamp);//商户订单号
        param.put("withdrawType",2);//出金类型 1 固定法币出金 2 固定USDT数量出金
        param.put("fiatCurrency","CNY");   //出金法币类型
//        param.put("amount",1000.0);  //出金法币数量
        param.put("currencyCount",500.0);  //出金USDT数量
        param.put("customerId","110"); //用户标识 用于查询历史的查询条件
        param.put("accountName","测试"); //银行账户姓名
        param.put("bankNumber","535224124242");//银行卡号
        param.put("account","345345245");           //银行账号
        param.put("bankName","中国银行");          //银行名称
        param.put("subbranch","北京西二旗分行");             //支行
        param.put("iBank","344244");               //IBANK
        param.put("bankAddress","");             //银行地址
        param.put("swiftCode","");              //Swift Code
        param.put("bankCode","");               //银行代码
        param.put("remarks","");                //备注
        param.put("type","1");                  //类型  1 正常  2 加急
        String return_url = HttpUtils.postJson(pay_url,param, heads);
        System.out.println(return_url);
        //{"code":200,"data":{"account":"345345245","accountName":"测试","amount":null,"bankAddress":"","bankCode":"","bankName":"中国银行","bankNumber":"535224124242","comeFrom":2,"createTime":null,"currencyCount":500.0,"currencyTypeId":null,"customerId":"110","fee":null,"fiatCurrency":"CNY","financialReviewTime":null,"iBank":"344244","id":null,"investorPassword":null,"merchantNo":"9357","merchantOrderNo":"TEST1618221736","orderNo":null,"refuseReason":null,"remarks":"","state":null,"subbranch":"北京西二旗分行","swiftCode":"","traderId":null,"traderReviewTime":null,"type":1,"userId":null,"withdrawType":2,"withdrawalRate":null},"message":"success"}
        JSONObject jsonObject = JSONObject.parseObject(return_url);
        if (jsonObject.getInteger("code") == 200){
            System.out.println("success");
            return "success";
        }else {
            System.out.println(jsonObject.getString("message"));
            return "failure";
        }
    }

    /**
     * 出金历史记录
     * @return
     */
    public static String withdrawalHistory(){
        long timestamp = System.currentTimeMillis()/1000;
        String pay_url = Constant.URL_ + "api/recharge/customer/withdrawal/history?timestamp="+timestamp;
        Map<String, String> heads = new HashMap<>();
        heads.put("content-type", "application/json");
        String params = Constant.merchant_no + "&" + timestamp;
        String access_key = HMACSHA1.hamcsha1(params.getBytes(), Constant.key.getBytes());
        heads.put("access_key", access_key);
        heads.put("app_id", Constant.app_id);
        Map<String, Object> param = new HashMap<>();
        //String merchantNo,String customerId,Date startTime,Date endTime,String fiatCurrency,Integer state,Integer type,String accountName
        param.put("merchantNo",Constant.merchant_no);//商户号(必传)
        //分页
        param.put("currPage",1);//当前页(必传)
        param.put("pageSize",20);//每页多少条数据(必传)
//        //搜索条件，不传时为所有记录
        param.put("merchantOrderNo","TEST1618221736"); //商户订单号
//        param.put("customerId","110"); //用户标识(可选)
//        param.put("fiatCurrency","CNY");   //出金法币类型（可选）
//        param.put("accountName","测试"); //银行账户姓名（可选）
//        param.put("state",1);//状态： 0 未审核 1 审核通过 2 已放款  -1 审核拒绝 -2 放款失败   (可选)
//        param.put("type",1);           // 类型 1正常 2 加急 (可选)
        for (Map.Entry<String, Object> entry : param.entrySet()) {
            pay_url = pay_url + "&" +entry.getKey() + "=" + entry.getValue();
        }
        System.out.println(pay_url);
        String return_url = HttpUtils.get(pay_url, heads);
        System.out.println(return_url);
        //{"total":1,"code":200,"data":[{"account":"345345245","accountName":"测试","amount":null,"bankAddress":"","bankCode":"","bankName":"中国银行","bankNumber":"535224124242","comeFrom":2,"createTime":"2021-04-12 18:02:17","currencyCount":500.0,"currencyTypeId":1,"customerId":"110","fee":null,"fiatCurrency":"CNY","financialReviewTime":null,"iBank":"344244","id":907,"investorPassword":null,"merchantNo":null,"merchantOrderNo":"TEST1618221736","orderNo":"WF1618221737454","refuseReason":null,"remarks":"","state":0,"subbranch":"北京西二旗分行","swiftCode":"","traderId":null,"traderReviewTime":null,"type":1,"userId":490,"withdrawType":2,"withdrawalRate":null}],"pageSize":20,"message":"success","currentPage":1}
        return return_url;
    }

    /**
     * 删除未审核的出金记录(只能删除state=0的申请记录)
     * @return
     */
    public static String withdrawalDelete(){
        long timestamp = System.currentTimeMillis()/1000;
        String pay_url = Constant.URL_ + "api/recharge/customer/withdrawal/delete?timestamp="+timestamp;
        Map<String, String> heads = new HashMap<>();
        heads.put("content-type", "application/json");
        String params = Constant.merchant_no + "&" + timestamp;
        String access_key = HMACSHA1.hamcsha1(params.getBytes(), Constant.key.getBytes());
        heads.put("access_key", access_key);
        heads.put("app_id", Constant.app_id);
        Map<String, Object> param = new HashMap<>();
        param.put("merchantNo",Constant.merchant_no);//商户号(必传)
        param.put("orderNo","WF1608866506958");//要删除出金订单的订单号(必传)

        for (Map.Entry<String, Object> entry : param.entrySet()) {
            pay_url = pay_url + "&" +entry.getKey() + "=" + entry.getValue();
        }
        System.out.println(pay_url);
        String return_url = HttpUtils.get(pay_url, heads);
        System.out.println(return_url);
        //{"code":200,"message":"success"}
        return return_url;
    }


    /**
     * 获取出金汇率（估值）
     * @param fiatCurrency 法币类型
     * @return
     */
    public static String withdrawalRate(String fiatCurrency){
        long timestamp = System.currentTimeMillis()/1000;
        String pay_url = Constant.URL_ + "/api/recharge/customer/withdrawal/rate?timestamp="+timestamp;
        Map<String, String> heads = new HashMap<>();
        heads.put("content-type", "application/json");
        String params = fiatCurrency + "&" + timestamp;
        String access_key = HMACSHA1.hamcsha1(params.getBytes(), Constant.key.getBytes());
        heads.put("access_key", access_key);
        heads.put("app_id", Constant.app_id);
        Map<String, Object> param = new HashMap<>();
        param.put("fiatCurrency",fiatCurrency);//法币类型(必传)

        for (Map.Entry<String, Object> entry : param.entrySet()) {
            pay_url = pay_url + "&" +entry.getKey() + "=" + entry.getValue();
        }
        System.out.println(pay_url);
        String return_url = HttpUtils.get(pay_url, heads);
        System.out.println(return_url);
        //{"code":200,"data":{"fiatCurrency":"CNY","withdrawalRate":6.69},"message":"success"}
        return return_url;
    }

    /**
     * 根据商户订单号查询出金订单
     * @param merchantOrderNo 商户出金订单号
     * @return
     */
    public static String withdrawalOrderQuery(String merchantOrderNo){
        long timestamp = System.currentTimeMillis()/1000;
        String pay_url = Constant.URL_ + "/api/recharge/customer/withdrawal/order/query?timestamp="+timestamp;
        Map<String, String> heads = new HashMap<>();
        heads.put("content-type", "application/json");
        String params = Constant.merchant_no +"&" + merchantOrderNo + "&" + timestamp;
        String access_key = HMACSHA1.hamcsha1(params.getBytes(), Constant.key.getBytes());
        heads.put("access_key", access_key);
        heads.put("app_id", Constant.app_id);
        Map<String, Object> param = new HashMap<>();
        param.put("merchantNo",Constant.merchant_no);//商户号(必传)
        param.put("merchantOrderNo",merchantOrderNo);//商户出金订单号(必传)
        for (Map.Entry<String, Object> entry : param.entrySet()) {
            pay_url = pay_url + "&" +entry.getKey() + "=" + entry.getValue();
        }
        System.out.println(pay_url);
        String return_url = HttpUtils.get(pay_url, heads);
        System.out.println(return_url);
        //{"code":200,"data":{"account":"345345245","accountName":"测试","amount":null,"bankAddress":"","bankCode":"","bankName":"中国银行","bankNumber":"535224124242","comeFrom":2,"createTime":"2021-04-12 18:02:17","currencyCount":500.0,"currencyTypeId":1,"customerId":"110","fee":null,"fiatCurrency":"CNY","financialReviewTime":null,"iBank":"344244","id":907,"investorPassword":null,"merchantNo":null,"merchantOrderNo":"TEST1618221736","orderNo":"WF1618221737454","refuseReason":null,"remarks":"","state":0,"subbranch":"北京西二旗分行","swiftCode":"","traderId":null,"traderReviewTime":null,"type":1,"userId":490,"withdrawType":2,"withdrawalRate":null},"message":"success"}
        return return_url;
    }
}
