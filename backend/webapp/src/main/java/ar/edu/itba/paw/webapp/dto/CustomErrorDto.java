package ar.edu.itba.paw.webapp.dto;

public class CustomErrorDto {
    private String message;

    public static CustomErrorDto fromException(final String message) {
        final CustomErrorDto dto = new CustomErrorDto();
        dto.message = message;
        return dto;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
