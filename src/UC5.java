// UC5.java

public class UC5 {

    enum LengthUnit {
        FEET(1.0),
        INCH(1.0 / 12.0),
        YARD(3.0),
        CM(0.393701 / 12.0);

        private final double toFeetFactor;

        LengthUnit(double toFeetFactor) {
            this.toFeetFactor = toFeetFactor;
        }

        public double toFeet(double value) {
            return value * toFeetFactor;
        }

        public double fromFeet(double feetValue) {
            return feetValue / toFeetFactor;
        }
    }

    static class Quantity {
        private final double value;
        private final LengthUnit unit;

        public Quantity(double value, LengthUnit unit) {
            if (unit == null) throw new IllegalArgumentException("Unit cannot be null");
            if (!Double.isFinite(value)) throw new IllegalArgumentException("Invalid value");
            this.value = value;
            this.unit = unit;
        }

        private double toFeet() {
            return unit.toFeet(value);
        }

        public double convertTo(LengthUnit targetUnit) {
            if (targetUnit == null) throw new IllegalArgumentException("Target unit null");
            double feet = this.toFeet();
            return targetUnit.fromFeet(feet);
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

    // static API
    public static double convert(double value, LengthUnit source, LengthUnit target) {
        if (source == null || target == null)
            throw new IllegalArgumentException("Unit cannot be null");
        if (!Double.isFinite(value))
            throw new IllegalArgumentException("Invalid value");

        double feet = source.toFeet(value);
        return target.fromFeet(feet);
    }

    // overloaded demo methods
    public static double demonstrateLengthConversion(double value, LengthUnit from, LengthUnit to) {
        return convert(value, from, to);
    }

    public static double demonstrateLengthConversion(Quantity q, LengthUnit to) {
        return q.convertTo(to);
    }

    public static void main(String[] args) {

        System.out.println(convert(1.0, LengthUnit.FEET, LengthUnit.INCH)); // 12.0
        System.out.println(convert(3.0, LengthUnit.YARD, LengthUnit.FEET)); // 9.0
        System.out.println(convert(36.0, LengthUnit.INCH, LengthUnit.YARD)); // 1.0
        System.out.println(convert(1.0, LengthUnit.CM, LengthUnit.INCH)); // ~0.393701

        Quantity q = new Quantity(2.0, LengthUnit.YARD);
        System.out.println(q.convertTo(LengthUnit.FEET)); // 6.0
    }
}