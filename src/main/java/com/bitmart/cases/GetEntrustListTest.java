package com.bitmart.dto;

import com.bitmart.base.BaseTest;
import com.bitmart.data.EntrustListDataProvider;
import com.bitmart.utils.HttpUtils;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static com.bitmart.common.ApiPathConstants.*;
import static com.bitmart.utils.SignUtils.getSortUrl;
import static com.bitmart.utils.SignUtils.stringToMD5;


/**
 * @author Peanut
 * @Desc 获取用户委托列表
 * @DateTime 2022/5/31 11:39 AM
 */
@Slf4j
public class GetEntrustListTest extends BaseTest {
    /**
     * 获取历史委托
     */
    @Test(dataProvider = "getEntrustList", dataProviderClass = EntrustListDataProvider.class)
    public void getEntrustListTest(int current, String tradeMappingId, String state, int orderMode) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("local", "zh_CN");
        paramsMap.put("timestamp", System.currentTimeMillis());
        paramsMap.put("limit", 5);
        paramsMap.put("current", current);
        paramsMap.put("tradeMappingId", tradeMappingId);
        paramsMap.put("state", state);
        paramsMap.put("orderMode", orderMode);

        String sign = stringToMD5(getSortUrl(paramsMap, salt));
        paramsMap.put("sign", sign);

        Response response = HttpUtils.post(DOMAIN + ENTRUST_LIST, headers, paramsMap);
        response.prettyPrint();
        String userId = getValueByJsonPath(response, "data[0].userId");
        Assert.assertEquals(userId, USERID_BUY, "用户编号不是预期值！");
    }

    /**
     * 获取钱包详情
     */
    @Test(dependsOnMethods = "getEntrustListTest")
    public void getBalanceTest() {

        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("local", "zh_CN");
        paramsMap.put("timestamp", System.currentTimeMillis());

        String sign = stringToMD5(getSortUrl(paramsMap, salt));
        paramsMap.put("sign", sign);

        Response response = HttpUtils.get(DOMAIN + GET_BALANCE, headers, paramsMap);
        response.prettyPrint();
        String currencySymbol = getValueByJsonPath(response, "data.currencySymbol");
        Assert.assertEquals(currencySymbol, "CNY", "币种不是预期值！");
    }

    /**
     * 获取委托详情
     */
    @Test(dependsOnMethods = "getBalanceTest")
    public void getEntrustDetailListTest() {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("local", "zh_CN");
        paramsMap.put("timestamp", System.currentTimeMillis());
        paramsMap.put("limit", 5);
        paramsMap.put("current", 1);
        paramsMap.put("tradeMappingId", 53);
        paramsMap.put("side", "");
        paramsMap.put("orderMode", 1);

        String sign = stringToMD5(getSortUrl(paramsMap, salt));
        paramsMap.put("sign", sign);

        Response response = HttpUtils.post(DOMAIN + ENTRUST_DETAIL_LIST, headers, paramsMap);
        response.prettyPrint();
        String currencySymbol = getValueByJsonPath(response, "msg");
        Assert.assertEquals(currencySymbol, "Success", "接口请求不成功！");
    }

    /**
     * 获取用户费率
     */
    @Test(dependsOnMethods = "getBalanceTest")
    public void getUserFeeRateTest() {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("timestamp", System.currentTimeMillis());
        paramsMap.put("symbolId", 53);

        String sign = stringToMD5(getSortUrl(paramsMap, salt));
        paramsMap.put("sign", sign);

        Response response = HttpUtils.get(DOMAIN + QUERY_USER_SPOT_TRADE_FEE_RATE_WEB, headers, paramsMap);
        response.prettyPrint();
        String currencySymbol = getValueByJsonPath(response, "msg");
        Assert.assertEquals(currencySymbol, "Success", "接口请求不成功！");
        String takerRate = getValueByJsonPath(response, "data.takerRate");
        String makerRate = getValueByJsonPath(response, "data.makerRate");
        String level = getValueByJsonPath(response, "data.level");
        System.out.println("takerRate :" + takerRate);
        System.out.println("makerRate :" + makerRate);
        System.out.println("level :" + level);
    }

    /**
     * 获取用户手续费率
     */
    @Test(dependsOnMethods = "getUserFeeRateTest")
    public void queryUserFeeRateTest() {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("timestamp", System.currentTimeMillis());
        paramsMap.put("symbolId", 53);

        String sign = stringToMD5(getSortUrl(paramsMap, salt));
        paramsMap.put("sign", sign);

        Response response = HttpUtils.get(DOMAIN + QUERY_USER_FEE, headers, paramsMap);
        response.prettyPrint();
        String currencySymbol = getValueByJsonPath(response, "msg");
        Assert.assertEquals(currencySymbol, "Success", "接口请求不成功！");
        String takerRate = getValueByJsonPath(response, "data.takerRate");
        String makerRate = getValueByJsonPath(response, "data.makerRate");

        System.out.println("takerRate :" + takerRate);
        System.out.println("makerRate :" + makerRate);
    }

    /**
     * 查询交易对记录
     * tradeMappingName: BTC-USDT
     * maxSize: 50
     */
    @Test(dependsOnMethods = "queryUserFeeRateTest")
    public void queryMarketTradeTest() {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("tradeMappingName", "BMX-USDT");
        paramsMap.put("maxSize", "1");

        Response response = HttpUtils.get(DOMAIN + MARKET_TRADE, headers, paramsMap);
        response.prettyPrint();
        String currencySymbol = getValueByJsonPath(response, "msg");
        Assert.assertEquals(currencySymbol, "成功", "接口请求不成功！");
        String amount = getValueByJsonPath(response, "data[0].amount");
        System.out.println("amount :" + amount);
    }

    @Test
