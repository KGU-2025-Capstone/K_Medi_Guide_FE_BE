package com.yakddok.k_medi_guide.repository;

import com.yakddok.k_medi_guide.entity.TranslateCard;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TranslateCardRepository extends MongoRepository<TranslateCard, String> {
}
