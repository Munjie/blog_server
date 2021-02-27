import com.munjie.blog.BlogApplication;
import com.munjie.blog.dao.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Auther: munjie
 * @Date: 2/22/2021 21:59
 * @Description:
 */
@Slf4j
@SpringBootTest(classes = BlogApplication.class)
@RunWith(SpringRunner.class)
public class ESTest {

    @Autowired
    private ArticleRepository articleRepository;



    /**
     * 删除所有
     */
    @Test
    public void deleteAllArticle() {
        articleRepository.deleteAll();
    }






}