//            (dependsOnMethods = "queryMarketTradeTest")
    public void queryMarketTradeMappingTest() {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("local", "en_US");

        Response response = HttpUtils.get(DOMAIN + MARKET_TRADE_MAPPING_FRONT, headers, paramsMap);
        response.prettyPrint();
        String currencySymbol = getValueByJsonPath(response, "msg");
        Assert.assertEquals(currencySymbol, "Success", "接口请求不成功！");

    }

    /**
     * 查询用户信息
     */
    @Test
//            (dependsOnMethods = "queryMarketTradeMappingTest")
    public void queryUserInfoTest() {
        Response response = HttpUtils.get(DOMAIN + USER_INFO, headers, null);
        response.prettyPrint();
        String currencySymbol = getValueByJsonPath(response, "msg");
        Assert.assertEquals(currencySymbol, "Success", "接口请求不成功！");
    }

    @Test
//            (dependsOnMethods = "queryMarketTradeMappingTest")
    public void queryFinanceSymbolWalletTest() {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("local", "zh_CN");
        paramsMap.put("symbol", "53");

        Response response = HttpUtils.getByQueryParams(DOMAIN + FINANCE_SYMBOL_WALLET, headers, paramsMap);
        response.prettyPrint();
        String currencySymbol = getValueByJsonPath(response, "msg");
        Assert.assertEquals(currencySymbol, "Success", "接口请求不成功！");
        String walletVO1 = getValueByJsonPath(response, "data.walletVO1.totalBalance");
        String walletVO2 = getValueByJsonPath(response, "data.walletVO2.totalBalance");

        System.out.println("walletVO1 : " + walletVO1);
        System.out.println("walletVO1 : " + walletVO2);
    }

    /**
     * symbol: 53
     * step: 15
     * from: 1653601568
     * to: 1654051568
     */
    @Test
//            (dependsOnMethods = "queryMarketTradeMappingTest")
    public void queryMarketKLineTest() {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("step", 15);
        paramsMap.put("symbol", 53);
        paramsMap.put("from", 1653601568);
        paramsMap.put("to", 1654051568);

        Response response = HttpUtils.getByQueryParams(DOMAIN + MARKET_KLINE, headers, paramsMap);
        response.prettyPrint();
        String currencySymbol = getValueByJsonPath(response, "msg");
        Assert.assertEquals(currencySymbol, "成功", "接口请求不成功！");

    }
}
