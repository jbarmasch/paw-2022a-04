package ar.edu.itba.paw.webapp.controller;

//@Validated
//@Controller
//public class FilterController {
//    @Autowired
//    private EventService eventService;
//    @Autowired
//    private LocationService locationService;
//    @Autowired
//    private TypeService typeService;
//    @Autowired
//    private TagService tagService;
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(FilterController.class);
//
//    @RequestMapping(value = "/events", method = { RequestMethod.GET })
//    public ModelAndView browseEvents(@ModelAttribute("filterForm") final FilterForm form, final BindingResult errors,
//                                     @ModelAttribute("searchForm") final SearchForm searchForm,
//                                     @RequestParam(value = "locations", required = false) final Integer[] locations,
//                                     @RequestParam(value = "types", required = false) final Integer[] types,
//                                     @RequestParam(value = "minPrice", required = false) final Double minPrice,
//                                     @RequestParam(value = "maxPrice", required = false) final Double maxPrice,
//                                     @RequestParam(value = "search", required = false) final String search,
//                                     @RequestParam(value = "tags", required = false) final Integer[] tags,
//                                     @RequestParam(value = "searchUsername", required = false) final String username,
//                                     @RequestParam(value = "order", required = false) final Order order,
//                                     @RequestParam(value = "soldOut", required = false) final Boolean showSoldOut,
//                                     @RequestParam(value = "page", required = false, defaultValue = "1") @Min(1) final int page) {
//        List<Event> events = eventService.filterBy(locations, types, minPrice, maxPrice, search, tags, username, order, showSoldOut, page);
//
//        final ModelAndView mav = new ModelAndView("events");
//        mav.addObject("page", page);
//        mav.addObject("allLocations", locationService.getAll());
//        mav.addObject("allTypes", typeService.getAll());
//        mav.addObject("allTags", tagService.getAll());
////        mav.addObject("events", events.stream().limit(10).collect(Collectors.toList()));
//        mav.addObject("events", events);
//        mav.addObject("size", events.size());
//        return mav;
//    }
//
//    @RequestMapping(value = "/events", method = { RequestMethod.POST })
//    public ModelAndView browseByFilters(@Valid @ModelAttribute("filterForm") final FilterForm form, final BindingResult errors) {
//        if (errors.hasErrors()) {
//            LOGGER.error("FilterForm has errors: {}", errors.getAllErrors().toArray());
//            return ErrorController.createErrorModel("400", "Bad request");
//        }
//
//        Map<String, Object> filters = new HashMap<>();
//        filters.put("locations", form.getLocations());
//        filters.put("types", form.getTypes());
//        filters.put("tags", form.getTags());
//        filters.put("minPrice", form.getMinPrice());
//        filters.put("maxPrice", form.getMaxPrice());
//        filters.put("search", form.getSearchQuery());
//        filters.put("searchUsername", form.getUsername());
//        filters.put("order", form.getOrder());
//        if (form.getShowSoldOut())
//            filters.put("soldOut", form.getShowSoldOut());
//        String endURL = FilterUtils.createFilter(filters);
//
//        if (endURL.isEmpty())
//            return new ModelAndView("redirect:/events");
//
//        LOGGER.debug("Created URL /events?{}", endURL);
//        return new ModelAndView("redirect:/events?" + endURL);
//    }
//
//    @RequestMapping(value = "/search", method = { RequestMethod.POST })
//    public ModelAndView search(@Valid @ModelAttribute("searchForm") final SearchForm searchForm, final BindingResult errors) {
//        if (errors.hasErrors()) {
//            LOGGER.error("SearchForm has errors: {}", errors.getAllErrors().toArray());
//            return ErrorController.createErrorModel("400", "Bad request");
//        }
//
//        Map<String, Object> filters = new HashMap<>();
//        filters.put("search", searchForm.getQuery());
//        if (searchForm.isByUsername())
//            filters.put("searchUsername", searchForm.getUsername());
//        String endURL = FilterUtils.createFilter(filters);
//
//        if (endURL.isEmpty())
//            return new ModelAndView("redirect:/events");
//
//        LOGGER.debug("Created URL /events?{}", endURL);
//        return new ModelAndView("redirect:/events?" + endURL);
//    }
//}
