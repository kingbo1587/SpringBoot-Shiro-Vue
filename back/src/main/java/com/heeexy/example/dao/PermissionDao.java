package com.heeexy.example.dao;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Set;

/**
 * @author: hxy
 * @date: 2017/10/30 13:28
 */
public interface PermissionDao {
	/**
	 * 查询用户的角色 菜单 权限
	 */
	JSONObject getUserPermission(String username);

	/**
	 * 查询所有的菜单
	 */
	Set<String> getAllMenu();

	/**
	 * 查询所有的权限
	 */
	Set<String> getAllPermission();


	/**
	 * 权限总条数
	 */
	int countPermission();

	/**
	 * 查询所有权限信息
	 */
	List<JSONObject> listPermission(JSONObject jsonObject);

	/**
	 * 新增权限
	 */
	int insertPermission(JSONObject jsonObject);

	/**
	 * 修改权限
	 */
	int updatePermission(JSONObject jsonObject);

	/**
	 * 获取拥有此permissionId的用户权限
	 */
	List<JSONObject> getRolePermissionsByPermissionId(int permissionId);

	/**
	 * 删除权限
	 */
	int deletePermission(int id);

}
