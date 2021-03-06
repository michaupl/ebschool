package com.ebschool.rest.core.utils;

import com.ebschool.ejb.model.*;
import com.ebschool.ejb.service.ClassInfoService;
import com.ebschool.ejb.service.UserService;
import com.ebschool.rest.core.model.*;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import java.util.HashSet;
import java.util.Set;

/**
 * User: michau
 * Date: 5/28/13
 */
@Singleton
public class RestElementBuilderImpl implements RestElementBuilder{

    @EJB
    UserService userService;

    @EJB
    ClassInfoService classInfoService;

    public StudentElement buildStudentElement(Student student){
        student = (Student) userService.getById(student.getId());
        StudentElement studentElement = new StudentElement(student);
        Set<Long> classIds = new HashSet<>();
        Set<Long> gradeIds = new HashSet<>();
        for (ClassInfo classInfo : student.getClasses()){
            classIds.add(classInfo.getId());
        }
        for (Grade grade : student.getGrades()){
            gradeIds.add(grade.getId());
        }
        studentElement.setClassIds(classIds);
        studentElement.setGradeIds(gradeIds);
        studentElement.setDetailedInfo(new DetailedInfoElement(student.getDetailedInfo()));
        studentElement.setType(UserElement.UserType.STUDENT);
        return studentElement;
    }

    @Override
    public TeacherElement buildTeacherElement(Teacher teacher) {
        teacher = (Teacher) userService.getById(teacher.getId());
        TeacherElement teacherElement = new TeacherElement(teacher);
        Set<Long> classIds = new HashSet<>();
        for (ClassInfo classInfo : teacher.getClasses()){
            classIds.add(classInfo.getId());
        }
        teacherElement.setClassIds(classIds);
        teacherElement.setDetailedInfo(new DetailedInfoElement(teacher.getDetailedInfo()));
        teacherElement.setType(UserElement.UserType.TEACHER);
        return teacherElement;
    }

    @Override
    public ParentElement buildParentElement(Parent parent) {
        parent = (Parent) userService.getById(parent.getId());
        ParentElement parentElement = new ParentElement(parent);
        Set<Long> childrenIds = new HashSet<>();
        for (Student student : parent.getChildrenAccounts()){
            childrenIds.add(student.getId());
        }
        parentElement.setChildrenAccounts(childrenIds);
        parentElement.setType(UserElement.UserType.PARENT);
        return parentElement;
    }

    @Override
    public UserElement buildUserElement(User user) {
        switch (user.getType()){
            case PARENT:
                return buildParentElement((Parent)user);
            case TEACHER:
                return buildTeacherElement((Teacher)user);
            case STUDENT:
                return buildStudentElement((Student)user);
        }
        return null;
    }

    @Override
    public ClassInfoElement buildClassInfoElement(ClassInfo classInfo) {
        classInfo = classInfoService.getById(classInfo.getId());
        ClassInfoElement classInfoElement = new ClassInfoElement(classInfo);
        Set<Long> teacherIds = new HashSet<>();
        Set<Long> studentIds = new HashSet<>();
        Set<Long> testIds = new HashSet<>();
        for (Teacher teacher : classInfo.getTeachers()){
            teacherIds.add(teacher.getId());
        }
        for (Student student : classInfo.getStudents()){
            studentIds.add(student.getId());
        }
        for (StudentTask studentTask : classInfo.getStudentTasks()){
            testIds.add(studentTask.getId());
        }
        classInfoElement.setTeachers(teacherIds);
        classInfoElement.setStudents(studentIds);
        classInfoElement.setTests(testIds);
        return classInfoElement;
    }

    // builds a set of restElements from given Set of model classes. Instead of converting them everywhere in getAll methods
    // this method can be used for the more complicated entities. Simple entities (ones that don't have lazy loaded collections of
    // other entities) are converted in service methods.
    @Override
    public <S, T> Set<S> buildElementSet(Set<T> entities, Class<S> type) {
        Set<S> resultSet = new HashSet();
        try{
            if (ClassInfoElement.class.isAssignableFrom(type)){
                for (T classInfo : entities){
                    resultSet.add((S)buildClassInfoElement((ClassInfo)classInfo));
                }
            } else if (StudentElement.class.isAssignableFrom(type)){
                for (T student : entities){
                    resultSet.add((S)buildStudentElement((Student)student));
                }
            } else if (TeacherElement.class.isAssignableFrom(type)){
                for (T teacher : entities){
                    resultSet.add((S)buildTeacherElement((Teacher)teacher));
                }
            } else if (ParentElement.class.isAssignableFrom(type)){
                for (T parent : entities){
                    resultSet.add((S)buildParentElement((Parent)parent));
                }
            } else {
                //TODO: warn about unknown class
            }
        } catch (ClassCastException ex){
            //TODO: log error
        }
        return resultSet;
    }

}
