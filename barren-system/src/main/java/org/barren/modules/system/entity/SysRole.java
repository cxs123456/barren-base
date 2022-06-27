package org.barren.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 系统角色表
 * </p>
 *
 * @author cxs
 * @since 2022-06-24
 */
@Getter
@Setter
@TableName("sys_role")
@ApiModel(value = "SysRole对象", description = "系统角色表")
public class SysRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("角色名称")
    private String name;

    @ApiModelProperty("角色权限字符")
    private String code;

    @ApiModelProperty("角色类型")
    private Integer type;

    @ApiModelProperty("角色级别")
    private Integer level;

    @ApiModelProperty("顺序")
    private Integer sort;

    @ApiModelProperty("数据范围（0-全部数据权限 1-自定数据权限 2-本部门数据权限 3-本部门及以下数据权限）")
    private Integer dataScope;

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
