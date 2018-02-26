package com.howtographql.hackernew.resolver;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.howtographql.hackernew.domain.Link;
import com.howtographql.hackernew.domain.User;
import com.howtographql.hackernew.repository.UserRepository;

public class LinkResolver implements GraphQLResolver<Link> {

	private final UserRepository userRepository;

	public LinkResolver(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User postedBy(Link link) {
		if(link.getUserId() == null) {
			return null;
		}
		return userRepository.findById(link.getUserId());
	}
}
