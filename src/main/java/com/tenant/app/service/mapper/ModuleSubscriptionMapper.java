package com.tenant.app.service.mapper;

import com.tenant.app.domain.Client;
import com.tenant.app.domain.Module;
import com.tenant.app.domain.ModuleSubscription;
import com.tenant.app.service.dto.ClientDTO;
import com.tenant.app.service.dto.ModuleDTO;
import com.tenant.app.service.dto.ModuleSubscriptionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ModuleSubscription} and its DTO {@link ModuleSubscriptionDTO}.
 */
@Mapper(componentModel = "spring")
public interface ModuleSubscriptionMapper extends EntityMapper<ModuleSubscriptionDTO, ModuleSubscription> {
    @Mapping(target = "module", source = "module", qualifiedByName = "moduleId")
    @Mapping(target = "client", source = "client", qualifiedByName = "clientId")
    ModuleSubscriptionDTO toDto(ModuleSubscription s);

    @Named("moduleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ModuleDTO toDtoModuleId(Module module);

    @Named("clientId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClientDTO toDtoClientId(Client client);
}
