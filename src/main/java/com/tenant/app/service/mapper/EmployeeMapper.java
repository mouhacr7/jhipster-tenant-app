package com.tenant.app.service.mapper;

import com.tenant.app.domain.Client;
import com.tenant.app.domain.Employee;
import com.tenant.app.domain.Module;
import com.tenant.app.service.dto.ClientDTO;
import com.tenant.app.service.dto.EmployeeDTO;
import com.tenant.app.service.dto.ModuleDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Employee} and its DTO {@link EmployeeDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmployeeMapper extends EntityMapper<EmployeeDTO, Employee> {
    @Mapping(target = "accessRights", source = "accessRights", qualifiedByName = "moduleIdSet")
    @Mapping(target = "client", source = "client", qualifiedByName = "clientId")
    EmployeeDTO toDto(Employee s);

    @Mapping(target = "removeAccessRights", ignore = true)
    Employee toEntity(EmployeeDTO employeeDTO);

    @Named("moduleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ModuleDTO toDtoModuleId(Module module);

    @Named("moduleIdSet")
    default Set<ModuleDTO> toDtoModuleIdSet(Set<Module> module) {
        return module.stream().map(this::toDtoModuleId).collect(Collectors.toSet());
    }

    @Named("clientId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClientDTO toDtoClientId(Client client);
}
