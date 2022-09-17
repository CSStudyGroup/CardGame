package com.csStudy.CardGame.domain.bookmark.service;

import com.csStudy.CardGame.domain.bookmark.entity.Bookmark;
import com.csStudy.CardGame.domain.bookmark.repository.BookmarkRepository;
import com.csStudy.CardGame.domain.category.entity.Category;
import com.csStudy.CardGame.domain.category.repository.CategoryRepository;
import com.csStudy.CardGame.domain.member.dto.MemberDetails;
import com.csStudy.CardGame.domain.member.entity.Member;
import com.csStudy.CardGame.domain.member.repository.MemberRepository;
import com.csStudy.CardGame.exception.ApiErrorEnums;
import com.csStudy.CardGame.exception.ApiErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class BookmarkServiceImpl implements BookmarkService{

    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;

    public BookmarkServiceImpl(
            BookmarkRepository bookmarkRepository,
            MemberRepository memberRepository,
            CategoryRepository categoryRepository
    ) {
        this.bookmarkRepository = bookmarkRepository;
        this.memberRepository = memberRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void addBookmark(Long categoryId) {
        Member member = memberRepository
                .findById(
                        ((MemberDetails) SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getPrincipal()).getId()
                )
                .orElseThrow(() -> ApiErrorException.createException(
                        ApiErrorEnums.RESOURCE_NOT_FOUND,
                        HttpStatus.NOT_FOUND,
                        null,
                        null
                ));

        Category category = categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> ApiErrorException.createException(
                        ApiErrorEnums.RESOURCE_NOT_FOUND,
                        HttpStatus.NOT_FOUND,
                        null,
                        null
                ));

        // 이미 북마크 되어있는 경우
        if (bookmarkRepository.existsByMemberAndCategory(member, category)) {
            throw ApiErrorException.createException(
                    ApiErrorEnums.RESOURCE_CONFLICT,
                    HttpStatus.CONFLICT,
                    null,
                    null
            );
        }

        bookmarkRepository.save(Bookmark.builder()
                .member(member)
                .category(category)
                .build());
    }

    @Override
    public void deleteBookmark(Long categoryId) {
        MemberDetails member = (MemberDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Bookmark target = bookmarkRepository
                .findByMember_IdAndCategoryId(member.getId(), categoryId)
                .orElseThrow(() -> ApiErrorException.createException(
                        ApiErrorEnums.RESOURCE_NOT_FOUND,
                        HttpStatus.NOT_FOUND,
                        null,
                        null
                ));
        bookmarkRepository.delete(target);
    }
}
