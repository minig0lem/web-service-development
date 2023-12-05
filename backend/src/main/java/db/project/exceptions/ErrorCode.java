package db.project.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    FAIL_REPORT(400, "FAILURE TO RECEIVE FAULT REPORT"),
    FAIL_MAP_INFO(400, "FAILURE VIEW INFO"),
    NO_TICKET(400, "DON'T HAVE TICKET"),
    FAIL_BIKE_STATUS_UPDATE(400, "BIKE STATUS UPDATE FAILED"),
    FAIL_BIKE_RETURN(400, "FAIL BIKE RETURN"),
    EXIST_UNPAID_AMOUNT(402, "EXIST UNPAID AMOUNT"),
    NOT_ENOUGH_MONEY(402, "NOT ENOUGH MONEY"),
    NOT_AUTHOR(403, "NOT AUTHOR OF THE POST"),
    NOT_FOUND_PAGE(404, "PAGE NOT FOUND"),
    NOT_FOUND_POST(404, "POST NOT FOUND"),
    FAVORITE_DUPLICATION(409, "ALREADY ADD FAVORITE"),
    TICKET_DUPLICATION(409, "ALREADY HAVE TICKET"),
    INTER_SERVER_ERROR(500, "INTER SERVER ERROR"),
    ;

    private int status;
    private String message;
}
