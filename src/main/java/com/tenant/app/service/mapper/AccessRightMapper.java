package com.tenant.app.service.mapper;

import com.tenant.app.domain.AccessRight;
import com.tenant.app.domain.Employee;
import com.tenant.app.domain.Module;
import com.tenant.app.service.dto.AccessRightDTO;
import com.tenant.app.service.dto.EmployeeDTO;
import com.tenant.app.service.dto.ModuleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AccessRight} and its DTO {@link AccessRightDTO}.
 */
@Mapper(componentModel = "spring")
public interface AccessRightMapper extends EntityMapper<AccessRightDTO, AccessRight> {
    @Mapping(target = "module", source = "module", qualifiedByName = "moduleId")
    @Mapping(target = "employee", source = "employee", qualifiedByName = "employeeId")
    AccessRightDTO toDto(AccessRight s);

    @Named("moduleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ModuleDTO toDtoModuleId(Module module);

    @Named("employeeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmployeeDTO toDtoEmployeeId(Employee employee);
}
