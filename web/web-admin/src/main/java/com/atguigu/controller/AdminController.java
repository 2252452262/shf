package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Admin;
import com.atguigu.service.AdminService;
import com.atguigu.util.QiniuUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;


// ctrl + r  查找替换
@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {

    public static final String PAGE_INDEX = "admin/index";
    public static final String PAGE_CREATE = "admin/create";
    public static final String ACTION_LIST = "redirect:/admin";
    public static final String PAGE_EDIT = "admin/edit";
    private final static String PAGE_UPLOED_SHOW = "admin/upload";

    @Reference
    AdminService adminService;

    @GetMapping("/uploadShow/{id}")
    public String uploadShow(ModelMap model, @PathVariable Long id) {
        model.addAttribute("id", id);
        return PAGE_UPLOED_SHOW;
    }

    @PostMapping("/upload/{id}")
    public String upload(@PathVariable Long id, @RequestParam(value = "file") MultipartFile file, HttpServletRequest request) throws IOException, IOException {
        String newFileName = UUID.randomUUID().toString();
        // 上传图片
        QiniuUtils.upload2Qiniu(file.getBytes(), newFileName);
        String url = "http://rdv2iuini.hb-bkt.clouddn.com/" + newFileName;
        Admin admin = new Admin();
        admin.setId(id);
        admin.setHeadUrl(url);
        adminService.update(admin);
        return this.successPage(this.MESSAGE_SUCCESS, request);
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        Integer count = adminService.delete(id); //返回结果表示sql语句对数据库起作用的行数
        return ACTION_LIST;
    }

    @RequestMapping("/update")
    public String update(Admin admin, Map map, HttpServletRequest request) { //springMVC框架根据反射创建bean对象，并调用参数名称的set方法，将参数封装到bean对象中。
        adminService.update(admin);
        return this.successPage("修改成功,哈哈", request);
    }

    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Map map) {
        Admin admin = adminService.getById(id);
        map.put("admin", admin);
        return PAGE_EDIT;
    }

    @RequestMapping("/save")
    public String save(Admin admin, Map map, HttpServletRequest request) { //springMVC框架根据反射创建bean对象，并调用参数名称的set方法，将参数封装到bean对象中。
        admin.setHeadUrl("http://47.93.148.192:8080/group1/M00/03/F0/rBHu8mHqbpSAU0jVAAAgiJmKg0o148.jpg");
        adminService.insert(admin);
        return this.successPage("添加成功,哈哈", request);
    }

    @RequestMapping("/create")
    public String create() {
        return PAGE_CREATE;
    }

    /**
     * 分页查询
     * 根据查询条件进行查询
     * adminName = ''
     * pageNum = 1   隐藏域
     * pageSize = 10  隐藏域
     *
     * @param map
     * @return
     */
    @RequestMapping
    public String index(HttpServletRequest request, Map map) {
        Map<String, Object> filters = getFilters(request);
        //分页对象，将集合数据，pageNum,pageSize,pages,total等
        PageInfo<Admin> pageInfo = adminService.findPage(filters);
        map.put("page", pageInfo);
        map.put("filters", filters);
        return PAGE_INDEX;
    }

}
