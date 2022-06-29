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
 * 系统角色表
 * </p>
 *
 * @author cxs
 * @since 2022-06-29
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_role")
@ApiModel(value = "SysRole对象", description = "系统角色表")
public class SysRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("角色名称")
    @TableField("name")
    private String name;

    @ApiModelProperty("角色权限字符")
    @TableField("code")
    private String code;

    @ApiModelProperty("角色类型")
    @TableField("type")
    private Integer type;

    @ApiModelProperty("角色级别")
    @TableField("level")
    private Integer level;

    @ApiModelProperty("顺序")
    @TableField("sort")
    private Integer sort;

    @ApiModelProperty("数据范围（0-全部数据权限 1-自定数据权限 2-本部门数据权限 3-本部门及以下数据权限）")
    @TableField("data_scope")
    private Integer dataScope;

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


}
