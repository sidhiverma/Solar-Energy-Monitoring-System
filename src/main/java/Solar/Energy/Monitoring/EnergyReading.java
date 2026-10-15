
package Solar.Energy.Monitoring;

import jakarta.persistence.*;

@Entity
@Table(name = "energy_readings")
public class EnergyReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String readingTime;

    @Column(nullable = false)
    private double production;

    @Column(nullable = false)
    private double consumption;

    @Column(nullable = false)
    private int battery;

    public EnergyReading() {
    }

    public EnergyReading(String readingTime, double production,
                         double consumption, int battery) {
        this.readingTime = readingTime;
        this.production = production;
        this.consumption = consumption;
        this.battery = battery;
    }

    public Long getId() {
        return id;
    }

    public String getReadingTime() {
        return readingTime;
    }

    public void setReadingTime(String readingTime) {
        this.readingTime = readingTime;
    }

    public double getProduction() {
        return production;
    }

    public void setProduction(double production) {
        this.production = production;
    }

    public double getConsumption() {
        return consumption;
    }

    public void setConsumption(double consumption) {
        this.consumption = consumption;
    }

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }
}
