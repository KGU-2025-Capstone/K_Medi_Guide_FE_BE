package com.yakddok.k_medi_guide.repository;

import com.yakddok.k_medi_guide.entity.CardNews;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardNewsRepository extends MongoRepository<CardNews, String> {

}
