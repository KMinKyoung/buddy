package me.minkyoung.buddy_back.controller;

import lombok.RequiredArgsConstructor;
import me.minkyoung.buddy_back.dto.CommentRequest;
import me.minkyoung.buddy_back.dto.CommentResponse;
import me.minkyoung.buddy_back.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentResponse> createComment(@PathVariable Long postId, @RequestBody CommentRequest request, Authentication authentication){
        CommentResponse response = commentService.addComment(postId,request,authentication);
        return  ResponseEntity.ok(response);
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> getComment(@PathVariable Long postId){
        List<CommentResponse> response = commentService.getAllComments(postId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable Long postId, @PathVariable Long commentId, @RequestBody CommentRequest request, Authentication authentication){
        CommentResponse response = commentService.updateComment(postId,commentId,request,authentication);
        return  ResponseEntity.ok(response);
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long postId, @PathVariable Long commentId, Authentication authentication){
        commentService.deleteComment(postId,commentId,authentication);
        return ResponseEntity.ok().build();
    }
}
