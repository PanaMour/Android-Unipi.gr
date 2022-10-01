package com.unipi.softeng.service;

import com.unipi.softeng.model.Admin;

public interface AdminService {

    Admin login(String username, String password);

    Boolean register(Admin admin);



}
