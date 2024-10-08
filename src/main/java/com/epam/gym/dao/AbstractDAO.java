package com.epam.gym.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Repository;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Repository
@RequiredArgsConstructor
public abstract class AbstractDAO<T> implements BaseDAO<T> {
    private final Class<T> entityClass;
    protected final SessionFactory sessionFactory;

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Optional<T> save(T entity) {
        try {
            getCurrentSession().clear();
            var savedEntity = getCurrentSession().merge(entity);
            getCurrentSession().flush();
            log.info("{} has been saved", entityClass.getSimpleName());
            return Optional.ofNullable(savedEntity);
        } catch (Exception e) {
            log.error("Error saving entity: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Optional<T> update(T entity, Long id) {
        try {
            T existingEntity = getCurrentSession().find(entityClass, id);
            if (existingEntity != null) {
                copyNonNullProperties(entity, existingEntity);
                var updatedEntity = getCurrentSession().merge(existingEntity);
                log.info("{} with ID {} has been updated", entityClass.getSimpleName(), id);
                return Optional.ofNullable(updatedEntity);
            } else {
                log.warn("Entity with ID {} not found", id);
            }
            return Optional.empty();
        } catch (Exception e) {
            log.error("Error updating entity: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Optional<T> findById(Long id) {
        try {
            T entity = getCurrentSession().find(entityClass, id);
            if (entity != null) {
                log.info("{} with ID {} has been found", entityClass.getSimpleName(), id);
                return Optional.of(entity);
            } else {
                log.debug("{} with ID {} has not been found", entityClass.getSimpleName(), id);
                return Optional.empty();
            }
        } catch (Exception e) {
            log.error("Error finding entity by ID: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public List<T> findAll() {
        try {
            var hql = String.format("FROM %s", entityClass.getSimpleName());
            List<T> entities = getCurrentSession().createQuery(hql, entityClass).getResultList();
            if (entities.isEmpty()) {
                log.info("No {} have been found", entityClass.getSimpleName());
            } else {
                log.info("All {} have been found", entityClass.getSimpleName());
            }
            return entities;
        } catch (Exception e) {
            log.error("Error finding all entities: {}", e.getMessage(), e);
            return List.of();
        }
    }

    @Override
    public void delete(Long id) {
        try {
            T entity = getCurrentSession().find(entityClass, id);
            if (entity != null) {
                getCurrentSession().remove(entity);
                log.info("{} with ID {} has been deleted", entityClass.getSimpleName(), id);
            } else {
                log.warn("{} with ID {} does not exist, cannot delete", entityClass.getSimpleName(), id);
            }
        } catch (Exception e) {
            log.error("Error deleting entity: {}", e.getMessage(), e);
            throw e;
        }
    }

    private void copyNonNullProperties(T source, T target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        return emptyNames.toArray(new String[0]);
    }
}