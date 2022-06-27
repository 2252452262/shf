package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Permission;
import com.atguigu.service.PermissionService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value="/permission")
public class PermissionController extends BaseController {

    @Reference
    private PermissionService permissionService;

    private final static String LIST_ACTION = "redirect:/permission";

    private final static String PAGE_INDEX = "permission/index";
    private final static String PAGE_CREATE = "permission/create";
    private final static String PAGE_EDIT = "permission/edit";

    /**
     * 获取菜单
     * @return
     */
    @GetMapping
    public String index(ModelMap model) {
        List<Permission> list = permissionService.findAllMenu();
        model.addAttribute("list", list);
        return PAGE_INDEX;
    }

    /**
     * 进入新增
     * @param model
     * @param permission
     * @return
     */
    @GetMapping("/create")
    public String create(ModelMap model, Permission permission) {
        model.addAttribute("permission",permission);
        return PAGE_CREATE;
    }

    /**
     * 保存新增
     * @param model
     * @param permission
     * @param request
     * @return
     */
    @PostMapping("/save")
    public String save(ModelMap model, Permission permission, HttpServletRequest request) {
        //SysUser user = this.currentSysUser(request);
        permissionService.insert(permission);
        return this.successPage(this.MESSAGE_SUCCESS, request);
    }

    /**
     * 编辑
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/edit/{id}")
    public String edit(ModelMap model,@PathVariable Long id) {
        Permission permission = permissionService.getById(id);
        model.addAttribute("permission",permission);
        return PAGE_EDIT;
    }

    /**
     * 保存更新
     * @param model
     * @param id
     * @param permission
     * @param request
     * @return
     */
    @PostMapping(value="/update/{id}")
    public String update(ModelMap model, @PathVariable Long id, Permission permission, HttpServletRequest request) {
        permission.setId(id);
        permissionService.update(permission);
        return this.successPage(this.MESSAGE_SUCCESS, request);
    }

    /**
     * 删除
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public String delete(ModelMap model,@PathVariable Long id) {
        permissionService.delete(id);
        return LIST_ACTION;
    }

}