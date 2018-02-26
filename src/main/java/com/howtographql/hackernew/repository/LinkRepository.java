package com.howtographql.hackernew.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.howtographql.hackernew.domain.Link;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

public class LinkRepository {

	private final MongoCollection<Document> links;

	public LinkRepository(MongoCollection<Document> links) {
		this.links = links;
	}

	public Link findById(String id) {
		Document document = links.find(Filters.eq("_id", new ObjectId(id))).first();
		return link(document);
	}

	public List<Link> getAllLinks() {
		List<Link> allLinks = new ArrayList<>();
		for (Document doc : links.find()) {
			Link link = new Link(
					doc.get("_id").toString(),
					doc.getString("url"),
					doc.getString("description"),
					doc.getString("postedBy")
			);
			allLinks.add(link);
		}
		return allLinks;
	}

	public void saveLink(Link link) {
		Document document = new Document();
		document.append("url", link.getUrl());
		document.append("description", link.getDescription());
		document.append("postedBy", link.getUserId());
		links.insertOne(document);
	}

	private Link link(Document document) {
		return new Link(document.get("_id").toString(), document.getString("url"), document.getString("description"));
	}
}
