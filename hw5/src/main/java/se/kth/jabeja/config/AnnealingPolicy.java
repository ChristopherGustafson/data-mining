package se.kth.jabeja.config;

public enum AnnealingPolicy {
    LINEAR("LINEAR"),
    EXPONENTIAL("EXPONENTIAL");
    String name;
    AnnealingPolicy(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return name;
    }
}
