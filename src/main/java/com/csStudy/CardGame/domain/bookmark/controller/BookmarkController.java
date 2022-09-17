package com.csStudy.CardGame.domain.bookmark.controller;

import com.csStudy.CardGame.domain.bookmark.dto.NewBookmarkForm;
import com.csStudy.CardGame.domain.bookmark.service.BookmarkService;
import com.csStudy.CardGame.domain.category.dto.CategoryDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BookmarkController {

    private final BookmarkService bookmarkService;

    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @PostMapping("/bookmarks")
    public ResponseEntity<Void> bookmark(@RequestBody Long categoryId) {
        bookmarkService.addBookmark(categoryId);
        return ResponseEntity
                .ok()
                .body(null);
    }

    @DeleteMapping("/bookmarks")
    public ResponseEntity<Void> unbookmark(@RequestBody Long categoryId) {
        bookmarkService.deleteBookmark(categoryId);
        return ResponseEntity
                .ok()
                .body(null);
    }
}
