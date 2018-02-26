package com.howtographql.hackernew.resolver;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.howtographql.hackernew.domain.SigninPayload;
import com.howtographql.hackernew.domain.User;

public class SigninResolver implements GraphQLResolver<SigninPayload> {

	public User user(SigninPayload signinPayload) {
		return signinPayload.getUser();
	}
}
