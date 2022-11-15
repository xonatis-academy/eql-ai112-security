package fr.eql.akatz.demo.petpal.spring.back.entity.dto;

public class Query {

	private Pages pages;

	public Query() {

	}

	public Query(Pages pages) {
		this.pages = pages;
	}

	public Pages getPages() {
		return pages;
	}
}
