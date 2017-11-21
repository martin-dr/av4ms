package net.droegemueller.av4ms.core.srv;

public abstract class ServerInteractorException extends Exception {
    private ServerInteractorException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public static class NotConfigured extends ServerInteractorException {
        public NotConfigured() { super(null, null); }
    }
    public static class BadUrl extends ServerInteractorException {
        public BadUrl(String s) { super(s, null); }
    }
    public static class NotAuthenticated extends ServerInteractorException {
        public NotAuthenticated(String s, Throwable throwable) { super(null, null); }
    }
    public static class NotFound extends ServerInteractorException {
        public NotFound(String s, Throwable throwable) { super(s, throwable); }
    }

    public static ServerInteractorException fromRetrofitException(RetrofitException e) {
        if (e.getKind() == RetrofitException.Kind.HTTP && e.getResponse() != null) {
            int code = e.getResponse().code();
            switch (code) {
                case 401: return new NotAuthenticated(e.getMessage(), e);
                case 404: return new NotFound(e.getMessage(), e);
            }
        }
        return null;
    }
}
