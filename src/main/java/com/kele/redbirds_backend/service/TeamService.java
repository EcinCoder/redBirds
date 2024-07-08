package com.kele.redbirds_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kele.redbirds_backend.model.domain.Team;
import com.kele.redbirds_backend.model.domain.User;
import com.kele.redbirds_backend.model.dto.TeamQuery;
import com.kele.redbirds_backend.model.requset.JoinTeamRequest;
import com.kele.redbirds_backend.model.requset.UpdateTeamRequest;
import com.kele.redbirds_backend.model.vo.UserTeamVO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
* @author hp-pc
* @description 针对表【team(队伍)】的数据库操作Service
* @createDate 2024-06-05 20:07:40
*/
public interface TeamService extends IService<Team> {

    /**
     * 创建队伍
     * @param loginUser 当前登录用户
     * @param team 创建队伍
     * @return 是否成功
     */
    long createTeam(User loginUser, Team team) ;

    /**
     * 搜索队伍
     * @param teamQuery
     * @return
     */
    List<UserTeamVO> listTeams(TeamQuery teamQuery, HttpServletRequest request);

    /**
     * 更新队伍信息
     * @param updateTeamRequest
     * @return 是否成功
     */
    boolean updateTeam(UpdateTeamRequest updateTeamRequest, User loginUser);

    /**
     * 加入队伍
     * @param joinTeamRequest
     * @return 是否成功
     */
    boolean joinTeam(JoinTeamRequest joinTeamRequest, User loginUser);
}
