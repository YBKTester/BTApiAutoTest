package com.bitmart.spot.api.request;

import com.bitmart.spot.api.enums.OrderBuyOrSellEnum;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 请求参数
 * 字段	类型	是否必填	描述
 * symbol	string	是	交易对 symbol
 * side	string	是	类型
 *      buy=买入
 *      sell=卖出
 * type	string	是	订单类型
 *      是	limit=市价单
 *      是	market=限价单
 *
 * 限价单特殊参数 (type=limit)
 * 字段	类型	是否必填	描述
 * size 	string	是	买入或卖出的数量
 * price	string	是	价格
 *
 *
 * 市价单特殊参数 (type=market)
 * 字段	类型	是否必填	描述
 * size 	string	是	卖出数量，市价卖出时必填 count
 * notional	string	是	买入金额，市价买入时必填 notional
 *
 */

/**
* @Author: Macrae 
* @Date: 2022/5/4 下午7:03
* @Description:
* @return: 
*/ 
@EqualsAndHashCode(callSuper = true)
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderRequest extends BaseRequest {

    @NotNull(message = "userId is empty")
    private Long userId;

    @NotNull(message = "symbol is empty")
    private Long symbol;

    @NotBlank(message = "reqNo is empty")
    private String reqNo;
    /**
     * 类型
     *  buy=买入
     *  sell=卖出
     */
    @NotNull(message = "side is empty")
    private String side;
    /**
     * 订单类型
     * limit=市价单
     * market=限价单
     */
    @NotNull(message = "type is empty")
    private String type;

    /**
     * 卖出数量，市价卖出时必填 count
     */
    private String size;
    /**
     * 价格
     */
    private String price;
    /**
     * 买入金额，市价买入时必填 notional
     */
    private String notional;

    private String clientOrderId;
    /**
     * 商户id
     */
    private Integer merchantId;


}
