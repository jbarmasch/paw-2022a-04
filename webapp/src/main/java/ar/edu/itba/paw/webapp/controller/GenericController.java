package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Tag;
import ar.edu.itba.paw.model.Type;
import ar.edu.itba.paw.webapp.auth.UserManager;
import ar.edu.itba.paw.webapp.form.SearchForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Locale;

@ControllerAdvice
public class GenericController {
    @Autowired
    UserManager userManager;

    @ModelAttribute
    public void addAttributes(Model model, final SearchForm searchForm) {
        Locale locale = LocaleContextHolder.getLocale();
        Tag.setLocale(locale);
        Type.setLocale(locale);
        model.addAttribute("username", userManager.getUsername());
        model.addAttribute("isCreator", userManager.isCreator());
        model.addAttribute("isBouncer", userManager.isBouncer());
    }
}
