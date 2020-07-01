package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: hxy
 * @date: 2017/10/30 13:10
 */
public interface PermissionService {
	/**
	 * 查询某用户的 角色  菜单列表   权限列表
	 */
	JSONObject getUserPermission(String username);

    JSONObject listPermission(JSONObject jsonObject);

	@Transactional(rollbackFor = Exception.class)
	JSONObject addPermission(JSONObject jsonObject);

	@Transactional(rollbackFor = Exception.class)
	JSONObject updatePermission(JSONObject jsonObject);

	@Transactional(rollbackFor = Exception.class)
	JSONObject deletePermission(int id);
}
