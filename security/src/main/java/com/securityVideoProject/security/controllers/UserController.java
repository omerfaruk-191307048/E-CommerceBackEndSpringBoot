package com.securityVideoProject.security.controllers;

import com.securityVideoProject.security.business.UserService;
import com.securityVideoProject.security.core.utilities.results.DataResult;
import com.securityVideoProject.security.dto.request.UserRequestDto;
import com.securityVideoProject.security.dto.response.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/find-by-name-contains")
    public DataResult<List<UserResponseDto>> findUserByUserNameContains(@RequestParam String name) {
        return userService.findUserByUserNameContains(name);
    }

    @GetMapping("/find-by-name")
    public DataResult<List<UserResponseDto>> findUserByName(@RequestParam String name) {
        return userService.findUserByName(name);
    }

    @GetMapping
    public DataResult<List<UserResponseDto>> findAllUser() {
        return userService.findAllUser();
    }

    @GetMapping("/find-by-id")
    public DataResult<UserResponseDto> findUserById(Integer userId) {
        return userService.findUserById(userId);
    }

    @GetMapping("/find-by-created-at-between")
    public DataResult<List<UserResponseDto>> findUserByCreatedAtBetween(@RequestParam String startDate, @RequestParam String endDate) throws ParseException {
        return userService.findUserByCreatedAt(startDate, endDate);
    }

    @PutMapping
    public DataResult<UserResponseDto> updateUser(@RequestParam Integer id, @RequestBody UserRequestDto userRequestDto) {
        return userService.updateUser(id, userRequestDto);
    }

    @DeleteMapping
    public DataResult<UserResponseDto> deleteUserById(@RequestParam Integer id) {
        return userService.deleteUserById(id);
    }

}
