package com.maitech.repositories;

import com.maitech.models.ContactModel;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ContactRepository extends PagingAndSortingRepository<ContactModel, Long>, JpaSpecificationExecutor<ContactModel> {
}
