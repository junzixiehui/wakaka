package com.junzixiehui.wakaka.application.dto.req;

import com.zhouyutong.zapplication.api.Req;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class OrderCreateReq extends Req {

    @ApiModelProperty(value = "手机号", required = true)
    private String phone;

    @ApiModelProperty(value = "用户经度", required = true)
    private String lng;

    @ApiModelProperty(value = "用户纬度", required = true)
    private String lat;
}
