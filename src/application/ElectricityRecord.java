package application;

public class ElectricityRecord {

	private double occupation_lines;
	private double power_plant;
	private double Egyption_lines;
	private double daily_supply;
	private double demand;
	private double power_cuts_hour_day;
	private double temp;
	private String label;

	public ElectricityRecord(double occupation_lines, double power_plant, double egyption_lines, double daily_supply,
			double demand, double power_cuts_hour_day, double temp) {
		super();
		try {
			setOccupation_lines(occupation_lines);
			setPower_plant(power_plant);
			setEgyption_lines(egyption_lines);
			setDaily_supply(daily_supply);
			setDemand(demand);
			setPower_cuts_hour_day(power_cuts_hour_day);
			setTemp(temp);
		} catch (IllegalArgumentException n) {

		}
	}

	public double getOccupation_lines() {
		return occupation_lines;
	}

	public void setOccupation_lines(double occupation_lines) {
		this.occupation_lines = occupation_lines;
	}

	public double getPower_plant() {
		return power_plant;
	}

	public void setPower_plant(double power_plant) {
		this.power_plant = power_plant;
	}

	public double getEgyption_lines() {
		return Egyption_lines;
	}

	public void setEgyption_lines(double egyption_lines) {
		Egyption_lines = egyption_lines;
	}

	public double getDaily_supply() {
		return daily_supply;
	}

	public void setDaily_supply(double daily_supply) {
		this.daily_supply = daily_supply;
	}

	public double getDemand() {
		return demand;
	}

	public void setDemand(double demand) {
		this.demand = demand;
	}

	public double getPower_cuts_hour_day() {
		return power_cuts_hour_day;
	}

	public void setPower_cuts_hour_day(double power_cuts_hour_day) {
		this.power_cuts_hour_day = power_cuts_hour_day;
	}

	public double getTemp() {
		return temp;
	}

	public void setTemp(double temp) {
		this.temp = temp;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return "occupation_lines= " + occupation_lines + ", power_plant= " + power_plant + ", Egyption_lines= "
				+ Egyption_lines + ", daily_supply= " + daily_supply + ", demand= " + demand + ", power_cuts_hour_day= "
				+ power_cuts_hour_day + ", temp= " + temp;
	}

}
