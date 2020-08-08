package com.maitech.services;

import com.maitech.models.ContactModel;
import com.maitech.repositories.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    private boolean existsById(Long id) {
        return contactRepository.existsById(id);
    }

    public ContactModel findById(Long id) throws Exception {
        ContactModel contactModel = contactRepository.findById(id).orElse(null);

        if (contactModel == null)
            throw new Exception("Cannot find Contact with id : " + id);
        else
            return contactModel;
    }

    public List<ContactModel> findAll(int pageNumber, int rowPerPage) {
        List<ContactModel> contactModels = new ArrayList<>();
        Pageable sortedByAsc = PageRequest.of(pageNumber - 1, rowPerPage, Sort.by("id").ascending());

        contactRepository.findAll(sortedByAsc).forEach(contactModels::add);

        return contactModels;
    }

    public ContactModel save(ContactModel contactModel) throws Exception {
        if (!StringUtils.isEmpty(contactModel.getName())) {
            if (contactModel.getId() != null && existsById(contactModel.getId()))
                throw new Exception("Contact with id : " + contactModel.getId() + " already exists");
            return contactRepository.save(contactModel);
        }
        else {
            Exception exception = new Exception("Failed to save contact");
            //exception.addErrorMessage("Contact is null or empty");
            throw exception;
        }
    }

    public void update(ContactModel contactModel) throws Exception {
        if (!StringUtils.isEmpty(contactModel.getName())) {
            if (contactModel.getId() != null && existsById(contactModel.getId()))
                throw new Exception("Contact with id : " + contactModel.getId() + " already exists");
            contactRepository.save(contactModel);
        }
        else {
            Exception exception = new Exception("Failed to save contact");
            //exception.addErrorMessage("Contact is null or empty");
            throw exception;
        }
    }

    public void deleteById(Long id) throws Exception {
        if (!existsById(id))
            throw new Exception("Cannot find contact with id : " + id);
        else
            contactRepository.deleteById(id);
    }

    public Long count() {
        return contactRepository.count();
    }
}
