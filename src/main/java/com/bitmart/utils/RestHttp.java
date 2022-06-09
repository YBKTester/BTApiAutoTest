package com.bitmart.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

/**
 * @Desc Rest请求方法类
 * @Author admin
 * @DateTime 2022/5/26  4:49 下午
 */
public class RestHttp {

    public static Map<String, Object> paramsMap = new HashMap<>();

    /**
     * 清空参数集方法
     */
    public static void clear() {
        paramsMap.clear();
    }

    private static final String CONTENT_TYPE_JSON = "application/json; charset=UTF-8";
    private static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded; charset=UTF-8";

    /**
     * 1、Post请求方法
     * 断言String类型关键字方法
     *
     * @param url          请求地址
     * @param paramsMap    参数
     * @param assertPath   要断言的关键字路径，如：data.Item.orderId
     * @param expectedWord 预期关键字
     */
    public static void postEquals(String url, int statusCode,
                                  Map<String, Object> paramsMap,
                                  String assertPath, String expectedWord) {

        given().
                contentType(CONTENT_TYPE_FORM).
                params(paramsMap).
                relaxedHTTPSValidation().
                when().
                post(url.trim()).

                then().
                log().ifError().
                assertThat().statusCode(statusCode).

                and().
                body(assertPath, equalTo(expectedWord));
    }

    /**
     * 2、Post请求方法
     * 断言Int类型关键字方法
     *
     * @param url
     * @param statusCode
     * @param paramsMap
     * @param assertPath
     * @param expectedWord
     */
    public static void postEquals(String url, int statusCode,
                                  Map<String, Object> paramsMap,
                                  String assertPath, int expectedWord) {

        given().
                contentType(CONTENT_TYPE_FORM).
                params(paramsMap).
                relaxedHTTPSValidation().
                when().
                post(url.trim()).

                then().
                log().ifError().
                assertThat().statusCode(statusCode).

                and().
                body(assertPath, equalTo(expectedWord));
    }

    /**
     * 3、Post请求方法
     * 断言boolean类型关键字方法
     *
     * @param url
     * @param statusCode
     * @param paramsMap
     * @param assertPath
     * @param expectedWord
     */
    public static void postEquals(String url, int statusCode,
                                  Map<String, Object> paramsMap,
                                  String assertPath, boolean expectedWord) {

        given().
                contentType(CONTENT_TYPE_FORM).
                params(paramsMap).
                relaxedHTTPSValidation().
                when().
                post(url.trim()).

                then().
                log().ifError().
                assertThat().statusCode(statusCode).

                and().
                body(assertPath, equalTo(expectedWord));
    }

    /**
     * 获取一组数据，断言包含某一特殊字段
     *
     * @param url
     * @param statusCode
     * @param paramsMap
     * @param assertPath
     * @param expectedWord
     */
    public static void postHasItems(String url, int statusCode,
                                    Map<String, Object> paramsMap,
                                    String assertPath, String expectedWord) {

        given().
                contentType(CONTENT_TYPE_FORM).
                params(paramsMap).
                relaxedHTTPSValidation().
                when().
                post(url.trim()).
                then().
                log().ifError().
                assertThat().statusCode(statusCode).
                and().
                body(assertPath, hasItems(expectedWord));

    }

    /**
     * 获取一组数据，断言包含某一特殊字段
     *
     * @param url
     * @param statusCode
     * @param paramsMap
     * @param assertPath
     * @param expectedWord
     */
    public static void postHasItems(String url, int statusCode,
                                    Map<String, Object> paramsMap,
                                    String assertPath, int expectedWord) {

        given().
                contentType(CONTENT_TYPE_FORM).
                params(paramsMap).
                relaxedHTTPSValidation().
                when().
                post(url.trim()).
                then().
                log().ifError().
                assertThat().statusCode(statusCode).
                and().
                body(assertPath, hasItems(expectedWord));

    }

    /**
     * 指定Root路径的Post方法
     *
     * @param paramsMap    请求参数
     * @param rootPath     root路径
     * @param assertPath   待断言的关键字路径
     * @param expectedWord 期望关键字
     */
    public static void rootIsPost(String url, Map<String, Object> paramsMap,
                                  String rootPath, String assertPath,
                                  String expectedWord) {
        given().
                contentType(CONTENT_TYPE_JSON).
                params(paramsMap).
                relaxedHTTPSValidation().
                when().log().body().
                post(url.trim()).
                then().
                root(rootPath).
                body(assertPath, is(expectedWord));
    }

