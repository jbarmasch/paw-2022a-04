package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.service.EventService;
import ar.edu.itba.paw.service.LocationService;
import ar.edu.itba.paw.service.TagService;
import ar.edu.itba.paw.service.TypeService;
import ar.edu.itba.paw.webapp.auth.UserManager;
import ar.edu.itba.paw.webapp.form.FilterForm;
import ar.edu.itba.paw.webapp.form.SearchForm;
import ar.edu.itba.paw.webapp.helper.FilterUtils;
import ar.edu.itba.paw.webapp.validations.IntegerArray;
import ar.edu.itba.paw.webapp.validations.NumberFormat;
import org.springframework.beans.factory.annotation.Autowired;
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
import javax.validation.constraints.Pattern;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @RequestMapping(value = "/events", method = { RequestMethod.GET })
    public ModelAndView browseEvents(@ModelAttribute("filterForm") final FilterForm form, final BindingResult errors,
                                     @ModelAttribute("searchForm") final SearchForm searchForm,
                                     @RequestParam(value = "locations", required = false) @IntegerArray final String[] locations,
                                     @RequestParam(value = "types", required = false) @IntegerArray final String[] types,
                                     @RequestParam(value = "minPrice", required = false) @NumberFormat(decimal = true) final String minPrice,
                                     @RequestParam(value = "maxPrice", required = false) @NumberFormat(decimal = true) final String maxPrice,
                                     @RequestParam(value = "search", required = false) final String search,
                                     @RequestParam(value = "tags", required = false) @IntegerArray final String[] tags,
                                     @RequestParam(value = "username", required = false) final String username,
                                     @RequestParam(value = "order", required = false) @Pattern(regexp = "minPrice|date") final String order,
                                     @RequestParam(value = "orderBy", required = false) @Pattern(regexp = "ASC|DESC") final String orderBy,
                                     @RequestParam(value = "page", required = false, defaultValue = "1") @Min(1) final int page) {
        List<Event> events = eventService.filterBy(locations, types, minPrice, maxPrice, search, tags, username, order, orderBy, page);

        final ModelAndView mav = new ModelAndView("events");
        mav.addObject("page", page);
        mav.addObject("allLocations", locationService.getAll());
        mav.addObject("allTypes", typeService.getAll());
        mav.addObject("allTags", tagService.getAll());
        mav.addObject("events", events);
        mav.addObject("size", events.size());
        return mav;
    }

    @RequestMapping(value = "/events", method = { RequestMethod.POST })
    public ModelAndView browseByFilters(@Valid @ModelAttribute("filterForm") final FilterForm form, final BindingResult errors) {
        if (errors.hasErrors())
            return new ModelAndView("error");

        Map<String, Object> filters = new HashMap<>();
        filters.put("locations", form.getLocations());
        filters.put("types", form.getTypes());
        filters.put("tags", form.getTags());
        filters.put("minPrice", form.getMinPrice());
        filters.put("maxPrice", form.getMaxPrice());
        filters.put("search", form.getSearchQuery());
        filters.put("username", form.getUsername());
        filters.put("order", form.getOrder());
        filters.put("orderBy", form.getOrderBy());
        String endURL = FilterUtils.createFilter(filters);

        if (endURL.isEmpty())
            return new ModelAndView("redirect:/events");
        return new ModelAndView("redirect:/events?" + endURL);
    }

    @RequestMapping(value = "/search", method = { RequestMethod.POST })
    public ModelAndView search(@Valid @ModelAttribute("searchForm") final SearchForm searchForm, final BindingResult errors) {
        if (errors.hasErrors())
            return new ModelAndView("error");

        Map<String, Object> filters = new HashMap<>();
        filters.put("search", searchForm.getQuery());
        if (searchForm.isByUsername())
            filters.put("username", searchForm.getUsername());
        String endURL = FilterUtils.createFilter(filters);

        if (endURL.isEmpty())
            return new ModelAndView("redirect:/events");
        return new ModelAndView("redirect:/events?" + endURL);
    }

    @ModelAttribute
    public void addAttributes(Model model, final SearchForm searchForm) {
        model.addAttribute("username", userManager.getUsername());
        model.addAttribute("isCreator", userManager.isCreator());
    }
}
