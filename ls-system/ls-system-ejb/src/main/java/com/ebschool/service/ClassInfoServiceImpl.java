package com.ebschool.service;

import com.ebschool.model.ClassInfo;
import com.ebschool.model.Student;
import com.ebschool.model.Teacher;
import com.ebschool.repo.ClassInfoRepository;

import javax.ejb.*;
import javax.inject.Inject;
import java.util.Set;

/**
 * User: michau
 * Date: 5/19/13
 */
@Stateless
@Local(ClassInfoServiceLocal.class)
@Remote(ClassInfoServiceRemote.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ClassInfoServiceImpl implements ClassInfoServiceLocal {

    @Inject
    ClassInfoRepository classInfoRepository;

    @Override
    public ClassInfo getById(Long id) {
        return classInfoRepository.getById(id);
    }

    @Override
    public ClassInfo create(ClassInfo classInfo) {
        return classInfoRepository.create(classInfo);
    }

    @Override
    public void delete(ClassInfo... classes) {
        classInfoRepository.delete(classes);
    }

    @Override
    public ClassInfo update(ClassInfo classInfo) {
        return classInfoRepository.update(classInfo);
    }

    @Override
    public Set<ClassInfo> getAll() {
        return classInfoRepository.getAll();
    }

    @Override
    public ClassInfo addStudent(Student student, ClassInfo classInfo) {
        ClassInfo c = classInfoRepository.getById(classInfo.getId());
        c.getStudents().add(student);
        return classInfoRepository.update(c);
    }

    @Override
    public ClassInfo removeStudent(Student student, ClassInfo classInfo) {
        ClassInfo c = classInfoRepository.getById(classInfo.getId());
        c.getStudents().remove(student);
        return classInfoRepository.update(c);
    }

    @Override
    public ClassInfo addTeacher(Teacher teacher, ClassInfo classInfo) {
        ClassInfo c = classInfoRepository.getById(classInfo.getId());
        c.getTeachers().add(teacher);
        return classInfoRepository.update(c);
    }

    @Override
    public ClassInfo removeTeacher(Teacher teacher, ClassInfo classInfo) {
        ClassInfo c = classInfoRepository.getById(classInfo.getId());
        c.getTeachers().remove(teacher);
        return classInfoRepository.update(c);
    }
}