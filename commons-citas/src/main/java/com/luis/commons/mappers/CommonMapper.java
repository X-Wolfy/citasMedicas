package com.luis.commons.mappers;

public interface CommonMapper<RQ, RS, E> {
    E requestToEntity(RQ request);

    RS entityToResponse(E entity);
    
    E updateEntityFromRequest(RQ request, E entity);
}
