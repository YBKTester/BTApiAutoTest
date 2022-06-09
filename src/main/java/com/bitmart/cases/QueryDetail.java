package com.bitmart.cases;

import com.bitmart.base.BaseTest;
import com.bitmart.utils.HttpUtils;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static com.bitmart.common.ApiPathConstants.*;
import static com.bitmart.utils.SignUtils.*;

/**
 * @author admin
 * @Desc 订单相关测试场景
 * @datetime 2022/6/1 11:56 AM
 */
public class OrderTest extends BaseTest {
    /**
     * 获取认证信息
     */
    @Test
    public void getKYCInfoTest() {
        Response response = HttpUtils.get(DOMAIN + KYC_INFO_SIMPLE, headers, null);
        response.prettyPrint();
        String currencySymbol = getValueByJsonPath(response, "msg");
        Assert.assertEquals(currencySymbol, "Success", "接口请求不成功！");
    }

    /**
     * 设置禁止国家
     */
    @Test(dependsOnMethods = "getKYCInfoTest")
    public void setForbiddenCountryTest() {
        Response response = HttpUtils.get(DOMAIN + SETTING_FORBIDDEN_COUNTRY, headers, null);
        response.prettyPrint();
        String currencySymbol = getValueByJsonPath(response, "msg");
        Assert.assertEquals(currencySymbol, "Success", "接口请求不成功！");
    }

    /**
     * 下单
     */
    @Test(dependsOnMethods = "setForbiddenCountryTest")
    public void queryMarketKLineTest() {
        int reqNo = getRandom();
        System.out.println("随机数为： " + reqNo);
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("reqSource", "2");
        paramsMap.put("symbol", "52");
        paramsMap.put("reqNo", reqNo);
        paramsMap.put("side", "buy");
        paramsMap.put("type", "ioc");
        paramsMap.put("tradeAmount", "1");
        paramsMap.put("timestamp", System.currentTimeMillis());
        paramsMap.put("orderModeCode", "normal_order");
        paramsMap.put("subOrderModeCode", "normal_order");
        paramsMap.put("tradePrice", "50000");

        String sign = stringToMD5(getSortUrl(paramsMap, salt));
        paramsMap.put("sign", sign);

        Response response = HttpUtils.post(DOMAIN + SPOT_ORDER_SUBMIT, headers, paramsMap);
        response.prettyPrint();
        String currencySymbol = getValueByJsonPath(response, "msg");
        Assert.assertEquals(currencySymbol, "Success", "接口请求不成功！");
        String orderId = getValueByJsonPath(response, "data.orderId");
        System.out.println("orderId : " + orderId);
    }
}
