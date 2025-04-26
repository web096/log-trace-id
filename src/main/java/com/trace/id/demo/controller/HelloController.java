package com.trace.id.demo.controller;

import com.trace.id.demo.common.response.CommonBody;
import com.trace.id.demo.common.response.PageBody;
import com.trace.id.demo.common.response.ResultResponse;
import com.trace.id.demo.dtos.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/hello")
public class HelloController {

    private final ResultResponse resultResponse;

    @GetMapping()
    public String getHello() {
        //CommonResponseAdvice이용
        return "Hello World";
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonBody<UserDto>> getUser(@PathVariable Long id) {
        // 여기서는 그냥 테스트용으로 가짜 데이터 리턴
        UserDto user = new UserDto(id, "홍길동", "hong@example.com");

        // Controller에서도 ResponseEntity 직접 리턴
        return resultResponse.success(user);
    }

    @GetMapping("/raw")
    public UserDto rawUser() {
        // 객체를 직접 리턴 CommonResponseAdvice이용
        return new UserDto(1L, "임꺽정", "lim@example.com");

    }

    @GetMapping("/page")
    public ResponseEntity<PageBody<List<UserDto>>> rawUserPage() {

        List<UserDto> userList = List.of(
                new UserDto(1L, "홍길동", "hong@example.com"),
                new UserDto(2L, "홍길동", "hong@example.com"),
                new UserDto(3L, "홍길동", "hong@example.com")
        );

        return resultResponse.success(userList, 2L, 10, 10L);
    }
}
