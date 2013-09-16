package com.ebschool.ejb.service;

import com.ebschool.ejb.model.ClassInfo;
import com.ebschool.ejb.model.Test;
import com.ebschool.ejb.repo.TestRepository;

import javax.ejb.*;
import javax.inject.Inject;
import java.util.List;
import java.util.Set;

import static com.ebschool.ejb.utils.QueryParameter.*;

/**
 * User: michau
 * Date: 5/19/13
 */
@Stateless
@Local(TestService.class)
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class TestServiceImpl implements TestService {

    @Inject
    TestRepository testRepository;

    @Override
    public Test getById(Long id) {
        return testRepository.getById(id);
    }

    @Override
    public Set<Test> getAll() {
        return testRepository.getAll();
    }

    @Override
    public List<Test> getTestsByClass(ClassInfo classInfo) {
        return testRepository.findWithNamedQuery(Test.class, Test.TESTS_BY_CLASS, with("classInfo", classInfo).parameters());
    }

    @Override
    public Test create(Test test) {
        return testRepository.create(test);
    }

    @Override
    public Test update(Test test) {
        return testRepository.update(test);
    }

    @Override
    public void delete(Test... tests) {
        testRepository.delete(tests);
    }
}
