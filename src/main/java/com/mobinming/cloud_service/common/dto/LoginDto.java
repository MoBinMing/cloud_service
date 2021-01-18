package com.mobinming.cloud_service.common.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class LoginDto implements Serializable {

    @NotBlank(message = "手机号字段phone不能为空")
    private String phone;

    @NotBlank(message = "密码字段password不能为空")
    private String password;
}
