package allwhite.team.support;

class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(String username) {
        this("Could not find member profile with username '%s'", username);
    }

    public MemberNotFoundException(String message, Object... args) {
        super(String.format(message, args));
    }
}
