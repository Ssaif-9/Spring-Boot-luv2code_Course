package com.luv2code.springboot.cruddemowithrelationaldatabase.service;

import com.luv2code.springboot.cruddemowithrelationaldatabase.dtoEntity.CourseDetail;
import com.luv2code.springboot.cruddemowithrelationaldatabase.dtoEntity.CourseDto;
import com.luv2code.springboot.cruddemowithrelationaldatabase.entity.Course;
import com.luv2code.springboot.cruddemowithrelationaldatabase.entity.Review;
import com.luv2code.springboot.cruddemowithrelationaldatabase.entity.Student;
import com.luv2code.springboot.cruddemowithrelationaldatabase.exception.CustomException;
import com.luv2code.springboot.cruddemowithrelationaldatabase.reposatity.CourseRepoInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImplement implements CourseServiceInterface{

    private final CourseRepoInterface courseRepoInterface;
    private final ModelMapper modelMapper;


    @Override
    public void addCourse(Course course) {
        courseRepoInterface.save(course);
    }

    @Override
    public List<CourseDto> findAllCourse() {
        List<Course> allCourses = courseRepoInterface.findAll();
        return allCourses.stream().map(course ->modelMapper.map(course,CourseDto.class)).collect(Collectors.toList());
    }

    @Override
    public Course findCourseById(Integer id) {
        Optional<Course> courseOptional=courseRepoInterface.findById(id);
        if(courseOptional.isPresent())
            return courseOptional.get();
        else
            throw new CustomException("404","Not Found ","Not Found Course With Id : " +id, HttpStatus.NOT_FOUND);
    }

    @Override
    public void deleteCourseById(Integer id) {
        courseRepoInterface.deleteById(id);
    }

    @Override
    public void updateCourse(Course course) {
        Optional<Course> courseOptional=courseRepoInterface.findById(course.getId());
        if(courseOptional.isPresent()) {
            courseOptional.get().setTitle(course.getTitle());
            courseOptional.get().setInstructor(course.getInstructor());
            courseRepoInterface.save(courseOptional.get());
        }
        else
            throw new CustomException("404","Not Found ","Not Found Course With Id : " +course.getId(), HttpStatus.NOT_FOUND);
    }

    @Override
    public void addCourseAndReview(Course course) {

        course.addReview(new Review("good"));
        course.addReview(new Review("long"));
        course.addReview(new Review("22 hour"));

        courseRepoInterface.save(course);

    }

    @Override
    public void addCourseWithReviewAndStudent(Course course) {
        course.addStudent(new Student("saif","sultan","saif@gmail.com"));
        course.addStudent(new Student("omar","zaki","omar@gmail.com"));
        course.addStudent(new Student("saber","ashraf","saber@gmail.com"));

        course.addReview(new Review("very helpful!!"));

        courseRepoInterface.save(course);
    }

    @Override
    public List<CourseDetail> findCoursesWithInstructorId(Integer id) {
        List<Course> courses = courseRepoInterface.findByInstructorId(id);

        return courses.stream().map(course->modelMapper.map(course,CourseDetail.class)).collect(Collectors.toList());
    }
}
