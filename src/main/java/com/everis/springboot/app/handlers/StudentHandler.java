package com.everis.springboot.app.handlers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.everis.springboot.app.models.documents.Person;
import com.everis.springboot.app.models.documents.Student;
import com.everis.springboot.app.models.service.StudentService;

import reactor.core.publisher.Mono;

@Component
public class StudentHandler {

	@Autowired
	private StudentService service;
	
	public Mono<ServerResponse> findAll(ServerRequest request){
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.body(service.findAllStudents(), Student.class);
	}
	
	public Mono<ServerResponse> findById(ServerRequest request){
		String id = request.pathVariable("id");
		return service.findByIdStudent(id).flatMap(p -> ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.syncBody(p))
				.switchIfEmpty(ServerResponse.notFound().build());
	}
	
	public Mono<ServerResponse> create(ServerRequest request){
		
		Mono<Person> person = request.bodyToMono(Person.class);
		
		return person.flatMap(p ->{
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
		})	.flatMap(s->service.saveStudent(s))
			.flatMap(s -> ServerResponse.created(URI.create("/apihandler/student/".concat(s.getIdStudent())))
			.contentType(MediaType.APPLICATION_JSON_UTF8)
			.syncBody(s));
	}
	
	public Mono<ServerResponse> edit(ServerRequest request){
		Mono<Student> student = request.bodyToMono(Student.class);
		String id = request.pathVariable("id");
		return student.flatMap(s->service.updateStudent(s, id))
				.flatMap(s ->
				service.findByIdPerson(s.getId()).flatMap(p-> {
					p.setDateOfBirth(s.getDateOfBirth());
					p.setFullName(s.getFullName());
					p.setGender(s.getGender());
					p.setTypeDocument(s.getTypeDocument());
					p.setNumberDocument(s.getNumberDocument());
					return service.savePerson(p);
				})			
			).flatMap(s -> ServerResponse
					.created(URI.create("/apihandler/student/".concat(id)))
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.syncBody(s));
		
	}
	
	public Mono<ServerResponse> delete(ServerRequest request){
		String id = request.pathVariable("id");
		Mono<Student> studentF = service.findByIdStudent(id);
		
		return studentF.flatMap(s->{
			
			
			return service.deletePerson(s.getId()).zipWith(service.deleteStudent(s));
					
		})
				
				.then(ServerResponse.noContent().build());
				
	}
	
	public Mono<ServerResponse> findFamily(ServerRequest request){
		String id = request.pathVariable("id"); 
				
		return service.findFamily(id)
				.flatMap(s -> ServerResponse
						.created(URI.create("/apihandler/student/findFamily".concat(id)))
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.syncBody(s)).next();
				
	}

	
}
