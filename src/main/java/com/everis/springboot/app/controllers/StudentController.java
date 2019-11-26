package com.everis.springboot.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.everis.springboot.app.models.documents.Person;
import com.everis.springboot.app.models.documents.Student;
import com.everis.springboot.app.models.service.StudentService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/student")
public class StudentController {

	@Autowired
	private StudentService service;
	
	@GetMapping
	public Flux<Student> findAll(){
		return service.findAllStudents();
	}
	
	@GetMapping("/{id}")
	public Mono<Student> findById(@PathVariable String id){
		return service.findByIdStudent(id);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<Student> create(@RequestBody Student stud){
		return Mono.just(stud).flatMap(p ->{
			return service.savePerson(p);
		}).map(p-> {
			Student student = new Student();
			student.setId(p.getId());
			student.setFullName(p.getFullName());
			student.setGender(p.getGender());
			student.setDateOfBirth(p.getDateOfBirth());
			student.setTypeDocument(p.getTypeDocument());
			student.setNumberDocument(p.getNumberDocument());
			return student;
		})	.flatMap(s->service.saveStudent(s));
	}
	
	@PutMapping("/{id}")
	public Mono<Person> update(@RequestBody Student student, @PathVariable String id){
		
		return service.updateStudent(student, id)
				.flatMap(s ->
				service.findByIdPerson(s.getId()).flatMap(p-> {
					p.setDateOfBirth(s.getDateOfBirth());
					p.setFullName(s.getFullName());
					p.setGender(s.getGender());
					p.setTypeDocument(s.getTypeDocument());
					p.setNumberDocument(s.getNumberDocument());
					return service.savePerson(p);
				})
			);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Mono<Void> delete(@PathVariable String id){
		return service.findByIdStudent(id).flatMap(s->service.deleteStudent(s).and(service.deletePerson(s.getId())));
	}
	
	@GetMapping("/findFamily/{idStudent}")
	public Flux<Person> findFamily(@PathVariable String idStudent){
		
		return service.findByIdStudent(idStudent).flatMapMany(s->service.findFamily(s.getId()));
	}
	
	@PutMapping("/addRelative/{id}/{nameMember}")
	public Mono<Person> addRelative(@PathVariable String id, @PathVariable String nameMember){
		return service.findByIdStudent(id).flatMap(s->service.addRelative(s.getId(), nameMember));
	}
	
}
