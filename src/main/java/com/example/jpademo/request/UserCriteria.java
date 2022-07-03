package com.example.jpademo.request;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCriteria {
	@Parameter(description = "Filter users by concrete ids")
	private Set<Long> ids = new HashSet<>();
	@Parameter(description = "Filter by shift dates are after given date", example = "2022-01-07T00:00:00")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime hasShiftAfter;
}
