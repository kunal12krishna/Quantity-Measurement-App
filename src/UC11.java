// UC11.java

// Interface (UC10)
interface IMeasurable {
    double getConversionFactor();
    double convertToBaseUnit(double value);
    double convertFromBaseUnit(double baseValue);
    String getUnitName();
}

// VolumeUnit Enum (UC11)
enum VolumeUnit implements IMeasurable {
    LITRE(1.0),
    MILLILITRE(0.001),
    GALLON(3.78541);

    private final double factor;

    VolumeUnit(double factor) {
        this.factor = factor;
    }

    public double getConversionFactor() {
        return factor;
    }

    public double convertToBaseUnit(double value) {
        return value * factor;
    }

    public double convertFromBaseUnit(double baseValue) {
        return baseValue / factor;
    }

    public String getUnitName() {
        return name();
    }
}

// Generic Quantity Class (UC10 reused)
class Quantity<U extends IMeasurable> {
    private final double value;
    private final U unit;

    public Quantity(double value, U unit) {
        if (unit == null) throw new IllegalArgumentException("Unit null");
        if (!Double.isFinite(value)) throw new IllegalArgumentException("Invalid value");
        this.value = value;
        this.unit = unit;
    }

    private double toBase() {
        return unit.convertToBaseUnit(value);
    }

    public Quantity<U> convertTo(U targetUnit) {
        if (targetUnit == null) throw new IllegalArgumentException("Target null");
        double base = toBase();
        double result = targetUnit.convertFromBaseUnit(base);
        return new Quantity<>(result, targetUnit);
    }

    public Quantity<U> add(Quantity<U> other) {
        if (other == null) throw new IllegalArgumentException("Other null");

        double sum = this.toBase() + other.toBase();
        double result = this.unit.convertFromBaseUnit(sum);

        return new Quantity<>(result, this.unit);
    }

    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        if (other == null || targetUnit == null)
            throw new IllegalArgumentException("Invalid input");

        double sum = this.toBase() + other.toBase();
        double result = targetUnit.convertFromBaseUnit(sum);

        return new Quantity<>(result, targetUnit);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Quantity<?> other = (Quantity<?>) obj;
        if (!this.unit.getClass().equals(other.unit.getClass())) return false;

        return Double.compare(this.toBase(), other.toBase()) == 0;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(toBase());
    }

    @Override
    public String toString() {
        return value + " " + unit.getUnitName();
    }
}

// Main Class
public class UC11 {

    public static void main(String[] args) {

        Quantity<VolumeUnit> v1 = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> v2 = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);

        System.out.println(v1.equals(v2)); // true

        System.out.println(v1.convertTo(VolumeUnit.MILLILITRE)); // 1000 mL

        Quantity<VolumeUnit> v3 = new Quantity<>(1.0, VolumeUnit.GALLON);
        System.out.println(v3.convertTo(VolumeUnit.LITRE)); // ~3.78541 L

        System.out.println(v1.add(v2)); // 2 L
        System.out.println(v1.add(v3, VolumeUnit.MILLILITRE)); // ~4785.41 mL
    }
}// UC11 fix
