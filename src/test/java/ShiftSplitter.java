//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//
//
//class Shift {
//    String userId;
//    LocalDateTime startTime;
//    LocalDateTime endTime;
//    String shift;
//
//    public Shift(String userId, String startTime, String endTime, String shift) {
//        this.userId = userId;
//        this.startTime = LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        this.endTime = LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        this.shift = shift;
//    }
//
//    public Shift(String userId, LocalDateTime startTime, LocalDateTime endTime, String shift) {
//        this.userId = userId;
//        this.startTime = startTime;
//        this.endTime = endTime;
//        this.shift = shift;
//    }
//
//    @Override
//    public String toString() {
//        return "Shift{" +
//                "userId='" + userId + '\'' +
//                ", startTime=" + startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) +
//                ", endTime=" + endTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) +
//                ", shift='" + shift + '\'' +
//                '}';
//    }
//}
//
//public class ShiftSplitter {
//    public static void main(String[] args) {
//        List<Shift> shifts = new ArrayList<>();
//        shifts.add(new Shift("user01", "2023-10-12 08:00:00", "2023-10-12 18:00:00", "working"));
//        shifts.add(new Shift("user01", "2023-10-12 09:00:00", "2023-10-12 09:30:00", "eat"));
//        shifts.add(new Shift("user01", "2023-10-12 11:00:00", "2023-10-12 11:30:00", "exercise"));
//        shifts.add(new Shift("user01", "2023-10-12 15:00:00", "2023-10-12 15:30:00", "tea"));
//        shifts.add(new Shift("user02", "2023-10-13 09:00:00", "2023-10-13 09:30:00", "jogging"));
//        shifts.add(new Shift("user02", "2023-10-13 13:00:00", "2023-10-13 14:30:00", "meeting"));
//
//        List<Shift> result = new ArrayList<>();
//
//        List<String> userIds = new ArrayList<>();
//        userIds.add("user01");
//        userIds.add("user02");
//        for (String userId : userIds) {
//            List<Shift> userShifts = new ArrayList<>();
//            for (Shift shift : shifts) {
//                if (shift.userId.equals(userId)) {
//                    userShifts.add(shift);
//                }
//            }
//            userShifts.sort((s1, s2) -> s1.startTime.compareTo(s2.startTime));
//
//            List<Shift> generalShifts = new ArrayList<>();
//            List<Shift> smallShifts = new ArrayList<>();
//            for (Shift shift : userShifts) {
//                if (shift.shift.equals("working")) {
//                    generalShifts.add(shift);
//                } else {
//                    smallShifts.add(shift);
//                }
//            }
//
//            for (Shift generalShift : generalShifts) {
//                LocalDateTime currentStart = generalShift.startTime;
//                for (Shift smallShift : smallShifts) {
//                    if (smallShift.startTime.isBefore(generalShift.endTime) && smallShift.endTime.isAfter(generalShift.startTime)) {
//                        if (currentStart.isBefore(smallShift.startTime)) {
//                            result.add(new Shift(userId, currentStart, smallShift.startTime, "working"));
//                        }
//                        result.add(new Shift(userId, smallShift.startTime, smallShift.endTime, smallShift.shift));
//                        currentStart = smallShift.endTime;
//                    }
//                }
//                if (currentStart.isBefore(generalShift.endTime)) {
//                    result.add(new Shift(userId, currentStart, generalShift.endTime, "working"));
//                }
//            }
//
//            if (generalShifts.isEmpty()) {
//                result.addAll(smallShifts);
//            }
//        }
//
//        for (Shift shift : result) {
//            System.out.println(shift);
//        }
//    }
//}
//
