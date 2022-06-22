package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.HouseImage;
import com.atguigu.result.Result;
import com.atguigu.service.HouseImageService;
import com.atguigu.util.QiniuUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

/**
 * @author rzcl
 * @description
 */
@Controller
@RequestMapping("/houseImage")
public class HouseImageController extends BaseController {
    private static final String PAGE_UPLOAD_SHOW = "house/upload";
    private static final String ACTION_LIST = "redirect:/house/detail/";

    @Reference
    private HouseImageService houseImageService;

    @GetMapping("/delete/{houseId}/{id}")
    public String uploadshow(@PathVariable("houseId") Long houseId,
                             @PathVariable("id") Long id) {
        HouseImage houseImage = houseImageService.getById(id);
        String imageName = houseImage.getImageName();
        houseImageService.delete(id);
        QiniuUtils.deleteFileFromQiniu(imageName);
        return ACTION_LIST + houseId;
    }

    @GetMapping("/uploadShow/{houseId}/{type}")
    public String uploadshow(@PathVariable("houseId") Long houseId,
                             @PathVariable("type") Integer type,
                             Model model) {
        model.addAttribute("houseId", houseId);
        model.addAttribute("type", type);

        return PAGE_UPLOAD_SHOW;
    }

    @PostMapping("/upload/{houseId}/{type}")
    @ResponseBody
    public Result upload(@RequestParam("file") MultipartFile[] files,
                         HttpServletRequest request,
                         @PathVariable("houseId") Long houseId,
                         @PathVariable("type") Integer type) throws IOException {
        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                byte[] bytes = file.getBytes();
//                String originalFilename = file.getOriginalFilename();
//                String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
                String filename = UUID.randomUUID().toString().replace("-", "");
                QiniuUtils.upload2Qiniu(bytes, filename);
                HouseImage houseImage = new HouseImage();
                houseImage.setHouseId(houseId);
                houseImage.setImageName(filename);
                houseImage.setType(type);
                houseImage.setImageUrl("http://rdv2iuini.hb-bkt.clouddn.com/" + filename);
                houseImageService.insert(houseImage);
            }
        }
        return Result.ok();
    }
}
