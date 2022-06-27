package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.AdminRoleDao;
import com.atguigu.dao.RoleDao;
import com.atguigu.entity.AdminRole;
import com.atguigu.entity.Role;
import com.atguigu.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = RoleService.class)
@Transactional
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {

    @Autowired  //Spring框架提供的依赖注入注解   先byType再byName
    //@Resource //JDK提供的依赖注入注解   先byName再byType
    RoleDao roleDao;

    @Autowired
    private AdminRoleDao adminRoleDao;

    @Override
    public BaseDao<Role> getEntityDao() {
        return roleDao;
    }

    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }

    //@Override
    public Map<String, Object> findRoleByAdminId(Long adminId) {
        //查询所有的角色
        List<Role> allRolesList = roleDao.findAll();

        //拥有的角色id
        List<Long> existRoleIdList = adminRoleDao.findRoleIdByAdminId(adminId);

        //对角色进行分类
        List<Role> noAssignRoleList = new ArrayList<>();
        List<Role> assignRoleList = new ArrayList<>();
        for (Role role : allRolesList) {
            //已分配
            if (existRoleIdList.contains(role.getId())) {
                assignRoleList.add(role);
            }
            else {
                noAssignRoleList.add(role);
            }
        }

        Map<String, Object> roleMap = new HashMap<>();
        roleMap.put("noAssignRoleList", noAssignRoleList);
        roleMap.put("assignRoleList", assignRoleList);
        return roleMap;
    }

    @Override
    public void saveUserRoleRelationShip(Long adminId, Long[] roleIds) {
        adminRoleDao.deleteByAdminId(adminId);

        List<AdminRole> userRoleList = new ArrayList<>();
        for (Long roleId : roleIds) {
            if (StringUtils.isEmpty(roleId)) continue;
            AdminRole userRole = new AdminRole();
            userRole.setAdminId(adminId);
            userRole.setRoleId(roleId);
            userRoleList.add(userRole);
        }
        adminRoleDao.insertBatch(userRoleList);
    }
}
