package com.bitmart.spot.api.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
@Data
public class BaseRequest implements Serializable {

    private String reqSource;
}
