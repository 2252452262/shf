package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.UserFollow;
import com.atguigu.vo.UserFollowVo;
import com.github.pagehelper.PageInfo;

public interface UserFollowService extends BaseService<UserFollow> {

    Boolean follow(Long userId, Long houseId);

    Boolean isFollow(Long userId, Long houseId);

    Boolean cancelFollow(Long id);

    PageInfo<UserFollowVo> findListPage(int pageNum, int pageSize, Long userId);
}
