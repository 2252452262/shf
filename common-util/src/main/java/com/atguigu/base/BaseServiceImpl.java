package com.atguigu.base;

import com.atguigu.util.CastUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.Map;

public abstract class BaseServiceImpl<T> implements BaseService<T>{

    public abstract BaseDao<T> getEntityDao();

    @Override
    public Integer insert(T t) {
        return getEntityDao().insert(t);
    }

    @Override
    public T getById(Serializable id) {
        return getEntityDao().getById(id);
    }

    @Override
    public void update(T t) {
        getEntityDao().update(t);
    }

    @Override
    public Integer delete(Serializable id) {
        return getEntityDao().delete(id);
    }

    @Override
    public PageInfo<T> findPage(Map<String, Object> filters) {
        int pageNum = CastUtil.castInt(filters.get("pageNum"),1);
        int pageSize = CastUtil.castInt(filters.get("pageSize"),2);

        //开启分页功能，将这两个参数，与当前线程进行绑定，传递给dao层。
        // startIndex = (pageNum-1)*pageSize
        // select 语句 最后，自动增加 limit ?,?         limit startIndex,pageSIze
        PageHelper.startPage(pageNum,pageSize); // ThreadLocal

        Page<T> page = getEntityDao().findPage(filters);
        return new PageInfo(page,5);
    }
}
