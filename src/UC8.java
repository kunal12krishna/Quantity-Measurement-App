// UC8.java

// Standalone Enum (separate responsibility)
enum LengthUnit {
    FEET(1.0),
    INCH(1.0 / 12.0),
    YARD(3.0),
    CM(0.393701 / 12.0);

    private final double toFeetFactor;

    LengthUnit(double toFeetFactor) {
        this.toFeetFactor = toFeetFactor;
    }

    public double convertToBaseUnit(double value) {
        return value * toFeetFactor;
    }

    public double convertFromBaseUnit(double baseValue) {
        return baseValue / toFeetFactor;
    }
}

// Quantity class (no conversion logic inside)
public class UC8 {

    static class Quantity {
        private final double value;
        private final LengthUnit unit;

        public Quantity(double value, LengthUnit unit) {
            if (unit == null) throw new IllegalArgumentException("Unit null");
            if (!Double.isFinite(value)) throw new IllegalArgumentException("Invalid value");
            this.value = value;
            this.unit = unit;
        }

        private double toFeet() {
            return unit.convertToBaseUnit(value);
        }

        public Quantity convertTo(LengthUnit targetUnit) {
            if (targetUnit == null) throw new IllegalArgumentException("Target null");
            double base = this.toFeet();
            double result = targetUnit.convertFromBaseUnit(base);
            return new Quantity(result, targetUnit);
        }

        public Quantity add(Quantity other, LengthUnit targetUnit) {
            if (other == null || targetUnit == null)
                throw new IllegalArgumentException("Invalid input");

            double sumBase = this.toFeet() + other.toFeet();
            double result = targetUnit.convertFromBaseUnit(sumBase);

            return new Quantity(result, targetUnit);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;

            Quantity other = (Quantity) obj;
            return Double.compare(this.toFeet(), other.toFeet()) == 0;
        }

        @Override
        public String toString() {
            return value + " " + unit;
        }
    }

    public static void main(String[] args) {

        Quantity q1 = new Quantity(1.0, LengthUnit.FEET);
        System.out.println(q1.convertTo(LengthUnit.INCH)); // 12 INCH

        Quantity q2 = new Quantity(12.0, LengthUnit.INCH);
        System.out.println(q1.add(q2, LengthUnit.FEET)); // 2 FEET

        Quantity q3 = new Quantity(36.0, LengthUnit.INCH);
        Quantity q4 = new Quantity(1.0, LengthUnit.YARD);
        System.out.println(q3.equals(q4)); // true
    }
}