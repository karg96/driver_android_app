package cos.tuk_tuk_driver.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class ArticleViewModel extends AndroidViewModel {


    public ArticleViewModel(@NonNull Application application) {
        super(application);

//        articleRepository = new ArticleRepository();
//        this.articleResponseLiveData = articleRepository.getMovieArticles(ARTICLE_QUERY, API_KEY);
    }
}
