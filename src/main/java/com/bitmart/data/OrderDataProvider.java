package com.bitmart.data;

import org.testng.annotations.DataProvider;

/**
 * @author admin
 * @Desc 获取用户委托列表
 * @datetime 2022/5/31 4:55 PM
 */
public class EntrustListDataProvider {
    @DataProvider
    public static Object[][] getEntrustList() {
        int current = 1;
        String tradeMappingId = "";
        String state = "6,8";
        int orderMode = 1;
        return new Object[][]{
                {current, tradeMappingId, state, orderMode}
        };
    }
}
