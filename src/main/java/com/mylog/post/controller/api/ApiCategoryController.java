package com.mylog.post.controller.api;

import com.mylog.post.dto.CategoryDataResponse;
import com.mylog.post.dto.CategorySaveRequest;
import com.mylog.post.service.CategoryService;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
@Slf4j
public class ApiCategoryController {

    private final CategoryService categoryService;

    @GetMapping("/category/{email}")
    public ResponseEntity<List<CategoryDataResponse>> allCategory(@PathVariable String email) {
        List<CategoryDataResponse> list = categoryService.findAllCategory(email)
                .stream()
                .map(c -> CategoryDataResponse.of(c))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(list);
    }

    @PostMapping("/category")
    public ResponseEntity<CategoryResult> addCategoryName(@Valid @RequestBody CategorySaveRequest category, BindingResult bindingResult) {
        log.info("name : {}", category);

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(CategoryResult.builder()
                    .result("닉네임을 입력해주세요.")
                    .build());
        }
        categoryService.saveCategory(category);

        return new ResponseEntity(CategoryResult.builder().result("success").build(),
                HttpStatus.CREATED);
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
