package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Admin;
import com.atguigu.entity.HouseBroker;
import com.atguigu.service.AdminService;
import com.atguigu.service.HouseBrokerService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author rzcl
 * @description
 */
@Controller
@RequestMapping("/houseBroker")
public class HouseBrokerController extends BaseController {
    private final static String LIST_ACTION = "redirect:/house/detail/";
    private final static String PAGE_CREATE = "houseBroker/create";
    private final static String PAGE_EDIT = "houseBroker/edit";

    @Reference
    private HouseBrokerService houseBrokerService;
    @Reference
    private AdminService adminService;

    @PostMapping("/save")
    public String save(HouseBroker houseBroker, HttpServletRequest request) {
        Admin admin = adminService.getById(houseBroker.getBrokerId());
        houseBroker.setBrokerName(admin.getName());
        houseBroker.setBrokerHeadUrl(admin.getHeadUrl());
        houseBrokerService.insert(houseBroker);
        return this.successPage(null, request);
    }

    @GetMapping("/delete/{HouseId}/{id}")
    public String delete(@PathVariable("HouseId") Long HouseId, @PathVariable("id") Long id) {
        houseBrokerService.delete(id);
        return LIST_ACTION + HouseId;
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Long id, HouseBroker houseBroker, HttpServletRequest request) {
        HouseBroker currentHouseBroker = houseBrokerService.getById(id);
        Admin admin = adminService.getById(houseBroker.getBrokerId());
        currentHouseBroker.setBrokerId(houseBroker.getBrokerId());
        currentHouseBroker.setBrokerName(admin.getName());
        currentHouseBroker.setBrokerHeadUrl(admin.getHeadUrl());
        houseBrokerService.update(currentHouseBroker);
        return this.successPage(null, request);
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable("id") Long id) {
        List<Admin> adminList = adminService.findAll();
        HouseBroker houseBroker = houseBrokerService.getById(id);
        model.addAttribute("adminList", adminList);
        model.addAttribute("houseBroker", houseBroker);
        return PAGE_EDIT;
    }

    @GetMapping("/create")
    public String create(Model model, HouseBroker houseBroker) {
        List<Admin> adminList = adminService.findAll();
        model.addAttribute("adminList", adminList);
        model.addAttribute("houseBroker", houseBroker);
        return PAGE_CREATE;
    }
}
