package com.howtographql.hackernew.query;

import com.coxautodev.graphql.tools.GraphQLRootResolver;
import com.howtographql.hackernew.authentication.AuthContext;
import com.howtographql.hackernew.domain.AuthData;
import com.howtographql.hackernew.domain.Link;
import com.howtographql.hackernew.domain.SigninPayload;
import com.howtographql.hackernew.domain.User;
import com.howtographql.hackernew.repository.LinkRepository;
import com.howtographql.hackernew.repository.UserRepository;

import graphql.GraphQLException;
import graphql.schema.DataFetchingEnvironment;

public class Mutation implements GraphQLRootResolver {

	private final LinkRepository linkRepository;
	private final UserRepository userRepository;

	public Mutation(LinkRepository linkRepository, UserRepository userRepository) {
		this.linkRepository = linkRepository;
		this.userRepository = userRepository;
	}

	public Link createLink(String url, String description, DataFetchingEnvironment environment) {
		AuthContext context = environment.getContext();
		Link link = new Link(url, description, context.getUser().getId());
		linkRepository.saveLink(link);
		return link;
	}

	public User createUser(String name, AuthData authData) {
		User user = new User(name, authData.getEmail(), authData.getPassword());
		return userRepository.saveUser(user);
	}

	public SigninPayload signinUser(AuthData authData) {
		User user = userRepository.findByEmail(authData.getEmail());
		if(user.getPassword().equals(authData.getPassword())) {
			return new SigninPayload(user.getId(), user);
		}
		throw new GraphQLException("Invalid credentials");
	}
}
