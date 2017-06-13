package com.github.igorek2312.blog.app.repositories;

import com.github.igorek2312.blog.app.model.Post;
import com.github.igorek2312.blog.app.transfer.PostListItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author Igor Rybak
 */
public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query("select new com.github.igorek2312.blog.app.transfer.PostListItem(p.id,p.title,p.dateTimePublished) from Post p where " +
            "p.user.id=:userId " +
            "order by p.dateTimePublished desc")
    Page<PostListItem> findByUserId(@Param("userId") int userId, Pageable pageable);

    @Query("update Post set title=:title, content=:content where id=:postId")
    @Modifying
    void update(
            @Param("postId") int postId,
            @Param("title") String title,
            @Param("content") String content
    );
}