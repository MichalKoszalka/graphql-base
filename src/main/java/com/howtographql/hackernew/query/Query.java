package com.howtographql.hackernew.query;

import java.util.List;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.howtographql.hackernew.domain.Link;
import com.howtographql.hackernew.repository.LinkRepository;

public class Query implements GraphQLQueryResolver {

	private final LinkRepository linkRepository;

	public Query(LinkRepository linkRepository) {
		this.linkRepository = linkRepository;
	}

	public List<Link> allLinks() {
		return linkRepository.getAllLinks();
	}
}
