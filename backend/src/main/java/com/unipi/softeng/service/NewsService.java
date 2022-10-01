package com.unipi.softeng.service;

import com.unipi.softeng.model.News;
import com.unipi.softeng.model.NewsType;
import org.springframework.data.domain.Page;

import java.util.List;

public interface NewsService {

    List<News> findAllNews();

    Page<News> findNews_Paginated(int offset, int pageSize);

    List<News> findAllNewsType(NewsType type);

    Page<News> findNewsType_Paginated(NewsType type, int offset, int pageSize);

    News findNews(Long id);

    News addNews(News news);

    Boolean deleteNews(Long id);

    News editNews(News news);

}
