package lib.utility;

import java.util.List;
import java.util.Objects;

public class Tuple<X, Y> {

    public final X x;
    public final Y y;

    public Tuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tuple)) return false;
        Tuple<?, ?> tuple = (Tuple<?, ?>) o;
        return Objects.equals(x, tuple.x) &&
                Objects.equals(y, tuple.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Tuple{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public Object findByX(List<Tuple<X, Y>> listOfTuples, X x) {
        for(Tuple tuple : listOfTuples) {
            if(x.equals(tuple.x))
                return tuple.y;
        }
        return null;
    }

    public String getXvalueAsString() {
        return x.toString();
    }

    public String getYvalueAsString() {
        return y.toString();
    }

    public Integer getXvalueAsInt() {
        return (Integer) x;
    }

    public Integer getYvalueAsInt() {
        return (Integer) y;
    }
}