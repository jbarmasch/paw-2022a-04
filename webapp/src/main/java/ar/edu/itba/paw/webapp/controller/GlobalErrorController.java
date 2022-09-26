package ar.edu.itba.paw.webapp.controller;

//@ControllerAdvice
//public class GlobalErrorController {
//    @Autowired
//    private MessageSource messageSource;
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalErrorController.class);
//
//    @ExceptionHandler({IllegalTicketException.class, TicketNotBookedException.class})
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ModelAndView illegalTicket(Exception e) {
//        LOGGER.error("BAD REQUEST {}", e.getMessage());
//        String errorCode = "400";
//        String errorMessage = messageSource.getMessage(e.getMessage(), null, LocaleContextHolder.getLocale());
//        return ErrorController.createErrorModel(errorCode, errorMessage);
//    }
//
//    @ExceptionHandler({EventNotFoundException.class, UserNotFoundException.class, ImageNotFoundException.class,
//            StatsNotFoundException.class, TicketNotFoundException.class, BookingNotFoundException.class})
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public ModelAndView notFound(Exception e) {
//        LOGGER.error("NOT FOUND {}", e.getMessage());
//        String errorCode = "404";
//        String errorMessage = messageSource.getMessage(e.getMessage(), null, LocaleContextHolder.getLocale());
//        return ErrorController.createErrorModel(errorCode, errorMessage);
//    }
//
//    @ExceptionHandler({CancelBookingFailedException.class, BookingFailedException.class})
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ModelAndView serverError(Exception e) {
//        LOGGER.error("SERVER ERROR {}", e.getMessage());
//        String errorCode = "500";
//        String errorMessage = messageSource.getMessage(e.getMessage(), null, LocaleContextHolder.getLocale());
//        return ErrorController.createErrorModel(errorCode, errorMessage);
//    }
//
//    @ExceptionHandler({UserCannotRateException.class, EventFinishedException.class})
//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    public ModelAndView forbiddenAction(Exception e) {
//        LOGGER.error("FORBIDDEN {}", e.getMessage());
//        String errorCode = "403";
//        String errorMessage = messageSource.getMessage(e.getMessage(), null, LocaleContextHolder.getLocale());
//        return ErrorController.createErrorModel(errorCode, errorMessage);
//    }
//
//    @SuppressWarnings("deprecation")
//    @ExceptionHandler(MethodConstraintViolationException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ModelAndView constraintViolation(MethodConstraintViolationException e) {
//        MethodConstraintViolation<?> violation = e.getConstraintViolations().iterator().next();
//        LOGGER.error("BAD REQUEST -- {} {}", violation.getParameterName(), violation.getMessage());
//        String errorCode = "400";
//        String errorMessage = messageSource.getMessage("exception.methodConstraint", null, LocaleContextHolder.getLocale());
//        return ErrorController.createErrorModel(errorCode, errorMessage);
//    }
//
//    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ModelAndView typeMismatch(MethodArgumentTypeMismatchException e) {
//        LOGGER.error("BAD REQUEST -- {} must be {}", e.getParameter().getParameterName(), e.getRequiredType().getSimpleName());
//        String errorCode = "400";
//        String errorMessage = messageSource.getMessage("exception.argumentTypeMismatch", null, LocaleContextHolder.getLocale());
//        return ErrorController.createErrorModel(errorCode, errorMessage);
//    }
//
//    @ExceptionHandler(MaxUploadSizeExceededException.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ModelAndView maxUploadSize(MaxUploadSizeExceededException e) {
//        LOGGER.error("INTERNAL SERVER ERROR -- Max upload size is {}", e.getMaxUploadSize());
//        String errorCode = "500";
//        String errorMessage = messageSource.getMessage("exception.maxUploadSize", null, LocaleContextHolder.getLocale());
//        return ErrorController.createErrorModel(errorCode, errorMessage);
//    }
//
//    @ExceptionHandler({DataIntegrityViolationException.class})
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ModelAndView integrityViolation(Exception e) {
//        LOGGER.error("DATA INTEGRITY VIOLATION {}", e.getMessage());
//        String errorCode = "500";
//        String errorMessage = messageSource.getMessage("exception.dataIntegrity", null, LocaleContextHolder.getLocale());
//        return ErrorController.createErrorModel(errorCode, errorMessage);
//    }
//}
