package src.model.dto;

public interface BaseMapper<D, E> {
    D toDto(E entity);
    E toEntity(D dto);
}
