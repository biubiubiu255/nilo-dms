
package com.nilo.dms.common.exception;

/**
 * Created by ronny on 2017/8/23.
 */
public enum BizErrorCode implements ErrorCode {

    /**
     * ================================
     */
    USER_ID_ILLEGAL("user id is illegal.", "100001"),
    OLD_PASSWORD_ERROR("old password is not right.", "100002"),
    VERIFY_CODE_ERROR("Verify code error", "100003"),
    LOGIN_FAILED("username or password not right.", "100004"),
    MERCHANT_NAME_EMPTY("Merchant Name is empty.", "100005"),
    COMPANY_NAME_EMPTY("Company Name is empty.", "100006"),
    MERCHANT_ID_EMPTY("Merchant ID is empty.", "100007"),
    MERCHANT_STATUS_EMPTY("Merchant Status is empty.", "100008"),
    ROLE_ID_EMPTY("Role Id is empty.", "100009"),

    CLIENT_NAME_EMPTY("Client Name is empty.", "100010"),
    REFERENCE_NO_EMPTY("Reference No is empty.", "100011"),
    SERIAL_TYPE_ERROR("Serial Number Type is not right.", "100012"),
    GOODS_EMPTY("goods info is empty.", "100013"),
    RECEIVER_EMPTY("receiver info is empty", "100014"),
    SENDER_EMPTY("sender info is empty.", "100015"),
    SERIAL_RULE_ID_EMPTY("serial rule id is empty.", "100016"),
    SERIAL_TYPE_EMPTY("serial type is empty.", "100017"),
    SERIAL_LENGTH_EMPTY("serial length is empty.", "100018"),
    ORDER_TYPE_EMPTY("Order Type is empty.", "100019"),
    DATA_EMPTY("Data is empty.", "100020"),
    SIGN_EMPTY("Sign is empty.", "100021"),
    Method_EMPTY("method is empty.", "100022"),
    TIMESTAMP_EMPTY("Timestamp is empty.", "100023"),
    METHOD_NOT_EXIST("method is not exist.", "100024"),
    SING_ERROR("Sign is error.", "100025"),


    REQUEST_ID_EMPTY("request id is empty", "100026"),
    SERVICE_ID_EMPTY("service id is empty", "100027"),

    ORDER_NOT_EXIST("Waybill Number {0} is not exist.", "100028"),
    ORDER_STATUS_LIMITED("Waybill Number {0} status is limited", "100029"),
    TASK_NOT_EXIST("Task {0}  not exist.", "100030"),
    TASK_NOT_ALLOW("Task {0}  not allow.", "100031"),

    LOADING_NOT_EXIST("Loading No. {0}  not exist.", "100032"),
    LOADING_EMPTY("Loading No. {0}  empty.", "100034"),
    LOADING_STATUS_LIMITED("Loading No. {0} status is limited.", "100033"),

    OPT_TYP_EMPTY("Opt Type emtpty", "100034"),
    ORDER_NO_EMPTY("Order No emtpty", "100035"),
    OPT_USER_EMPTY("Operate User emtpty", "100036"),


    CODE_NOT_EXIST("code:{0} not exist", "100037"),
    STAFF_EXIST("Staff ID:{0} is already exist", "100038"),
    DISABLED_ACCOUNT("Account is disabled.", "1000039"),

    ABNORMAL_ORDER_NOT_EXIST("Abnormal order {0}  not exist.", "100040"),
    ABNORMAL_ORDER_STATUS_LIMITED("Abnormal order {0} status is limited", "100041"),

    HANDLE_TYPE_NOT_CONFIG("Handle Type {0} not config", "100042"),
    HANDLE_TYPE_CONFIG_EXIST("Handle Type {0} exist", "100043"),

    GOODS_TYPE_EMPTY("Goods Type empty", "100044"),

    RECEIVE_NAME_EMPTY("receiver name empty", "100045"),
    RECEIVE_PHONE_EMPTY("receiver contact number empty", "100046"),
    RECEIVE_ADDRESS_EMPTY("receiver address emtpy", "100047"),

    USER_NAME_EMPTY("User Name empty.", "100048"),
    USER_TYPE_EMPTY("User Type empty.", "100049"),
    NAME_EMPTY("name empty", "100050"),
    EMAIL_EMPTY("email empty", "100051"),
    PHONE_EMPTY("phone empty", "100052"),
    USER_NAME_EXIST("User Name exist.", "100053"),
    PASSWORD_EMPTY("Password empty.", "100054"),

    TIMESTAMP_ERROR("Timestamp not right.", "100055"),
    BIZ_FEE_CONFIG_EXIST("Biz Type {0} exist", "100056"),
    CODE_EXIST("code:{0}  exist", "100057"),
    DELAY_TIMES_LIMIT("Delay Times is limit", "100058"),
    RIDER_IS_EMPTY("Rider is empty", "100059"),
    USER_URL_NOT_ALLOWED("User Url Not Allowed.", "100060"),
    HANDLE_TYPE_EMPTY("Handle type is empty.", "100061"),
    PACKAGE_NO_ERROR("Package No is not exist.", "100062"),

    APP_KEY_NOT_EXIST("App Key {0} is not exist.", "100063"),
    ARRIVE_EMPTY("Arrive Scan is empty.", "100064"),
    ALREADY_SCAN("{0}  already scan", "100065"),
    NEW_PASSWORD_NOT_EQUAL("New Password not equal.", "100066"),
    PACKAGE_ALREADY_PACKAGE("Delivery No {0} already in package.", "100067"),
    UNPACK_NOT_ALLOW("Delivery No {0} can not unpack.", "100068"),
    DELIVERY_NO_EXIST("Delivery No {0}  exist.", "100069"),
    PACKAGE_EMPTY("Package is empty", "100070"),
    WEIGHT_MORE_THAN_0("Weight needs more than 0 ", "100071"),

    ORDER_NO_ARRIVE("The order has not arrived", "100071"),  //订单未到达
    STAFF_NOT_EXIST("Staff not exist", "100072"),

    HandleNO_NOT_EXIST("The loading list does not exist", "100073"),
    //STAFF_NOT_EXIST("Staff not exist", "100074"),

    PASSWORD_ERROR("Password Error.", "100073"),
    SIGN_PHOTO_EMPTY("Sign Photo Empty.", "100074"),

    NOT_STATION_INFO("No site information found.", "100075"),
    THIRD_EXPRESS_EMPTY("Third Express Empty.", "100076"),
    THIRD_DRIVER_EMPTY("Third Driver Empty.", "100077"),
    WAYBILL_EMPTY("Waybill Empty.", "100078"),

    REFUSE_ALREADY_EXISTS("The refuse package already exists", "100079"),

    NOT_FOUND_NEXTWORK("Do not found nextWork, please contact administrator.", "100080"),

    ;

    private final String description;
    private final String code;

    private BizErrorCode(String description, String code) {
        this.description = description;
        this.code = code;
    }

    @Override
    public ExceptionType getType() {
        return ExceptionType.BIZ_VERIFY_ERROR;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

}



