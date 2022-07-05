package org.barren.modules.system.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户修改密码
 *
 * @author cxs
 **/
@Data
public class UserPassVo implements Serializable {

    private Long id;

    private String oldPass;

    private String newPass;
}
