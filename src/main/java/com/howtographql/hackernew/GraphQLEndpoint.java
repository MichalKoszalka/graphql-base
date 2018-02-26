package com.howtographql.hackernew;

import java.util.Optional;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.coxautodev.graphql.tools.SchemaParser;
import com.howtographql.hackernew.authentication.AuthContext;
import com.howtographql.hackernew.domain.User;
import com.howtographql.hackernew.query.Mutation;
import com.howtographql.hackernew.query.Query;
import com.howtographql.hackernew.repository.LinkRepository;
import com.howtographql.hackernew.repository.UserRepository;
import com.howtographql.hackernew.resolver.LinkResolver;
import com.howtographql.hackernew.resolver.SigninResolver;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import graphql.schema.GraphQLSchema;
import graphql.servlet.GraphQLContext;
import graphql.servlet.SimpleGraphQLServlet;

@WebServlet(urlPatterns = "/graphql")
public class GraphQLEndpoint extends SimpleGraphQLServlet {

	private static final LinkRepository linkRepository;
	private static final UserRepository userRepository;

	static {
		MongoDatabase mongoDatabase = new MongoClient().getDatabase("hackernews");
		linkRepository = new LinkRepository(mongoDatabase.getCollection("links"));
		userRepository = new UserRepository(mongoDatabase.getCollection("users"));
	}

	public GraphQLEndpoint() {
		super(buildSchema());
	}

	private static GraphQLSchema buildSchema() {
		return SchemaParser.newParser().file("schema.graphqls")
				.resolvers(new Query(linkRepository), new Mutation(linkRepository, userRepository),
						new SigninResolver(), new LinkResolver(userRepository)).build().makeExecutableSchema();
	}

	@Override
	protected GraphQLContext createContext(Optional<HttpServletRequest> request,
			Optional<HttpServletResponse> response) {
		User user = request.map(req -> req.getHeader("Authorization"))
				.filter(id -> !id.isEmpty())
				.map(id -> id.replace("Bearer ", ""))
				.map(userRepository::findById)
				.orElse(null);
		return new AuthContext(user, request, response);
	}
}
