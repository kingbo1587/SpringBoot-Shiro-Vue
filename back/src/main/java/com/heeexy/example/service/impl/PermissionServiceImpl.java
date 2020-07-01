package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.PermissionDao;
import com.heeexy.example.service.PermissionService;
import com.heeexy.example.util.CommonUtil;
import com.heeexy.example.util.constants.ErrorEnum;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * @author: hxy
 * @date: 2017/10/30 13:15
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;

    /**
     * 查询某用户的 角色  菜单列表   权限列表
     */
    @Override
    public JSONObject getUserPermission(String username) {
        JSONObject userPermission = getUserPermissionFromDB(username);
        return userPermission;
    }

    /**
     * 从数据库查询用户权限信息
     */
    private JSONObject getUserPermissionFromDB(String username) {
        JSONObject userPermission = permissionDao.getUserPermission(username);
        //管理员角色ID为1
        int adminRoleId = 1;
        //如果是管理员
        String roleIdKey = "roleId";
        if (adminRoleId == userPermission.getIntValue(roleIdKey)) {
            //查询所有菜单  所有权限
            Set<String> menuList = permissionDao.getAllMenu();
            Set<String> permissionList = permissionDao.getAllPermission();
            userPermission.put("menuList", menuList);
            userPermission.put("permissionList", permissionList);
        }
        return userPermission;
    }

    /**
     * 查询所有权限, 给角色分配权限时调用
     */
    @Override
    public JSONObject listPermission(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        int count = permissionDao.countPermission();
        List<JSONObject> list = permissionDao.listPermission(jsonObject);
        return CommonUtil.successPage(jsonObject, list, count);
    }

    /**
     * 新增权限
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public JSONObject addPermission(JSONObject jsonObject) {
        try {
            permissionDao.insertPermission(jsonObject);
        } catch (DuplicateKeyException dye) {
            return CommonUtil.errorJson(ErrorEnum.E_10011, "权限id已存在");
        }
        return CommonUtil.successJson();
    }

    /**
     * 修改权限
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public JSONObject updatePermission(JSONObject jsonObject) {
        permissionDao.updatePermission(jsonObject);
        return CommonUtil.successJson();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JSONObject deletePermission(int id) {
        List<JSONObject> rolePermissions = permissionDao.getRolePermissionsByPermissionId(id);
        if (CollectionUtils.isNotEmpty(rolePermissions)) {
            return CommonUtil.errorJson(ErrorEnum.E_10010);
        }
        // 删除权限
        permissionDao.deletePermission(id);
        return CommonUtil.successJson();
    }

}
