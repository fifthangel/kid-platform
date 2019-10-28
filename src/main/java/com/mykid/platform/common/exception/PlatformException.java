package com.mykid.platform.common.exception;

/**
 * FEBS系统内部异常
 *
 * @author MrBird
 */
public class PlatformException extends Exception {

    private static final long serialVersionUID = -994962710559017255L;

    public PlatformException(String message) {
        super(message);
    }
}
