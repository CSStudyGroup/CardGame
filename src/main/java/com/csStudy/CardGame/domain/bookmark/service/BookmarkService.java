package com.csStudy.CardGame.domain.bookmark.service;

import com.csStudy.CardGame.domain.bookmark.dto.NewBookmarkForm;

import java.util.UUID;

public interface BookmarkService {
    void addBookmark(Long categoryId);
    void deleteBookmark(Long categoryId);

}
