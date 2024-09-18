
public class GUT {
	private String System;
	private String Planet;
	private String FacilityName;
	private double price;
	private String Product;
	
	public GUT(String system, String planet, String facilityName,String Product, double price) {
		System = system;
		Planet = planet;
		FacilityName = facilityName;
		this.price = price;
		this.Product = Product;
	}

	public String getSystem() {
		return System;
	}

	public String getPlanet() {
		return Planet;
	}

	public String getFacilityName() {
		return FacilityName;
	}

	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String toString() {
		return System+", " + Planet +", " + FacilityName + ", " + Product +": "+price;
	}
	public String getProduct() {
		return Product;
	}
	
}
