// UC6.java

public class UC6 {

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
            if (unit == null) throw new IllegalArgumentException("Unit null");
            if (!Double.isFinite(value)) throw new IllegalArgumentException("Invalid value");
            this.value = value;
            this.unit = unit;
        }

        private double toFeet() {
            return unit.toFeet(value);
        }

        public Quantity add(Quantity other) {
            if (other == null) throw new IllegalArgumentException("Other null");

            double sumFeet = this.toFeet() + other.toFeet();
            double resultValue = this.unit.fromFeet(sumFeet);

            return new Quantity(resultValue, this.unit);
        }

        // static version (optional)
        public static Quantity add(Quantity a, Quantity b, LengthUnit targetUnit) {
            if (a == null || b == null || targetUnit == null)
                throw new IllegalArgumentException("Invalid input");

            double sumFeet = a.toFeet() + b.toFeet();
            double resultValue = targetUnit.fromFeet(sumFeet);

            return new Quantity(resultValue, targetUnit);
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
        Quantity q2 = new Quantity(12.0, LengthUnit.INCH);
        System.out.println(q1.add(q2)); // 2.0 FEET

        Quantity q3 = new Quantity(12.0, LengthUnit.INCH);
        Quantity q4 = new Quantity(1.0, LengthUnit.FEET);
        System.out.println(q3.add(q4)); // 24.0 INCH

        Quantity q5 = new Quantity(1.0, LengthUnit.YARD);
        Quantity q6 = new Quantity(3.0, LengthUnit.FEET);
        System.out.println(q5.add(q6)); // 2.0 YARD
    }
}