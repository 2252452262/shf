package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.DictDao;
import com.atguigu.dao.HouseDao;
import com.atguigu.entity.House;
import com.atguigu.service.DictService;
import com.atguigu.service.HouseService;
import com.atguigu.vo.HouseQueryVo;
import com.atguigu.vo.HouseVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Service(interfaceClass = HouseService.class)
@Transactional
public class HouseServiceImpl extends BaseServiceImpl<House> implements HouseService {

    @Autowired
    HouseDao houseDao;

    @Autowired
    DictDao dictDao; //房源服务接口和数据字典接口在同一个服务应用中，直接调用dao层即可。无需远程调用。

    //@Reference
    //DictService dictService;

    @Override
    public BaseDao<House> getEntityDao() {
        return houseDao;
    }

    @Override
    public void publish(Long id, Integer status) {
        House house = new House();
        house.setId(id);
        house.setStatus(status);
        houseDao.update(house);
    }

    @Override
    public PageInfo<HouseVo> findListPage(int pageNum, int pageSize, HouseQueryVo houseQueryVo) {
        PageHelper.startPage(pageNum, pageSize);
        Page<HouseVo> page = houseDao.findListPage(houseQueryVo);
        List<HouseVo> list = page.getResult();
        for (HouseVo houseVo : list) {
            //户型：
            String houseTypeName = dictDao.getNameById(houseVo.getHouseTypeId());
            //楼层
            String floorName = dictDao.getNameById(houseVo.getFloorId());
            //朝向：
            String directionName = dictDao.getNameById(houseVo.getDirectionId());
            houseVo.setHouseTypeName(houseTypeName);
            houseVo.setFloorName(floorName);
            houseVo.setDirectionName(directionName);
        }
        return new PageInfo<HouseVo>(page, 10);
    }

    @Override
    public House getById(Serializable id) {
        House house = houseDao.getById(id);
        if (house != null) {
            if (null == house) return null;

            //户型：
            String houseTypeName = dictDao.getNameById(house.getHouseTypeId());
            //楼层
            String floorName = dictDao.getNameById(house.getFloorId());
            //建筑结构：
            String buildStructureName = dictDao.getNameById(house.getBuildStructureId());
            //朝向：
            String directionName = dictDao.getNameById(house.getDirectionId());
            //装修情况：
            String decorationName = dictDao.getNameById(house.getDecorationId());
            //房屋用途：
            String houseUseName = dictDao.getNameById(house.getHouseUseId());
            house.setHouseTypeName(houseTypeName);
            house.setFloorName(floorName);
            house.setBuildStructureName(buildStructureName);
            house.setDirectionName(directionName);
            house.setDecorationName(decorationName);
            house.setHouseUseName(houseUseName);
        }

        return house;
    }
}
