package org.barren.modules.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 租户表
 * </p>
 *
 * @author cxs
 * @since 2022-11-29
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_tenant")
@ApiModel(value = "SysTenant对象", description = "租户表")
public class SysTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("租户id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("租户名")
    @TableField("name")
    private String name;

    @ApiModelProperty("联系人的用户id")
    @TableField("contact_user_id")
    private Long contactUserId;

    @ApiModelProperty("联系人")
    @TableField("contact_name")
    private String contactName;

    @ApiModelProperty("联系手机号")
    @TableField("contact_phone")
    private String contactPhone;

    @ApiModelProperty("联系地址")
    @TableField("address")
    private String address;

    @ApiModelProperty("绑定域名")
    @TableField("domain")
    private String domain;

    @ApiModelProperty("租户套餐编号")
    @TableField("package_id")
    private Long packageId;

    @ApiModelProperty("过期时间")
    @TableField("expire_time")
    private LocalDateTime expireTime;

    @ApiModelProperty("账号数量")
    @TableField("account_count")
    private Integer accountCount;

    @ApiModelProperty("授权码")
    @TableField("license_key")
    private String licenseKey;

    @ApiModelProperty("创建人")
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

    @ApiModelProperty("修改人")
    @TableField(value = "update_user", fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("帐号状态，1启用 0停用")
    @TableField("status")
    private Integer status;

    @ApiModelProperty("删除标志，0正常 1删除")
    @TableField("del_flag")
    private Integer delFlag;

    @ApiModelProperty("备注")
    @TableField("remark")
    private String remark;


}
