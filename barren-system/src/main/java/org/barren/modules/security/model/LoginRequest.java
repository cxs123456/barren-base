package org.barren.modules.security.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author cxs
 **/
@Data
@ApiModel(value = "登录请求实体", description = "登录请求实体")
public class LoginRequest implements Serializable {

    @NotBlank
    @ApiModelProperty(value = "用户名")
    private String username;

    @NotBlank
    @ApiModelProperty(value = "密码")
    private String password;

    @NotBlank
    @ApiModelProperty(value = "图片验证码，计算值")
    private String code;

    @NotBlank
    @ApiModelProperty(value = "验证码key")
    private String codeKey;
}
