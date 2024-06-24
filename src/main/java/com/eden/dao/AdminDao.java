package com.eden.dao;

import com.eden.entity.Admin;

public interface AdminDao {

	Admin findByAdminName(String adminname);

	void save(Admin admin);
}
