package academy.certif.petpal.entity.dto;

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
