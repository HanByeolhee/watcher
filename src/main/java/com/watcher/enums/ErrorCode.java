package com.watcher.enums;

public enum ErrorCode {
    /** 1xxx~5xxx : 클라이언트 오류, 6xxx~9xxx : 서버오류 */


    // 2xxx: 클라이언트 요청 값 오류
    ERROR_2001(400,"2001","API TOKEN 검증 오류"),



    // 7xxx: 통신관련 오류

    // 8xxx: 암호화, 토큰 관련 오류

    // 9xxx: 서버 기본 오류
    ERROR_9999(500,"9999","정의되지 않은 서버 오류"),
    ;

    ErrorCode(int status, String code, String msg) {
        this.status = status;
        this.errorCode = code;
        this.message = msg;
    }

    private int status;
    private String errorCode;
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}