package kz.taxikz.controllers.client;

import kz.taxikz.bus.ApiFailureEvent;

public class ClientEvents {

    public static class GetClientSuccess {
    }

    public static class UpdateClientSuccess {
    }

    public static class GetClientFailure extends ApiFailureEvent {
        public GetClientFailure(Throwable ex) {
            super(ex);
        }
    }

    public static class UpdateClientFailure extends ApiFailureEvent {
        public UpdateClientFailure(Throwable ex) {
            super(ex);
        }
    }
}
