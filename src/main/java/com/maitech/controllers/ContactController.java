package com.maitech.controllers;

import com.maitech.models.ContactModel;
import com.maitech.services.ContactService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ContactController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final int ROW_PER_PAGE = 5;

    @Autowired
    private ContactService contactService;

    @Value("${msg.title}")
    private String title;

    @GetMapping(value = {"/", "/index"})
    public String index(Model model) {
        model.addAttribute("title", title);
        return "index";
    }

    @GetMapping(value = "/contacts")
    public String getContacts(Model model, @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
        List<ContactModel> contacts = contactService.findAll(pageNumber, ROW_PER_PAGE);

        Long count = contactService.count();
        boolean hasPrev = pageNumber > 1;
        boolean hasNext = (pageNumber * ROW_PER_PAGE) < count;

        model.addAttribute("contacts", contacts);
        model.addAttribute("hasPrev", hasPrev);
        model.addAttribute("prev", pageNumber - 1);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("next", pageNumber + 1);

        return "contact-list";
    }

    @GetMapping(value = "/contacts/{id}")
    public String getContactById(Model model, @PathVariable Long id) {
        ContactModel contactModel = null;
        try {
            contactModel = contactService.findById(id);
        } catch (Exception ex) {
            model.addAttribute("errorMessage", "Contact not found");
        }
        model.addAttribute("contact", contactModel);
        return "contact";
    }

    @GetMapping(value = {"/contacts/add"})
    public String showAddContact(Model model) {
        ContactModel contactModel = new ContactModel();
        model.addAttribute("add", true);
        model.addAttribute("contact", contactModel);

        return "contact-edit";
    }

    @PostMapping(value = "/contacts/add")
    public String addContact(Model model, @ModelAttribute("contact") ContactModel contactModel) {
        try {
            ContactModel newContact = contactService.save(contactModel);
            return "redirect:/contacts/" + String.valueOf(newContact.getId());
        } catch (Exception ex) {
            // log exception first,
            // then show error
            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);

            //model.addAttribute("contact", contact);
            model.addAttribute("add", true);
            return "contact-edit";
        }
    }

    @GetMapping(value = {"/contacts/{id}/edit"})
    public String showEditContact(Model model, @PathVariable Long id) {
        ContactModel contactModel = null;
        try {
            contactModel = contactService.findById(id);
        } catch (Exception ex) {
            model.addAttribute("errorMessage", "Contact not found");
        }
        model.addAttribute("add", false);
        model.addAttribute("contact", contactModel);
        return "contact-edit";
    }

    @PostMapping(value = {"/contacts/{id}/edit"})
    public String updateContact(Model model, @PathVariable Long id, @ModelAttribute("contact") ContactModel contactModel) {
        try {
            contactModel.setId(id);
            contactService.update(contactModel);
            return "redirect:/contacts/" + String.valueOf(contactModel.getId());
        } catch (Exception ex) {
            // log exception first,
            // then show error
            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);

            model.addAttribute("add", false);
            return "contact-edit";
        }
    }

    @GetMapping(value = {"/contacts/{id}/delete"})
    public String showDeleteContactById(Model model, @PathVariable Long id) {
        ContactModel contactModel = null;
        try {
            contactModel = contactService.findById(id);
        } catch (Exception ex) {
            model.addAttribute("errorMessage", "Contact not found");
        }
        model.addAttribute("allowDelete", true);
        model.addAttribute("contact", contactModel);
        return "contact";
    }

    @PostMapping(value = {"/contacts/{id}/delete"})
    public String deleteContactById(Model model, @PathVariable Long id) {
        try {
            contactService.deleteById(id);
            return "redirect:/contacts";
        } catch (Exception ex) {
            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            return "contact";
        }
    }
}
