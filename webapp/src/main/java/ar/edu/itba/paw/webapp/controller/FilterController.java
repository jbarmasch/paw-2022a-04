package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Order;
import ar.edu.itba.paw.service.EventService;
import ar.edu.itba.paw.service.LocationService;
import ar.edu.itba.paw.service.TagService;
import ar.edu.itba.paw.service.TypeService;
import ar.edu.itba.paw.webapp.auth.UserManager;
import ar.edu.itba.paw.webapp.form.FilterForm;
import ar.edu.itba.paw.webapp.form.SearchForm;
import ar.edu.itba.paw.webapp.helper.FilterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.*;

@Validated
@Controller
public class FilterController {
    @Autowired
    private EventService eventService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;
    @Autowired
    private UserManager userManager;

    private static final Logger LOGGER = LoggerFactory.getLogger(FilterController.class);

    @RequestMapping(value = "/events", method = { RequestMethod.GET })
    public ModelAndView browseEvents(@ModelAttribute("filterForm") final FilterForm form, final BindingResult errors,
                                     @ModelAttribute("searchForm") final SearchForm searchForm,
                                     @RequestParam(value = "locations", required = false) final Integer[] locations,
                                     @RequestParam(value = "types", required = false) final Integer[] types,
                                     @RequestParam(value = "minPrice", required = false) final Double minPrice,
                                     @RequestParam(value = "maxPrice", required = false) final Double maxPrice,
                                     @RequestParam(value = "search", required = false) final String search,
                                     @RequestParam(value = "tags", required = false) final Integer[] tags,
                                     @RequestParam(value = "searchUsername", required = false) final String username,
                                     @RequestParam(value = "order", required = false) final Order order,
                                     @RequestParam(value = "showSoldOut", required = false) final Boolean showSoldOut,
                                     @RequestParam(value = "page", required = false, defaultValue = "1") @Min(1) final int page) {
        Locale locale = LocaleContextHolder.getLocale();
        List<Event> events = eventService.filterBy(locations, types, minPrice, maxPrice, search, tags, username, order, showSoldOut, page, locale);

        final ModelAndView mav = new ModelAndView("events");
        mav.addObject("page", page);
        mav.addObject("allLocations", locationService.getAll());
        mav.addObject("allTypes", typeService.getAll(locale));
        mav.addObject("allTags", tagService.getAll(locale));
        mav.addObject("events", events);
        mav.addObject("size", events.size());
        return mav;
    }

    @RequestMapping(value = "/events", method = { RequestMethod.POST })
    public ModelAndView browseByFilters(@Valid @ModelAttribute("filterForm") final FilterForm form, final BindingResult errors) {
        if (errors.hasErrors()) {
            LOGGER.error("FilterForm has errors: {}", errors.getAllErrors().toArray());
            return ErrorController.createErrorModel("400");
        }

        Map<String, Object> filters = new HashMap<>();
        filters.put("locations", form.getLocations());
        filters.put("types", form.getTypes());
        filters.put("tags", form.getTags());
        filters.put("minPrice", form.getMinPrice());
        filters.put("maxPrice", form.getMaxPrice());
        filters.put("search", form.getSearchQuery());
        filters.put("searchUsername", form.getUsername());
        filters.put("order", form.getOrder());
        filters.put("showSoldOut", form.getShowSoldOut());
        String endURL = FilterUtils.createFilter(filters);

        if (endURL.isEmpty())
            return new ModelAndView("redirect:/events");

        LOGGER.debug("Created URL /events?{}", endURL);
        return new ModelAndView("redirect:/events?" + endURL);
    }

    @RequestMapping(value = "/search", method = { RequestMethod.POST })
    public ModelAndView search(@Valid @ModelAttribute("searchForm") final SearchForm searchForm, final BindingResult errors) {
        if (errors.hasErrors()) {
            LOGGER.error("SearchForm has errors: {}", errors.getAllErrors().toArray());
            return ErrorController.createErrorModel("400");
        }

        Map<String, Object> filters = new HashMap<>();
        filters.put("search", searchForm.getQuery());
        if (searchForm.isByUsername())
            filters.put("searchUsername", searchForm.getUsername());
        String endURL = FilterUtils.createFilter(filters);

        if (endURL.isEmpty())
            return new ModelAndView("redirect:/events");

        LOGGER.debug("Created URL /events?{}", endURL);
        return new ModelAndView("redirect:/events?" + endURL);
    }

    @ModelAttribute
    public void addAttributes(Model model, final SearchForm searchForm) {
        model.addAttribute("username", userManager.getUsername());
        model.addAttribute("isCreator", userManager.isCreator());
    }
}
