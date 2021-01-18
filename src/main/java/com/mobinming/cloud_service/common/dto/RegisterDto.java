package com.mobinming.cloud_service.common.dto;

import lombok.Data;

import javax.net.ssl.*;
import javax.validation.constraints.NotBlank;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Data
public class RegisterDto implements Serializable {

    @NotBlank(message = "用户名不能为空")
    private String aliasName;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "手机号不能为空")
    private String phone;

}
