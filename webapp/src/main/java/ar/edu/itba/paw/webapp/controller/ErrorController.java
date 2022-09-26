package ar.edu.itba.paw.webapp.controller;

//@Controller
//public class ErrorController {
//    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorController.class);
//    @Autowired
//    private MessageSource messageSource;
//
//    @RequestMapping("/403")
//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    public ModelAndView forbidden() {
//        LOGGER.error("FORBIDDEN");
//        String errorCode = "403";
//        String errorMessage = messageSource.getMessage("exception.forbidden", null, LocaleContextHolder.getLocale());
//        return createErrorModel(errorCode, errorMessage);
//    }
//
//    @RequestMapping("/404")
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public ModelAndView notFound() {
//        LOGGER.error("NOT FOUND");
//        String errorCode = "404";
//        String errorMessage = messageSource.getMessage("exception.notFound", null, LocaleContextHolder.getLocale());
//        return createErrorModel(errorCode, errorMessage);
//    }
//
//    public static ModelAndView createErrorModel(String errorCode, String errorMessage) {
//        final ModelAndView mav = new ModelAndView("error");
//        mav.addObject("code", errorCode);
//        mav.addObject("message", errorMessage);
//        return mav;
//    }
//}
