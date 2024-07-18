package com.fiiiiive.zippop.common.baseresponse;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),

    /**
     * 2000 : COURSE 에러
     */
    POST_COURSE_EMPTY_NAME(false, 2001, "코스의 이름을 입력해주세요."),
    POST_COURSE_EMPTY_PRICE(false, 2002, "코스의 가격을 입력해주세요."),
    POST_COURSE_EMPTY_DESCRIPTION(false, 2003, "코스의 설명을 입력해주세요."),

    POST_COURSE_PRE_EXIST_NAME(false, 2004, "이미 존재하는 코스 이름입니다. 다른 이름을 입력해주세요."),
    COURSE_LIST_NULL(false,2005,"등록된 코스가 존재하지 않습니다."),
    COURSE_NULL(false,2006,"등록된 코스가 존재하지 않습니다."),

    /**
     * 3000 : MEMBER 에러
     */
    POST_USERS_EMPTY_EMAIL(false, 3001, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 3002, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false,3003,"중복된 이메일입니다."),

    POST_USERS_EMPTY_NAME(false,3004,"이름을 입력해주세요."),
    POST_USERS_EMPTY_PASSWORD(false,3005,"비밀번호를 입력해주세요."),
    POST_USERS_INVALID_USER_INFO(false,3006,"이메일 또는 패스워드를 확인해주세요."),


    /**
     * 4000 : 주문 및 결제 에러
     */
    ORDERS_VALIDATION_FAIL(false, 4001, "결제 정보가 잘못되었습니다."),
    IAMPORT_ERROR(false, 4002, "결제 금액이 잘못되었습니다."),
    ORDERS_NOT_ORDERED(false, 4003, "결제 정보가 없습니다. 구해 후 이용해주세요."),



    /**
     * 5000 : 장바구니
     */
    CART_ADD_SUCCESS(true, 5000, "강의가 장바구니에 추가되었습니다.");


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
