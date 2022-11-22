package com.mylog.post.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mylog.post.dto.CategorySaveRequest;
import com.mylog.post.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = ApiCategoryController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@Slf4j
class ApiCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    String email = "temp";

    @Test
    void allCategory() throws Exception {
        //Given

        //When&Then
        mockMvc.perform(get("/api/mypage/category/" + email))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
//                .andExpect(jsonPath("$.data").isArray());
//                .andExpect(jsonPath("$.data[0].id").value(1L))
//                .andExpect(jsonPath("$.data[0].name").value("db"));
    }

    @Test
    void addCategoryName() throws Exception {
        //Given

        CategorySaveRequest request = new CategorySaveRequest();
        request.setEmail(email);
        request.setName("카테고리");

        //When&Then
        mockMvc.perform(post("/api/mypage/category")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.result").value("success"));
    }

    @Test
    void removeCategory() throws Exception {
        //Given

        //When&Then
        mockMvc.perform(delete("/api/mypage/category/"+3L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("success"))
        .andDo(MockMvcResultHandlers.print());
    }
}