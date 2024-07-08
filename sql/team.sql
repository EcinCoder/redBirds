-- 队伍表

create table team
(
    id           bigint auto_increment              primary key,
    name     	 varchar(256)                       not null comment '队伍名称',
    description  varchar(1024)                      null comment '队伍描述',
    maxNum       int                                null comment '最大人数',
    expireTime   datetime                           null comment '过期时间',
    userId       bigint                                 comment '用户id',
    teamStatus   varchar(256)                       null comment '用户身份 user - 用户 admin - 管理员 ban - 封号',
    teamPassword varchar(512)                       null comment '队伍密码',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint  default 0                 null comment '是否删除'

) comment'队伍';