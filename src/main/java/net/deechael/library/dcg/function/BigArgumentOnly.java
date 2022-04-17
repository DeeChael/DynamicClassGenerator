package net.deechael.library.dcg.function;

@FunctionalInterface
public interface BigArgumentOnly<T, E> {

    void apply(T first, E second);

}
