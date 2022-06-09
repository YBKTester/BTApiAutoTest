package com.bitmart.spot.api.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author macrae * @date 2022年04月15日 上午11:00
 * @decription:
 */
@Data
public class CancelOrderRequest extends BaseRequest{

    private Long orderId;

    @NotNull(message = "userId is empty")
    private Long userId;

    private String clientOrderId;
    /**
     * 交易对id
     */
    private Long symbolId;

}
