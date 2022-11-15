package fr.eql.akatz.demo.petpal.spring.back.entity.dto;

public class WikiExtract {

	private Query query;

	public WikiExtract() {

	}

	public WikiExtract(Query query) {
		this.query = query;
	}

	public Query getQuery() {
		return query;
	}
}
