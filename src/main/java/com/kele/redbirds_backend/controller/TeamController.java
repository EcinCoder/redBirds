package com.kele.redbirds_backend.controller;/*
 * @author kele
 * @version 1.0
 *
 */

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kele.redbirds_backend.common.BaseResponse;
import com.kele.redbirds_backend.common.ErrorCode;
import com.kele.redbirds_backend.common.ResultUtils;
import com.kele.redbirds_backend.exception.BusinessException;
import com.kele.redbirds_backend.model.domain.Team;
import com.kele.redbirds_backend.model.domain.User;
import com.kele.redbirds_backend.model.dto.TeamQuery;
import com.kele.redbirds_backend.model.requset.CreateTeamRequest;
import com.kele.redbirds_backend.model.requset.JoinTeamRequest;
import com.kele.redbirds_backend.model.requset.UpdateTeamRequest;
import com.kele.redbirds_backend.model.vo.UserTeamVO;
import com.kele.redbirds_backend.service.TeamService;
import com.kele.redbirds_backend.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "队伍接口")
@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/team")
@Slf4j
public class TeamController {

    @Resource
    private TeamService teamService;

    @Resource
    private UserService userService;

    @PostMapping("/create")
    public BaseResponse<Long> createTeam(@RequestBody CreateTeamRequest createTeamRequest, HttpServletRequest request) {
        if (createTeamRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Team team = new Team();
        BeanUtils.copyProperties(createTeamRequest,team);
        long teamId = teamService.createTeam(loginUser, team);
        return ResultUtils.success(teamId);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTeam(long id) {
        if (id < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = teamService.removeById(id);
        if (!result) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return ResultUtils.success(true);
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateTeam(@RequestBody UpdateTeamRequest updateTeamRequest, HttpServletRequest request) {
        if (updateTeamRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = teamService.updateTeam(updateTeamRequest, userService.getLoginUser(request));
        if (!result) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return ResultUtils.success(true);
    }

    @GetMapping("/get")
    public BaseResponse<Team> getTeamById(long id) {
        if (id < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = teamService.getById(id);
        if (team == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return ResultUtils.success(team);
    }

    @GetMapping("/list")
    public BaseResponse<List<UserTeamVO>> getTeamByList(TeamQuery teamQuery,HttpServletRequest request) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        List<UserTeamVO> teamList = teamService.listTeams(teamQuery,request);
        return ResultUtils.success(teamList);
    }

    @GetMapping("/list/page")
    public BaseResponse<Page<Team>> getTeamByPageList(TeamQuery teamQuery) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = new Team();
        BeanUtils.copyProperties(teamQuery,team); // 把teamQuery 的数据传入team
        Page<Team> page = new Page<>(teamQuery.getPageNum(),teamQuery.getPageSize());
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
        Page<Team> teamPageList = teamService.page(page,queryWrapper);
        return ResultUtils.success(teamPageList);

    }

    @PostMapping("/join")
    public BaseResponse<Boolean> joinTeam(@RequestBody JoinTeamRequest joinTeamRequest, HttpServletRequest request) {
        if (joinTeamRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (joinTeamRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = teamService.joinTeam(joinTeamRequest,userService.getLoginUser(request));
        return ResultUtils.success(result);
    }


}
