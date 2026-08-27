package com.example.InkHub_backend.service;

import com.example.InkHub_backend.dto.LoginDTO;
import com.example.InkHub_backend.dto.RegisterDTO;
import com.example.InkHub_backend.vo.LoginVO;

public interface UserService {

    void register(RegisterDTO dto);

    LoginVO login(LoginDTO dto);
}