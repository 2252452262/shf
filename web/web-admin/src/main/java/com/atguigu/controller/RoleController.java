package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Role;
import com.atguigu.service.RoleService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {

    private static final String PAGE_INDEX = "role/index";
    private static final String PAGE_CREATE = "role/create";
    private static final String ACTION_LIST = "redirect:/role";
    private static final String PAGE_EDIT = "role/edit";

    @Reference
    RoleService roleService;


    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        Integer count = roleService.delete(id); //返回结果表示sql语句对数据库起作用的行数
        return ACTION_LIST;
    }

    @RequestMapping("/update")
    public String update(Role role,Map map,HttpServletRequest request){ //springMVC框架根据反射创建bean对象，并调用参数名称的set方法，将参数封装到bean对象中。
        roleService.update(role);
        return this.successPage("修改成功,哈哈",request);
    }

    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Map map){
        Role role = roleService.getById(id);
        map.put("role",role);
        return PAGE_EDIT;
    }

    @RequestMapping("/save")
    public String save(Role role,Map map,HttpServletRequest request){ //springMVC框架根据反射创建bean对象，并调用参数名称的set方法，将参数封装到bean对象中。
        roleService.insert(role);
        return this.successPage("添加成功,哈哈",request);
    }

   /* @RequestMapping("/save")
    public String save(Role role,Map map){ //springMVC框架根据反射创建bean对象，并调用参数名称的set方法，将参数封装到bean对象中。
        roleService.insert(role);
        //return ACTION_LIST;
        map.put("messagePage","添加成功,哈哈");
        return PAGE_SUCCESS;
    }*/

    @RequestMapping("/create")
    public String create(){
        return PAGE_CREATE;
    }

    /*@RequestMapping
    public String index(Map map){
        List<Role> list = roleService.findAll();

        map.put("list",list);

        return PAGE_INDEX;
    }*/

    /**
     * 分页查询
     *      根据查询条件进行查询
     *          roleName = ''
     *          pageNum = 1   隐藏域
     *          pageSize = 10  隐藏域
     * @param map
     * @return
     */
    @RequestMapping
    public String index(HttpServletRequest request,Map map){
        Map<String,Object> filters =  getFilters(request);
        //分页对象，将集合数据，pageNum,pageSize,pages,total等
        PageInfo<Role> pageInfo = roleService.findPage(filters);
        map.put("page",pageInfo);
        map.put("filters",filters);
        return PAGE_INDEX;
    }

}
