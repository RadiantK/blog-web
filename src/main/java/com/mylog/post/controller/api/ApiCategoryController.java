package com.mylog.post.controller.api;

import com.mylog.global.argumentResolver.LoginMember;
import com.mylog.member.dto.MemberLoginResponse;
import com.mylog.post.domain.Category;
import com.mylog.post.dto.CategoryDataResponse;
import com.mylog.post.dto.CategorySaveRequest;
import com.mylog.post.service.CategoryService;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
@Slf4j
public class ApiCategoryController {

    private final CategoryService categoryService;

    @GetMapping("/category")
    public ResponseEntity<List<CategoryDataResponse>> allCategory(@LoginMember MemberLoginResponse member) {
        List<CategoryDataResponse> list = categoryService.findAllCategory(member.getEmail())
                .stream()
                .map(c -> CategoryDataResponse.of(c))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(list);
    }

    @PostMapping("/category")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResult addCategoryName(@RequestBody CategorySaveRequest category) {
        log.info("name : {}", category);
        if (!StringUtils.hasText(category.getName())) {
            throw new IllegalArgumentException("카테고리명을 입력하세요.");
        }
        categoryService.saveCategory(category);

        return CategoryResult.builder()
                .result("success")
                .build();
    }

    @DeleteMapping("/category/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryResult removeCategory(@PathVariable Long id) {
        log.info("category : {}", id);
        categoryService.removeCategory(id);

        return CategoryResult.builder()
                .result("success")
                .build();
    }


    @Data
    @Builder
    static class CategoryResult {
        private String result;
    }
}
