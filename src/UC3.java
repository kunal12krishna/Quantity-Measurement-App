// UC12.java

interface IMeasurable {
    double getConversionFactor();
    double convertToBaseUnit(double value);
    double convertFromBaseUnit(double baseValue);
    String getUnitName();
}

// Example LengthUnit (works for all categories similarly)
enum LengthUnit implements IMeasurable {
    FEET(1.0),
    INCH(1.0 / 12.0);

    private final double factor;

    LengthUnit(double factor) {
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

// Generic Quantity
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

    // SUBTRACTION (implicit)
    public Quantity<U> subtract(Quantity<U> other) {
        if (other == null) throw new IllegalArgumentException("Other null");

        double resultBase = this.toBase() - other.toBase();
        double result = unit.convertFromBaseUnit(resultBase);

        return new Quantity<>(round(result), unit);
    }

    // SUBTRACTION (explicit unit)
    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {
        if (other == null || targetUnit == null)
            throw new IllegalArgumentException("Invalid input");

        double resultBase = this.toBase() - other.toBase();
        double result = targetUnit.convertFromBaseUnit(resultBase);

        return new Quantity<>(round(result), targetUnit);
    }

    // DIVISION
    public double divide(Quantity<U> other) {
        if (other == null) throw new IllegalArgumentException("Other null");

        double divisor = other.toBase();
        if (divisor == 0) throw new ArithmeticException("Divide by zero");

        return this.toBase() / divisor;
    }

    private double round(double val) {
        return Math.round(val * 100.0) / 100.0;
    }

    @Override
    public String toString() {
        return value + " " + unit.getUnitName();
    }
}

// Main
public class UC12 {
    public static void main(String[] args) {

        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(6.0, LengthUnit.INCH);

        System.out.println(q1.subtract(q2)); // 9.5 FEET
        System.out.println(q1.subtract(q2, LengthUnit.INCH)); // 114 INCH

        Quantity<LengthUnit> q3 = new Quantity<>(2.0, LengthUnit.FEET);
        System.out.println(q1.divide(q3)); // 5.0
    }
}