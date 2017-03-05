package kz.taxikz.bus;

public class ApiFailureEvent {
    private String mErrorMessage;
    private  String mErrorName;
    private  ErrorType mErrorType;

    public ApiFailureEvent(final String mErrorName, final ErrorType mErrorType) {
        this.mErrorName = mErrorName;
        this.mErrorType = mErrorType;
    }

    public ApiFailureEvent(final Throwable ex) {
//        switch (ex.getRetrofitError().getKind()) {
//            case NETWORK: {
//                this.mErrorType = ErrorType.NETWORK;
//                this.mErrorName = TaxiKzApp.get().getString(R.string.oops);
//                this.mErrorMessage = ex.getMessage();
//            }
//            case HTTP: {
//                this.mErrorType = ErrorType.HTTP;
//                final ApiError apiError = ex.getApiError();
//                this.mErrorName = TaxiKzApp.get().getString(R.string.oops);
//                this.mErrorMessage = apiError.getMessage();
//            }
//            case CONVERSION: {
//                this.mErrorType = ErrorType.CONVERSION;
//                final ApiError apiError = ex.getApiError();
//                this.mErrorName = TaxiKzApp.get().getString(R.string.oops);
//                this.mErrorMessage = apiError.getMessage();
//            }
//            default: {
//                this.mErrorType = ErrorType.UNEXPECTED;
//                this.mErrorName = TaxiKzApp.get().getString(R.string.internet_connection_problem);
//                this.mErrorMessage = ex.getMessage();
//            }
//        }
    }

    public String getErrorDisplayText() {
        return this.mErrorName;
    }

    public String getErrorMessage() {
        return this.mErrorMessage;
    }

    public ErrorType getErrorType() {
        return this.mErrorType;
    }

    public enum ErrorType {
        CONVERSION,
        HTTP,
        NETWORK,
        UNEXPECTED, ApiModule;
    }
}
