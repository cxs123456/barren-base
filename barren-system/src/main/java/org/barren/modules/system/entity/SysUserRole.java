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
 * 用户角色关联表
 * </p>
 *
 * @author cxs
 * @since 2022-06-29
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_user_role")
@ApiModel(value = "SysUserRole对象", description = "用户角色关联表")
public class SysUserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户ID")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty("角色ID")
    @TableField("role_id")
    private Long roleId;

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

    @ApiModelProperty("删除标志，0正常 1删除")
    @TableField("del_flag")
    private Integer delFlag;

    @ApiModelProperty("备注")
    @TableField("remark")
    private String remark;

    @ApiModelProperty("租户id")
    @TableField("tenant_id")
    private Long tenantId;
}
