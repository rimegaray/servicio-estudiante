package com.everis.springboot.app.models.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.everis.springboot.app.models.documents.Student;

public interface StudentDao extends ReactiveMongoRepository<Student, String>{

}
