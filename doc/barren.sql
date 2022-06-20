-- 基于BRAC 模型 权限表设计： sys_user, sys_role, sys_menu, sys_user_role, sys_role_menu
-- 系统用户表
CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  
  `username` varchar(20) DEFAULT NULL COMMENT '用户名',
  `password` varchar(45) DEFAULT NULL COMMENT '密码', 
  `nick_name` varchar(255) DEFAULT NULL COMMENT '昵称',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',  
  `sex` int(2) DEFAULT NULL COMMENT '性别',
  `is_admin` bit(1) DEFAULT b'0' COMMENT '是否为admin账号',
  `avatar` varchar(100) DEFAULT NULL COMMENT '头像地址',
  `login_ip` varchar(50) DEFAULT NULL COMMENT '最后登录IP',
  `login_date` datetime DEFAULT NULL COMMENT '最后登录时间',
  
  `create_user` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_user` bigint(20) DEFAULT NULL COMMENT '修改人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `status` int(2) DEFAULT 1 COMMENT '帐号状态，1启用 0停用',
  `del_flag` int(2) DEFAULT 0 COMMENT '删除标志，0正常 1删除',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- 系统角色表
CREATE TABLE `sys_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(100) DEFAULT NULL COMMENT '角色名称',
  `code` varchar(100) DEFAULT NULL COMMENT '角色权限字符',
  `type` int(5) DEFAULT NULL COMMENT '角色类型',
  `level` int(5) DEFAULT NULL COMMENT '角色级别',
  `sort` int(5) DEFAULT 0 COMMENT '顺序',
  `data_scope` int(5) DEFAULT 0 COMMENT '数据范围（0-全部数据权限 1-自定数据权限 2-本部门数据权限 3-本部门及以下数据权限）',
  
  `create_user` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_user` bigint(20) DEFAULT NULL COMMENT '修改人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` int(2) DEFAULT 0 COMMENT '删除标志，0正常 1删除',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统角色表';

-- 系统菜单表
CREATE TABLE `sys_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `pid` bigint(20) DEFAULT NULL COMMENT '上级菜单ID',
  `name` varchar(255) DEFAULT NULL COMMENT '菜单名称',
  `sort` int(5) DEFAULT 0 COMMENT '显示排序',
  `type` tinyint(4) NOT NULL DEFAULT 1 COMMENT '菜单类型，0-目录 1-菜单 2-按钮',
  `path` varchar(255) DEFAULT NULL COMMENT '路由地址，路由地址/外链地址',
  `component` varchar(255) DEFAULT NULL COMMENT '组件路径，vue组件路径',
  `permission` varchar(255) DEFAULT NULL COMMENT '权限标识',
  
  `icon` varchar(100) DEFAULT NULL COMMENT '图标',
  `i_frame` bit(1) DEFAULT b'0' COMMENT '是否外链',
  `cache` bit(1) DEFAULT b'0' COMMENT '是否缓存',
  `hidden` bit(1) DEFAULT b'0' COMMENT '是否隐藏',
   
  `create_user` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_user` bigint(20) DEFAULT NULL COMMENT '修改人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` int(2) DEFAULT 0 COMMENT '删除标志，0正常 1删除',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统菜单表';

-- 用户角色关联表
CREATE TABLE `sys_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  
  `create_user` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_user` bigint(20) DEFAULT NULL COMMENT '修改人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` int(2) DEFAULT 0 COMMENT '删除标志，0正常 1删除',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 角色菜单关联表
CREATE TABLE `sys_role_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
 
  `role_id` varchar(64) NOT NULL COMMENT '角色编号',
  `menu_id` varchar(64) NOT NULL COMMENT '菜单编号',
  
  `create_user` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_user` bigint(20) DEFAULT NULL COMMENT '修改人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` int(2) DEFAULT 0 COMMENT '删除标志，0正常 1删除',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关联表';

-- 部门表
CREATE TABLE `sys_dept` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',

  `pid` bigint(20) DEFAULT NULL COMMENT '上级部门',
  `name` varchar(255) DEFAULT NULL COMMENT  '部门名称',
  `sort` int(5) DEFAULT 0 COMMENT '排序',
  `leader` bigint(20) DEFAULT NULL COMMENT '负责人',
  
  `create_user` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_user` bigint(20) DEFAULT NULL COMMENT '修改人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` int(2) DEFAULT 0 COMMENT '删除标志，0正常 1删除',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- 岗位表
CREATE TABLE `sys_post` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',

  `code` varchar(100) DEFAULT NULL COMMENT '岗位编码',
  `name` varchar(100) DEFAULT NULL COMMENT '岗位名称',
  `sort` int(5) DEFAULT 0 COMMENT '排序',
  `status` int(2) DEFAULT 1 COMMENT '状态，1启用 0停用',
  
  `create_user` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_user` bigint(20) DEFAULT NULL COMMENT '修改人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` int(2) DEFAULT 0 COMMENT '删除标志，0正常 1删除',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='岗位表';

-- 用户岗位关联表
CREATE TABLE `sys_user_post` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `post_id` bigint(20) NOT NULL COMMENT '岗位ID',
  
  `create_user` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_user` bigint(20) DEFAULT NULL COMMENT '修改人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` int(2) DEFAULT 0 COMMENT '删除标志，0正常 1删除',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户岗位关联表';

--角色部门关联表
CREATE TABLE `sys_role_dept` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `dept_id` bigint(20) NOT NULL COMMENT '部门ID',
  
  `create_user` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_user` bigint(20) DEFAULT NULL COMMENT '修改人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` int(2) DEFAULT 0 COMMENT '删除标志，0正常 1删除',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色部门关联表';

-- 数据字典表
CREATE TABLE `sys_dict` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',

  `dict_name` varchar(100) DEFAULT NULL COMMENT '字典名称，数据库字段描述名称',
  `dict_code` varchar(100) NOT NULL COMMENT '字典编码，数据库字段英文名',

  `create_user` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_user` bigint(20) DEFAULT NULL COMMENT '修改人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` int(2) DEFAULT 0 COMMENT '删除标志，0正常 1删除',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据字典表';

-- 数据字典详情表
CREATE TABLE `sys_dict_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `dict_id` bigint(20) DEFAULT NULL COMMENT '字典表ID',
  
  `dict_code` varchar(100) NOT NULL COMMENT '字典表-字典编码',
  `item_name` varchar(100) DEFAULT NULL COMMENT '字典项名称',
  `item_value` varchar(100) DEFAULT NULL COMMENT '字典项值',
  `sort` int(5) DEFAULT 0 COMMENT '显示排序',
  
  `create_user` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_user` bigint(20) DEFAULT NULL COMMENT '修改人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` int(2) DEFAULT 0 COMMENT '删除标志，0正常 1删除',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据字典详情表';

-- 通知公告表
CREATE TABLE `sys_notice` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  
  `title` varchar(255) NOT NULL COMMENT '公告标题',
  `content` text DEFAULT NULL COMMENT '公告内容',
  `type` int(2) NOT NULL DEFAULT 1 COMMENT '公告类型（1通知 2公告）',
  `status` int(2) DEFAULT 1 COMMENT '状态，1启用 0停用',
  
  `create_user` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_user` bigint(20) DEFAULT NULL COMMENT '修改人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` int(2) DEFAULT 0 COMMENT '删除标志，0正常 1删除',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) 
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='通知公告表';

