package com.bitmart.cases;

import com.bitmart.base.BaseTest;
import com.bitmart.data.OrderDataProvider;
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
    private String ORDER_ID;

    /**
     * 下单
     */
    @Test(dataProvider = "orderData", dataProviderClass = OrderDataProvider.class)
    public void submitOrderTest(String symbolId, String side, String type,
                                double tradeAmount, double tradePrice) {
        int reqNo = getRandom();
        System.out.println("随机数为： " + reqNo);
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("reqSource", "2");
        paramsMap.put("symbol", symbolId);
        paramsMap.put("reqNo", reqNo);
        paramsMap.put("side", side);
        paramsMap.put("type", type);
        paramsMap.put("tradeAmount", tradeAmount);
        paramsMap.put("timestamp", System.currentTimeMillis());
        paramsMap.put("orderModeCode", "normal_order");
        paramsMap.put("subOrderModeCode", "normal_order");
        paramsMap.put("tradePrice", tradePrice);

        String sign = stringToMD5(getSortUrl(paramsMap, saltSell));
        paramsMap.put("sign", sign);

        Response response = HttpUtils.post(DOMAIN + SPOT_ORDER_SUBMIT, headersSell, paramsMap);
        response.prettyPrint();
        String currencySymbol = getValueByJsonPath(response, "msg");
        Assert.assertEquals(currencySymbol, "Success", "接口请求不成功！");
        ORDER_ID = getValueByJsonPath(response, "data.orderId");
        System.out.println("orderId : " + ORDER_ID);
    }

    @Test(dataProvider = "getAllMarket", dataProviderClass = OrderDataProvider.class)
    public void getAllMarket(String tradeMappingName) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("tradeMappingName", tradeMappingName);
        Response response = HttpUtils.getByQueryParams(MARKET_DEPTH_ALL, headersSell, paramsMap);
        response.prettyPrint();
        String msg = getValueByJsonPath(response, "msg");
        Assert.assertEquals(msg, "成功", "接口请求不成功！");
        String buysPriceList = getValueByJsonPath(response, "data.buys.price");
        String sellsPriceList = getValueByJsonPath(response, "data.sells.price");

        System.out.println(buysPriceList);
        System.out.println(sellsPriceList);
        if (buysPriceList.equals("[]")) {
            System.out.println("当前无可用买方深度！");
        } else {
            String buyMaxPrice = getValueByJsonPath(response, "data.buys[0].price");
            System.out.println("该交易对最高买单价为 : " + buyMaxPrice);
        }
        if (sellsPriceList.equals("[]")) {
            System.out.println("当前无可用卖方深度！");
        } else {
            String sellMinPrice = getValueByJsonPath(response, "data.sells[0].price");
            System.out.println("该交易对最低卖单价为 : " + sellMinPrice);
        }
    }


    /**
     * 单个撤单
     *
     * @param orderId       订单编号
     * @param reqSource     编号
     * @param timestamp     时间
     * @param orderModeCode 下单模式
     *                      6555729882757644000
     */
    @Test(dependsOnMethods = "submitOrderTest", dataProvider = "cancelOrderData", dataProviderClass = OrderDataProvider.class)
    public void cancelOrderTest(String orderId, String reqSource, long timestamp,
                                String orderModeCode) {
        Map<String, Object> paramsMap = new HashMap<>();
        if (orderId.equals("0")) {
            paramsMap.put("orderId", ORDER_ID);
        } else {
            paramsMap.put("orderId", orderId);
        }
        if (reqSource != null) {
            paramsMap.put("reqSource", reqSource);
        } else {
            paramsMap.put("reqSource", null);
        }
        if (timestamp == 0) {
            paramsMap.put("timestamp", null);
        } else {
            paramsMap.put("timestamp", timestamp);
        }

        paramsMap.put("orderModeCode", orderModeCode);
        String sign = stringToMD5(getSortUrl(paramsMap, saltSell));
        paramsMap.put("sign", sign);

        Response response = HttpUtils.post(DOMAIN + BATCH_CANCEL, headersSell, paramsMap);
        response.prettyPrint();
        String currencySymbol = getValueByJsonPath(response, "msg");
        Assert.assertEquals(currencySymbol, "Success", "接口请求不成功！");
    }

    /**
     * 批量撤单
     *
     * @param reqSource     编号
     * @param orderModeCode 下单模式
     *                      dependsOnMethods = "submitOrderTest",
     */
    @Test(dataProvider = "batchCancelOrderData", dataProviderClass = OrderDataProvider.class)
    public void batchCancelOrderTest(String reqSource,
                                     String orderModeCode) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("reqSource", reqSource);
        paramsMap.put("orderModeCode", orderModeCode);

        Response response = HttpUtils.post(DOMAIN + BATCH_CANCEL, headersSell, paramsMap);
        response.prettyPrint();
        String currencySymbol = getValueByJsonPath(response, "msg");
        Assert.assertEquals(currencySymbol, "Success", "接口请求不成功！");
    }
}
