package com.fiiiiive.zippop.common.baseresponse;

public enum BaseResponseMessage {
    // 요청 성공 1000
    REQUEST_SUCCESS(true, 1000, "요청이 정상적으로 처리되었습니다"),

    // 회원 기능
    // 회원가입 2000
    MEMBER_REGISTER_SUCCESS(true, 2000, "이메일 인증을 완료해주세요"),
    MEMBER_REGISTER_FAIL_ID_EMPTY(false, 2001, "아이디를 입력해주세요"),
    MEMBER_REGISTER_FAIL_ID_FORMAT(false, 2002, "아이디 형식이 맞지 않습니다."),
    MEMBER_REGISTER_FAIL_ID_DUPLICATION(false, 2003, "아이디가 중복되었습니다."),
    MEMBER_REGISTER_FAIL_PASSWORD_EMPTY(false, 2004, "패스워드를 입력해주세요"),
    MEMBER_REGISTER_FAIL_PASSWORD_FORMAT(false, 2014, "패스워드 형식이 맞지 않습니다."),
    MEMBER_REGISTER_FAIL_PASSWORD_COMPLEXITY(false, 2015, "복잡한 패스워드를 사용해주세요"),
    MEMBER_REGISTER_FAIL_NAME_EMPTY(false, 2016, "이름을 입력해주세요"),

    // 이메일 인증 2010
    MEMBER_EMAIL_VERIFY_SUCCESS(true, 2010, "이메일 인증을 완료했습니다."),
    MEMBER_EMAIL_VERIFY_FAIL(false, 2011, "이메일 인증에 실패했습니다."),

    // 로그인 2100
    MEMBER_LOGIN_SUCCESS(true, 2100, "로그인에 성공했습니다."),
    MEMBER_LOGIN_FAIL(false, 2101, "아이디 또는 비밀번호를 확인해주세요"),
    //    MEMBER_LOGIN_FAIL(false, 2101, "사용자 인증에 실패했습니다."),

    // 팝업 3000
    // 팝업 스토어 등록(C)
    POPUP_STORE_REGISTER_SUCCESS(true, 3000, "팝업 스토어 등록에 성공했습니다."),
    POPUP_STORE_REGISTER_FAIL_DUPLICATION(false, 3001, "이미 등록된 팝업 스토어 입니다."),

    // 팝업 스토어 목록 조회(R)
    POPUP_STORE_SEARCH_SUCCESS(true, 3100, "팝업 스토어 목록 조회에 성공했습니다."),
    POPUP_STORE_SEARCH_FAIL_NOT_EXIST(false, 3101, "해당 팝업 스토어가 존재하지 않습니다."),
    // POPUP_STORE_VIEW_DETAIL, 팝업 스토어 상세 정보 보기

    // 팝업 스토어 예약
    POPUP_STORE_RESERVATION_SUCCESS(true, 3200, "예약에 성공했습니다."),
    POPUP_STORE_RESERVATION_FAIL_TIME_CLOSED(false, 3201, "해당 시간대는 예약이 마감되었습니다."),

    // 팝업 스토어 리뷰 등록
    POPUP_STORE_REVIEW_SUCCESS(true, 3300, "팝업 스토어 리뷰 등록에 성공했습니다."),
    POPUP_STORE_REVIEW_FAIL_CONTENTS_EMPTY(false, 3301, "팝업 스토어 리뷰 내용을 작성해주세요"),

    // 팝업 굿즈 4000
    // 팝업 굿즈 등록(C)
    POPUP_GOODS_REGISTER_SUCCESS(true, 4000,"상품 등록에 성공했습니다."),
    POPUP_GOODS_REGISTER_FAIL(true, 4001, "상품 등록에 실패했습니다."),
    POPUP_GOODS_REGISTER_FAIL_IMG_FORMAT(false, 4002, "상품 이미지 파일 형식이 맞지 않습니다."),
    POPUP_GOODS_REGISTER_FAIL_NAME_EMPTY(false, 4003, "상품명을 입력해주세요"),
    POPUP_GOODS_REGISTER_FAIL_PRICE_EMPTY(false, 4004, "상품 가격을 입력해주세요"),
    POPUP_GOODS_REGISTER_FAIL_AMOUNT_EMPTY(false, 4005, "등록할 상품의 수량을 입력해주세요"),
    // 팝업 굿즈 조회(R)
    POPUP_GOODS_SEARCH_SUCCESS(true, 4100, "팝업 굿즈 조회에 성공했습니다."),
    POPUP_GOODS_SEARCH_FAIL(false, 4101, "팝업 굿즈 조회에 실패했습니다."),
    // 팝업 굿즈 수정(U)
    POPUP_GOODS_UPDATE_SUCCESS(true, 4200, "팝업 굿즈 수정에 성공했습니다."),
    POPUP_GOODS_UPDATE_FAIL(false, 4201, "팝업 굿즈 수정에 실패했습니다."),
    // 팝업 굿즈 삭제(D)
    POPUP_GOODS_DELETE_SUCCESS(true, 4300, "팝업 굿즈 삭제에 성공했습니다."),
    POPUP_GOODS_DELETE_FAIL(false, 4301, "팝업 굿즈 삭제에 실패했습니다."),
    // 팝업 굿즈 구매
    POPUP_GOODS_PAY_SUCCESS(true, 4400, "결제에 성공했습니다."),
    POPUP_GOODS_PAY_FAIL_PAYMENT_EMPTY(false, 4401, "결제 수단을 입력해주세요"),
    POPUP_GOODS_PAY_FAIL_SOLD_OUT(false, 4402,"해당 상품은 품절되었습니다.");

    // 지금은 code 4402이런식인데 나중엔 요구사항번호로 code값 넣기
    // 커뮤니티 기능 5000
    // 게시글
    // 댓글
    private Boolean success;
    private Integer code;
    private String message;

    BaseResponseMessage(Boolean success, Integer code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}