    /**
     * 6、Post请求方法
     * 接口请求返回值
     *
     * @param url         请求地址
     * @param paramsMap   参数
     * @param keywordPath 需要返回值的路径，如："data.token"
     * @return keyWord
     */
    public static String returnValue(String url, Map<String, Object> paramsMap,
                                     String keywordPath) {
        String keyWord = given().
                params(paramsMap).
                relaxedHTTPSValidation().
                post(url).
                path(keywordPath).toString();
        System.out.println("所需返回值为：" + keywordPath + "  ===>  " + keyWord);
        return keyWord;
    }

    /**
     * @param url
     * @param paramsMap
     * @param index
     * @param returnKey
     * @return
     */
    public String returnItemsValue(String url, Map<String, Object> paramsMap,
                                   String dataTag, String itemsTag,
                                   int index, String returnKey) {
        String returnValue = null;
        Response response = given().
                contentType(CONTENT_TYPE_FORM).
                params(paramsMap).
                relaxedHTTPSValidation().
                post(url);
//        response.prettyPrint();
        String body = response.getBody().asString();
        JSONObject bodyObject = JSON.parseObject(body);
        JSONObject dataJson = bodyObject.getJSONObject(dataTag);
        String items = dataJson.get(itemsTag).toString();
        JSONArray itemsJson = JSON.parseArray(items);
        returnValue = itemsJson.getJSONObject(index).getString(returnKey);
        System.out.println("获取到的关键词为: " + returnValue);
        return returnValue;
    }

    /**
     * 使用Path方法获取响应内容
     *
     * @param url
     * @param assertPath
     * @param expectedWord
     * @param path
     */
    public static String extractDetailsByPath(String url, Map<String, Object> paramsMap,
                                              String assertPath, int expectedWord,
                                              String path) {
        String keyWord = given().
                contentType(CONTENT_TYPE_JSON).
                params(paramsMap).
                relaxedHTTPSValidation().
                get(url).
                then().
                body(assertPath, equalTo(expectedWord)).
                extract().
                path(path);
        System.out.println(keyWord);
        return keyWord;
    }

    public static String extractDetailsByPath(String url, Map<String, Object> paramsMap,
                                              String assertPath) {
        String keyWord = given().
                contentType(CONTENT_TYPE_JSON).
                params(paramsMap).
                relaxedHTTPSValidation().
                get(url).andReturn().jsonPath().getString(assertPath);
        System.out.println(keyWord);
        return keyWord;
    }

    /**
     * 类型三：含有cookie的post请求
     */
    public static Response cookiesPost(String contentType, String url, String cookies) {
        System.out.println("Cookie = " + cookies);

        Response response = given().
                contentType(contentType).
                relaxedHTTPSValidation().
                cookies("web-session", cookies).
                expect().statusCode(200).
                when().
                post(url.trim());

        return response;
    }


    /**
     * 打印消息返回体
     *
     * @param url        请求地址
     * @param paramsMap  参数
     * @param headersMap 消息头
     */
    public static void print(String url, Map<String, String> paramsMap,
                             Map<String, Object> headersMap) {
        Response response = given().
                contentType(CONTENT_TYPE_FORM).
                headers(headersMap).
                relaxedHTTPSValidation().
                params(paramsMap).
                post(url);
        response.prettyPrint();
    }

    /**
     * 打印消息返回体
     *
     * @param url       请求地址
     * @param paramsMap 参数
     */
    public static void print(String url, Map<String, Object> paramsMap) {
        Response response = given().
                contentType(CONTENT_TYPE_FORM).
                params(paramsMap).relaxedHTTPSValidation().when().log().all().
                post(url);
        response.prettyPrint();
    }

    public static void print(String contentType, String url,
                             Map<String, Object> paramsMap) {
        Response response = given().
                contentType(contentType).
                params(paramsMap).relaxedHTTPSValidation().
                post(url);
        response.prettyPrint();
    }
}
