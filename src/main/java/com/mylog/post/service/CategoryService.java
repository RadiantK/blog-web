package com.mylog.post.service;

import com.mylog.member.domain.Member;
import com.mylog.member.exception.MemberNotFoundException;
import com.mylog.member.repository.MemberRepository;
import com.mylog.post.controller.api.ApiCategoryController;
import com.mylog.post.domain.Category;
import com.mylog.post.dto.CategorySaveRequest;
import com.mylog.post.exception.DuplicateCategoryNameException;
import com.mylog.post.repository.CategoryRepository;
import com.mylog.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public Category findCategory(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public Category saveCategory(CategorySaveRequest category) {
        Member member = getMember(category.getEmail());

        Category dupleCategory = categoryRepository.findByNameAndMember(category.getName(), member).orElse(null);
        if (dupleCategory != null) {
            throw new DuplicateCategoryNameException();
        }

        return categoryRepository.save(new Category(category.getName(), member));
    }

    public List<Category> findAllCategory(String email) {
        Member member = getMember(email);

        return categoryRepository.findAllByMember(member);
    }

    private Member getMember(String email) {
        return memberRepository.findByEmailAndEnabled(email, 1)
                .orElseThrow(MemberNotFoundException::new);
    }

    public void removeCategory(Long id) {
        Category category = categoryRepository.findById(id).orElse(null);

        categoryRepository.delete(category);
    }
}
