import java.util.Objects;

// Interface
interface IMeasurable {
    double getConversionFactor();

    default double convertToBaseUnit(double value) {
        return value * getConversionFactor();
    }

    default double convertFromBaseUnit(double baseValue) {
        return baseValue / getConversionFactor();
    }

    String getUnitName();
}

// Length Enum
enum LengthUnit implements IMeasurable {
    FEET(12.0),
    INCHES(1.0);

    private final double factor;

    LengthUnit(double factor) {
        this.factor = factor;
    }

    public double getConversionFactor() {
        return factor;
    }

    public String getUnitName() {
        return name();
    }
}

// Weight Enum
enum WeightUnit implements IMeasurable {
    KILOGRAM(1000.0),
    GRAM(1.0);

    private final double factor;

    WeightUnit(double factor) {
        this.factor = factor;
    }

    public double getConversionFactor() {
        return factor;
    }

    public String getUnitName() {
        return name();
    }
}

// Generic Quantity Class
class Quantity<U extends IMeasurable> {
    private final double value;
    private final U unit;

    public Quantity(double value, U unit) {
        if (unit == null) throw new IllegalArgumentException("Unit null");
        if (Double.isNaN(value) || Double.isInfinite(value))
            throw new IllegalArgumentException("Invalid value");

        this.value = value;
        this.unit = unit;
    }

    // 🔥 ENUM FOR OPERATIONS
    private enum Operation {
        ADD {
            double apply(double a, double b) { return a + b; }
        },
        SUBTRACT {
            double apply(double a, double b) { return a - b; }
        },
        DIVIDE {
            double apply(double a, double b) {
                if (b == 0) throw new ArithmeticException("Divide by zero");
                return a / b;
            }
        };

        abstract double apply(double a, double b);
    }

    // 🔥 CENTRAL HELPER (UC13 CORE)
    private double perform(Quantity<U> other, Operation op) {
        if (other == null) throw new IllegalArgumentException("Null operand");

        if (this.unit.getClass() != other.unit.getClass())
            throw new IllegalArgumentException("Different categories");

        double base1 = unit.convertToBaseUnit(this.value);
        double base2 = other.unit.convertToBaseUnit(other.value);

        return op.apply(base1, base2);
    }

    // ADD
    public Quantity<U> add(Quantity<U> other) {
        return add(other, this.unit);
    }

    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        double resultBase = perform(other, Operation.ADD);
        double result = targetUnit.convertFromBaseUnit(resultBase);
        return new Quantity<>(round(result), targetUnit);
    }

    // SUBTRACT
    public Quantity<U> subtract(Quantity<U> other) {
        return subtract(other, this.unit);
    }

    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {
        double resultBase = perform(other, Operation.SUBTRACT);
        double result = targetUnit.convertFromBaseUnit(resultBase);
        return new Quantity<>(round(result), targetUnit);
    }

    // DIVIDE
    public double divide(Quantity<U> other) {
        return perform(other, Operation.DIVIDE);
    }

    // EQUALS
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Quantity<?>)) return false;

        Quantity<?> other = (Quantity<?>) obj;

        if (this.unit.getClass() != other.unit.getClass())
            return false;

        double base1 = unit.convertToBaseUnit(this.value);
        double base2 = other.unit.convertToBaseUnit(other.value);

        return Double.compare(base1, base2) == 0;
    }

    @Override
    public int hashCode() {
        double base = unit.convertToBaseUnit(value);
        return Objects.hash(base, unit.getClass());
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit.getUnitName() + ")";
    }

    private double round(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}

// MAIN
public class UC13 {
    public static void main(String[] args) {

        Quantity<LengthUnit> l1 = new Quantity<>(10, LengthUnit.FEET);
        Quantity<LengthUnit> l2 = new Quantity<>(12, LengthUnit.INCHES);

        System.out.println(l1.add(l2));        // 11 FEET
        System.out.println(l1.subtract(l2));   // 9 FEET
        System.out.println(l1.divide(l2));     // 10

        Quantity<WeightUnit> w1 = new Quantity<>(1, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> w2 = new Quantity<>(1000, WeightUnit.GRAM);

        System.out.println(w1.add(w2));        // 2 KG
    }
}