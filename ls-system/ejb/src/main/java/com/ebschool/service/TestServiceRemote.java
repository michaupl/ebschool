package com.ebschool.service;

import com.ebschool.model.ClassInfo;
import com.ebschool.model.Test;

import java.util.List;
import java.util.Set;

/**
 * User: michau
 * Date: 5/19/13
 */
public interface TestServiceRemote {

    public Test getById(Long id);
    public Set<Test> getAll();
    public List<Test> getTestsByClass(ClassInfo classInfo);

    public Test create(Test test);
    public Test update(Test test);
    public void delete(Test... tests);

}