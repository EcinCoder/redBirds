package com.kele.redbirds_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kele.redbirds_backend.common.ErrorCode;
import com.kele.redbirds_backend.exception.BusinessException;
import com.kele.redbirds_backend.mapper.TeamMapper;
import com.kele.redbirds_backend.mapper.UserMapper;
import com.kele.redbirds_backend.model.domain.Team;
import com.kele.redbirds_backend.model.domain.User;
import com.kele.redbirds_backend.model.domain.UserTeam;
import com.kele.redbirds_backend.model.dto.TeamQuery;
import com.kele.redbirds_backend.model.enums.TeamStatusEnum;
import com.kele.redbirds_backend.model.requset.JoinTeamRequest;
import com.kele.redbirds_backend.model.requset.UpdateTeamRequest;
import com.kele.redbirds_backend.model.vo.UserTeamVO;
import com.kele.redbirds_backend.model.vo.UserVO;
import com.kele.redbirds_backend.service.TeamService;
import com.kele.redbirds_backend.service.UserService;
import com.kele.redbirds_backend.service.UserTeamService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author hp-pc
 * @description 针对表【team(队伍)】的数据库操作Service实现
 * @createDate 2024-06-05 20:07:40
 */
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
        implements TeamService {

    @Resource
    private UserTeamService userTeamService;

    @Resource
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class) // 开启事物
    public long createTeam(User loginUser, Team team) {
        //1. 请求参数是否为空？
        if (team == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        //2. 是否登录，未登录不允许创建
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        final long userId = loginUser.getId();
        //3. 校验信息
        //	1. 队伍人数 >1 且 <=20
        int maxNum = Optional.ofNullable(team.getMaxNum()).orElse(4); // 如果为空则赋值 4
        if (maxNum < 1 || maxNum > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍人数不满足要求");
        }
        team.setMaxNum(maxNum);
        //	2. 队伍标题 <= 20
        String name = team.getName();
        if (StringUtils.isBlank(name) || name.length() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍标题不满足要求");
        }
        //	3. 描述 <= 512
        String description = team.getDescription();
        if (StringUtils.isNotBlank(description) && description.length() > 512) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍描述过长");
        }
        //	4. teamStatus 是否公开（int）不传默认为 0 （公开）
        int teamStatus = Optional.ofNullable(team.getTeamStatus()).orElse(0); // 如果为空则赋值 0
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(teamStatus);
        if (statusEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍状态不满足要求");
        }
        team.setTeamStatus(statusEnum.getValue());
        //	5. 密码有的话 <= 32
        String teamPassword = team.getTeamPassword();
        if (TeamStatusEnum.SECRET.equals(statusEnum)) {
            if (StringUtils.isBlank(teamPassword) || teamPassword.length() > 32) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码设置不正确");
            }
            team.setTeamStatus(1);
        }
        Date expireTime = team.getExpireTime();
        if (expireTime == null) {
            expireTime = new Date();
            expireTime.setTime(expireTime.getTime() + 24 * 60 * 60 * 1000);
        }
        //	6. 超时时间 > 当前时间
        if (new Date().after(expireTime)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "超时时间 > 当前时间");
        }
        team.setExpireTime(expireTime);
        //	7. 校验用户最多创建5个队伍
        // todo 有bug，可能同时创建100个队伍
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        // todo S
        long hasTeamNum = this.count(queryWrapper);
        if (hasTeamNum >= 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "最多创建5个队伍");
        }
        //4. 插入队伍信息到队伍表
        team.setId(null);
        team.setUserId(userId);
        boolean resultTeam = this.save(team);
        Long teamId = team.getId();
        if (!resultTeam || teamId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "创建队伍失败");
        }
        //5. 插入用户 => 队伍关系到关系表
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(userId);
        userTeam.setTeamId(teamId);
        userTeam.setJoinTime(new Date());
        boolean resultUserTeam = userTeamService.save(userTeam);
        if (!resultUserTeam) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "创建队伍失败");
        }
        return teamId;
    }


    @Override
    public List<UserTeamVO> listTeams(TeamQuery teamQuery, HttpServletRequest request) {
        // 组合查询条件
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        if (teamQuery != null) {
            // 根据队伍ID来查询
            Long id = teamQuery.getId();
            if (id != null && id > 0) {
                queryWrapper.eq("id", id);
            }
            // 根据关键词搜索
            String searchText = teamQuery.getSearchText();
            if (StringUtils.isNotBlank(searchText)) {
                queryWrapper.and(qw -> qw.like("name",searchText).or().like("description",searchText));
            }
            // 根据队伍名称来查询
            String name = teamQuery.getName();
            if (StringUtils.isNotBlank(name) && name.length() <= 20) {
                queryWrapper.like("name", name);
            }
            // 根据队伍描述来查询
            String description = teamQuery.getDescription();
            if (StringUtils.isNotBlank(description) && name.length() <= 20) {
                queryWrapper.like("description", description);
            }
            // 根据最大人数来查询
            Integer maxNum = teamQuery.getMaxNum();
            if (maxNum != null && maxNum > 0) {
                queryWrapper.eq("maxNum", maxNum);
            }
            // 根据创建人来查询
            Long userId = teamQuery.getUserId();
            if (userId != null && userId > 0) {
                queryWrapper.eq("userId", userId);
            }
            // 根据队伍状态查询
            Integer teamStatus = teamQuery.getTeamStatus();
            TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(teamStatus);
            if (statusEnum != null) {
                if (statusEnum == TeamStatusEnum.PRIVATE) {
                    if (!userService.checkAdmin(request)) {
                        throw new BusinessException(ErrorCode.NO_AUTH);
                    }
                }
                queryWrapper.eq("teamStatus", teamStatus);
            }
            List<Team> teamList = this.list(queryWrapper);
            if (CollectionUtils.isEmpty(teamList)) {
                return new ArrayList<>();
            }
            // 不展示已过期队伍
            queryWrapper.and(qw -> qw.gt("expireTime",new Date()).or().isNull("expireTime"));
            List<UserTeamVO> teamVOList = new ArrayList<>();
            // 关联查询用户信息
            for (Team team : teamList) {
                Long teamUserId = team.getUserId();
                if (teamUserId == null) {
                    continue;
                }
                User user = userService.getById(teamUserId);
                User safetyUser = userService.getSafetyUser(user);
                UserTeamVO userTeamVO = new UserTeamVO();
                BeanUtils.copyProperties(team,userTeamVO);
                // 脱敏
                UserVO userVO = new UserVO();
                if (user != null) {
                    BeanUtils.copyProperties(user,userVO);
                }
                userTeamVO.setCreateUser(userVO);
                teamVOList.add(userTeamVO);
            }

        }
        return null;
    }

    // todo 有BUG
    @Override
    public boolean updateTeam(UpdateTeamRequest updateTeamRequest, User loginUser) {
        if (updateTeamRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = updateTeamRequest.getId();
        if(id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team oldTeam = this.getById(id);
        if (oldTeam == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if(!oldTeam.getUserId().equals(loginUser.getId()) && !userService.checkAdmin(loginUser)){
            throw new BusinessException(ErrorCode.NO_AUTH,"无法修改");
        }
        // 如果状态改为加密。需要设置密码
        if(!updateTeamRequest.getTeamStatus().equals(oldTeam.getTeamStatus()) && updateTeamRequest.getTeamStatus() == 1) {
            if(updateTeamRequest.getTeamPassword().isBlank()) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"未设置密码");
            }
        }
        Team updateTeam = new Team();
        BeanUtils.copyProperties(updateTeamRequest,updateTeam);
        return this.updateById(updateTeam);
    }

    @Override
    public boolean joinTeam(JoinTeamRequest joinTeamRequest, User loginUser) {
        if (joinTeamRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long teamId = joinTeamRequest.getId();
        if(teamId == null || teamId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Team team = this.getById(teamId);
        if(team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍不存在");
        }
        if(team.getExpireTime() != null && team.getExpireTime().before(new Date())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍已过期");
        }
        Integer teamStatus = team.getTeamStatus();
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(teamStatus);
        if(TeamStatusEnum.SECRET.equals(statusEnum)) {
            throw new BusinessException(ErrorCode.NO_AUTH,"禁止加入私有队伍");
        }
        String teamPassword = joinTeamRequest.getTeamPassword();
        if(TeamStatusEnum.PRIVATE.equals(statusEnum)) {
            if(StringUtils.isBlank(teamPassword) || !teamPassword.equals(team.getTeamPassword())) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码错误");
            }
        }
        // 该用户已加入队伍数量
        Long userId = loginUser.getId();
        QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
        userTeamQueryWrapper.eq("userId", userId);
        long hasJoinNum = userTeamService.count(userTeamQueryWrapper);
        if(hasJoinNum > 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"可加入队伍数量到达上限");
        }
        // 不能加入已加入的队伍
        userTeamQueryWrapper = new QueryWrapper<>();
        userTeamQueryWrapper.eq("userId", userId);
        userTeamQueryWrapper.eq("teamId", teamId);
        long hasUserJoinTeam = userTeamService.count(userTeamQueryWrapper);
        if(hasUserJoinTeam > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户已加入该队伍");
        }
        // 已加入队伍人数
        userTeamQueryWrapper = new QueryWrapper<>();
        userTeamQueryWrapper.eq("teamId", teamId);
        long hasTeamJoinNum = userTeamService.count(userTeamQueryWrapper);
        if(hasTeamJoinNum > team.getMaxNum()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍人数已达上限");
        }
        // 修改队伍信息
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(userId);
        userTeam.setTeamId(teamId);
        userTeam.setJoinTime(new Date());
        return userTeamService.save(userTeam);
    }
//        Team team = new Team();
//        try {
//            BeanUtils.copyProperties(team, teamQuery); // 把teamQuery 的数据传入team
//        } catch (Exception e) {
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
//        }
}





