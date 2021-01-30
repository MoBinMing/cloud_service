package com.mobinming.cloud_service.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author mbm
 * @since 2020-11-15
 */
@Data
@ApiModel(value="User对象", description="用户信息")
public class UserDao{
    @ApiModelProperty(value = "唯一ID")
    private Integer id;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "用户别名")
    private String aliasName;

    @ApiModelProperty(value = "头像url")
    private String headUrl;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "真实姓名")
    private String userName;

    @ApiModelProperty(value = "token")
    private String token;

    @ApiModelProperty(value = "注册时间")
    private Date registerTime;

    @ApiModelProperty(value = "个性签名")
    private String signature;

    @ApiModelProperty(value = "身份证")
    private String idCard;

    @ApiModelProperty(value = "性别")
    private Integer gender;
}
