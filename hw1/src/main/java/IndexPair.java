public class IndexPair implements Comparable<IndexPair>{

    int aIndex;
    int bIndex;

    public IndexPair(int aIndex, int bIndex) {
        this.aIndex = aIndex;
        this.bIndex = bIndex;
    }

    @Override
    public int compareTo(IndexPair o) {
        if((this.aIndex == o.aIndex && this.bIndex == o.bIndex) ||
            (this.aIndex == o.bIndex && this.bIndex == o.aIndex)) {
            return 1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IndexPair pair = (IndexPair) o;
        if((this.aIndex == pair.aIndex && this.bIndex == pair.bIndex) ||
                (this.aIndex == pair.bIndex && this.bIndex == pair.aIndex)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode(){
        String stringRepresentation = aIndex < bIndex ? ""+aIndex+bIndex : ""+bIndex+aIndex;
        return stringRepresentation.hashCode();
    }

    @Override
    public String toString(){
        return String.format("Pair for indexes %d and %d", aIndex, bIndex);
    }
}
