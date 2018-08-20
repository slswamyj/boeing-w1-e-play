package com.stackroute.eplay.search.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stackroute.eplay.search.domain.Movie;
import com.stackroute.eplay.search.domain.Query;
import com.stackroute.eplay.search.services.SearchServiceImpl;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1")
public class SearchServiceControllers {

	private SearchServiceImpl searchService;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public SearchServiceControllers(SearchServiceImpl searchService) {
		this.searchService = searchService;
	}

	// get events by city name
	@GetMapping("/city/{city}")
	public ResponseEntity<?> getEventsBycity(@PathVariable String city, HttpSession session) {
		try {
			logger.info("Getting events in " + city);
			session.setAttribute("selectedCity", city);
			return new ResponseEntity<Iterable<Movie>>(searchService.getEventsByCity(city), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Could not fetch events in " + city);
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}

	}

	// get all queries
	@GetMapping("/queries")
	public ResponseEntity<?> getAllQueries() {
		try {
			logger.info("Getting all queries...");
			return new ResponseEntity<Iterable<Query>>(searchService.getAllQueries(), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Could not fetch queries");
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}

	}

	// get movies by name
	@GetMapping("/movies/{name}")
	public ResponseEntity<?> getMovieByName(@PathVariable String name) {
		try {
			logger.info("Getting movies by name " + name);
			return new ResponseEntity<Iterable<Movie>>(searchService.getMoviesByName(name), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Could not fetch movies by name " + name);
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}

	// get auto-complete suggestions by search string
	@GetMapping("/suggestion/{searchstr}")
	public ResponseEntity<?> autocompleteSuggestions(@PathVariable("searchstr") String searchstr) {
		try {
			logger.info("Getting movie suggestions by name " + searchstr);
			return new ResponseEntity<List<Movie>>(searchService.getMovieAutoSuggestions(searchstr), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Could not fetch movie suggestions by search string: " + searchstr);
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}

	// get movie by id
	@GetMapping("/movie/{id}")
	public ResponseEntity<?> getMovieById(@PathVariable("id") Integer id, HttpSession session) {
		String city = (String) session.getAttribute("selectedCity");
		return new ResponseEntity<Movie>(searchService.getMoviesByIdAndCity(city, id), HttpStatus.OK);
	}

}
