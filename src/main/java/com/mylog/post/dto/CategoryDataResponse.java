package com.mylog.post.dto;

import com.mylog.post.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDataResponse {

    private Long id;
    private String name;

    public static CategoryDataResponse of(Category category) {
        return new CategoryDataResponse(category.getId(), category.getName());
    }
}
