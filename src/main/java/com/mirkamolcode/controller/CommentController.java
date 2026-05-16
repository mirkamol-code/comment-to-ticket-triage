package com.mirkamolcode.controller;

import com.mirkamolcode.dto.CommentResponse;
import com.mirkamolcode.dto.CreateCommentRequest;
import com.mirkamolcode.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public CommentResponse create(@Valid @RequestBody CreateCommentRequest request){
        return commentService.save(request);
    }

    @GetMapping
    public List<CommentResponse> getAll(){
        return commentService.getAll();
    }
}
