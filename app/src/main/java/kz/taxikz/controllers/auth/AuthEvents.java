package kz.taxikz.controllers.auth;

import kz.taxikz.bus.ApiFailureEvent;

public class AuthEvents {

    public static class CheckRegistrationUserFailed {
    }

    public static class CheckRegistrationUserSuccess {
    }

    public static class RegisterUserFailed {
    }

    public static class RegisterUserSuccess {
    }

    public static class SmsCodeSuccess {
    }

    public static class UnauthorizedUserDetected {
    }

    public static class SmsCodeFailed extends ApiFailureEvent {
        public SmsCodeFailed(Throwable ex) {
            super(ex);
        }
    }
}
