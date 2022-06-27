package org.barren.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 系统菜单表
 * </p>
 *
 * @author cxs
 * @since 2022-06-24
 */
@Getter
@Setter
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
    private Long pid;

    @ApiModelProperty("组件名称")
    private String name;

    @ApiModelProperty("菜单名称")
    private String title;

    @ApiModelProperty("显示排序")
    private Integer sort;

    @ApiModelProperty("菜单类型，0-目录 1-菜单 2-按钮")
    private Integer type;

    @ApiModelProperty("路由地址，路由地址/外链地址")
    private String path;

    @ApiModelProperty("组件路径，vue组件路径")
    private String component;

    @ApiModelProperty("权限标识")
    private String permission;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("是否外链")
    private Boolean iFrame;

    @ApiModelProperty("是否缓存")
    private Boolean cache;

    @ApiModelProperty("是否隐藏")
    private Boolean hidden;

    @ApiModelProperty("创建人")
    private Long createUser;

    @ApiModelProperty("修改人")
    private Long updateUser;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("删除标志，0正常 1删除")
    private Integer delFlag;

    @ApiModelProperty("备注")
    private String remark;


}
