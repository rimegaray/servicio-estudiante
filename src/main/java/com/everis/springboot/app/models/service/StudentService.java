package com.everis.springboot.app.models.service;

import com.everis.springboot.app.models.documents.Person;
import com.everis.springboot.app.models.documents.Student;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StudentService {

	public Flux<Person> findAllPersons();

	public Mono<Person> findByIdPerson(String id);
	
	public Flux<Person> findFamily(String id);

	public Mono<Void> deletePerson(String id);
	
	public Mono<Person> addRelative(String id, String nameMember);

	public Mono<Person> savePerson(Person persona);
	
	public Mono<Person> updatePerson(Person persona, String id);
	
	public Flux<Person> findByDateRange(String date1, String date2);
	
	

	public Mono<Student> findStudentByIdPerson(String idPerson);
	
	public Flux<Student> findAllStudents();

	public Mono<Student> findByIdStudent(String id);

	public Mono<Void> deleteStudent(Student student);

	public Mono<Student> saveStudent(Student student);
	
	public Mono<Student> updateStudent(Student student, String id);
}
