package com.everis.springboot.app.models.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.everis.springboot.app.models.dao.StudentDao;
import com.everis.springboot.app.models.documents.Person;
import com.everis.springboot.app.models.documents.Student;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class StudentServiceImpl implements StudentService{

	@Autowired
	private WebClient client;
	
	@Autowired
	private StudentDao studentDao;
	
	@Override
	public Flux<Person> findAllPersons() {
		
		return client.get().accept(MediaType.APPLICATION_JSON_UTF8)
				.exchange()
				.flatMapMany(response -> response.bodyToFlux(Person.class));
	}

	@Override
	public Mono<Person> findByIdPerson(String id) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("id", id);
		return client.get().uri("/{id}",params)
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.retrieve()
				.bodyToMono(Person.class);
	}

	@Override
	public Mono<Void> deletePerson(String id) {
		return client.delete().uri("/{id}",Collections.singletonMap("id", id))
				.exchange()
				.then();
	}

	@Override
	public Mono<Person> savePerson(Person persona) {
		return client.post()
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.syncBody(persona)
				.retrieve()
				.bodyToMono(Person.class);
	}
	
	@Override
	public Mono<Person> updatePerson(Person persona, String id) {
		return client.put()
				.uri("/{id}",Collections.singletonMap("id", id))
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.syncBody(persona)
				.retrieve()
				.bodyToMono(Person.class);
	}
	
	@Override
	public Flux<Person> findFamily(String idPerson){
		return client.get()
				.uri("/findFamily/{idPerson}",Collections.singletonMap("idPerson", idPerson))
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.retrieve()
				.bodyToFlux(Person.class);
	}
	
	@Override
	public Mono<Person> addRelative(String id, String nameMember){
		
		Map<String,String> mapa = new HashMap<>();
		mapa.put("id", id);
		mapa.put("nameMember", nameMember);
		return client.put()
				.uri("/addRelative/{id}/{nameMember}",mapa)
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.retrieve()
				.bodyToMono(Person.class);
	}

	@Override
	public Flux<Person> findByDateRange(String dateInit, String dateEnd) {
		
		Map<String,String> mapa = new HashMap<>();
		mapa.put("dateInit", dateInit);
		mapa.put("dateEnd", dateEnd);
		
		return client.get()
				.uri("/dateRange/{dateInit}/{dateEnd}",mapa)
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.retrieve()
				.bodyToFlux(Person.class);
	}
	
	@Override
	public Flux<Student> findAllStudents() {
		return studentDao.findAll();
	}

	@Override
	public Mono<Student> findByIdStudent(String id) {
		return studentDao.findById(id);
	}

	@Override
	public Mono<Void> deleteStudent(Student student) {
		return studentDao.delete(student);
	}

	@Override
	public Mono<Student> saveStudent(Student student) {
		return studentDao.save(student);
	}
	
	@Override
	public Mono<Student> updateStudent(Student student, String id) {
		return studentDao.findById(id).flatMap(s -> {
			s.setFullName(student.getFullName());
			s.setDateOfBirth(student.getDateOfBirth());
			s.setGender(student.getGender());
			s.setTypeDocument(student.getTypeDocument());
			s.setNumberDocument(student.getNumberDocument());
			return studentDao.save(s);
		});
	}

	@Override
	public Mono<Student> findStudentByIdPerson(String idPerson) {
		return studentDao.findAll().filter(s->s.getId().equals(idPerson)).next();
	}

	

}
