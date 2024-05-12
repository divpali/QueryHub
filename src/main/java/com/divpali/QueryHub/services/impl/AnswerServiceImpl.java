package com.divpali.QueryHub.services.impl;

import com.divpali.QueryHub.entities.Answer;
import com.divpali.QueryHub.repository.AnswerRepository;
import com.divpali.QueryHub.services.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnswerServiceImpl implements AnswerService {

    @Autowired
    private AnswerRepository answerRepository;

    @Override
    public Answer save(Answer answer) {
        return answerRepository.save(answer);
    }
}
