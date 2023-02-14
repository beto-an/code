package assignment;

public class ArgumentInterpreter {

    public static Integer interpretInteger(String arg) {
        int parsedInteger;
        try {
            parsedInteger = Integer.parseInt(arg);
        } catch (Exception e) {
            return null;
        }
        return parsedInteger;
    }

    public static Integer interpretIntegerWithOverflow(String arg) {
        int parsedInteger;
        try {
            long parsedLong = Long.parseLong(arg);
            if (parsedLong > (long) Integer.MAX_VALUE || parsedLong < (long) Integer.MIN_VALUE) {
                parsedInteger = Integer.MIN_VALUE;
            } else {
                parsedInteger = (int) parsedLong;
            }
        } catch (Exception e) {
            return null;
        }
        return parsedInteger;
    }

    public static Integer interpretBearing(String arg) {
        int parsedBearing;
        try {
            parsedBearing = Integer.parseInt(arg);
        } catch (Exception e) {
            return null;
        }

        if (parsedBearing != Critter.FRONT
                && parsedBearing != Critter.FRONT_RIGHT
                && parsedBearing != Critter.RIGHT
                && parsedBearing != Critter.REAR_RIGHT
                && parsedBearing != Critter.REAR
                && parsedBearing != Critter.REAR_LEFT
                && parsedBearing != Critter.LEFT
                && parsedBearing != Critter.FRONT_LEFT) {
            return null;
        }
        return parsedBearing;
    }

    public static Integer interpretRegister(String arg) {
        if (!isRegister(arg)) {
            return null;
        }
        if (arg.charAt(1) == '0' || arg.charAt(1) == '+') {
            return null;
        }

        int parsedInteger;
        try {
            parsedInteger = Integer.parseInt(arg.substring(1));
        } catch (Exception e) {
            return null;
        }

        if (parsedInteger < 1 || parsedInteger > 10) {
            return null;
        }

        return parsedInteger;
    }

    public static Integer interpretJump(String arg, boolean isRegister) {
        if (isRegister) {
            return interpretRegister(arg);
        } else {
            return interpretIntegerWithOverflow(arg);
        }
    }

    public static boolean isRelative(String arg) {
        if (arg.length() <= 1) {
            return false;
        }
        return arg.charAt(0) == '+' || arg.charAt(0) == '-';
    }

    public static boolean isRegister(String arg) {
        if (arg.length() <= 1) {
            return false;
        }
        return arg.charAt(0) == 'r';
    }

}

