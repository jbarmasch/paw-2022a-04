package ar.edu.itba.paw.service;

public interface CodeService {
    String getCode(String plainText);

    byte[] createQr(String text);
}
