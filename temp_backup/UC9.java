// UC9.java

// Standalone WeightUnit Enum
enum WeightUnit {
    KILOGRAM(1.0),
    GRAM(0.001),
    POUND(0.453592);

    private final double toKgFactor;

    WeightUnit(double toKgFactor) {
        this.toKgFactor = toKgFactor;
    }

    public double convertToBaseUnit(double value) {
        return value * toKgFactor;
    }

    public double convertFromBaseUnit(double baseValue) {
        return baseValue / toKgFactor;
    }
}

// QuantityWeight Class
public class UC9 {

    static class QuantityWeight {
        private final double value;
        private final WeightUnit unit;

        public QuantityWeight(double value, WeightUnit unit) {
            if (unit == null) throw new IllegalArgumentException("Unit null");
            if (!Double.isFinite(value)) throw new IllegalArgumentException("Invalid value");
            this.value = value;
            this.unit = unit;
        }

        private double toKg() {
            return unit.convertToBaseUnit(value);
        }

        public QuantityWeight convertTo(WeightUnit targetUnit) {
            if (targetUnit == null) throw new IllegalArgumentException("Target null");
            double base = this.toKg();
            double result = targetUnit.convertFromBaseUnit(base);
            return new QuantityWeight(result, targetUnit);
        }

        // default add (result in first operand unit)
        public QuantityWeight add(QuantityWeight other) {
            if (other == null) throw new IllegalArgumentException("Other null");

            double sumKg = this.toKg() + other.toKg();
            double result = this.unit.convertFromBaseUnit(sumKg);

            return new QuantityWeight(result, this.unit);
        }

        // explicit target unit add
        public QuantityWeight add(QuantityWeight other, WeightUnit targetUnit) {
            if (other == null || targetUnit == null)
                throw new IllegalArgumentException("Invalid input");

            double sumKg = this.toKg() + other.toKg();
            double result = targetUnit.convertFromBaseUnit(sumKg);

            return new QuantityWeight(result, targetUnit);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;

            QuantityWeight other = (QuantityWeight) obj;
            return Double.compare(this.toKg(), other.toKg()) == 0;
        }

        @Override
        public int hashCode() {
            return Double.hashCode(toKg());
        }

        @Override
        public String toString() {
            return value + " " + unit;
        }
    }

    public static void main(String[] args) {

        QuantityWeight w1 = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight w2 = new QuantityWeight(1000.0, WeightUnit.GRAM);
        System.out.println(w1.equals(w2)); // true

        System.out.println(w1.convertTo(WeightUnit.GRAM)); // 1000 GRAM

        QuantityWeight w3 = new QuantityWeight(2.0, WeightUnit.POUND);
        System.out.println(w3.convertTo(WeightUnit.KILOGRAM)); // ~0.907 KG

        System.out.println(w1.add(w2)); // 2 KG
        System.out.println(w1.add(w2, WeightUnit.GRAM)); // 2000 GRAM
    }
}