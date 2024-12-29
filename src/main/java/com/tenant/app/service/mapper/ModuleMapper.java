package com.tenant.app.service.mapper;

import com.tenant.app.domain.Module;
import com.tenant.app.service.dto.ModuleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Module} and its DTO {@link ModuleDTO}.
 */
@Mapper(componentModel = "spring")
public interface ModuleMapper extends EntityMapper<ModuleDTO, Module> {}
