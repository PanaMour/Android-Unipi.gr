package com.unipi.softeng.repository;

import com.unipi.softeng.model.News;
import com.unipi.softeng.model.NewsType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.Collection;
import java.util.List;

public interface NewsRepo extends JpaRepository<News, Long> {

    Collection<News> findByDate(Date date);

    List<News> findByType(NewsType type);

    Page<News> findAllByTypeOrderByDateDesc(NewsType type,Pageable pageable);

}
