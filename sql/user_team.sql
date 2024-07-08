-- 用户队伍关系

create table user_team
(
    id           bigint auto_increment				primary key,
    userId       bigint 							comment '用户id',
    teamId       bigint 							comment '队伍id',
    joinTime   	 datetime 						  null comment '加入时间',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint  default 0                 null comment '是否删除'

) comment'队伍';
