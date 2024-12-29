package com.tenant.app.service;

import com.tenant.app.domain.Employee;
import com.tenant.app.repository.EmployeeRepository;
import com.tenant.app.service.dto.EmployeeDTO;
import com.tenant.app.service.mapper.EmployeeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Employee}.
 */
@Service
@Transactional
public class EmployeeService {

    private final Logger log = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepository employeeRepository;

    private final EmployeeMapper employeeMapper;

    public EmployeeService(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    /**
     * Save a employee.
     *
     * @param employeeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<EmployeeDTO> save(EmployeeDTO employeeDTO) {
        log.debug("Request to save Employee : {}", employeeDTO);
        return employeeRepository.save(employeeMapper.toEntity(employeeDTO)).map(employeeMapper::toDto);
    }

    /**
     * Update a employee.
     *
     * @param employeeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<EmployeeDTO> update(EmployeeDTO employeeDTO) {
        log.debug("Request to update Employee : {}", employeeDTO);
        return employeeRepository.save(employeeMapper.toEntity(employeeDTO)).map(employeeMapper::toDto);
    }

    /**
     * Partially update a employee.
     *
     * @param employeeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<EmployeeDTO> partialUpdate(EmployeeDTO employeeDTO) {
        log.debug("Request to partially update Employee : {}", employeeDTO);

        return employeeRepository
            .findById(employeeDTO.getId())
            .map(existingEmployee -> {
                employeeMapper.partialUpdate(existingEmployee, employeeDTO);

                return existingEmployee;
            })
            .flatMap(employeeRepository::save)
            .map(employeeMapper::toDto);
    }

    /**
     * Get all the employees.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<EmployeeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Employees");
        return employeeRepository.findAllBy(pageable).map(employeeMapper::toDto);
    }

    /**
     * Get all the employees with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<EmployeeDTO> findAllWithEagerRelationships(Pageable pageable) {
        return employeeRepository.findAllWithEagerRelationships(pageable).map(employeeMapper::toDto);
    }

    /**
     * Returns the number of employees available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return employeeRepository.count();
    }

    /**
     * Get one employee by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<EmployeeDTO> findOne(Long id) {
        log.debug("Request to get Employee : {}", id);
        return employeeRepository.findOneWithEagerRelationships(id).map(employeeMapper::toDto);
    }

    /**
     * Delete the employee by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Employee : {}", id);
        return employeeRepository.deleteById(id);
    }
}
