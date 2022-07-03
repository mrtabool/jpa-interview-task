package com.example.jpademo.controller;

import com.example.jpademo.entity.User;
import com.example.jpademo.request.UserCriteria;
import com.example.jpademo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	/////////////////////////////////
	// ⚠️ Задания
	// TODO: 1. Исправить проблему n+1 запроса
	// TODO: 2. Добавить фильтрацию по полю ids из UserCriteria
	// TODO: 3. Добавить фильтрацию по полю hasShiftAfter из UserCriteria
	/////////////////////////////////

	@GetMapping("/search")
	@ResponseBody
	public List<User> search(@ParameterObject UserCriteria criteria) {
		return userService.search(criteria);
	}
}
