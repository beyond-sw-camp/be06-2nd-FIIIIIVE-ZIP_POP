package com.fiiiiive.zippop.common.responses;

public enum BaseResponseMessage {

    // 프로토콜
    // 인증 실패()
    INTERNAL_SERVER_ERROR(false, 500, "내부 서버 오류"),
    // REQUEST_FAIL(false, 500, "내부 서버 오류"),
    TOKEN_EXPIRED(false, 403, "JWT 토큰이 만료되었습니다."),
    TOKEN_NULL(false, 403, "JWT 토큰이 없습니다."),
    TOKEN_INVALID(false, 404, "JWT 토큰이 유효하지 않습니다."),


    // 요청 성공 1000
    REQUEST_SUCCESS(true, 200, "요청이 정상적으로 처리되었습니다"),
    REQUEST_FAIL(false, 405, "요청을 실패했습니다."),

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


    // 5000 : 주문 및 결제 에러
    // 주문 실패 500
    IAMPORT_ERROR(false, 5002, "결제 금액이 잘못되었습니다."),
    ORDERS_NOT_ORDERED(false, 5003, "결제 정보가 없습니다. 구해 후 이용해주세요."),
    GOODS_NULL(false, 5004, "굿즈가 존재하지 않습니다."),
    ORDERS_VALIDATION_SUCCESS(true, 5000, "결제 검증에 실패했습니다."),
    ORDERS_VALIDATION_FAIL(false, 5001, "결제 정보가 잘못되었습니다."),
    ORDERS_VALIDATION_ERROR(false, 5002, "결제 금액이 잘못되었습니다."),
    POPUP_GOODS_NULL(false, 5004, "굿즈가 존재하지 않습니다."),

    POPUP_GOODS_PAY_SUCCESS(true, 4400, "결제에 성공했습니다."),
    POPUP_GOODS_PAY_FAIL_PAYMENT_EMPTY(false, 4401, "결제 수단을 입력해주세요"),
    POPUP_GOODS_PAY_FAIL_SOLD_OUT(false, 4402,"해당 상품은 품절되었습니다."),

    // 6000 : 게시글
    // 7000 : 댓글
    // 8000: 채팅
    //메세지 전송
    CHAT_MESSAGE_SEND_SUCCESS(true, 8000, "채팅 메시지 전송에 성공했습니다."),
    CHAT_MESSAGE_SEND_FAIL(false, 8001, "채팅 메시지 전송에 실패했습니다."),
    //채팅방 생성
    CHAT_ROOM_CREATE_SUCCESS(true, 8100, "채팅방 생성에 성공했습니다."),
    CHAT_ROOM_CREATE_FAIL(false, 8101, "채팅방 생성에 실패했습니다."),
    //채팅방 참여
    CHAT_ROOM_JOIN_SUCCESS(true, 8200, "채팅방에 성공적으로 참여했습니다."),
    CHAT_ROOM_JOIN_FAIL(false, 8201, "채팅방 참여에 실패했습니다."),
    //채팅방 나가기
    CHAT_ROOM_LEAVE_SUCCESS(true, 8300, "채팅방에서 성공적으로 나갔습니다."),
    CHAT_ROOM_LEAVE_FAIL(false, 8301, "채팅방 나가기에 실패했습니다."),
    //채팅방 사용자 조회
    CHAT_USER_COUNT_SEARCH_SUCCESS(true, 8400, "채팅방 사용자 수 조회에 성공했습니다."),
    CHAT_USER_COUNT_SEARCH_FAIL(false, 8401, "채팅방 사용자 수 조회에 실패했습니다."),
    //채팅방 기록 조회
    CHAT_HISTORY_SEARCH_SUCCESS(true, 8500, "채팅 기록 조회에 성공했습니다."),
    CHAT_HISTORY_SEARCH_FAIL(false, 8501, "채팅 기록 조회에 실패했습니다."),
    //채팅방 조회
    CHAT_ROOM_SEARCH_SUCCESS(true, 8600, "채팅방 목록 조회에 성공했습니다."),
    CHAT_ROOM_SEARCH_FAIL(false, 8601, "채팅 기록 조회에 실패했습니다.");



    private Boolean success;
    private Integer code;
    private String message;

    BaseResponseMessage(Boolean success, Integer code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }
    public static BaseResponseMessage findByCode(Integer code) {
        for (BaseResponseMessage message : values()) { if (message.getCode().equals(code)) { return message; }}
        return null;
    }
    public Boolean getSuccess() { return success; }

    public Integer getCode() { return code; }

    public String getMessage() { return message; }
}