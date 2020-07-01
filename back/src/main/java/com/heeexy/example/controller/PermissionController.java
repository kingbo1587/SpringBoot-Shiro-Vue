package com.heeexy.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.PermissionService;
import com.heeexy.example.util.CommonUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    /**
     * 查询所有权限
     */
    @RequiresPermissions("permission:list")
    @GetMapping("/list")
    public JSONObject listPermission(HttpServletRequest request) {
        return permissionService.listPermission(CommonUtil.request2Json(request));
    }

    /**
     * 新增权限
     */
    @RequiresPermissions("permission:add")
    @PostMapping("/add")
    public JSONObject addPermission(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "id,menuCode,menuName,permissionCode,requiredPermission");
        return permissionService.addPermission(requestJson);
    }

    /**
     * 更新权限
     */
    @RequiresPermissions("permission:update")
    @PostMapping("/update")
    public JSONObject updatePermission(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "id,menuCode,menuName,permissionCode,requiredPermission");
        return permissionService.updatePermission(requestJson);
    }

    /**
     * 删除权限
     */
    @RequiresPermissions("permission:delete")
    @PostMapping("/delete")
    public JSONObject deletePermission(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "id");
        int id = requestJson.getInteger("id");
        return permissionService.deletePermission(id);
    }

}
