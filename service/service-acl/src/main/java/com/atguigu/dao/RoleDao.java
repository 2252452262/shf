package com.atguigu.dao;

import com.atguigu.base.BaseDao;
import com.atguigu.entity.Role;
import com.github.pagehelper.Page;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public interface RoleDao extends BaseDao<Role> {

    //模块特有的方法，声明到子接口中，公共的方法声明到父接口中。

    List<Role> findAll();

}
