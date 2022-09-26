package ar.edu.itba.paw.webapp.controller;

//@Controller
//public class ImageController {
//    @Autowired
//    private ImageService imageService;
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(ImageController.class);
//
//    @RequestMapping(value = "/image/{id}", method = { RequestMethod.GET }, produces = MediaType.IMAGE_PNG_VALUE)
//    @ResponseBody
//    public byte[] getImage(@PathVariable("id") @Min(1) final int imageId) {
//        Image image = imageService.getImageById(imageId).orElse(null);
//        if (image == null) {
//            LOGGER.error("Image not found");
//            throw new ImageNotFoundException();
//        }
//
//        return image.getImage();
//    }
//}
