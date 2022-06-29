package org.barren.modules.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 系统菜单表
 * </p>
 *
 * @author cxs
 * @since 2022-06-29
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_menu")
@ApiModel(value = "SysMenu对象", description = "系统菜单表")
public class SysMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("子菜单列表，非数据库字段")
    @TableField(exist = false)
    private List<SysMenu> children;

    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("上级菜单ID")
    @TableField("pid")
    private Long pid;

    @ApiModelProperty("组件名称")
    @TableField("name")
    private String name;

    @ApiModelProperty("菜单名称")
    @TableField("title")
    private String title;

    @ApiModelProperty("显示排序")
    @TableField("sort")
    private Integer sort;

    @ApiModelProperty("菜单类型，0-目录 1-菜单 2-按钮")
    @TableField("type")
    private Integer type;

    @ApiModelProperty("路由地址，路由地址/外链地址")
    @TableField("path")
    private String path;

    @ApiModelProperty("组件路径，vue组件路径")
    @TableField("component")
    private String component;

    @ApiModelProperty("权限标识")
    @TableField("permission")
    private String permission;

    @ApiModelProperty("图标")
    @TableField("icon")
    private String icon;

    @ApiModelProperty("是否外链")
    @TableField("i_frame")
    private Boolean iFrame;

    @ApiModelProperty("是否缓存")
    @TableField("cache")
    private Boolean cache;

    @ApiModelProperty("是否隐藏")
    @TableField("hidden")
    private Boolean hidden;

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
