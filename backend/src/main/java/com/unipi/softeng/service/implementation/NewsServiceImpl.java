package com.unipi.softeng.service.implementation;

import com.unipi.softeng.model.News;
import com.unipi.softeng.model.NewsType;
import com.unipi.softeng.repository.NewsRepo;
import com.unipi.softeng.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor @Service @Transactional @Slf4j
public class NewsServiceImpl implements NewsService {

    private final NewsRepo newsRepo;

    @Override
    public List<News> findAllNews() {
        log.info("Fetching all news");
        return newsRepo.findAll();
    }



    @Override
    public Page<News> findNews_Paginated(int offset, int pageSize) {
        return newsRepo.findAll(PageRequest.of(offset,pageSize));
    }

    @Override
    public List<News> findAllNewsType(NewsType type) {
        return newsRepo.findByType(type);
    }

    @Override
    public Page<News> findNewsType_Paginated(NewsType type, int offset, int pageSize) {
        return newsRepo.findAllByTypeOrderByDateDesc(type,PageRequest.of(offset,pageSize));
    }

    @Override
    public News findNews(Long id) {
        Optional<News> news = newsRepo.findById(id);
        return news.orElse(null);
    }

    @Override
    public News addNews(News news) {
        log.info("Adding news");
        return newsRepo.save(news);
    }

    @Override
    public Boolean deleteNews(Long id) {
        log.info("Deleting news # {}", id.toString());
        newsRepo.deleteById(id);
        return Boolean.TRUE;
    }

    @Override
    public News editNews(News news) {
        log.info("Editing news # {}",news.getId().toString());
        return newsRepo.save(news);
    }
}
