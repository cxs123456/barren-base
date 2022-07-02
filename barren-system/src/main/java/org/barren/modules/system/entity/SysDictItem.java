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
 * 数据字典详情表
 * </p>
 *
 * @author cxs
 * @since 2022-07-02
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_dict_item")
@ApiModel(value = "SysDictItem对象", description = "数据字典详情表")
public class SysDictItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("字典表ID")
    @TableField("dict_id")
    private Long dictId;

    @ApiModelProperty("字典表-字典编码")
    @TableField("dict_code")
    private String dictCode;

    @ApiModelProperty("字典项名称")
    @TableField("item_name")
    private String itemName;

    @ApiModelProperty("字典项值")
    @TableField("item_value")
    private String itemValue;

    @ApiModelProperty("显示排序")
    @TableField("sort")
    private Integer sort;

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
