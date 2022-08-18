package com.fastcampus.projectboard.repository;

import static org.assertj.core.api.Assertions.*;

import com.fastcampus.projectboard.config.JpaConfig;
import com.fastcampus.projectboard.domain.Article;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

//@ActiveProfiles("testdb")
//@AutoConfigureTestDatabase(replace = Replace.NONE)
@DisplayName("JPA 연결 테스트")
@Import(JpaConfig.class)
@DataJpaTest
class JPARepositoryTest {

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;

    public JPARepositoryTest(@Autowired ArticleRepository articleRepository, @Autowired ArticleCommentRepository articleCommentRepository) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
    }

    @DisplayName("Select 테스트")
    @Test
    void givenTestData_whenSelecting_thenWorksFine() {
        //Given

        //When
        List<Article> articles = articleRepository.findAll();
        //Then
        assertThat(articles)
            .isNotNull()
            .hasSize(123);
    }

    @DisplayName("Insert 테스트")
    @Test
    void givenTestData_whenInserting_thenWorksFine() {
        //Given
        long previousCount = articleRepository.count();
        Article article = articleRepository.save(Article.of("new article", "new content", "#spring"));

        //When
        List<Article> articles = articleRepository.findAll();
        //Then
        assertThat(articleRepository.count()).isEqualTo(previousCount + 1);
    }

    @DisplayName("Update 테스트")
    @Test
    void givenTestData_whenUpdating_thenWorksFine() {
        //Given
        Article article = articleRepository.findById(1L).orElseThrow();
        String updateHashtag = "#springBoot";
        article.setHashtag(updateHashtag);

        //When
        Article savedArticle = articleRepository.saveAndFlush(article);

        //Then
        assertThat(savedArticle).hasFieldOrPropertyWithValue("hashtag", updateHashtag);
    }

    @DisplayName("Delete 테스트")
    @Test
    void givenTestData_whenDeleting_thenWorksFine() {
        //Given
        Article article = articleRepository.findById(1L).orElseThrow();
        long previousArticleCount = articleRepository.count();
        long previousArticleCommentCount = articleCommentRepository.count();
        int deletedCommentsSize = article.getArticleComments().size();

        //When
        articleRepository.delete(article);

        //Then
        assertThat(articleRepository.count()).isEqualTo(previousArticleCount - 1);
        assertThat(articleCommentRepository.count()).isEqualTo(previousArticleCommentCount - deletedCommentsSize);
    }
}