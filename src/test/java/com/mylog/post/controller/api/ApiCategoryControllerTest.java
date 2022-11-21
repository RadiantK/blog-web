package com.mylog.post.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mylog.member.domain.Address;
import com.mylog.member.domain.GenderType;
import com.mylog.member.domain.Member;
import com.mylog.member.domain.RoleType;
import com.mylog.member.repository.MemberRepository;
import com.mylog.post.domain.Category;
import com.mylog.post.domain.Post;
import com.mylog.post.dto.CategorySaveRequest;
import com.mylog.post.repository.CategoryRepository;
import com.mylog.post.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Disabled
@WebMvcTest(value = ApiCategoryController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@Slf4j
class ApiCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private MemberRepository memberRepository;
    @MockBean
    private CategoryRepository categoryRepository;

    Member member;
    String email = "temp";

    void setup() {
        if (memberRepository.findByEmailAndEnabled(email, 1).orElse(null) != null) {
            member = Member.builder()
                    .email(email)
                    .password(passwordEncoder.encode("1234"))
                    .name("김자바")
                    .gender(GenderType.MALE)
                    .phone("010-1234-4564")
                    .address(new Address("1234", "서울시", "22층"))
                    .role(RoleType.ROLE_MEMBER)
                    .enabled(1)
                    .build();

            memberRepository.save(member);

            Category category = new Category("db", member);
            Category category2 = new Category("spring", member);
            Category category3 = new Category("data", member);
            categoryRepository.save(category);
            categoryRepository.save(category2);
            categoryRepository.save(category3);
        }

    }

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
        setup();

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
        setup();

        //When&Then
        mockMvc.perform(delete("/api/mypage/category/"+3L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("success"));
    }
}