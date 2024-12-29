package com.tenant.app.service.mapper;

import com.tenant.app.domain.Client;
import com.tenant.app.service.dto.ClientDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Client} and its DTO {@link ClientDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClientMapper extends EntityMapper<ClientDTO, Client> {}
