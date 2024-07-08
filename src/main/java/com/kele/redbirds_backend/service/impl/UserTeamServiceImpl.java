package com.kele.redbirds_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kele.redbirds_backend.model.domain.UserTeam;
import com.kele.redbirds_backend.service.UserTeamService;
import com.kele.redbirds_backend.mapper.UserTeamMapper;
import org.springframework.stereotype.Service;

/**
* @author hp-pc
* @description 针对表【user_team(队伍)】的数据库操作Service实现
* @createDate 2024-06-05 20:08:35
*/
@Service
public class UserTeamServiceImpl extends ServiceImpl<UserTeamMapper, UserTeam>
    implements UserTeamService{

}




