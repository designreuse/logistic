package com.frame.web.base.baseRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class BaseRepositoryFactoryBean<R extends JpaRepository<T, I>, T, I extends Serializable>
        extends JpaRepositoryFactoryBean<R, T, I> {

    public BaseRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
        super(repositoryInterface);
    }
    @Override
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
        return new BaseRepositoryFactory(entityManager);
    }

    public static class BaseRepositoryFactory<T, I extends Serializable>
            extends JpaRepositoryFactory {

        private final EntityManager em;

        public BaseRepositoryFactory(EntityManager entityManager) {
            super(entityManager);
            this.em = entityManager;
        }

        @Override
        protected JpaRepositoryImplementation<?, ?> getTargetRepository(RepositoryInformation information, EntityManager entityManager) {
            return new BaseRepositoryImpl<T, I>((Class<T>) information.getDomainType(), em);
        }

        @Override
        protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
            return BaseRepositoryImpl.class;
        }
    }
}
